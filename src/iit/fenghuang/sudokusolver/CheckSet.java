package iit.fenghuang.sudokusolver;




public class CheckSet {
	boolean[] possibles;
	int id;
	String type;
	public CheckSet(String type, int id) {
		super();
		this.type = type;
		this.id = id;
		possibles = new boolean[10];
		setAllPossible();
	}
	public void addPossible(int i){
		possibles[i] = true;
	}
	public void delPossible(int i){
		possibles[i] = false;
	}
	public boolean isPossible(int i){
		return possibles[i];
	}
	public int getPossible(){ // return first true number, if all false, return -1
		for(int i = 1; i <= 9; i++)
			if(possibles[i])
				return i;
		return -1;
	}
	public void setAllPossible(){
		for(int i = 1; i <= 9; i++)
			possibles[i] = true;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = type+" "+id+" : ";
		for (int i = 1; i <=9; i++)
			if (possibles[i])
				str = str + i + " ";
			else
				str = str + "_" + " ";
		return str;
	}
}
