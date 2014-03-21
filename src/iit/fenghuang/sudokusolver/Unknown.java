package iit.fenghuang.sudokusolver;


public class Unknown {
	int row;
	int col;
	int box;
	int index;
	boolean[] tried;
	public Unknown(int row, int col, int box, int index) {
		super();
		this.row = row;
		this.col = col;
		this.box = box;
		this.index = index;
		this.tried = new boolean[10];
		for(int i = 1; i <= 9; i++)
			tried[i] = false;
	}
	public void setTried(int i){
		tried[i] = true;
	}
	public boolean isTried(int i){
		return tried[i];
	}
	public void clearTried(){
		for(int i = 1; i <= 9; i++)
			tried[i] = false;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "("+row+","+col+")";
	}
}
