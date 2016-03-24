package com.example.wetland;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class UI extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui);
        new Thread(new Runnable(){    
        	public void run(){    
	        	try {
					Thread.sleep(2000);
					Intent intent = new Intent();
			        intent.setClass(UI.this, Login.class);
			        startActivity(intent);
			        finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}      
        	}    
        	}).start(); 
        
	}
}
