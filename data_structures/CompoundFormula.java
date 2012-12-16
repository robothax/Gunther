package data_structures;

public class CompoundFormula {
	private Operator function;
	private Object arguments[];
	public CompoundFormula(Operator func, Object args[]){
		function = func;
		arguments = args;
	}
	public Operator getOperator() { return function; }
	public Object[] getArguments() { return arguments; }
	
}
