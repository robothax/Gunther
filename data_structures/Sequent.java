package data_structures;

/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class Sequent {
	 
	private FormulaList Hypotheses;
	private FormulaList Conclusions;

	public Sequent() {
		Hypotheses = new FormulaList();
		Conclusions = new FormulaList();
	}
	
	public Sequent(FormulaList hypo,FormulaList conc) {
		Hypotheses = hypo;
		Conclusions = conc;
	}
	
	public FormulaList getHypotheses() {	return Hypotheses;	}
	public FormulaList getConclusions() {	return Conclusions;	}
	
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
		return Hypotheses.isAtomic();

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
		return Conclusions.isAtomic();
	}
	
	public String toString() {
String seq = "";
		
		Formula[] hypo = Hypotheses.toArray();
		for (int i = hypo.length-1; i >= 0; i--) {
			seq += hypo[i].toString();
			if (i-1 >= 0)
				seq += ", ";
		}
		seq += " |- ";
		
		Formula[] conc = Conclusions.toArray();
		for (int i = conc.length-1; i >= 0; i--) {
			seq += conc[i].toString();
			if (i-1 >= 0)
				seq += ", ";
		}
		
		return seq;
	}
	
}
