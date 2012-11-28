package parser;

<<<<<<< HEAD
import data_structures.*;
/**
 * 
 * @author Jeffrey Kabot
 *
 */
public class Parser {
	
	public static final String IMPLICATION_SYM = "=>";
	public static final String CONJUNCTION_SYM = "/\\";
	public static final String DISJUNCTION_SYM = "\\/";
	public static final String NEGATION_SYM = "~";
	public static final String EQUIVALENCE_SYM = "<=>";
=======
import java.util.Stack;

import data_structures.Formula;
import data_structures.Operator;

public class Parser {
	
	private static Parser parse;
	private TheorumProver tp;
	private String statement;
	private Stack<Formula> formuli;
	
	public static void main(String[] args){
		Parser parse=Parser.getParser();
		parse.setStatement((new Postfix()).infixToPostfix("A>B"));
		
		parse.firstRun();
		Formula finalStatement=parse.secondRun();
	}
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
>>>>>>> finished parser, but requires testing, along with a few minor changes to other files
	
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
	public Formula secondRun(){
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
				Formula[] args = new Formula[arity];
				for(int j=0; j<arity; j++){
					args[j]=formuli.remove(formuli.size()-1);
				}
				Formula newForm = new Formula(oper, args);
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