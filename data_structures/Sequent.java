package data_structures;

import java.util.LinkedList;

public class Sequent {
	 
	private LinkedList<Formula> Hypotheses;
	private LinkedList<Formula> Conclusions;
	
	public Sequent() {
		Hypotheses = new LinkedList<Formula>();
		Conclusions = new LinkedList<Formula>();
	}
	
	public Sequent(LinkedList<Formula> hypo, LinkedList<Formula> conc) {
		Hypotheses = hypo;
		Conclusions = conc;
	}
	
	public LinkedList<Formula> getHypotheses() {	return Hypotheses;	}
	public LinkedList<Formula> getConclusions() {	return Conclusions;	}
	
	
}
