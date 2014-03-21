package iit.fenghuang.sudokusolver;

import java.util.ArrayList;
import java.util.List;


public class Grid {
	Unit[] units;
	CheckSet[] rows;
	CheckSet[] cols;
	CheckSet[] boxes;
	List<Unknown> unknown;
	List<Action> action;
	public Grid() {
		super();
//		units = new Unit[82]; // this is done by each time call load_puzzle()
		rows = new CheckSet[10];
		cols = new CheckSet[10];
		boxes = new CheckSet[10];
		unknown = new ArrayList<Unknown>();
		action = new ArrayList<Action>();
		// Initiate possible sets
		for(int i = 1; i <= 9; i++){
			rows[i] = new CheckSet("row", i);
			cols[i] = new CheckSet("col", i);
			boxes[i] = new CheckSet("box", i);
		}
	}
//	public void print_grid(){
//		int i;
//		System.out.println("--1--2--3--|--4--5--6--|--7--8--9--|---");
//		System.out.println("-----------+-----------+-----------+---");
//		for(i = 1; i <= 81; i++){
//			if (units[i].value == 0)
//				System.out.print("  _");
//			else
//				System.out.print("  "+units[i].value);
//			if (i%3 == 0)
//				System.out.print("  |");
//			if (i%9 == 0){
//				System.out.print(" "+i/9);
//				if(i%27 == 0)
//					System.out.print("\n-----------+-----------+-----------+---\n");
//				else
//					System.out.print("\n");
//			}
//		}
//	}
	// print intermediate records
//	public void print_set(){
//		System.out.println(unknown);
//		int i;
//		for(i = 1; i <= 9; i++)
//			System.out.println(rows[i]);
//		for(i = 1; i <= 9; i++)
//			System.out.println(cols[i]);
//		for(i = 1; i <= 9; i++)
//			System.out.println(boxes[i]);
//	}

	public boolean load_puzzle(String puzzle){
		// records clean up
		for(int i = 1; i <= 9; i++){
			rows[i].setAllPossible();
			cols[i].setAllPossible();
			boxes[i].setAllPossible();
		}
		unknown.clear();
		action.clear();
		units = new Unit[82];
		String str = puzzle;

		// initiate units with puzzle inputs
		for (int i = 1; i <= 81; i++){
			units[i] = new Unit(i, Character.getNumericValue(str.charAt(i)));
			if (units[i].fixed)
				update_possible(units[i], units[i].value);
			else
				unknown.add(new Unknown(units[i].row, units[i].col, units[i].box, units[i].index));
		}
		return true;
	}
	public boolean solve(){
		int i = 0;
		int ret;
		while(i < unknown.size()){
			while(true){
				ret = check_available(unknown.get(i));
				if (ret != 0){
					update_action(unknown.get(i), ret);
					break;
				}
				else if (rollback()){
					unknown.get(i).clearTried();
				}
				else{
					unknown.get(i).clearTried();
					i -= 2;
					break;
				}
			}
			i += 1;
			if (i < 0){
	//			System.err.println("Did not find solution");
				return false;
			}
		}
		return true;
	}
	public String generateOutput(){
		String result = "R";
		for (int i = 1; i <= 81; i++)
			result+= units[i].value;
		return result;
	}
	public void update_action(Unknown u, int value){
		rows[u.row].delPossible(value);
		cols[u.col].delPossible(value);
		boxes[u.box].delPossible(value);
		units[u.index].value = value;
		u.setTried(value);
		action.add(new Action(u, value));
	}
	public boolean rollback(){
		Action act = action.get(action.size()-1);
		action.remove(action.size()-1);
		rows[act.u.row].addPossible(act.value);
		cols[act.u.col].addPossible(act.value);
		boxes[act.u.box].addPossible(act.value);
		units[act.u.index].value = 0;
		int a = check_available(act.u);
		if (a == 0)
			return false;
		else{
			update_action(act.u, a);
			return true;
		}
	}
	public int check_available(Unknown u){
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 1; i <= 9; i++)
			if(rows[u.row].isPossible(i) && cols[u.col].isPossible(i) && boxes[u.box].isPossible(i) && !u.isTried(i))
				list.add(new Integer(i));
		if (list.isEmpty())
			return 0;
		else
			return list.get(0);
	}
	public boolean get_available(int index, int test_value){
			if(rows[units[index].row].isPossible(test_value) && cols[units[index].col].isPossible(test_value) && boxes[units[index].box].isPossible(test_value))
				return true;
			else 
				return false;
	}
	public void update_possible(Unit unit, int value){
		rows[unit.row].delPossible(value);
		cols[unit.col].delPossible(value);
		boxes[unit.box].delPossible(value);
	}
	public void add_possible(Unit u, int value){
		rows[u.row].addPossible(value);
		cols[u.col].addPossible(value);
		boxes[u.box].addPossible(value);
		units[u.index].value = 0;
	}
}
