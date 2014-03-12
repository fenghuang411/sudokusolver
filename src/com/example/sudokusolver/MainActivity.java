package com.example.sudokusolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	Spinner spEasy;
	Spinner spMedium;
	Spinner spHard;
	Spinner spSuper;
	ImageView ivEasy;
	ImageView ivMedium;
	ImageView ivHard;
	ImageView ivSuper;
	ImageView welcome_iv;
	ImageView toPlay;
	ImageView assist_switch;
	ImageView solve_it;
	ImageView go_back;
	ImageView create_one;
	ImageView create_go_back;
	ImageView create_commit;
	TextView[] create_units;
	TextView[] create_select;
	TextView[] units;
	TextView[] unit_hint;
	TextView[] unit_select;
	TextView last_touched;
	int last_touched_index;
	int last_saved_color;
	TextView create_last_touched;
	int create_last_touched_index;
	int create_last_saved_color;
	int create_last_index;
	Map<TextView,Integer> unknow_list;
	Map<TextView,Integer> select_list;
	Map<TextView,Integer> create_unit_list;
	Map<TextView,Integer> create_select_list;
	String puzzle_string;
	String answer_string;
	String create_string;
	char[] create_chars;
	char[] current_answer;
	OnClickListener common_listener;
	OnItemSelectedListener common_spinner;
	Grid result_generator;
	boolean assist_state;
	MediaPlayer mMediaPlayer;
	AssetFileDescriptor afd;
	int play_pos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//			.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		// full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		common_listener = new myOnClickListener();
		common_spinner = new CustomOnItemSelectedListener();
		welcome_iv = (ImageView) findViewById(R.id.welcome_iv);
		welcome_iv.setOnClickListener(common_listener);
		units = new TextView[82];
		units[0] = null;
		unit_hint = new TextView[10];
		unit_hint[0] = null;
		unit_select = new TextView[10];
		result_generator = new Grid();
		assist_state = false;
		last_touched = null;
		try{
			afd = getAssets().openFd("Gump.mp3");
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
			mMediaPlayer.pause();
			play_pos = mMediaPlayer.getCurrentPosition();

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMediaPlayer.seekTo(play_pos);
			mMediaPlayer.start();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mMediaPlayer.stop();
		try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initLevels(ImageView iv, Spinner sp){
		iv.setOnClickListener(common_listener);
		sp.setVisibility(View.INVISIBLE);	
	}
	public class myOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == welcome_iv){
				setContentView(R.layout.puzzle_select);
				ivEasy = (ImageView) findViewById(R.id.puzzle_easy);
				spEasy = (Spinner) findViewById(R.id.spinner_easy);
				initLevels(ivEasy, spEasy);
				
				ivMedium = (ImageView) findViewById(R.id.puzzle_medium);
				spMedium = (Spinner) findViewById(R.id.spinner_medium);
				initLevels(ivMedium, spMedium);
				
				ivHard = (ImageView) findViewById(R.id.puzzle_hard);
				spHard = (Spinner) findViewById(R.id.spinner_hard);
				initLevels(ivHard, spHard);
				
				ivSuper = (ImageView) findViewById(R.id.puzzle_superhard);
				spSuper = (Spinner) findViewById(R.id.spinner_superhard);
				initLevels(ivSuper, spSuper);
				
				create_one = (ImageView) findViewById(R.id.puzzle_create);
				create_one.setOnClickListener(common_listener);
				
				toPlay = (ImageView) findViewById(R.id.toPlay);
				toPlay.setVisibility(View.INVISIBLE);
			}
			else if (v == ivEasy){
				ivEasy.setImageResource(R.drawable.easy2);
				ivMedium.setImageResource(R.drawable.medium);
				ivHard.setImageResource(R.drawable.hard);
				ivSuper.setImageResource(R.drawable.superhard);
				
				spEasy.setOnItemSelectedListener(common_spinner);
				
				spEasy.setVisibility(View.VISIBLE);
				spMedium.setVisibility(View.INVISIBLE);
				spHard.setVisibility(View.INVISIBLE);
				spSuper.setVisibility(View.INVISIBLE);
				toPlay.setVisibility(View.INVISIBLE);
			}
			else if (v == ivMedium){
				ivEasy.setImageResource(R.drawable.easy);
				ivMedium.setImageResource(R.drawable.medium2);
				ivHard.setImageResource(R.drawable.hard);
				ivSuper.setImageResource(R.drawable.superhard);
				
				spMedium.setOnItemSelectedListener(common_spinner);
				
				spEasy.setVisibility(View.INVISIBLE);
				spMedium.setVisibility(View.VISIBLE);
				spHard.setVisibility(View.INVISIBLE);
				spSuper.setVisibility(View.INVISIBLE);
				toPlay.setVisibility(View.INVISIBLE);
			}
			else if (v == ivHard){
				ivEasy.setImageResource(R.drawable.easy);
				ivMedium.setImageResource(R.drawable.medium);
				ivHard.setImageResource(R.drawable.hard2);
				ivSuper.setImageResource(R.drawable.superhard);
				
				spHard.setOnItemSelectedListener(common_spinner);
				
				spEasy.setVisibility(View.INVISIBLE);
				spMedium.setVisibility(View.INVISIBLE);
				spHard.setVisibility(View.VISIBLE);
				spSuper.setVisibility(View.INVISIBLE);
				toPlay.setVisibility(View.INVISIBLE);
			}
			else if (v == ivSuper){
				ivEasy.setImageResource(R.drawable.easy);
				ivMedium.setImageResource(R.drawable.medium);
				ivHard.setImageResource(R.drawable.hard);
				ivSuper.setImageResource(R.drawable.superhard2);
				
				spSuper.setOnItemSelectedListener(common_spinner);
				
				spEasy.setVisibility(View.INVISIBLE);
				spMedium.setVisibility(View.INVISIBLE);
				spHard.setVisibility(View.INVISIBLE);
				spSuper.setVisibility(View.VISIBLE);
				toPlay.setVisibility(View.INVISIBLE);
			}
			else if (v == toPlay){
				setContentView(R.layout.panel);
				String prefix = "unit_textView";
				String prefix_hint = "unit_hint";
				String prefix_select = "unit_select";
				go_back = (ImageView) findViewById(R.id.goback);
				go_back.setOnClickListener(common_listener);
				solve_it = (ImageView) findViewById(R.id.solveit);
				solve_it.setOnClickListener(common_listener);
				assist_switch = (ImageView) findViewById(R.id.assist);
				assist_switch.setOnClickListener(common_listener);
				unknow_list = new HashMap<TextView,Integer>();
				select_list = new HashMap<TextView, Integer>();
				for (int i = 1; i <= 81; i++){
					units[i] = getTextViewResourceByName(prefix+i);// load all units
					if (puzzle_string.charAt(i) != '0'){
						units[i].setText(String.valueOf(puzzle_string.charAt(i)));
						units[i].setTextColor(getResources().getColor(R.color.unit_red));
						units[i].setEnabled(false);
						units[i].setTextSize(25);
					}
					else {
						units[i].setText("");
		//				units[i].setTextAppearance(context, resid)
						unknow_list.put(units[i], i);
						units[i].setOnClickListener(common_listener);
					}
				}
				for (int i = 1; i <= 9; i++)
					unit_hint[i] = getTextViewResourceByName(prefix_hint+i); // init hint views
				for (int i = 0; i <= 9; i++){
					unit_select[i] = getTextViewResourceByName(prefix_select+i); // init select views
					unit_select[i].setOnClickListener(common_listener);
					select_list.put(unit_select[i],i);
				}
				result_generator.load_puzzle(puzzle_string); // load for get answer
				if (result_generator.solve())
					answer_string = result_generator.generateOutput();
				result_generator.load_puzzle(puzzle_string); // load again for user solving
			}
			else if (v == go_back){
				setContentView(R.layout.puzzle_select);
				ivEasy = (ImageView) findViewById(R.id.puzzle_easy);
				spEasy = (Spinner) findViewById(R.id.spinner_easy);
				initLevels(ivEasy, spEasy);
				
				ivMedium = (ImageView) findViewById(R.id.puzzle_medium);
				spMedium = (Spinner) findViewById(R.id.spinner_medium);
				initLevels(ivMedium, spMedium);
				
				ivHard = (ImageView) findViewById(R.id.puzzle_hard);
				spHard = (Spinner) findViewById(R.id.spinner_hard);
				initLevels(ivHard, spHard);
				
				ivSuper = (ImageView) findViewById(R.id.puzzle_superhard);
				spSuper = (Spinner) findViewById(R.id.spinner_superhard);
				initLevels(ivSuper, spSuper);
				
				create_one = (ImageView) findViewById(R.id.puzzle_create);
				create_one.setOnClickListener(common_listener);
				
				toPlay = (ImageView) findViewById(R.id.toPlay);
				toPlay.setVisibility(View.INVISIBLE);
				
				puzzle_string = "";
				unknow_list.clear();
				select_list.clear();
			}
			else if (v == solve_it){
				if (last_touched != null)
					last_touched.setBackgroundColor(last_saved_color);
				if (answer_string != null)
					for (int i = 1; i <= 81; i++){
						if (puzzle_string.charAt(i) == '0'){
							units[i].setText(String.valueOf(answer_string.charAt(i)));
							units[i].setTextColor(getResources().getColor(R.color.unit_black));
							units[i].setEnabled(false);
						}
					}
			//	go_back.setOnClickListener(common_listener);
			}
			else if (v == assist_switch){
				if (assist_state){
					assist_switch.setImageResource(R.drawable.assistoff);
					assist_state = false;
					for (int i = 1; i <= 9; i++)
						unit_hint[i].setText("");
				}
				else{
					assist_switch.setImageResource(R.drawable.assiston);
					assist_state = true;
				}
			}
			else if (v == create_commit){
				setContentView(R.layout.panel);
				String prefix = "unit_textView";
				String prefix_hint = "unit_hint";
				String prefix_select = "unit_select";
				go_back = (ImageView) findViewById(R.id.goback);
				go_back.setOnClickListener(common_listener);
				solve_it = (ImageView) findViewById(R.id.solveit);
				solve_it.setOnClickListener(common_listener);
				assist_switch = (ImageView) findViewById(R.id.assist);
				assist_switch.setOnClickListener(common_listener);
				unknow_list = new HashMap<TextView,Integer>();
				select_list = new HashMap<TextView, Integer>();
				puzzle_string = String.valueOf(create_chars);
				for (int i = 1; i <= 81; i++){
					units[i] = getTextViewResourceByName(prefix+i);// load all units
					if (puzzle_string.charAt(i) != '0'){
						units[i].setText(String.valueOf(puzzle_string.charAt(i)));
						units[i].setTextColor(getResources().getColor(R.color.unit_red));
						units[i].setEnabled(false);
						units[i].setTextSize(25);
					}
					else {
						units[i].setText("");
		//				units[i].setTextAppearance(context, resid)
						unknow_list.put(units[i], i);
						units[i].setOnClickListener(common_listener);
					}
				}
				for (int i = 1; i <= 9; i++)
					unit_hint[i] = getTextViewResourceByName(prefix_hint+i); // init hint views
				for (int i = 0; i <= 9; i++){
					unit_select[i] = getTextViewResourceByName(prefix_select+i); // init select views
					unit_select[i].setOnClickListener(common_listener);
					select_list.put(unit_select[i],i);
				}
				result_generator.load_puzzle(puzzle_string); // load for get answer
				if (result_generator.solve())
					answer_string = result_generator.generateOutput();
				result_generator.load_puzzle(puzzle_string); // load again for user solving
				
				create_select_list.clear();
				create_unit_list.clear();
			}
			else if (v == create_go_back){				
				create_select_list.clear();
				create_unit_list.clear();
				
				setContentView(R.layout.puzzle_select);
				ivEasy = (ImageView) findViewById(R.id.puzzle_easy);
				spEasy = (Spinner) findViewById(R.id.spinner_easy);
				initLevels(ivEasy, spEasy);
				
				ivMedium = (ImageView) findViewById(R.id.puzzle_medium);
				spMedium = (Spinner) findViewById(R.id.spinner_medium);
				initLevels(ivMedium, spMedium);
				
				ivHard = (ImageView) findViewById(R.id.puzzle_hard);
				spHard = (Spinner) findViewById(R.id.spinner_hard);
				initLevels(ivHard, spHard);
				
				ivSuper = (ImageView) findViewById(R.id.puzzle_superhard);
				spSuper = (Spinner) findViewById(R.id.spinner_superhard);
				initLevels(ivSuper, spSuper);
				
				create_one = (ImageView) findViewById(R.id.puzzle_create);
				create_one.setOnClickListener(common_listener);
				
				toPlay = (ImageView) findViewById(R.id.toPlay);
				toPlay.setVisibility(View.INVISIBLE);

			}
			else if (v == create_one){
				setContentView(R.layout.create_panel);
				create_commit = (ImageView) findViewById(R.id.create_commit);
				create_commit.setOnClickListener(common_listener);
				create_go_back = (ImageView) findViewById(R.id.create_goback);
				create_go_back.setOnClickListener(common_listener);
				create_select = new TextView[10];
				create_units = new TextView[82];
				create_chars = new char[82];
				create_select_list = new HashMap<TextView, Integer>();
				create_unit_list  = new HashMap<TextView, Integer>();
				create_chars[0] = 'C';
				for (int i = 1; i <= 81; i++)
					create_chars[i] = '0';
				String create_select_prefix = "create_select";
				String create_unit_prefix = "create_textView";
				for (int i = 0; i <= 9; i++){
					create_select[i] = getTextViewResourceByName(create_select_prefix+i);
					create_select[i].setOnClickListener(common_listener);
					create_select_list.put(create_select[i], i);
				}
				for (int i = 1; i <= 81; i++){
					create_units[i] = getTextViewResourceByName(create_unit_prefix+i);
					create_units[i].setOnClickListener(common_listener);
					create_unit_list.put(create_units[i], i);
				}
			}
			else if (create_select_list.containsKey(v)){ // on select touched in create panel
				if (create_select_list.get(v) == 0){ // erase
					create_last_touched.setText("");
					create_chars[create_last_index] = '0';
				}
				else {
					create_last_touched.setText(""+create_select_list.get(v));
					create_chars[create_last_index] = (char) ('0'+create_select_list.get(v));
				}
				// move to next ? 
				
				if (create_last_index < 81){
					if (create_last_touched != null)
						create_last_touched.setBackgroundColor(create_last_saved_color);
					create_last_index++;
					for (Entry<TextView, Integer> entry : create_unit_list.entrySet()) {
						if (entry.getValue().equals(create_last_index)) {
							create_last_touched = entry.getKey();
						}
					}
					create_last_touched.setBackgroundColor(getResources().getColor(R.color.unit_yellow));
				}
			}
			else if (create_unit_list.containsKey(v)){ // on unit touched in create panel
				if (create_last_touched != null)
					create_last_touched.setBackgroundColor(create_last_saved_color);
				// save this for next call
				create_last_saved_color = ((ColorDrawable) v.getBackground()).getColor();
				create_last_touched = (TextView) v;
				create_last_index = create_unit_list.get(v);
				v.setBackgroundColor(getResources().getColor(R.color.unit_yellow));
			}
			else if (unknow_list.containsKey(v)){ // on unit touched on solving panel
				int index = unknow_list.get(v);
				if (last_touched != null)
					last_touched.setBackgroundColor(last_saved_color);
				// save this for next call
				last_saved_color = ((ColorDrawable) v.getBackground()).getColor();
				last_touched = (TextView) v;
				last_touched_index = index;
				// change the tapped box to yellow
				v.setBackgroundColor(getResources().getColor(R.color.unit_yellow));
			//	Toast.makeText(getApplicationContext(), "checked@"+index,  Toast.LENGTH_SHORT).show();
				boolean ret;
				for (int i = 1; i <= 9; i++){
					ret = result_generator.get_available(index,i);
					if (ret && assist_state)
						unit_hint[i].setText(""+i);  // setText(i) error
					else 
						unit_hint[i].setText("");
				}
			}
			else if (select_list.containsKey(v)){
				String text = (String) ((TextView)v).getText();
				int new_value = Integer.parseInt(text);
				if (new_value != 0){
					result_generator.units[last_touched_index].value = new_value;
					result_generator.update_possible(result_generator.units[last_touched_index], new_value);
					last_touched.setText(text);
					current_answer[last_touched_index] = (char) ('0'+new_value);
					if (Arrays.equals(answer_string.toCharArray(), current_answer))
						Toast.makeText(getApplicationContext(), "You Have Won !",  Toast.LENGTH_SHORT).show();
						
				}
				else {
					int value_back = Integer.parseInt((String)last_touched.getText());
					result_generator.add_possible(result_generator.units[last_touched_index],value_back);
					last_touched.setText("");
					current_answer[last_touched_index] = '0';
				}
			}
		}
	}
	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			create_one.setVisibility(View.INVISIBLE);
			puzzle_string =  getStringResourceByName((String) arg0.getItemAtPosition(arg2));
			current_answer = puzzle_string.toCharArray();
			current_answer[0] = 'R';
			toPlay.setVisibility(View.VISIBLE);
			toPlay.setOnClickListener(common_listener);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private String getStringResourceByName(String aString) {
	      String packageName = getPackageName();
	      int resId = getResources().getIdentifier(aString, "string", packageName);
	      return getString(resId);
	    }
	private TextView getTextViewResourceByName(String name){
		String packageName = getPackageName();
		int resId = getResources().getIdentifier(name, "id", packageName);
		return (TextView) findViewById(resId);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
