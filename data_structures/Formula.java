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
	
	public Operator getOperator() {
		if (atomic)
			return function;
		else
			return null;
	}
	public Formula[] getArguments() {
		if (atomic) 
			return arguments;
		else
			return null;
	}
	
	
	public boolean isAtomic() {
		return atomic;
	}
	public char getName() {
		if (atomic)
			return literal;
		return 0;
	}
	
}
