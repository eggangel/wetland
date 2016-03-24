package com.example.person;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.artifex.mupdfdemo.ChoosePDFActivity;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.example.talk.Talk;
import com.example.test.TestControl;
import com.example.wetland.Content;
import com.example.wetland.DialogUtil;
import com.example.wetland.JSONParser;
import com.example.wetland.Login;
import com.example.wetland.MyPreference;
import com.example.wetland.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Person extends Activity{
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private TextView user,sentence;
	private Button person1,person2,
	person3,exit,button1,button2,button3,button4,button5;
	private String UID,username,uriString;
	private boolean isExit;
    private Handler handler,handler2;
    private static String url = "http://www.wsthome.com/mysql_test/person.php";
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.person);
	        
	        handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					isExit = false;
				}
			};
	        
	        Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
	        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
	        UID=bundle.getString("UID");//getString()返回指定key的值  
	        username=bundle.getString("username");
	        uriString = bundle.getString("uri"); 
	        
	        
	        user = (TextView)findViewById(R.id.user);
	        user.setText(username+"，你好");
	        sentence = (TextView)findViewById(R.id.sentence);
	        sentence.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent();
					intent.setClass(Person.this, Sentence.class);
					intent.putExtra("UID", UID);
					intent.putExtra("username", username);
					intent.putExtra("uri", uriString);
					intent.putExtra("sentence", sentence.getText().toString());
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
				}
			});
	        
	        handler2 = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					String success = msg.getData().getString("success");
					String sent = msg.getData().getString("sent");
					if(success.equals("-1"))
	                {
	                	Toast.makeText(Person.this, "个性签名获取失败！", Toast.LENGTH_SHORT).show();
	                }
	                if(success.equals("1"))
	                {
						sentence.setText(sent);
					}

				}
			};
	        if(validate()){                
                new Up().execute(); 
                
	        }
	        
	        
	        
	        person1 = (Button)findViewById(R.id.person1);
	        person1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent();
					intent.setClass(Person.this, Data.class);
					intent.putExtra("UID", UID);
					intent.putExtra("username", username);
					intent.putExtra("uri", uriString);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
				}
			});
	        
	        person2 = (Button)findViewById(R.id.person2);
	        person2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent();
					intent.setClass(Person.this, Passwd.class);
					intent.putExtra("UID", UID);
					intent.putExtra("username", username);
					intent.putExtra("uri", uriString);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
				}
			});
	        
	        person3 = (Button)findViewById(R.id.person3);
	        person3.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent();
					intent.setClass(Person.this, Location.class);
					intent.putExtra("UID", UID);
					intent.putExtra("username", username);
					intent.putExtra("uri", uriString);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
				}
			});
	        
	        exit = (Button)findViewById(R.id.exit);
	        exit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(Person.this,Login.class);
					intent.putExtra("logout", "");
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
				}
			});
	        
	        button1 = (Button)findViewById(R.id.button1);
	        button1.setOnClickListener(new View.OnClickListener() {
	        	@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	        		Intent intent;
	        		intent = new Intent();
	    			intent.setClass(Person.this, Content.class);
	    			intent.putExtra("str", username);
	    			intent.putExtra("uri", uriString);
	    			startActivity(intent);
	    			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	        		finish();
				}
			});
	        
	        button1.setOnTouchListener(new OnTouchListener() {  
	            
	            @Override  
	            public boolean onTouch(View v, MotionEvent event) {  
	                // TODO Auto-generated method stub  
	                if(event.getAction()==MotionEvent.ACTION_DOWN){  
	                    v.setBackgroundResource(R.drawable.button1_choose);  
	                }else if(event.getAction()==MotionEvent.ACTION_UP){  
	                    v.setBackgroundResource(R.drawable.button1);  
	                }  
	                return false;  
	            }  
	        });
	        
	        button2 = (Button)findViewById(R.id.button2);
	        button2.setOnClickListener(new View.OnClickListener() {
	        	@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	        		Intent intent;
	        		intent = new Intent();
	    	        intent.setClass(Person.this, Content.class);
	    	        intent.putExtra("UID", UID);
	    	        intent.putExtra("str", username);
	    	        intent.putExtra("uri", uriString);
	    	        intent.putExtra("getin", 5);
	    			startActivity(intent);
	    			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	    			finish();
				}
			});
	        
	        button2.setOnTouchListener(new OnTouchListener() {  
	            
	            @Override  
	            public boolean onTouch(View v, MotionEvent event) {  
	                // TODO Auto-generated method stub  
	                if(event.getAction()==MotionEvent.ACTION_DOWN){  
	                    v.setBackgroundResource(R.drawable.button2_choose);  
	                }else if(event.getAction()==MotionEvent.ACTION_UP){  
	                    v.setBackgroundResource(R.drawable.button2);  
	                }  
	                return false;  
	            }  
	        });
	        
	        button3 = (Button)findViewById(R.id.button3);
	        button3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(Person.this, TestControl.class);
					intent.putExtra("UID", UID);
					intent.putExtra("user", username);
					intent.putExtra("uri", uriString);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
				}
			});
	        
	        button3.setOnTouchListener(new OnTouchListener() {  
	            
	            @Override  
	            public boolean onTouch(View v, MotionEvent event) {  
	                // TODO Auto-generated method stub  
	                if(event.getAction()==MotionEvent.ACTION_DOWN){  
	                    v.setBackgroundResource(R.drawable.button3_choose);  
	                }else if(event.getAction()==MotionEvent.ACTION_UP){  
	                    v.setBackgroundResource(R.drawable.button3);  
	                }  
	                return false;  
	            }  
	        });
	        
	        button4 = (Button)findViewById(R.id.button4);
	        button4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent();
					intent.setClass(Person.this, Talk.class);
					intent.putExtra("UID", UID);
					intent.putExtra("user", username);
					intent.putExtra("uri", uriString);
					intent.putExtra("TalkID", 5);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
				}
			});
	        
	        button4.setOnTouchListener(new OnTouchListener() {  
	            
	            @Override  
	            public boolean onTouch(View v, MotionEvent event) {  
	                // TODO Auto-generated method stub  
	                if(event.getAction()==MotionEvent.ACTION_DOWN){  
	                    v.setBackgroundResource(R.drawable.button4_choose);  
	                }else if(event.getAction()==MotionEvent.ACTION_UP){  
	                    v.setBackgroundResource(R.drawable.button4);  
	                }  
	                return false;  
	            }  
	        });
	        
	        button5 = (Button)findViewById(R.id.button5);
	        button5.setBackgroundResource(R.drawable.button5_choose);
	 }
	 
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if(keyCode==KeyEvent.KEYCODE_BACK){
	            if(!isExit){
	                isExit = true;
	                handler.sendEmptyMessageDelayed(0, 1500);
	                Toast.makeText(this, "再按一次退出程序", 1000).show();
	                return false;
	            }else{
	                finish();
	            }
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	 
	 class Up extends AsyncTask<String, String, String> {
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Person.this);
	            pDialog.setMessage("正在获取个性签名..");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
	        protected String doInBackground(String... args) {
	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("UID", UID));
	           try{
	            JSONObject json = jsonParser.makeHttpRequest(url,
	                    "POST", params);
	            String success = json.getString("success");
	            String sent;
	            if(success.equals("1"))
	            {
	            	sent = json.getString("sentence");
	            }
	            else
	            {
	            	sent = "";
	            }
	            Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("success",success);
				bundle.putString("sent",sent);
				msg.setData(bundle);
				handler2.sendMessage(msg);
	            return "";
	           }catch(Exception e){
	               e.printStackTrace(); 
	               return "-1";          
	           }
	        }
	        protected void onPostExecute(String message) {                  
	            pDialog.dismiss();
	           //message 为接收doInbackground的返回值
	            //Toast.makeText(getApplicationContext(), message, 8000).show();
	        }
	    }
	 
	 public boolean isNetworkAvailable(Activity activity)
	    {
	        Context context = activity.getApplicationContext();
	        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
	        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        
	        if (connectivityManager == null)
	        {
	            return false;
	        }
	        else
	        {
	            // 获取NetworkInfo对象
	            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
	            
	            if (networkInfo != null && networkInfo.length > 0)
	            {
	                for (int i = 0; i < networkInfo.length; i++)
	                {
	                    System.out.println(i + "===状态===" + networkInfo[i].getState());
	                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
	                    // 判断当前网络状态是否为连接状态
	                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
	                    {
	                        return true;
	                    }
	                }
	            }
	        }
	        return false;
	    }
	 
	 private boolean validate()
	    {
	    	if (!isNetworkAvailable(Person.this))
	        {
	    		DialogUtil.showDialog(this, "当前无可用网络", false);
	            return false;
	        }
	        
	        return true;
	    }
	 
}
