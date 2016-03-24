package com.example.talk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.person.Person;
import com.example.test.TestControl;
import com.example.wetland.Content;
import com.example.wetland.DialogUtil;
import com.example.wetland.JSONParser;
import com.example.wetland.JSONParser2;
import com.example.wetland.Login;
import com.example.wetland.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class Talk extends Activity
{
	
	JSONParser2 jsonParser2 = new JSONParser2();
	JSONParser jsonParser = new JSONParser();
	private static String url = "http://www.wsthome.com/mysql_test/discuss.php";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_SUCCESS = "success";
	private ProgressDialog pDialog;
	String exchange[][] = new String[3][100];
	ListView list;
	Button commit,back;
	private Handler handler;
	String uriString;
	int talkid;

	EditText discuss;
	int flag = 0;
	String UID;
	String username;
	TextView user,logout;
	SimpleAdapter simpleAdapter;
	List<Map<String, Object>> listItems;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 去应用的标题
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.talk_main);
		
		// 创建一个List集合，List集合的元素是Map
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String[] buffer1 = msg.getData().getStringArray(
						"buffer1");
				String[] buffer2 = msg.getData().getStringArray(
						"buffer2");
				String[] buffer3 = msg.getData().getStringArray(
						"buffer3");

				int length = msg.getData().getInt("length");
				if(length == 0)
				{
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("personName", buffer1[0]);
					listItem.put("desc", buffer2[0]);
					listItem.put("time", buffer3[0]);
					listItems.add(listItem);
				}
				
				for (int i = 0; i < length; i++)
				{
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("personName", buffer1[i]);
					listItem.put("desc", buffer2[i]);
					listItem.put("time", buffer3[i]);
					listItems.add(listItem);
				}
				
				// 创建一个SimpleAdapter
				simpleAdapter = new SimpleAdapter(Talk.this, listItems,
					R.layout.simple_item, 
					new String[] { "personName", "time", "desc"},
					new int[] { R.id.name,   R.id.time, R.id.desc});
				
				// 为ListView设置Adapter
				list.setAdapter(simpleAdapter);
				
				listItems = new ArrayList<Map<String, Object>>();
			}
		};
		
		list = (ListView) findViewById(R.id.mylist);
		
		listItems = new ArrayList<Map<String, Object>>();
		 
		if (validate1()) {
			flag = 1;
			new Up().execute();
		}
		

		
		commit = (Button) findViewById(R.id.commit);
		
		commit.setOnTouchListener(new OnTouchListener() {  
            
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                // TODO Auto-generated method stub  
                if(event.getAction()==MotionEvent.ACTION_DOWN){  
                    v.setBackgroundResource(R.drawable.background_reg_press);  
                }else if(event.getAction()==MotionEvent.ACTION_UP){  
                    v.setBackgroundResource(R.drawable.background_reg);  
                }  
                return false;  
            }  
        });  
		
		commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (validate2()) {
					flag = -1;
					new Up().execute();
					Toast.makeText(Talk.this, "评论成功！", Toast.LENGTH_SHORT).show();
				}
				discuss.setText("");
				
				
			}
		});
		
		Intent intent = getIntent();// getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
		Bundle bundle = intent.getExtras();// .getExtras()得到intent所附带的额外数据
		UID = bundle.getString("UID");// getString()返回指定key的值
		username = bundle.getString("user");
		uriString = bundle.getString("uri");
		talkid = bundle.getInt("TalkID");
		
		user = (TextView) findViewById(R.id.username);
		user.setText("欢迎，"+username);
		discuss = (EditText) findViewById(R.id.discuss);
		
		logout = (TextView) findViewById(R.id.logout);
        logout.setText(Html.fromHtml("<u>退出</u>"));
        logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Talk.this,Login.class);
				intent.putExtra("logout", "1");
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});
        
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent;
				switch (talkid) {
				case 1:
					intent = new Intent();
					intent.setClass(Talk.this, Content.class);
					intent.putExtra("str", username);
					intent.putExtra("uri", uriString);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
					break;
				case 3:
					intent = new Intent();
					intent.setClass(Talk.this, TestControl.class);
					intent.putExtra("UID", UID);
					intent.putExtra("user", username);
					intent.putExtra("uri", uriString);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
					break;
				case 5:
					intent = new Intent();
					intent.setClass(Talk.this, Person.class);
					intent.putExtra("username", username);
					intent.putExtra("uri", uriString);
					intent.putExtra("UID", UID);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
					break;

				default:
					break;
				}
			}
		});
	}
	
	
	private boolean validate1() {
		if (!isNetworkAvailable(Talk.this)) {
			DialogUtil.showDialog(this, "当前无可用网络", false);
			return false;
		}
		return true;
	}

	private boolean validate2() {
		if (!isNetworkAvailable(Talk.this)) {
			DialogUtil.showDialog(this, "当前无可用网络", false);
			return false;
		}
		String discuss1 = discuss.getText().toString().trim();
		if (discuss1.equals("")) {
			DialogUtil.showDialog(this, "您还没有填写评论", false);
			return false;
		}
		return true;
	}

	class Up extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Talk.this);
			pDialog.setMessage("正在获取评论内容..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("flag", "-1"));
			if (flag == 1) {
				try {
					JSONTokener json = jsonParser2.makeHttpRequest(url, "POST",
							params);
					// Object testObject = json.getJSONArray(TAG_CONTENT);
					JSONArray person;
					JSONObject jsob;
					person = (JSONArray) json.nextValue();
					int l = 0, i = 0;
					i = person.length();
					while (i != 0) {
						jsob = person.getJSONObject(l);

						exchange[0][l] = jsob.getString("username");
						exchange[1][l] = jsob.getString("content");
						exchange[2][l] = jsob.getString("time");
						l++;
						i--;
					}
					// String success = json.getString(TAG_SUCCESS);
					String[] buffer1;
					String[] buffer2;
					String[] buffer3;
					if(person.length()==0)
					{
						buffer1 = new String[1];
						buffer1[0] = "Powered C.J";
						buffer2 = new String[1];
						buffer2[0] = "暂时没有评论！";
						buffer3 = new String[1];
						buffer3[0] = "";
					}
					else {
						buffer1 = exchange[0];
						buffer2 = exchange[1];
						buffer3 = exchange[2];
					}
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putStringArray("buffer1", buffer1);
					bundle.putStringArray("buffer2", buffer2);
					bundle.putStringArray("buffer3", buffer3);
					bundle.putInt("length", l);
					msg.setData(bundle);
					handler.sendMessage(msg);

					return null;
				} catch (Exception e) {
					e.printStackTrace();
					return "出现bug";
				}
			}
			if (flag == -1) {
				String discuss1 = discuss.getText().toString();
				params.add(new BasicNameValuePair("content", discuss1));
				params.add(new BasicNameValuePair("UID", UID));
				try {
					JSONObject json = jsonParser.makeHttpRequest(url, "POST",
							params);
					// String message = json.getString(TAG_MESSAGE);
					
					
					JSONTokener json1 = jsonParser2.makeHttpRequest(url, "POST",
							params1);
					// Object testObject = json.getJSONArray(TAG_CONTENT);
					JSONArray person;
					JSONObject jsob;
					person = (JSONArray) json1.nextValue();
					int l = 0, i = 0;
					i = person.length();
					while (i != 0) {
						jsob = person.getJSONObject(l);

						exchange[0][l] = jsob.getString("username");
						exchange[1][l] = jsob.getString("content");
						exchange[2][l] = jsob.getString("time");
						l++;
						i--;
					}
					// String success = json.getString(TAG_SUCCESS);
					String[] buffer1;
					String[] buffer2;
					String[] buffer3;
					if(person.length()==0)
					{
						buffer1 = new String[1];
						buffer1[0] = "Powered C.J";
						buffer2 = new String[1];
						buffer2[0] = "暂时没有评论！";
						buffer3 = new String[1];
						buffer3[0] = "";
					}
					else {
						buffer1 = exchange[0];
						buffer2 = exchange[1];
						buffer3 = exchange[2];
					}
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putStringArray("buffer1", buffer1);
					bundle.putStringArray("buffer2", buffer2);
					bundle.putStringArray("buffer3", buffer3);
					bundle.putInt("length", l);
					msg.setData(bundle);
					handler.sendMessage(msg);

					String success = json.getString(TAG_SUCCESS);
					if (success.equals("1"))
						return "评论成功";
					else
						return "评论失败";
					
				} catch (Exception e) {
					e.printStackTrace();
					return "出现bug";
				}
				
				
				
			}
			flag = 0;
			return null;
		}

		protected void onPostExecute(String message) {
			pDialog.dismiss();
			// message 为接收doInbackground的返回值
			//Toast.makeText(getApplicationContext(), message, 8000).show();
		}
	}

	public boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					System.out.println(i + "===状态==="
							+ networkInfo[i].getState());
					System.out.println(i + "===类型==="
							+ networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
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
        	Intent intent;
			switch (talkid) {
			case 1:
				intent = new Intent();
				intent.setClass(Talk.this, Content.class);
				intent.putExtra("str", username);
				intent.putExtra("uri", uriString);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
				break;
			case 3:
				intent = new Intent();
				intent.setClass(Talk.this, TestControl.class);
				intent.putExtra("UID", UID);
				intent.putExtra("user", username);
				intent.putExtra("uri", uriString);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
				break;
			case 5:
				intent = new Intent();
				intent.setClass(Talk.this, Person.class);
				intent.putExtra("username", username);
				intent.putExtra("uri", uriString);
				intent.putExtra("UID", UID);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
				break;

			default:
				break;
			}
        }
        return super.onKeyDown(keyCode, event);
    }
}