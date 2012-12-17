package parser;

import java.util.Stack;

public class Postfix {
	public Postfix(String input){
	}
	public static String infixToPostfix(String infix){
		String outfix= "";
		Stack<String> stack = new Stack<String>();
		//go through the whole input
		for(int i=0; i<infix.length(); i++){
			/*
			 * If the current character is not an operand just add it to the outfix. Also put a ":" to signify
			 * that it is not an operand. 
			 */
			if(getPrecedent(String.valueOf(infix.charAt(i)))==6){
				//lower case is just a variable, upper case is a statement
				if(infix.charAt(i)>64 && infix.charAt(i)<91){
					while(infix.charAt(i)!=')'){
						outfix+=infix.charAt(i);
						i++;
					}
					outfix+=')';
					outfix+=":";
				}
				else{
					outfix+=(infix.charAt(i)+":");
				}
			}
			else{
				//If the stack is empty put the current infix value in the stack. No point in emptying an
				//empty stack
				if(stack.isEmpty()) stack.add(String.valueOf(infix.charAt(i)));
				/*if you reach a right paren, empty the stack until you reach the left paren
				 * and stop. Don't put that in the outfix (we don't want any parenthesis) so stop just before,
				 * then pop it off the stack
				 */
				else if(infix.charAt(i)==')'){
					while(!stack.lastElement().equals("(")){
						outfix+=stack.lastElement();
						stack.pop();
					}
					stack.pop();
				}
				/*
				 * If you get to an element that has a lower precedent, and its not a "(" pop it off the stack
				 * like before
				 */
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
				/*
				 * otherwise just add the operator to the stack
				 */
				else{
					stack.add(String.valueOf(infix.charAt(i)));
				}
			}
		}
		/*
		 * if there is anything left in the stack, pop it to outfix. There won't be any parens left so
		 * no need to worry about them
		 */
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
	
	public static int getPrecedent(String operator){
		if(operator.equals("@") || operator.equals("#")) return 2;
		else if(operator.equals("=")) return 0;
		else if(operator.equals(">")) return 1;
		else if(operator.equals("v"))return 3;
		else if(operator.equals("^"))return 4;
		else if(operator.equals("~"))return 5;
		return 6;
	}
}
