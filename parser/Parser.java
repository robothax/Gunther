package parser;

import java.util.Stack;

import data_structures.CompoundFormula;
import data_structures.FirstOrderFormula;
import data_structures.Formula;
import data_structures.FormulaList;
import data_structures.Operator;
import data_structures.Sequent;

public class Parser {
	
	private static Parser parse;
	private Stack<Formula> formuli;
	
	private Parser(){
		formuli = new Stack<Formula>();
	}
	public static Parser getParser(){
		if(parse==null){
			parse= new Parser();
			return parse;
		}
		return parse;
	}
	
	public Sequent initParser(String statement){
		String currentLiteral="";
		statement=Postfix.infixToPostfix(statement);
		Atomizer tp = new Atomizer();
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
		
		return new Sequent(new FormulaList(),new FormulaList(secondRun(statement, tp)));
	}
	private Formula secondRun(String statement, Atomizer tp){
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
				for(int j=arity-1; j>-1; j--){
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
}