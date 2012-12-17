package prover;
import data_structures.*;

import java.util.Vector;

import prover.ThreadMonitor;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class Prover implements Runnable {

	private static Vector<Thread> branches;
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
		new Prover(query, record.getRoot());
		tm = new ThreadMonitor();
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
		doneYetIndex=tm.getDoneYet().size();
		//indicate that there will be a new proof thread running
		branches.add(t);
		t.start();
	}
	/**
	 * This method determines the behavior of a prover thread.
	 * It decomposes a sequent until the sequent terminates or branches.
	 * Upon determination that the original goal is not provable a signal is sent for all provers to stop.
	 */
	public void run() {
		//use another thread to periodically check if we have any thread still running?

		Thread t = Thread.currentThread();

		//if the thread returned true then it either reached axioms or branched into new sequent threads, so remove the thread from the vector
		if (prove(localBranch, currentNode)) {
			branches.remove(t);
		}
		//if failure, then stop all other threads, we can't have a proof
		else {
			//t.setPriority(Thread.MAX_PRIORITY);
			record.unsuccessful();
			stop = true;
			/*branches.remove(t);

			//iterator structure in case we need to do something to each thread
			Iterator<Thread> it = branches.iterator();
			while(it.hasNext()) {
				branches.remove(it.next());
			}*/
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

		if (branch.isAxiom()) {	//branch success
			return true;
		}
		else if (branch.isRightAtomic() && branch.isLeftAtomic()){//branch failure
			return false;
		}
		else {	//then keep breaking down the branch
			OperatorType o;
			//pick hypotheses or conclusions
			//conclusions first, most calls to prove will have all/more elements in the conclusions
			if (!branch.isRightAtomic()) {	
				
				Formula f = branch.getConclusions().removeFirst();
				
				//is the first formula a compound formula? (i.e., not first order)
				if (f instanceof CompoundFormula) {
					CompoundFormula cf = (CompoundFormula) f;
					o = cf.getOperator().getType();
					
					switch (o) {
					case NEGATION:
						rightNegation(branch, cf);
						prove(branch, new ProofNode(branch, pNode));
						break;
					case CONJUNCTION:
						rightConjunction(branch, cf, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					case DISJUNCTION:
						rightDisjunction(branch, cf);
						prove(branch, new ProofNode(branch, pNode));
						break;
					case IMPLICATION:
						rightImplication(branch, cf);
						prove(branch, new ProofNode(branch, pNode));
						break;
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
						//TODO
						break;
					case UNIVERSAL:
						//TODO
						break;
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
						prove(branch, new ProofNode(branch, pNode));
						break;
					case CONJUNCTION:
						leftConjunction(branch, cf);
						prove(branch, new ProofNode(branch, pNode));
						break;
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
				//otherwise f must be a first order formula (as we cannot retrieve atoms from the list normally
				else {
					FirstOrderFormula fof = (FirstOrderFormula)f;
					o = fof.getQuantifier().getType();
					
					switch(o) {
					case EXISTENTIAL:
						//TODO
						break;
					case UNIVERSAL:
						//TODO
						break;
					}
				}
			}
			else {}
		}
		//we don't get here
		return false;
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

		Sequent branch1 = new Sequent(left, right);
		branch1.getConclusions().addFirst(f_sub1);

		Sequent branch2 = new Sequent(left, right);
		branch2.getConclusions().addFirst(f_sub2);

		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
	}

	private void leftDisjunction(Sequent branch, CompoundFormula cf, ProofNode pNode) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);

		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);

		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
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

		Sequent branch1 = new Sequent(left, right);
		branch1.getConclusions().addFirst(f_sub1);

		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);

		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
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

		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);
		branch1.getHypotheses().addFirst(f_sub2);

		Sequent branch2 = new Sequent(left, right);
		branch2.getConclusions().addFirst(f_sub2);
		branch2.getConclusions().addFirst(f_sub1);

		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
	}

	private void rightEquivalence(Sequent branch, CompoundFormula cf, ProofNode pNode) {
		FormulaList left = branch.getHypotheses();
		FormulaList right = branch.getConclusions();

		Formula f_sub1 = cf.getArguments()[0];
		Formula f_sub2 = cf.getArguments()[1];

		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);
		branch1.getConclusions().addFirst(f_sub2);

		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);
		branch2.getConclusions().addFirst(f_sub1);

		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
	}
}
