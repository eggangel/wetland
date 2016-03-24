package com.example.wetland;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.artifex.mupdfdemo.ChoosePDFActivity;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.example.person.Person;
import com.example.talk.Talk;
import com.example.test.Test;
import com.example.test.TestControl;

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
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Content extends Activity{
	
	//定义画廊控件
	private Gallery gallery = null;
	//定义图片控件
	private ImageView image1, image2, image3, image4;
	//当前位置
	private int position = 0;
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	TextView user,logout;
	Button button1,button2,button3,button4,button5;
	private boolean isExit;
    private Handler handler,handler2;
    private String UID;
    String str;
    String uriString;
    int getin;
    private static String url = "http://www.wsthome.com/mysql_test/search.php";
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_main);

		// 实例化图片控件
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		image4 = (ImageView) findViewById(R.id.image4);
		// 实例化画廊控件
		gallery = (Gallery) findViewById(R.id.gallery);
		// 装载内容
		gallery.setAdapter(new GalleryAdapter(this));
		// 设置项的选中监听
		gallery.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
		// 启动线程
		timer.schedule(task, 3000, 3000);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				isExit = false;
			}
		};
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				UID = msg.getData().getString("UID");

			}
		};

		user = (TextView) findViewById(R.id.username);
		button1 = (Button) findViewById(R.id.button1);

		// button1.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if(event.getAction()==MotionEvent.ACTION_DOWN){
		// v.setBackgroundResource(R.drawable.button1_choose);
		// }else if(event.getAction()==MotionEvent.ACTION_UP){
		// v.setBackgroundResource(R.drawable.button1);
		// }
		// return false;
		// }
		// });
		button1.setBackgroundResource(R.drawable.button1_choose);

		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file = new File(MyPreference.getInstance(Content.this)
						.getUri());
				if (!file.exists()) {
					Intent intent = new Intent(Content.this,
							ChoosePDFActivity.class);
					intent.setAction(Intent.ACTION_VIEW);
					intent.putExtra("username", str);
					startActivity(intent);
				} else {
					Uri uri = Uri.parse(MyPreference.getInstance(Content.this)
							.getUri());
					Intent intent = new Intent(Content.this,
							MuPDFActivity.class);
					intent.setAction(Intent.ACTION_VIEW);
					intent.putExtra("username", str);
					intent.putExtra("uri",
							MyPreference.getInstance(Content.this).getUri());
					intent.setData(uri);
					startActivity(intent);
				}
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});

		button2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.button2_choose);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.button2);
				}
				return false;
			}
		});

		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Content.this, TestControl.class);
				intent.putExtra("UID", UID);
				intent.putExtra("user", str);
				intent.putExtra("uri", MyPreference.getInstance(Content.this)
						.getUri());
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});

		button3.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.button3_choose);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.button3);
				}
				return false;
			}
		});

		button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				intent.setClass(Content.this, Talk.class);
				intent.putExtra("UID", UID);
				intent.putExtra("user", str);
				intent.putExtra("uri", MyPreference.getInstance(Content.this)
						.getUri());
				intent.putExtra("TalkID", 1);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});

		button4.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.button4_choose);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.button4);
				}
				return false;
			}
		});

		button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				intent.setClass(Content.this, Person.class);
		        intent.putExtra("username", str);
		        intent.putExtra("uri", MyPreference.getInstance(Content.this)
						.getUri());
		        intent.putExtra("UID", UID);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});

		button5.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.button5_choose);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.button5);
				}
				return false;
			}
		});

		logout = (TextView) findViewById(R.id.logout);
		logout.setText(Html.fromHtml("<u>退出</u>"));
		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Content.this, Login.class);
				intent.putExtra("logout", "1");
				startActivity(intent);
				finish();
			}
		});

		Intent intent = getIntent();// getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
		Bundle bundle = intent.getExtras();// .getExtras()得到intent所附带的额外数据
		str = bundle.getString("str");// getString()返回指定key的值
		uriString = bundle.getString("uri");
		getin = bundle.getInt("getin");
		
		if (!uriString.equals("-1")) {
			MyPreference.getInstance(Content.this).SetUri(uriString.trim());
		}
		user.setText("欢迎，" + str);

		if (getin == 3 || getin == 5) {
			File file = new File(MyPreference.getInstance(Content.this)
					.getUri());
			if (!file.exists()) {
				UID = bundle.getString("UID");
				Intent intent1 = new Intent(Content.this,
						ChoosePDFActivity.class);
				intent1.setAction(Intent.ACTION_VIEW);
				intent1.putExtra("username", str);
				intent1.putExtra("UID", UID);
				intent1.putExtra("getin", getin);
				startActivity(intent1);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			} else {
				UID = bundle.getString("UID");
				Uri uri = Uri.parse(MyPreference.getInstance(Content.this)
						.getUri());
				Intent intent1 = new Intent(Content.this, MuPDFActivity.class);
				intent1.setAction(Intent.ACTION_VIEW);
				intent1.putExtra("username", str);
				intent1.putExtra("uri", MyPreference.getInstance(Content.this)
						.getUri());
				intent1.putExtra("UID", UID);
				intent1.putExtra("getin", getin);
				intent1.setData(uri);
				startActivity(intent1);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}
			finish();
		}
		else if(getin == 6)
		{
			String id = bundle.getString("UID");
			Intent intent1 = new Intent();
			intent1.setClass(Content.this, TestControl.class);
			intent1.putExtra("UID", id);
			intent1.putExtra("user", str);
			intent1.putExtra("uri", MyPreference.getInstance(Content.this)
					.getUri());
			startActivity(intent1);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
		}
		else if(getin == 10)
		{
			String id = bundle.getString("UID");
			Intent intent1 = new Intent();
			intent1.setClass(Content.this, Person.class);
	        intent1.putExtra("username", str);
	        intent1.putExtra("uri", uriString);
	        intent1.putExtra("UID", id);
			startActivity(intent1);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
		}
		else {
			if (validate()) {
				new Up().execute();
			}
		}

	}
	 class Up extends AsyncTask<String, String, String> {
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Content.this);
	            pDialog.setMessage("正在验证身份..");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
	        protected String doInBackground(String... args) {
	            String name1 = str;
	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("name", name1));
	           try{
	            JSONObject json = jsonParser.makeHttpRequest(url,
	                    "POST", params);
	            String message = json.getString("UID");
	            Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("UID",message);
				msg.setData(bundle);
				handler2.sendMessage(msg);
	            //String success = json.getString(TAG_SUCCESS);
	            return message;
	           }catch(Exception e){
	               e.printStackTrace(); 
	               return "";          
	           }
	        }
	        protected void onPostExecute(String message) {
	            pDialog.dismiss();
	           //message 为接收doInbackground的返回值
	           //Toast.makeText(getApplicationContext(), message, 8000).show();
	        }
	    }
	 
	 private boolean validate()
	    {
	    	if (!isNetworkAvailable(Content.this))
	        {
	    		DialogUtil.showDialog(this, "当前无可用网络", false);
	            return false;
	        }
	    	return true;
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
	 
	//项的监听实现
		private class OnItemSelectedListenerImpl implements OnItemSelectedListener {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position % 4) {
				case 0:
					//设置显示图片
					image1.setImageResource(R.drawable.focused);
					image2.setImageResource(R.drawable.normal);
					image3.setImageResource(R.drawable.normal);
					image4.setImageResource(R.drawable.normal);
					break;
				case 1:
					image1.setImageResource(R.drawable.normal);
					image2.setImageResource(R.drawable.focused);
					image3.setImageResource(R.drawable.normal);
					image4.setImageResource(R.drawable.normal);
					break;
				case 2:
					image1.setImageResource(R.drawable.normal);
					image2.setImageResource(R.drawable.normal);
					image3.setImageResource(R.drawable.focused);
					image4.setImageResource(R.drawable.normal);
					break;
				case 3:
					image1.setImageResource(R.drawable.normal);
					image2.setImageResource(R.drawable.normal);
					image3.setImageResource(R.drawable.normal);
					image4.setImageResource(R.drawable.focused);
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		}

		//销魂线程
		@Override
		protected void onDestroy() {
			timer.cancel();
			super.onDestroy();
		}

		// 设置自动循环的线程
		private static final int timerAnimation = 1;
		private final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case timerAnimation:
					gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
					position++;
					break;
				default:
					break;
				}
			};
		};
		private final Timer timer = new Timer();
		private final TimerTask task = new TimerTask() {
			public void run() {
				mHandler.sendEmptyMessage(timerAnimation);
			}
		};
}
