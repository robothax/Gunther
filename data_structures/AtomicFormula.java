package data_structures;

/**
 * An atomic formula is either a proposition variable with no connective (a literal, i.e. A),
 * or is a first-order formula of one or more terms (i.e. P(x))
 * @author Jeffrey Kabot, Aaron Meltzer
 *
 */
public class AtomicFormula extends Formula {
	
	private char descriptor;
	private char[] terms;
	
	private boolean firstOrder;
	
	/**
	 * Constructor for atomic formula.
	 * @param lit Capital letter representative of the formula.
	 * @param t List of terms, can be empty.  If non-empty then the formula is first-order.
	 */
	public AtomicFormula(char lit, char... t) {
		if (t.length == 0) {
			firstOrder = false;
			terms = null;
		}
		else {
			firstOrder =  true;
			terms = new char[t.length];
		}
		
		descriptor = lit;
		
		for (int i = 0; i < t.length; i++) {
			terms[i] = t[i];
		}
	}
	
	public boolean isFirstOrder()	{	return firstOrder; 	}
	public char[] getTerms() {	return terms;	}
	
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
