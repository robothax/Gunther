package prover;

import data_structures.*;

import java.util.LinkedList;
import java.util.Vector;
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
	
	/**
	 * Retrieves the record containing all the proof states propagated by the prover.
	 * @return
	 * The proof state characterizing the proof.
	 */
	public ProofTree getProof() {	return record;	}
	
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
		if (Prove(localBranch, currentNode)) {
			branches.remove(t);
		}
		//if failure, then stop all other threads, we can't have a proof
		else {
			//t.setPriority(Thread.MAX_PRIORITY);
			stop = true;
			/*branches.remove(t);
			
			//iterator structure in case we need to do something to each thread
			Iterator<Thread> it = branches.iterator();
			while(it.hasNext()) {
				branches.remove(it.next());
			}*/
		}
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
	public boolean Prove(Sequent branch, ProofNode pNode) {
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
			//conclusions, most calls to prove will have all/more elements in the conclusions
			if (!branch.isRightAtomic()) {	
				o = branch.getConclusions().getFirst().getOperator().getType();
				switch (o) {
					case NEGATION:
						rightNegation(branch);
						Prove(branch, new ProofNode(branch, pNode));
						break;
					case CONJUNCTION:
						rightConjunction(branch, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					case DISJUNCTION:
						rightDisjunction(branch);
						Prove(branch, new ProofNode(branch, pNode));
						break;
					case IMPLICATION:
						rightImplication(branch);
						Prove(branch, new ProofNode(branch, pNode));
						break;
					case EQUIVALENCE:
						rightEquivalence(branch, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
				}
			}
			//hypotheses
			else if (!branch.isLeftAtomic()) {
				o = branch.getHypotheses().getFirst().getOperator().getType();
				switch (o) {
					case NEGATION:
						leftNegation(branch);
						Prove(branch, new ProofNode(branch, pNode));
						break;
					case CONJUNCTION:
						leftConjunction(branch);
						Prove(branch, new ProofNode(branch, pNode));
						break;
					case DISJUNCTION:
						leftDisjunction(branch, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					case IMPLICATION:
						leftImplication(branch, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
					case EQUIVALENCE:
						leftEquivalence(branch, pNode);
						//thread branches into two new threads, original thread returns
						return true;
						//break;
				}
			}
			else {}
		}
		//we don't get here
		return false;
	}
	
	private void leftNegation(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		f = f.getArguments()[0]; //refactor f, remove the negation
		
		right.addFirst(f);	
	}
	
	private void rightNegation(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		f = f.getArguments()[0]; //refactor f by removing the negation
		
		left.addFirst(f);
	}
	
	private void leftConjunction(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		//LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		left.addFirst(f_sub1);
		left.addFirst(f_sub2);
	}
	
	private void rightConjunction(Sequent branch, ProofNode pNode) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];

		Sequent branch1 = new Sequent(left, right);
		branch1.getConclusions().addFirst(f_sub1);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getConclusions().addFirst(f_sub2);
		
		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
	}
	
	private void leftDisjunction(Sequent branch, ProofNode pNode) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);
		
		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
	}
	
	private void rightDisjunction(Sequent branch) {
		//LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		right.addFirst(f_sub2);
		right.addFirst(f_sub1);
	}
	
	private void leftImplication(Sequent branch, ProofNode pNode) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];

		Sequent branch1 = new Sequent(left, right);
		branch1.getConclusions().addFirst(f_sub1);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);

		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
	}
	
	private void rightImplication(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		left.addFirst(f_sub1);
		right.addFirst(f_sub2);
	}
	
	private void leftEquivalence(Sequent branch, ProofNode pNode) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];

		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);
		branch1.getHypotheses().addFirst(f_sub2);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getConclusions().addFirst(f_sub2);
		branch2.getConclusions().addFirst(f_sub1);

		new Prover(branch1, new ProofNode(branch1, pNode));
		new Prover(branch2, new ProofNode(branch2, pNode));
	}
	
	private void rightEquivalence(Sequent branch, ProofNode pNode) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];

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