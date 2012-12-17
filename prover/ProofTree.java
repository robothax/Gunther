package prover;
import data_structures.Sequent;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class ProofTree {
	
	private ProofNode root;
	boolean success;
	
	ProofTree() {
		root = null;
		success = true;
	}
	
	ProofTree(Sequent init) {
		root = new ProofNode(init);
		success = false;
	}
	
	public ProofNode getRoot() {	return root;	}
	
	void setRoot(ProofNode r) {
		root = r;
	}
	
	void unsuccessful() {
		success = false;
	}
	
	public boolean isSucess() {
		return success;
	}
	
	/**
	 * Generic print proof.  Prints proof from the beginning.
	 */
	public void printProof() {
		System.out.println("Proof " + success + ":");
		printProof(root);
	}
	
	/**
	 * Post order traversal of the proofTree beginning with the node referenced by start.
	 * @param start
	 * The node for which to show the proof
	 */
	private void printProof(ProofNode start) {
		if (start == null)
			return;
		
		if (start.getLChild() != null)
			printProof(start.getLChild());
		if (start.getRChild() != null)
			printProof(start.getRChild());
		
		System.out.println(start.getState());
	}
}
