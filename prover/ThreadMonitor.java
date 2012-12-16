package prover;

import java.util.Vector;

public class ThreadMonitor {
	private Vector<Boolean> doneYet;
	private final Boolean FALSE = new Boolean(false);
	public ThreadMonitor(){
		doneYet=new Vector<Boolean>();
	}
	public Vector<Boolean> getDoneYet() {
		return doneYet;
	}
	public void setDoneYet(Vector<Boolean> doneYet) {
		this.doneYet = doneYet;
	}
	
	public void addThread(boolean addBool){
		doneYet.add(addBool);
	}
	public boolean checkIfDone(){
		for(int i=0; i<doneYet.size(); i++){
			if(doneYet.get(i).equals(FALSE)) return false;
		}
		return true;
	}
}
