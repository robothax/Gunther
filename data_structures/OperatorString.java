package data_structures;

public class OperatorString {
	private char operate;
	private int precedent;
	public OperatorString(char opera){
		operate=opera;
		if(opera=='>') precedent=2;
		else if(opera=='v')precedent=1;
		else if(opera=='^') precedent=0;
		else{
			System.out.println("not a valid operator");
		}
	}
	public char getOperate(){
		return operate;
	}
	public int getPrecedent(){
		return precedent;
	}
}
