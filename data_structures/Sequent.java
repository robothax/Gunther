package data_structures;

import java.util.Iterator;
import java.util.LinkedList;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
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
	 * Determines if the sequent is an axiom.
	 * A sequent is an axiom if the same atomic formula is in both the hypotheses and the conclusions.
	 * @return Returns true if a sequent represents an axiom, false if otherwise.
	 */
	//@TARGET FOR OPTIMIZATION
	public boolean isAxiom() {
		//TODO
		return false;
	}
	
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
			if (!(Hypotheses.get(i) instanceof AtomicFormula)) {
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
			if (!(Conclusions.get(i) instanceof AtomicFormula)) {
				Formula f = Conclusions.remove(i);
				Conclusions.addFirst(f);
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		String seq = "";
		
		Iterator<Formula> hit = Hypotheses.descendingIterator();
		while(hit.hasNext()) {
			seq += hit.next().toString();
			if (hit.hasNext())
				seq += ", ";
		}
		seq += " |- ";
		
		Iterator<Formula> cit = Conclusions.iterator();
		while(cit.hasNext()) {
			seq += cit.next().toString();
			if (cit.hasNext())
				seq += ", ";
		}
		
		return seq;
	}
	
}
