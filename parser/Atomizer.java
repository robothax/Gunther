package parser;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import data_structures.AtomicFormula;
import data_structures.Operator;
import data_structures.Term;

public class Atomizer {
	
	private HashMap<Double, AtomicFormula>Atomics;
	private HashMap<Character, Operator>operators;
	public Atomizer(){
		Atomics =new HashMap<Double, AtomicFormula>();
		operators = new HashMap<Character, Operator>();
		operators.put('=', Operator.EQUIVALENCE);
		operators.put('>', Operator.IMPLICATION);
		operators.put('v', Operator.DISJUNCTION);
		operators.put('^', Operator.CONJUNCTION);
		operators.put('~', Operator.NEGATION);
		operators.put('@', Operator.UNIVERSAL);
		operators.put('#', Operator.EXISTENTIAL);
	}
	public void addAtomic(String Atomic){
		AtomicFormula toAdd= new AtomicFormula(Atomic, getTerms(Atomic));
		double uniqueNumber = 0;
		for(int i=0; i<Atomic.length(); i++) uniqueNumber+= Atomic.charAt(i);
		Atomics.put(uniqueNumber, toAdd);
	}
	public AtomicFormula getAtomic(String Atomic){
		double uniqueNumber=0;
		for(int i=0; i<Atomic.length(); i++) uniqueNumber+= Atomic.charAt(i);
		AtomicFormula toReturn = Atomics.get(uniqueNumber);
		return toReturn;
	}
	public Operator getOperator(char op){
		return operators.get(op);
	}
	public Term[] getTerms(String Atomic){
		Vector<String> vs = new Vector<String>();
		String newString="";;
		for(int i=0; i<Atomic.length(); i++){
			if(Atomic.charAt(i)=='('){
				i++;
				if(i>=Atomic.length()){
					throw new IllegalArgumentException("An open paren must end with a closed paren.");
				}
				while(Atomic.charAt(i)!=')'){
					if(i+1> Atomic.length()){
						throw new IllegalArgumentException("An open paren must end with a closed paren.");
					}
					newString+=Atomic.charAt(i);
					i++;
				}
			}
		}
		StringTokenizer st = new StringTokenizer(newString, ",");
		while(st.hasMoreElements()) vs.add(st.nextToken());
		if(vs.isEmpty()){
			return new Term[0];
		}
		return convertToStringArray(vs);
	}
	public Term[] convertToStringArray(Vector<String> vs){
		Term[] newString = new Term[vs.size()];
		for(int i=0; i<vs.size(); i++){
			newString[i]= new Term(vs.get(i));
		}
		return newString;
	}
}