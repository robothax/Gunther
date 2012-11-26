package data_structures;

import java.util.LinkedList;

public class Sequent {
	 
	private LinkedList<Formula> Hypotheses;
	private LinkedList<Formula> Conclusions;
	
	public Sequent() {
		Hypotheses = new LinkedList<Formula>();
		Conclusions = new LinkedList<Formula>();
	}
	
	public Sequent(LinkedList<Formula> hypo, LinkedList<Formula> conc) {
		Hypotheses = hypo;
		Conclusions = conc;
	}
	
	public LinkedList<Formula> getHypotheses() {	return Hypotheses;	}
	public LinkedList<Formula> getConclusions() {	return Conclusions;	}
	
	/**
	 * Determines if the list of hypotheses is all atomic
	 * If yes returns true.
	 * If no returns false and puts the first non atomic element found at the head of the list.
	 * @return
	 * Returns false if there is any non-atmoic formula in the hypotheses.
	 * True if otherwise.
	 */
	public boolean isLeftAtomic() {
		for (int i = 0; i < Hypotheses.size(); i++) {
			if (!Hypotheses.get(i).isAtomic()) {
				Formula f = Hypotheses.remove(i);
				Hypotheses.addFirst(f);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determines if the list of conclcusions is all atomic
	 * If yes returns true.
	 * If no returns false and puts the first non atomic element found at the head of the list.
	 * @return
	 * Returns false if there is any non-atomic formula in the conclusions.
	 * True if otherwise.
	 */
	public boolean isRightAtomic() {
		for (int i = 0; i < Conclusions.size(); i++) {
			if (!Conclusions.get(i).isAtomic()) {
				Formula f = Conclusions.remove(i);
				Conclusions.addFirst(f);
				return false;
			}
		}
		return true;
	}
	
}
