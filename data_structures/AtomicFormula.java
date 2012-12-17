package data_structures;

/**
 * An atomic formula is either a proposition variable with no connective (a literal, i.e. A),
 * or is a first-order formula of one or more terms (i.e. P(x))
 * @author Jeffrey Kabot, Aaron Meltzer
 *
 */
public class AtomicFormula extends Formula {
	
	private String descriptor;
	private String[] terms;
	
	private boolean firstOrder;
	
	/**
	 * Constructor for atomic formula.
	 * @param lit Capital letter representative of the formula.
	 * @param t List of terms, can be empty.  If non-empty then the formula is first-order.
	 */
	public AtomicFormula(String lit, String... t) {
		if (t.length == 0) {
			firstOrder = false;
		}
		else {
			firstOrder =  true;
		}
		
		descriptor = lit;
		terms = t;
	}
	
	public boolean isFirstOrder()	{	return firstOrder; 	}
	public String[] getTerms() {	return terms;	}
	
	public String toString() {
		String formString = null;
		if (!firstOrder) {
			formString += descriptor;
		}
		else {
			formString += descriptor;
			formString += "(";
			for (int i = 0; i < terms.length; i++) {
				formString += terms[i];
				if (i+1 < terms.length)
					formString += ", ";
			}
			formString += ")";
		}
		return formString;
	}
	
}
