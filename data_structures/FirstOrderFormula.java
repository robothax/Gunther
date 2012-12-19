package data_structures;

import java.util.ArrayList;

/**
 * A first-order formula is one with a quantifier on some number of terms paired with some formula,
 * possibly compound or atomic (but, importantly, not first-order again), in which the terms are bound or are free
 * (i.e. Ex [P(x) \/ A])
 * @author Jeffrey Kabot
 *
 */
public class FirstOrderFormula extends Formula implements Cloneable {
	
	private Formula arg;
	private Operator quantifier;
	private Term[] boundTerms;
	private ArrayList<Term> allTerms;
	
	/**
	 * Constructor for first-order formula
	 * @param f The inner formula
	 * @param q Quantifier on the terms
	 * @param t List of terms
	 */
	public FirstOrderFormula(Formula f, Operator q, Term... t) {
		if (q != Operator.UNIVERSAL && q != Operator.EXISTENTIAL)
			throw new IllegalArgumentException("Invalid quantifier");
		
		arg = f;
		quantifier = q;
		
		boundTerms = t;
		
		allTerms = new ArrayList<Term>();
		allTerms.addAll(arg.getAllTerms());
		for (int i = 0; i < boundTerms.length; i++) {
			allTerms.add(boundTerms[i]);
		}
	}
	
	public Operator getQuantifier() {	return quantifier;	}
	public Formula getArgument() 	{	return arg;		}
	public Term[] getBoundTerms()	{	return boundTerms;		}
	
	public ArrayList<Term> getAllTerms() {
		return allTerms;
	}
	
	public String toString() {
		String formString = "";
		
		formString += quantifier.toString();
		for (int i = 0; i < boundTerms.length; i++) {
			formString += boundTerms[i].toString();
			if (i+1 < boundTerms.length)
				formString += ",";
		}
		
		formString += " [" + arg.toString() + "]";
		return formString;
	}
	
	public FirstOrderFormula clone() {
		Term[] cloneTerms = new Term[boundTerms.length];
		for (int i = 0; i < boundTerms.length; i++) {
			cloneTerms[i] = new Term(boundTerms[i].toString());
		}
		
		Formula f = arg.clone();
		Term[] argTerms = new Term[0];
		argTerms = f.getAllTerms().toArray(argTerms);
		for (int i = 0; i < cloneTerms.length; i++) {
			for (int j = 0; j < argTerms.length; j++) {
				if (cloneTerms[i].toString().equals(argTerms[j].toString()))
					cloneTerms[i] = argTerms[j];
			}
		}
		
		FirstOrderFormula fof = new FirstOrderFormula(f, quantifier, cloneTerms);
		return fof;
	}
	
	public void bindTerm(Term b) {
		arg.bindTerm(b);
	}
}