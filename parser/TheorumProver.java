package parser;

import java.util.HashMap;

import data_structures.LiteralFormula;
import data_structures.Operator;

public class TheorumProver {
	
	private HashMap<Double, LiteralFormula>literals;
	private HashMap<Character, Operator>operators;
	public TheorumProver(){
		literals =new HashMap<Double, LiteralFormula>();
		operators = new HashMap<Character, Operator>();
		operators.put('=', Operator.EQUIVALENCE);
		operators.put('>', Operator.IMPLICATION);
		operators.put('v', Operator.DISJUNCTION);
		operators.put('^', Operator.CONJUNCTION);
		operators.put('~', Operator.NEGATION);
	}
	public static void main(String[] args){
		
	}
	public HashMap<Double, LiteralFormula> getLiterals() {
		return literals;
	}
	public void setLiterals(HashMap<Double, LiteralFormula> literals) {
		this.literals = literals;
	}
	public void addLiteral(String literal){
		LiteralFormula toAdd= new LiteralFormula(literal);
		double uniqueNumber = 0;
		for(int i=0; i<literal.length(); i++) uniqueNumber+= literal.charAt(i);
		literals.put(uniqueNumber, toAdd);
	}
	public LiteralFormula getLiteral(String literal){
		double uniqueNumber=0;
		for(int i=0; i<literal.length(); i++) uniqueNumber+= literal.charAt(i);
		LiteralFormula toReturn = literals.get(uniqueNumber);
		return toReturn;
	}
	public Operator getOperator(char op){
		return operators.get(op);
	}
}