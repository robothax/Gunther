package data_structures;

import java.util.ArrayList;
import java.util.LinkedList;

public class FormulaList extends LinkedList<Formula> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Formula> atoms;
	
	public FormulaList() {
		super();
		atoms = new ArrayList<Formula>();
	}
	public FormulaList(Formula f){
		super();
		atoms = new ArrayList<Formula>();
		atoms.add(f);
	}
	
	public boolean add(Formula f) {
		if (f instanceof AtomicFormula)
			return atoms.add(f);
		else
			return super.add(f);
	}
	public void add(int index, Formula f) {
		if (f instanceof AtomicFormula) {
			atoms.add(f);
		}
		else {
			super.add(index, f);
		}
	}
	public void addFirst(Formula f) {
		if (f instanceof AtomicFormula)
			atoms.add(f);
		else
			super.addFirst(f);
	}
	public void addLast(Formula f) {
		if (f instanceof AtomicFormula)
			atoms.add(f);
		else
			super.addLast(f);
	}

	
	
	public boolean remove(Formula f) {
		if (f instanceof AtomicFormula)
			return atoms.remove(f);
		else
			return super.remove(f);
	}
	public Formula remove(int index) {
		Formula f = super.remove(index);
		/*if (f instanceof AtomicFormula) {
			atoms.remove(f);
		}*/
		return f;
	}
	public Formula removeFirst() {
		Formula f = super.removeFirst();
		/*if (f instanceof AtomicFormula) {
			atoms.remove(f);
		}*/
		return f;
	}
	public Formula removeLast() {
		Formula f = super.removeLast();
		/*if (f instanceof AtomicFormula) {
			atoms.remove(f);
		}*/
		return f;
	}
	
	public Formula get(int index) {
		return super.get(index);
	}
	
	public boolean isAtomic() {
		return (this.size() == 0);
	}
	
	public ArrayList<Formula> getAtoms() {
		return atoms;
	}
	
	public Formula[] toArray() {
		Formula[] farr = new Formula[this.size() + atoms.size()];
		Formula[] aarr = new Formula[atoms.size()];
		farr = super.toArray(farr);
		aarr = atoms.toArray(aarr);
		
		int j = this.size();
		for(int i = 0; i < atoms.size(); i++) {
			farr[j+i] = atoms.get(i);
		}
		
		return farr;
	}

}

