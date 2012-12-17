package data_structures;

/**
 * A compound formula is a sequence of one or of two subformula connected together by a logical operator.
 * @author Jeffrey Kabot, Aaron Meltzer
 *
 */
public class CompoundFormula extends Formula {
	
	private Operator op;
	private Formula arguments[];
	
	/**
	 * Constructor for compound formula.
	 * @param o The logical connective acting upon the sequence of subformula
	 * @param args Sequence of subformla
	 */
	public CompoundFormula(Operator o, Formula... args) {
		op = o;
		arguments = args;
	}
	
	public Operator getOperator() 	{	return op;		}
	public Formula[] getArguments() {	return arguments;	}
	
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
}
