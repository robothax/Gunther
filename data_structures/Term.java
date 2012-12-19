package data_structures;

public class Term {
	private String variable;
	
	public Term(String v) {
		variable = v;
	}
	
	public void setTerm(String t) {
		variable = t;
	}
	
	public String toString() {
		return variable;
	}
}
