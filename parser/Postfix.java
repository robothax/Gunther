package parser;

import java.util.Stack;

public class Postfix {
	public Postfix(){
	}
	public String infixToPostfix(String infix){
		String outfix= "";
		Stack<String> stack = new Stack<String>();
		for(int i=0; i<infix.length(); i++){
			if(!checkIfOperand(String.valueOf(infix.charAt(i)))){
				outfix+=(infix.charAt(i)+":");
			}
			else{
				if(stack.isEmpty()) stack.add(String.valueOf(infix.charAt(i)));
				else if(infix.charAt(i)==')'){
					while(!stack.lastElement().equals("(")){
						outfix+=stack.lastElement();
						stack.pop();
					}
					stack.pop();
				}
				else if(getPrecedent(String.valueOf(infix.charAt(i)))<= getPrecedent(stack.lastElement()) &&
						!stack.lastElement().equals("(")){
					while(!stack.isEmpty() && 
							getPrecedent(String.valueOf(infix.charAt(i)))<= getPrecedent(stack.lastElement())
							&& !stack.lastElement().equals("(")){
						outfix+=stack.lastElement();
						stack.pop();
					}
					stack.add(String.valueOf(infix.charAt(i)));
				}
				else{
					stack.add(String.valueOf(infix.charAt(i)));
				}
			}
		}
		if(!stack.isEmpty()){
			while(!stack.isEmpty()){
				outfix+=stack.lastElement();
				stack.pop();
			}
		}
		return outfix;
	}
	public String placeHolder(String token){
		return token;
	}
	public static boolean checkIfOperand(String oper){
		if(oper.equals("v") || oper.equals("^") || oper.equals(">") || oper.equals("(") || oper.equals(")")
				|| oper.equals("=") || oper.equals("~")) return true;
		return false;
	}
	
	public static int getPrecedent(String operator){
		if(operator.equals("=")) return 4;
		else if(operator.equals(">")) return 3;
		else if(operator.equals("v"))return 2;
		else if(operator.equals("^"))return 1;
		else if(operator.equals("~"))return 0;
		return 5;
	}
}
