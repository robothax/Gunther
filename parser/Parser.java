package parser;

import java.util.Stack;

import data_structures.CompoundFormula;
import data_structures.FirstOrderFormula;
import data_structures.Formula;
import data_structures.Operator;

public class Parser {
	
	private static Parser parse;
	private TheorumProver tp;
	private String statement;
	private Stack<Formula> formuli;
	
	private Parser(){
		tp = new TheorumProver();
		formuli = new Stack<Formula>();
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
				tp.addAtomic(currentLiteral);
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
				formuli.add(tp.getAtomic(currentLiteral));
			}
			else{
				Operator oper= tp.getOperator(statement.charAt(i));
				int arity = oper.getArity();
				Formula[] args = new Formula[arity];
				for(int j=0; j<arity; j++){
					args[j]=formuli.remove(formuli.size()-1);
				}
				Formula newForm=null;
				if(oper.equals(Operator.UNIVERSAL) || oper.equals(Operator.EXISTENTIAL)){
					
					newForm = new FirstOrderFormula(args[0],oper, tp.getTerms(statement) );
				}
				else{
					newForm = new CompoundFormula(oper, (Formula[])args);
				}
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