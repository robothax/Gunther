package prover;

import data_structures.*;

import java.util.LinkedList;
import java.util.Vector;

public class Prover implements Runnable{
	
	private static Vector<Sequent> branches;
	
	
	public void initProof(Sequent query) { //initialize proof
		branches = new Vector<Sequent>();
		branches.add(query);
	}
	
	public void Prove(Sequent branch) { //leave this void for now?
		
		//make a new thread for each sequent in "branches"
		//run the until the sequent is atomic and determine if axiom
	}
	
	public void run() {
		
	}
	
	private void leftNegation(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		f = f.getArguments()[0]; //refactor f, remove the negation
		
		right.addFirst(f);
	}
	
	private void rightNegation(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		f = f.getArguments()[0]; //refactor f by removing the negation
		
		left.addFirst(f);
	}
	
	private void leftConjunction(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		left.addFirst(f_sub1);
		left.addFirst(f_sub2);
	}
	
	private void rightConjunction(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		branches.remove(branch);
		
		Sequent branch1 = new Sequent(left, right);
		branch1.getConclusions().addFirst(f_sub1);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getConclusions().addFirst(f_sub2);
		
		branches.add(branch1);
		branches.add(branch2);
	}
	
	private void leftDisjunction(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		branches.remove(branch);
		
		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);
		
		branches.add(branch1);
		branches.add(branch2);
	}
	
	private void rightDisjunction(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		right.addFirst(f_sub2);
		right.addFirst(f_sub1);
	}
	
	private void leftImplication(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		branches.remove(branch);
		
		Sequent branch1 = new Sequent(left, right);
		branch1.getConclusions().addFirst(f_sub1);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);
		
		branches.add(branch1);
		branches.add(branch2);
	}
	
	private void rightImplication(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		left.addFirst(f_sub1);
		right.addFirst(f_sub2);
	}
	
	private void leftEquivalence(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = left.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		branches.remove(branch);
		
		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);
		branch1.getHypotheses().addFirst(f_sub2);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getConclusions().addFirst(f_sub2);
		branch2.getConclusions().addFirst(f_sub1);
		
		branches.add(branch1);
		branches.add(branch2);
	}
	
	private void rightEquivalence(Sequent branch) {
		LinkedList<Formula> left = branch.getHypotheses();
		LinkedList<Formula> right = branch.getConclusions();
		
		Formula f = right.removeFirst();
		
		Formula f_sub1 = f.getArguments()[0];
		Formula f_sub2 = f.getArguments()[1];
		
		branches.remove(branch);
		
		Sequent branch1 = new Sequent(left, right);
		branch1.getHypotheses().addFirst(f_sub1);
		branch1.getConclusions().addFirst(f_sub2);
		
		Sequent branch2 = new Sequent(left, right);
		branch2.getHypotheses().addFirst(f_sub2);
		branch2.getConclusions().addFirst(f_sub1);
	}

}
