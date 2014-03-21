package iit.fenghuang.sudokusolver;
public class Unit {
	int index;
	int row;
	int col;
	int box;
	int value;
	boolean fixed = false;
	public Unit(int index, int value) {
		super();
		this.index = index;
		this.value = value;
		this.row = (index-1)/9 + 1;
		this.col = index%9;
		if (this.col == 0) this.col = 9;
		this.box = ((this.row-1)/3)*3 + (this.col-1)/3 + 1;
		this.value = value;
		if (this.value != 0)
			this.fixed = true;
	}
	
}
