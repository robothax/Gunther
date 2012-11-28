package data_structures;

/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class Formula {
	
	private boolean atomic;
	
	private String literal;
	
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
	public Formula(Operator func, Formula args[], char fake){
		atomic = false;
		
		function = func;
		arguments = args;
	}
	/**
	 * Constructor for atomic formula.
	 * @param lit
	 */
	public Formula(String lit) {
		atomic = true;
		
		function = null;
		arguments = null;
		
		literal = lit;
	}
	
	public Operator getOperator() {		return function;	}
	public Formula[] getArguments() {	return arguments;	}
	
	public boolean isAtomic() {		return atomic;		}
	
<<<<<<< HEAD
=======
	public boolean isAtomic() {
		return atomic;
	}
	public String getName() {
		if (atomic)
			return literal;
		return null;
	}
>>>>>>> finished parser, but requires testing, along with a few minor changes to other files
	
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
