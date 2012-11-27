package prover;
import data_structures.Sequent;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class ProofTree {
	
	private ProofNode root;
	
	ProofTree() {
		root = null;
	}
	
	ProofTree(Sequent init) {
		root = new ProofNode(init);
	}
	
	public ProofNode getRoot() {	return root;	}
	
	void setRoot(ProofNode r) {
		root = r;
	}
	
	/**
	 * Post order traversal of the proofTree beginning with the node referenced by start.
	 * @param start
	 * The node for which to show the proof
	 */
	public void printProof(ProofNode start) {
		if (start == null)
			return;
		if (start.getLChild() != null)
			System.out.println(start.getLChild().getState());
		if (start.getRChild() != null)
			System.out.println(start.getRChild().getState());
		
		System.out.println(start.getState());
	}
}
