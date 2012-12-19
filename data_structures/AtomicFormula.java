package data_structures;

import java.util.ArrayList;

/**
 * An atomic formula is either a proposition variable with no connective (a literal, i.e. A),
 * or is a first-order formula of one or more terms (i.e. P(x))
 * @author Jeffrey Kabot, Aaron Meltzer
 *
 */
public class AtomicFormula extends Formula {
	
	private String descriptor;
	private Term[] terms;
	private boolean firstOrder;
	
	private ArrayList<Term> allTerms;
	
	/**
	 * Constructor for atomic formula.
	 * @param lit Capital letter representative of the formula.
	 * @param t List of terms, can be empty.  If non-empty then the formula is first-order.
	 */
	public AtomicFormula(String lit, Term... t) {
		if (t.length == 0) {
			firstOrder = false;
		}
		else {
			firstOrder =  true;
		}
		
		descriptor = lit;
		terms = t;
		
		allTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.length; i++)
			allTerms.add(terms[i]);
	}
	
	public boolean isFirstOrder()	{	return firstOrder; 	}
	public Term[] getTerms() {	return terms;	}
	
	public ArrayList<Term> getAllTerms() {
		return allTerms;
	}
	public String getDescriptor() {	return descriptor;	}
	
	public boolean isMeta() {
		if (!firstOrder)
			return false;
		for (int i = 0; i < terms.length; i++) {
			if (terms[i].isMeta())
				return true;
		}
		return false;
	}
	
	public String toString() {
		String formString = "";
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
	
	public AtomicFormula clone() {
		Term[] cloneTerms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++) {
			cloneTerms[i] = new Term(terms[i].toString());
		}
		AtomicFormula af = new AtomicFormula(descriptor, cloneTerms);
		return af;
	}
	
}
