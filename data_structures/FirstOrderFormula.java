package data_structures;

/**
 * A first-order formula is one with a quantifier on some number of terms paired with some formula,
 * possibly compound or atomic (but, importantly, not first-order again), in which the terms are bound or are free
 * (i.e. Ex [P(x) \/ A])
 * @author Jeffrey Kabot
 *
 */
public class FirstOrderFormula extends Formula {
	
	private Formula arg;
	private Operator quantifier;
	private char[] terms;
	
	/**
	 * Constructor for first-order formula
	 * @param f The inner formula
	 * @param q Quantifier on the terms
	 * @param t List of terms
	 */
	public FirstOrderFormula(Formula f, Operator q, char... t) {
		if (q != Operator.UNIVERSAL && q != Operator.EXISTENTIAL)
			throw new IllegalArgumentException("Invalid quantifier");
		
		arg = f;
		quantifier = q;
		
		terms = t;
	}
	
	public Operator getQuantifier() {	return quantifier;	}
	public Formula getArgument() 	{	return arg;		}
	public char[] getTerms()	{	return terms;		}
	
	public String toString() {
		String formString = null;
		
		formString += quantifier.toString();
		for (int i = 0; i < terms.length; i++) {
			formString += terms[i];
			if (i+1 < terms.length)
				formString += ", ";
		}
		
		formString += " [" + arg.toString() + "]";
		return formString;
	}
}