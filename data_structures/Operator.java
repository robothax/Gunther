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
	
	public static final String IMPLICATION_SYM = "=>";
	public static final String CONJUNCTION_SYM = "/\\";
	public static final String DISJUNCTION_SYM = "\\/";
	public static final String NEGATION_SYM = "~";
	public static final String EQUIVALENCE_SYM = "<=>";
	
	public int getArity() {
		return arity;
	}
	
	public String toString() {
		if (this.equals(NEGATION))
			return NEGATION_SYM;
		else if (this.equals(IMPLICATION))
			return IMPLICATION_SYM;
		else if (this.equals(CONJUNCTION))
			return CONJUNCTION_SYM;
		else if (this.equals(DISJUNCTION))
			return DISJUNCTION_SYM;
		else if (this.equals(EQUIVALENCE))
			return EQUIVALENCE_SYM;
		else
			return null;
	}
}
