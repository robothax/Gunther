package data_structures;

import java.util.ArrayList;

/**
 * Superclass of all formula.
 * @author Jeffrey Kabot
 *
 */
public abstract class Formula implements Cloneable {
	public abstract String toString();
	public abstract Formula clone();
	public abstract ArrayList<Term> getAllTerms();
}
