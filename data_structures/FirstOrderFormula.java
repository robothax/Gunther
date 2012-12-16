package data_structures;

/**
 * Basically the first order literal. Its not 
 *
 */
public class FirstOrderFormula extends Formula {
	
	private Formula arg;
	private Operator quantifier;
	private String[] terms;
	
	/**
	 * Constructor for first-order formula
	 * @param f The inner formula
	 * @param q Quantifier on the terms
	 * @param t List of terms
	 */
	public FirstOrderFormula(Formula f, Operator q, String[] t) {
		if (q != Operator.UNIVERSAL && q != Operator.EXISTENTIAL)
			throw new IllegalArgumentException("Invalid quantifier. I mean seriously man. wtf.");
		
		arg = f;
		quantifier = q;
		terms = t;
	}
	
	public Operator getQuantifier() {	return quantifier;	}
	public Formula getArgument() 	{	return arg;		}
	public String[] getTerms()	{	return terms;		}
	
	public String toString() {
		String formString = null;
		
		formString += quantifier.toString();
		for (int i = 0; i < terms.length; i++) {
			formString += terms[i];
			if (i+1 < terms.length)
				formString += ", ";
		}
		
		formString += "[" + arg.toString() + "]";
		return formString;
	}
}
