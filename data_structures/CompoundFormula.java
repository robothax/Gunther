package data_structures;

import java.util.ArrayList;

/**
 * A compound formula is a sequence of one or of two subformula connected together by a logical operator.
 * @author Jeffrey Kabot, Aaron Meltzer
 *
 */
public class CompoundFormula extends Formula {
	
	private Operator op;
	private Formula arguments[];
	private ArrayList<Term> terms;
	boolean firstOrder = false;
	
	/**
	 * Constructor for compound formula.
	 * @param o The logical connective acting upon the sequence of subformula
	 * @param args Sequence of subformla
	 */
	public CompoundFormula(Operator o, Formula... args) {
		op = o;
		arguments = args;
		
		terms = new ArrayList<Term>();
		for (int i = 0; i < args.length; i++) {
			terms.addAll(args[i].getAllTerms());
		}
		
		/* we say that a compound formula is first order if one of its arguments is first order or any subargument of its arguments are first order
		 * i.e. that a first order formula appears somewhere in it
		 */
		for (Formula f: args) {
			if (f instanceof FirstOrderFormula || (f instanceof CompoundFormula && ((CompoundFormula)f).firstOrder))
				firstOrder = true;
		}
		
		/*
		 * Don't merge between first order arguments
		 */
		/*if (!firstOrder) {
			
		}*/
		/* merge terms */
		//boolean merge = (arguments[0] instanceof AtomicFormula && !(arguments[1] instanceof FirstOrderFormula)) || (arguments[1] instanceof AtomicFormula && !(arguments[0] instance of FirstOrderFormula)) 
		/*if (args.length == 2 && (arguments[0] instanceof AtomicFormula) && (arguments[1] instanceof AtomicFormula)) {
			Term[] t0 = ((AtomicFormula)arguments[0]).getTerms();
			Term[] t1 = ((AtomicFormula)arguments[1]).getTerms();
			
			for (int i = 0; i < t0.length; i++) {
				for (int j = 0; j < t1.length; j++) {
					if ()
				}
			}
		}*/
	}
	
	public Operator getOperator() 	{	return op;		}
	public Formula[] getArguments() {	return arguments;	}
	
	public ArrayList<Term> getAllTerms() {
		return terms;
	}
	
	public String toString() {
		String formString = "";
		if (op.getArity() == 1) {	//if  the formula is composed of only one subformula then prefix the subformula with the operator
			formString += op.toString() + arguments[0].toString();
		}
		else {				//if the formula is composed of numerous subformulas then put the operator between each subformula and bookend with parentheses
			formString += "(" + arguments[0].toString();
			for (int i = 1; i < op.getArity(); i++) {
				formString += op.toString() + arguments[i].toString();
			}
			formString += ")";
		}
		return formString;
	}

	public CompoundFormula clone() {
		Formula[] cloneArgs = new CompoundFormula[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			cloneArgs[i] = arguments[i].clone();
		}
		CompoundFormula cf = new CompoundFormula(op, cloneArgs);
		//copy terms
		
		return cf;
	}
	
	public void bindTerm(Term b) {
		for (int i = 0; i < arguments.length; i++) {
			arguments[i].bindTerm(b);
		}
	}
}
