package data_structures;

import java.util.ArrayList;

/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class Term {
	private String variable;
	private static ArrayList<String> metavariables;
	private boolean meta;
	
	public Term(String v) {
		meta = false;
		variable = v;
	}
	
	public void setVariable(String t) {
		variable = t;
	}
	
	public String toString() {
		return variable;
	}
	
	public boolean isMeta() {
		return meta;
	}
	public void setMeta(boolean m) {
		meta = m;
	}
	
	/**
	 * Given a list of terms, returns a new term not in that list.
	 * Used to instantiate terms which need that the term is not bound elsewhere.
	 * @param terms
	 * @return
	 */
	public static String getUnique(Term[] terms) {
		
		boolean usedTerms[] = new boolean[26];
		for (int i = 0; i < terms.length; i++) {
			usedTerms[terms[i].variable.charAt(0) - 'a'] = true;
		}
		
		char u = 'a';
		for (int i = 0; i < 26; i++) {
			if (!usedTerms[i]) {
				u+= i;
				return "" + u;
			}
		}
		return null;
	}
	
	/**
	 * Initializes the list of metavariables.  Must be called before attempting to access metavariables.
	 */
	public static void initMetavariables() {
		metavariables = new ArrayList<String>();
		metavariables.ensureCapacity(24);
		char u = '\u03B1';
		for (int i = 0; i < 24; i++) {
			metavariables.add("" + u++);
		}
	}
	
	/**
	 * Gives the first metavariable remaining in the list of metavariables.
	 * Used to give dummy terms to be instantiated later.
	 * @return Returns a string representing the metavariable.
	 */
	public static String getMetavariable() {
		if (metavariables == null || metavariables.isEmpty())
			return null;
		else
			return metavariables.remove(0);
	}
	
	
}
