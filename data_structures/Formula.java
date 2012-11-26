package data_structures;

public class Formula {
	
	private boolean atomic;
	
	private char literal;
	
	private Operator function;
	private Formula arguments[];
	
	/**
	 * Constructor for non-atomic formula.
	 * @param func
	 * @param args
	 */
	public Formula(Operator func, Formula... args) {
		atomic = false;
			
		function = func;
		arguments = new Formula[func.getArity()];
		
		for(int i = 0; i < func.getArity(); i++) {
			arguments[i] = args[i];
		}
	}
	
	/**
	 * Constructor for atomic formula.
	 * @param lit
	 */
	public Formula(char lit) {
		atomic = true;
		
		function = null;
		arguments = null;
		
		literal = lit;
	}
	
	public Operator getOperator() {		return function;	}
	public Formula[] getArguments() {	return arguments;	}
	
	public boolean isAtomic() {		return atomic;		}
	
	
	public String toString() {
		String formString = null;
		if (atomic) {
			formString += literal;		//if the formula is atomic just give the literal
		}
		else {
			if (function.getArity() == 1) {	//if  the formula is composed of only one subformula then prefix the subformula with the operator
				formString += function.toString() + arguments[0].toString();
			}
			else {				//if the formula is composed of numerous subformulas then put the operator between each subformula and bookend with parentheses
				formString += "(" + arguments[0].toString();
				for (int i = 1; i < function.getArity(); i++) {
					formString += function.toString() + arguments[i].toString();
				}
				formString += ")";
			}
		}
		return formString;
	}
}
