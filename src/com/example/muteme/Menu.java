package com.example.muteme;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {
	
	String classes[] = {"My Events","New Event","example2","example3"};
	String menuItem[] = {"MainActivity","Event","example2","example3"};

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, classes));
		
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String cheese = menuItem[position];
		
		try{
			
			Class menuClass = Class.forName("com.example.muteme."+cheese);
		
			Intent menuIntent = new Intent(Menu.this,menuClass);
			startActivity(menuIntent);
			
		}catch(ClassNotFoundException e){
			
			e.printStackTrace();
			
		}
		
	}	

}
