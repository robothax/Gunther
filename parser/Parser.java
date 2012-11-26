package parser;

import data_structures.*;

public class Parser {
	
	public static final String IMPLICATION_SYM = "=>";
	public static final String CONJUNCTION_SYM = "/\\";
	public static final String DISJUNCTION_SYM = "\\/";
	public static final String NEGATION_SYM = "~";
	public static final String EQUIVALENCE_SYM = "<=>";
	
	public static Sequent parse(String input) {
		Sequent init = new Sequent();
		
		Formula[] literals = new Formula[26]; //dynamically create this later
		
		int len = input.length();
		int i = 0;
		
		/* Pass 1: populate a list of active literals */
		while(i < len) {
			char buf = input.charAt(i);
			if(Character.isLetter(buf)) {
				if (literals[buf-'A'] != null) {
					literals[buf-'A'] = new Formula(buf);
				}
			}
		}
		
		i = 0;
		/* Pass 2: generate the initial formula structure */
		while(i < len) {
			
		}
		
		return init;
	}
}
