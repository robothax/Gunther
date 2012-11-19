package data_structures;

public class Operator {
	private OperatorType type;
	private int arity;
	
	private Operator(OperatorType t, int a) {
		type = t;
		arity = a;
	}
	
	public static final Operator NEGATION = new Operator(OperatorType.NEGATION, 1);
	public static final Operator IMPLICATION = new Operator(OperatorType.IMPLICATION, 2);
	public static final Operator CONJUNCTION = new Operator(OperatorType.CONJUNCTION, 2);
	public static final Operator DISJUNCTION = new Operator(OperatorType.DISJUNCTION, 2);
	public static final Operator EQUIVALENCE = new Operator(OperatorType.EQUIVALENCE, 2);
	
	public int getArity() {
		return arity;
	}
}
