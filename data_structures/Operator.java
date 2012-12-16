package data_structures;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
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
	
	public static final Operator EXISTENTIAL = new Operator(OperatorType.EXISTENTIAL, 1);
	public static final Operator UNIVERSAL = new Operator(OperatorType.UNIVERSAL, 1);
	
	public static final String IMPLICATION_SYM = "=>";
	public static final String CONJUNCTION_SYM = "/\\";
	public static final String DISJUNCTION_SYM = "\\/";
	public static final String NEGATION_SYM = "~";
	public static final String EQUIVALENCE_SYM = "<=>";
	
	public static final String EXISTENTIAL_SYM = "\u2204";
	public static final String UNIVERSAL_SYM = "\u2200";
	
	public int getArity() {		return arity;	}
	public OperatorType getType() {	return type;	}
	
	public String toString() {
		switch(type) {
			case NEGATION:
				return NEGATION_SYM;
			case IMPLICATION:
				return IMPLICATION_SYM;
			case CONJUNCTION:
				return CONJUNCTION_SYM;
			case DISJUNCTION:
				return DISJUNCTION_SYM;
			case EQUIVALENCE:
				return EQUIVALENCE_SYM;
			case EXISTENTIAL:
				return EXISTENTIAL_SYM;
			case UNIVERSAL:
				return UNIVERSAL_SYM;
			default:
				return null;
		}
	}
}
