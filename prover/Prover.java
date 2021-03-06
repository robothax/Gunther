package prover;
import java.util.ArrayList;

import data_structures.*;

import prover.ThreadMonitor;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class Prover implements Runnable {

	private static ProofTree record;
	private static volatile boolean stop;
	private Sequent localBranch;
	private ProofNode currentNode;
	
	private static ThreadMonitor tm;
	private int doneYetIndex=0;

	//private static final Boolean FALSE = new Boolean(false);

	/**
	 * The method used to initiate a proof.
	 * This is the only entry for a proof since the prover constructor is private.
	 * @param query
	 * The statement to prove.
	 */
	public static void initProof(Sequent query) { //initialize proof
		stop = false;
		record = new ProofTree(query);
		tm = new ThreadMonitor();
		new Prover(query, record.getRoot());
	}
	
	/**
	 * Retrieves the record containing all the proof states propagated by the prover.
	 * @return
	 * The proof state characterizing the proof.
	 */
	public static ProofTree getProof() {	return record;	}
	
	/**
	 * Used to determine if the proof is completed.
	 * @return
	 * Returns true for proof is complete, false for proof is incomplete.
	 */
	public static boolean isComplete() {
		return tm.checkIfDone();
	}

	/**
	 * The constructor for an autonomous prover element.
	 * The prover runs in its own thread, seeking to decompose a target sequent, unique to each prover.
	 * If the sequent branches the prover terminates and generates a new prover for each branch.
	 * @param branch
	 * The initial Sequent for the prover to work on.
	 * @param initNode
	 * The node corresponding to that initial sequent.
	 */
	private Prover(Sequent branch, ProofNode initNode) {
		localBranch = branch;
		currentNode = initNode;
		Thread t = new Thread(this, "Sequent " + branch);
		tm.addThread(new Boolean(false));
		doneYetIndex=tm.getDoneYet().size()-1;
		//indicate that there will be a new proof thread running
		//branches.add(t);
		t.start();
	}
	/**
	 * This method determines the behavior of a prover thread.
	 * It decomposes a sequent until the sequent terminates or branches.
	 * Upon determination that the original goal is not provable a signal is sent for all provers to stop.
	 */
	public void run() {
		//use another thread to periodically check if we have any thread still running?
		
		
		//if the thread returned true then it either reached axioms or branched into new sequent threads, so remove the thread from the vector
		if (prove(localBranch, currentNode)) {
			//branches.remove(t);
		}
		//if failure, then stop all other threads, we can't have a proof
		else {
			//t.setPriority(Thread.MAX_PRIORITY);
			record.unsuccessful();
			stop = true;
		}
		//when the thread is done set it as such.
		tm.getDoneYet().setElementAt(new Boolean(true), doneYetIndex);
		//check if any of the other threads are not done, if one is not done simply return from this function
		return;

	}

	/**
	 * This method decomposes a sequent recursively by the principal operator of one of the formula in the list of the hypotheses and conclusions.
	 * Exactly which formula it targets first is up for optimization.
	 * The method calls itself recursively on a sequent until the sequent is deemed an axiom, is fully atomic, or branches into new sequents.
	 * At each transformation of a sequent a new node in the proof tree is generated for the new state of the sequent.
	 * Upon branching the new branches have a reference to their parent sequent.
	 * @param branch
	 * The initial sequent to be decomposed.
	 * @param pNode
	 * The node in  the proof tree corresponding to the sequent specified by branch.
	 * @return
	 * Returns true if this proof thread runs to completion (i.e. has branched into new threads) or if it has reached a sequent which is an axiom.
	 * Returns false if this proof branch has generated a fully atomic sequent that is not an axiom.
	 */
	public boolean prove(Sequent branch, ProofNode pNode) {
		// if another thread found a fully atomic sequent that is not an axiom, then cease computation on this sequent as there cannot be a proof
		if (stop) {
			return true;
		}

		//add this sequent to the proof tree
		
		//check metavariables
		if (branch.isLeftMeta() && !branch.isRightCompound()) {
			//can we convert a metavariable to a real variable to get an axiom?
			//look through atoms on both sides, if there is a match in descriptors and one has meta variables and the other does not
			//then convert the metavariables in one to the instanced variables in the other
			if (instantiateLeft(branch, pNode))
				return true;
			
		}
		if (branch.isRightMeta() && !branch.isLeftCompound()) {
			if (instantiateRight(branch, pNode))
				return true;
		}
		
		if (branch.isAxiom()) {	//branch success
			return true;
		}
		//possible branch failure, this doesn't happen in proofs requiring contraction, so we should add another condition
		else if (branch.isRightAtomic() && branch.isLeftAtomic()){
			return false;
		}
		else {	//then keep breaking down the branch
			OperatorType o;
			//pick hypotheses or conclusions
			//conclusions first, most calls to prove will have all/more elements in the conclusions
			//have a chance to randomly go to hypotheses instead in case of a contraction loop
			//P(keep working on conclusions | hypotheses can be worked on too) = 0.3
			if (!branch.isRightAtomic() && (branch.isLeftAtomic() || (Math.random() < 0.3))) {	
				
				Formula f = branch.getConclusions().removeFirst();
				
				//is the first formula a compound formula? (i.e., not first order)
				if (f instanceof CompoundFormula) {
					CompoundFormula cf = (CompoundFormula) f;
					o = cf.getOperator().getType();
					
					switch (o) {
					case NEGATION:
						rightNegation(branch, cf);
						return prove(branch, new ProofNode(branch, pNode, Operator.NEGATION_SYM + "R"));
						//break;
					case CONJUNCTION:
						rightConjunction(branch, cf, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					case DISJUNCTION:
						rightDisjunction(branch, cf);
						return prove(branch, new ProofNode(branch, pNode, Operator.DISJUNCTION_SYM + "R"));
						//break;
					case IMPLICATION:
						rightImplication(branch, cf);
						return prove(branch, new ProofNode(branch, pNode, Operator.IMPLICATION_SYM + "R"));
						//break;
					case EQUIVALENCE:
						rightEquivalence(branch, cf, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					}
				}
				//otherwise it must be first order, as we cannot retrieve atoms from the list
				else {
					FirstOrderFormula fof = (FirstOrderFormula) f;
					o = fof.getQuantifier().getType();
					
					switch (o) {
					case EXISTENTIAL:
						rightExistential(branch, fof);
						return prove(branch, new ProofNode(branch, pNode, Operator.EXISTENTIAL_SYM + "R"));
					case UNIVERSAL:
						rightUniversal(branch, fof);
						return prove(branch, new ProofNode(branch, pNode, Operator.UNIVERSAL_SYM + "R"));
					}
				}
			}
			//hypotheses
			else if (!branch.isLeftAtomic()) {
				
				Formula f = branch.getHypotheses().removeFirst();
				
				if (f instanceof CompoundFormula) {
					CompoundFormula cf = (CompoundFormula) f;
					o = cf.getOperator().getType();
					
					switch (o) {
					case NEGATION:
						leftNegation(branch, cf);
						return prove(branch, new ProofNode(branch, pNode, Operator.NEGATION_SYM + "L"));
					case CONJUNCTION:
						leftConjunction(branch, cf);
						return prove(branch, new ProofNode(branch, pNode, Operator.CONJUNCTION_SYM + "L"));
					case DISJUNCTION:
						leftDisjunction(branch, cf, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					case IMPLICATION:
						leftImplication(branch, cf, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					case EQUIVALENCE:
						leftEquivalence(branch, cf, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					}
				}
				//otherwise f must be a first order formula (as we cannot retrieve atoms from the list normally)
				else {
					FirstOrderFormula fof = (FirstOrderFormula)f;
					o = fof.getQuantifier().getType();
					
					switch(o) {
					case EXISTENTIAL:
						leftExistential(branch, fof);
						return prove(branch, new ProofNode(branch, pNode, Operator.EXISTENTIAL_SYM + "L"));
					case UNIVERSAL:
						leftUniversal(branch, fof);
						return prove(branch, new ProofNode(branch, pNode, Operator.UNIVERSAL_SYM + "L"));
					}
				}
			}
			else {}
		}
		//we don't get here
		return true;
	}

	private void leftNegation(Sequent branch, CompoundFormula cf) {
		FormulaList right = branch.getConclusions();

		Formula f = cf.getArguments()[0]; //refactor f, remove the negation

		right.addFirst(f);	
	}

	private void rightNegation(Sequent branch, CompoundFormula cf) {
		FormulaList left = branch.getHypotheses();
		
		Formula f = cf.getArguments()[0]; //refactor f by removing the negation

		left.addFirst(f);
	}

	private void leftConjunction(Sequent branch, CompoundFormula cf) {
		FormulaList left = branch.getHypotheses();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		left.addFirst(f_sub1);
		left.addFirst(f_sub2);
	}

	private void rightConjunction(Sequent branch, CompoundFormula cf, ProofNode pNode) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		Sequent branch1 = new Sequent(left.clone(), right.clone());
		branch1.getConclusions().addFirst(f_sub1);

		Sequent branch2 = new Sequent(left.clone(), right.clone());
		branch2.getConclusions().addFirst(f_sub2);

		new Prover(branch1, new ProofNode(branch1, pNode, Operator.CONJUNCTION_SYM + "R1"));
		new Prover(branch2, new ProofNode(branch2, pNode, Operator.CONJUNCTION_SYM + "R2"));
	}

	private void leftDisjunction(Sequent branch, CompoundFormula cf, ProofNode pNode) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		Sequent branch1 = new Sequent(left.clone(), right.clone());
		branch1.getHypotheses().addFirst(f_sub1);

		Sequent branch2 = new Sequent(left.clone(), right.clone());
		branch2.getHypotheses().addFirst(f_sub2);

		new Prover(branch1, new ProofNode(branch1, pNode, Operator.DISJUNCTION_SYM + "L1"));
		new Prover(branch2, new ProofNode(branch2, pNode, Operator.DISJUNCTION_SYM + "L2"));
	}

	private void rightDisjunction(Sequent branch, CompoundFormula cf) {
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		right.addFirst(f_sub2);
		right.addFirst(f_sub1);
	}

	private void leftImplication(Sequent branch, CompoundFormula cf, ProofNode pNode) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		Sequent branch1 = new Sequent(left.clone(), right.clone());
		branch1.getConclusions().addFirst(f_sub1);
		
		Sequent branch2 = new Sequent(left.clone(), right.clone());
		branch2.getHypotheses().addFirst(f_sub2);

		new Prover(branch1, new ProofNode(branch1, pNode, Operator.IMPLICATION_SYM + "L1"));
		new Prover(branch2, new ProofNode(branch2, pNode, Operator.IMPLICATION_SYM + "L2"));
	}

	private void rightImplication(Sequent branch, CompoundFormula cf) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		left.addFirst(f_sub1);
		right.addFirst(f_sub2);
	}

	private void leftEquivalence(Sequent branch, CompoundFormula cf, ProofNode pNode) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		Sequent branch1 = new Sequent(left.clone(), right.clone());
		branch1.getHypotheses().addFirst(f_sub1);
		branch1.getHypotheses().addFirst(f_sub2);

		Sequent branch2 = new Sequent(left.clone(), right.clone());
		branch2.getConclusions().addFirst(f_sub2);
		branch2.getConclusions().addFirst(f_sub1);

		new Prover(branch1, new ProofNode(branch1, pNode, Operator.EQUIVALENCE_SYM + "L1"));
		new Prover(branch2, new ProofNode(branch2, pNode, Operator.EQUIVALENCE_SYM + "L2"));
	}

	private void rightEquivalence(Sequent branch, CompoundFormula cf, ProofNode pNode) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		Sequent branch1 = new Sequent(left.clone(), right.clone());
		branch1.getHypotheses().addFirst(f_sub1);
		branch1.getConclusions().addFirst(f_sub2);

		Sequent branch2 = new Sequent(left.clone(), right.clone());
		branch2.getHypotheses().addFirst(f_sub2);
		branch2.getConclusions().addFirst(f_sub1);

		new Prover(branch1, new ProofNode(branch1, pNode, Operator.EQUIVALENCE_SYM + "R1"));
		new Prover(branch2, new ProofNode(branch2, pNode, Operator.EQUIVALENCE_SYM + "R2"));
	}
	
	private void leftUniversal(Sequent branch, FirstOrderFormula fof) {
		//implicit contraction
		branch.getHypotheses().addLast(fof.clone());
		
		//instantiate dummy terms of fof
		Term[] terms = fof.getBoundTerms();
		for (Term t: terms) {
			String m = Term.getMetavariable();
			if (m == null) {
				record.unsuccessful();
				record.printProof();
				System.err.println("Too many metavariables in use...\nProof failure");
				System.exit(1);
			}
			t.setVariable(m);
			t.setMeta(true);
		}
		
		branch.getHypotheses().addFirst(fof.getArgument());
	}
	
	private void rightUniversal(Sequent branch, FirstOrderFormula fof) {
		//instantiate unique term of fof
		Term[] terms = fof.getBoundTerms();
		for (int i = 0; i < terms.length; i++) {
			
			/* get a unique term (i.e not elsewhere in teh sequent) to which to instantiate for each term bound by the first order formula */
			ArrayList<Term> used = branch.getTerms();
			used.addAll(fof.getAllTerms());
			Term[] t = new Term[0];
			t = used.toArray(t);
			String u = Term.getUnique(t);
			if (u == null) {
				record.unsuccessful();
				record.printProof();
				System.err.println("Too many terms in use..\nProof failure");
				System.exit(1);
			}
			terms[i].setVariable(u);
			//t.setMeta(false);
		}
		
		branch.getConclusions().addFirst(fof.getArgument());
	}
	
	private void leftExistential(Sequent branch, FirstOrderFormula fof) {
		//instantiate unique terms of fof
		Term[] terms = fof.getBoundTerms();
		
		/* get a unique term (occurring no where else in the formula) to instantiate to for each term bound in the first order formula */
		for (int i = 0; i < terms.length; i++) {
			
			ArrayList<Term> used = branch.getTerms();
			used.addAll(fof.getAllTerms());
			Term[] t = new Term[0];
			t = used.toArray(t);
			String u = Term.getUnique(t);
			if (u == null) {
				record.unsuccessful();
				record.printProof();
				System.err.println("Too many terms in use...\nProof failure");
				System.exit(1);
			}
			terms[i].setVariable(u);
			terms[i].setMeta(false);
		}
		
		branch.getHypotheses().addFirst(fof.getArgument());
	}
	
	private void rightExistential(Sequent branch, FirstOrderFormula fof) {
		
		//implicit contraction
		branch.getConclusions().addLast(fof.clone());
		
		//instantiate dummy terms of fof
		Term[] terms = fof.getBoundTerms();
		for (Term t: terms) {
			String m = Term.getMetavariable();
			if (m == null) {
				record.unsuccessful();
				record.printProof();
				System.err.println("Too many metavariables in use...\nProof failure");
				System.exit(1);
			}
			t.setVariable(m);
			t.setMeta(true);
		}
		
		branch.getConclusions().addFirst(fof.getArgument());
	}
	
	private boolean instantiateLeft(Sequent branch, ProofNode parent) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();
		
		ArrayList<Formula> leftAtoms = left.getAtoms();
		ArrayList<Formula> rightAtoms = right.getAtoms();
		
		Term[] old;
		
		for (Formula l: leftAtoms) {
			AtomicFormula al = (AtomicFormula)l;
			for (Formula r: rightAtoms) {
				AtomicFormula ar = (AtomicFormula)r;
				if (al.isMeta() && ar.getDescriptor().equals(al.getDescriptor())) {
					/* 
					 * for each meta variable in al, try to match it to an instantiated variable in ar
					 * we can just match them easily since our parser only handles unary predicates
					 * but for n-ary predicates this is more complicated
					 */
					Term[] rightAtomTerms = ar.getTerms();
					Term[] leftAtomTerms = al.getTerms();
					
					//used to restore the state of the metavariables in case this doesn't give us a proof
					old = new Term[leftAtomTerms.length];
							
					for (int i = 0; i < leftAtomTerms.length; i++) {
						if (leftAtomTerms[i].isMeta()) {
							old[i] = new Term(leftAtomTerms[i].toString());
							leftAtomTerms[i].setVariable(rightAtomTerms[i].toString());
						}
					}
					
					if (branch.isAxiom()) {
						String mv = "";
						for (int i = 0; i < old.length; i++) {
							if (old[i] != null) {
								mv += old[i].toString() + ",";
							}
						}
						mv = mv.substring(0, mv.length() - 1);
						new ProofNode(branch, parent, "Metavariable(s) " + mv + " instantiated");
						return true;
					}
					else {
						for (int i = 0; i < leftAtomTerms.length; i++) {
							if (leftAtomTerms[i].isMeta()) {
								leftAtomTerms[i].setVariable(old[i].toString());
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean instantiateRight(Sequent branch, ProofNode parent) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();
		
		ArrayList<Formula> leftAtoms = left.getAtoms();
		ArrayList<Formula> rightAtoms = right.getAtoms();
		
		Term[] old;
		
		for (Formula r: rightAtoms) {
			AtomicFormula ar = (AtomicFormula)r;
			for (Formula l: leftAtoms) {
				AtomicFormula al = (AtomicFormula)l;
				if (ar.isMeta() && al.getDescriptor().equals(ar.getDescriptor())) {
					
					/*
					 * for each meta variable in ar, match it to a real variable in al
					 * in general we will only have to match one formula to another to make an axiom
					 */
					
					Term[] rightAtomTerms = ar.getTerms();
					Term[] leftAtomTerms = al.getTerms();
					
					//used to restore the state of the metavariables in case this doesn't give us a proof
					old = new Term[rightAtomTerms.length];
							
					for (int i = 0; i < rightAtomTerms.length; i++) {
						if (rightAtomTerms[i].isMeta()) {
							old[i] = new Term(rightAtomTerms[i].toString());
							rightAtomTerms[i].setVariable(leftAtomTerms[i].toString());
						}
					}
					
					if (branch.isAxiom()) {
						String mv = "";
						for (int i = 0; i < old.length; i++) {
							if (old[i] != null) {
								mv += old[i].toString() + ",";
							}
						}
						mv = mv.substring(0, mv.length() - 1);
						new ProofNode(branch, parent, "Metavariable " + " instantiated");
						return true;
					}
					else {
						for (int i = 0; i < rightAtomTerms.length; i++) {
							if (rightAtomTerms[i].isMeta()) {
								rightAtomTerms[i].setVariable(old[i].toString());
							}
						}
					}
				}
			}
		}
		return false;
	}
}