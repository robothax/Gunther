package parser;

import java.util.Stack;

import data_structures.AtomicFormula;
import data_structures.CompoundFormula;
import data_structures.FirstOrderFormula;
import data_structures.Formula;
import data_structures.FormulaList;
import data_structures.Operator;
import data_structures.Sequent;
import data_structures.Term;

public class Parser {
	
	private static Parser parse;
	private static Stack<Formula> formuli;
	
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
	
	public static Sequent parse(String statement){
		
		formuli = new Stack<Formula>();
		
		String currentLiteral="";
		statement=Postfix.infixToPostfix(statement);
		Atomizer tp = new Atomizer();
		
		for(int i=0; i<statement.length(); i++){
			//we will add this token to the stack of formuli if it is not an operator, it is not a parenthese
			//and it starts with a capital letter
			if(Postfix.getPrecedent(String.valueOf(statement.charAt(i)))==6 && statement.charAt(i)!='(' &&
					statement.charAt(i)!=')' && Character.isUpperCase(statement.charAt(i))){
				while(statement.charAt(i)!=':'){
					currentLiteral+=statement.charAt(i);
					i++;
				}
				if (Character.isUpperCase(currentLiteral.charAt(0)))
					tp.addAtomic(currentLiteral);
				//else
					
				currentLiteral="";
			}
		}
		
		FormulaList init = new FormulaList();
		init.add(secondRun(statement, tp));
		
		return new Sequent(new FormulaList(), init);
	}
	private static Formula secondRun(String statement, Atomizer tp){
		
		for(int i=0; i<statement.length(); i++){
			
			//if it is not an operand
			char arg = statement.charAt(i);
			
			if(Postfix.getPrecedent(String.valueOf(arg))==6) {
				String currentLiteral = "";
				
				while(statement.charAt(i)!=':'){
					currentLiteral+=statement.charAt(i);
					i++;
				}
				/* if the first letter is upper  case, then it is an atomic formula of some kind */
				/* if it isn't first order then it is a flyweight, (all instances of it are the same instance) */
				if (Character.isUpperCase(currentLiteral.charAt(0)) && !currentLiteral.contains("(")) {
					formuli.add(tp.getAtomic(currentLiteral));
				}
				else if (Character.isUpperCase(currentLiteral.charAt(0)) && currentLiteral.contains("(")) {
					int parenthesesIndex = currentLiteral.indexOf('(');
					
					formuli.add(new AtomicFormula(currentLiteral.substring(0, parenthesesIndex), tp.getTerms(currentLiteral)));
				}
				else {
					//Term t = new Term(currentLiteral);
				}
			}
			//if it is an operand
			else{
				Operator oper= tp.getOperator(statement.charAt(i));
				int arity = oper.getArity();
				Formula[] args = new Formula[arity];
				for(int j=arity-1; j>-1; j--){
					args[j]=formuli.remove(formuli.size()-1);
				}
				Formula newForm=null;
				
				if(oper.equals(Operator.UNIVERSAL) || oper.equals(Operator.EXISTENTIAL)) {
					
					String quantString = "";
					quantString += statement.charAt(i);
					
					while (statement.charAt(++i) != '!') {
						quantString+= statement.charAt(i);
					}
					Term[] quantTerms = tp.getQuantTerms(quantString);
					
					Term[] argTerms = new Term[0];
					argTerms = args[0].getAllTerms().toArray(argTerms);
					
					/* 
					 * ensure the variable bound under the quantifier is the same 
					 * object reference as the occurences of that variable in hte formula
					 */
					for (int j = 0; j < quantTerms.length; j++) {
						for (int k = 0; k < argTerms.length; k++) {
							if (quantTerms[j].toString().equals(argTerms[k].toString())) {
								args[0].bindTerm(quantTerms[j]);
							}
						}
					}
					
					
					newForm = new FirstOrderFormula(args[0] ,oper, quantTerms);
				}
				else{
					newForm = new CompoundFormula(oper, args);
				}
				formuli.add(newForm);
			}
		}
		return formuli.pop();
	}
}