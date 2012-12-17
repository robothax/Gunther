package prover;

import data_structures.Sequent;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
//@THIS WHOLE CLASS IS A CANDIDATE FOR BEATIFICATION
public class ProofNode {
	//each node is a sequent's toString
	//can't use the sequents themselves because the sequents change during computation (i.e. are mutable and the record won't hold)
	//can change this later using clones but just use strings for now
	private String state;
	
	private ProofNode parent;
	
	private ProofNode lchild;
	private ProofNode rchild;
	
	ProofNode(Sequent s) {
		state = s.toString();
		parent = null;
		lchild = null;
		rchild = null;
	}
	
	ProofNode(Sequent s, ProofNode p) {
		state = s.toString();
		parent = p;
		p.setChild(this);
	}
	
	public String getState() {	return state;	}
	
	public ProofNode getParent() {	return parent;	}
	public ProofNode getLChild() {	return lchild;	}
	public ProofNode getRChild() {	return rchild;	}
	
	public void setParent(ProofNode p) {
		parent = p;
	}
	/*public synchronized void setChildren(ProofNode... c) {
		for (int i = 0; i < c.length && i < 2; i++) {
			lchild = c[0];
			rchild = c[1];
		}
	}*/
	public synchronized void setChild(ProofNode c) {
		if (lchild == null) {
			lchild = c;
		}
		else if (rchild == null) {
			rchild = c;
		}
		else {
			//can't set any more children, shouldn't get here with binary branching
		}
	}
	
	/*public synchronized void setLChild(ProofNode l) {
		lchild = l;
	}
	public synchronized void setRChild(ProofNode r) {
		rchild = r;
	}*/
}