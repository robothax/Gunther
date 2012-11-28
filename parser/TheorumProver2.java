package parser;

import java.util.HashMap;

import data_structures.Formula;
import data_structures.Operator;

public class TheorumProver {
	
	private HashMap<Double, Formula>literals;
	private HashMap<Character, Operator>operators;
	public TheorumProver(){
		literals =new HashMap<Double,Formula>();
		operators = new HashMap<Character, Operator>();
		operators.put('=', Operator.EQUIVALENCE);
		operators.put('>', Operator.IMPLICATION);
		operators.put('v', Operator.DISJUNCTION);
		operators.put('^', Operator.CONJUNCTION);
		operators.put('~', Operator.NEGATION);
	}
	public static void main(String[] args){
		
	}
	public HashMap<Double, Formula> getLiterals() {
		return literals;
	}
	public void setLiterals(HashMap<Double, Formula> literals) {
		this.literals = literals;
	}
	public void addLiteral(String literal){
		Formula toAdd= new Formula(literal);
		double uniqueNumber = 0;
		for(int i=0; i<literal.length(); i++) uniqueNumber+= literal.charAt(i);
		literals.put(uniqueNumber, toAdd);
	}
	public Formula getLiteral(String literal){
		double uniqueNumber=0;
		for(int i=0; i<literal.length(); i++) uniqueNumber+= literal.charAt(i);
		Formula toReturn = literals.get(uniqueNumber);
		return toReturn;
	}
	public Operator getOperator(char op){
		return operators.get(op);
	}
}