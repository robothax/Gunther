package parser;

import java.util.Stack;

import data_structures.CompoundFormula;
import data_structures.Operator;

public class Parser {
	
	private static Parser parse;
	private TheorumProver tp;
	private String statement;
	private Stack<Object> formuli;
	
	private Parser(){
		tp = new TheorumProver();
		formuli = new Stack<Object>();
	}
	public static Parser getParser(){
		if(parse==null){
			parse= new Parser();
			return parse;
		}
		return parse;
	}
	
	public void firstRun(){
		String currentLiteral="";
		for(int i=0; i<statement.length(); i++){
			if(!Postfix.checkIfOperand(String.valueOf(statement.charAt(i)))){
				while(statement.charAt(i)!=':'){
					currentLiteral+=statement.charAt(i);
					i++;
				}
				tp.addLiteral(currentLiteral);
				currentLiteral="";
			}
		}
	}
	public Object secondRun(){
		for(int i=0; i<statement.length(); i++){
			if(!Postfix.checkIfOperand(String.valueOf(statement.charAt(i)))){
				String currentLiteral = "";
				while(statement.charAt(i)!=':'){
					currentLiteral+=statement.charAt(i);
					i++;
				}
				formuli.add(tp.getLiteral(currentLiteral));
			}
			else{
				Operator oper= tp.getOperator(statement.charAt(i));
				int arity = oper.getArity();
				Object[] args = new Object[arity];
				for(int j=0; j<arity; j++){
					args[j]=formuli.remove(formuli.size()-1);
				}
				CompoundFormula newForm = new CompoundFormula(oper, args);
				this.formuli.add(newForm);
			}
		}
		return this.formuli.pop();
	}
	public void setTheorumProver(TheorumProver tp){
		this.tp=tp;
	}
	public void setStatement(String statement){
		this.statement=statement;
	}
}