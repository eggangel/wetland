package com.example.person;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.person.Sentence.Up;
import com.example.wetland.DialogUtil;
import com.example.wetland.JSONParser;
import com.example.wetland.R;

public class Data extends Activity{
	private String UID;
	private String username;
	private String uriString;
	private static String url = "http://www.wsthome.com/mysql_test/data.php";
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private Button handin,back;
	EditText sex,phone,email;
	String sex1,phone1,email1;
	int flag;
	Handler handler,handler2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_data);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String success = msg.getData().getString("success");
				sex1 = msg.getData().getString("sex");
				phone1 = msg.getData().getString("phone");
				email1 = msg.getData().getString("email");

				if(success.equals("1"))
				{
					sex.setText(sex1);
					phone.setText(phone1);
					email.setText(email1);
				}
				if(success.equals("-2")){
					Toast.makeText(Data.this, "个人信息获取失败！", Toast.LENGTH_SHORT).show();
				}

			}
		};
		handler2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String success = msg.getData().getString("success");
				if(!success.equals("1"))
	            {
	            	Toast.makeText(Data.this, "上传失败！", Toast.LENGTH_SHORT).show();
	            }
	            else {
	            	sex1 = sex.getText().toString();
	            	phone1 = phone.getText().toString();
	            	email1 = email.getText().toString();
	            	Toast.makeText(Data.this, "上传成功！", Toast.LENGTH_SHORT).show();
				}

			}
		};
		
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
        UID=bundle.getString("UID");//getString()返回指定key的值  
        username=bundle.getString("username");
        uriString = bundle.getString("uri");
		
		sex = (EditText)findViewById(R.id.sex);
		phone = (EditText)findViewById(R.id.phone);
		email = (EditText)findViewById(R.id.email);
		sex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				sex.setText(sex.getText().toString());
				sex.setSelectAllOnFocus(true);
				sex.selectAll();
			}
		});
		phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				phone.setText(phone.getText().toString());
				phone.setSelectAllOnFocus(true);
				phone.selectAll();
			}
		});
		email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
	
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				email.setText(email.getText().toString());
				email.setSelectAllOnFocus(true);
				email.selectAll();
			}
		});
		
		if(validate2())
		{
			flag = 1;
			new Up().execute();
			
		}
		
		handin = (Button)findViewById(R.id.handin);
		handin.setOnTouchListener(new OnTouchListener() {  
            
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
		
		handin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(validate())
				{
					flag = 2;
					new Up().execute();
				}
			}
		});
		
		back = (Button)findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(Data.this,Person.class);
				intent.putExtra("UID", UID);
				intent.putExtra("username", username);
				intent.putExtra("uri", uriString);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	Intent intent = new Intent(Data.this,Person.class);
			intent.putExtra("UID", UID);
			intent.putExtra("username", username);
			intent.putExtra("uri", uriString);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
        }
        return super.onKeyDown(keyCode, event);
    }
	
	class Up extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Data.this);
            pDialog.setMessage("正在获取个性签名..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if(flag==1)
            {
            	params.add(new BasicNameValuePair("UID", UID));
            	params.add(new BasicNameValuePair("download", "1"));
                try{
                 JSONObject json = jsonParser.makeHttpRequest(url,
                         "POST", params);
	             String success = json.getString("success");
	             String sex1,phone1,email1;
	             if(success.equals("1"))
	             {
	            	 sex1 = json.getString("sex");
		             phone1 = json.getString("phone");
		             email1 = json.getString("email");
	             }
	             else {
					sex1=phone1=email1="";
				}
	             
	             Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("success",success);
				bundle.putString("sex",sex1);
				bundle.putString("phone",phone1);
				bundle.putString("email",email1);
				msg.setData(bundle);
				handler.sendMessage(msg);
                 return "";
                }catch(Exception e){
                    e.printStackTrace(); 
                    return "-1";          
                }
            }
            if(flag==2)
            {
            	String sex1 = sex.getText().toString();
                String phone1 = phone.getText().toString();
                String email1 = email.getText().toString();
            	params.add(new BasicNameValuePair("UID", UID));
            	params.add(new BasicNameValuePair("sex", sex1));
            	params.add(new BasicNameValuePair("phone", phone1));
            	params.add(new BasicNameValuePair("email", email1));
                try{
                 JSONObject json = jsonParser.makeHttpRequest(url,
                         "POST", params);
                 String success = json.getString("success");
                 Message msg = new Message();
 				Bundle bundle = new Bundle();
 				bundle.putString("success",success);
 				msg.setData(bundle);
 				handler2.sendMessage(msg);
                 return "";
                }catch(Exception e){
                    e.printStackTrace(); 
                    return "-1";          
                }
            }
            return "";
        }
        protected void onPostExecute(String message) {                  
            pDialog.dismiss();
           //message 为接收doInbackground的返回值
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
 
	private boolean validate()
    {
    	if (!isNetworkAvailable(Data.this))
        {
    		DialogUtil.showDialog(this, "当前无可用网络", false);
            return false;
        }
    	String sex2 = sex.getText().toString().trim();
        if (sex2.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写性别", false);
            return false;
        }
        String phone2 = phone.getText().toString().trim();
        if (phone2.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写手机号", false);
            return false;
        }
        String email2 = email.getText().toString().trim();
        if (email2.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写邮箱", false);
            return false;
        }
        if (sex2.equals(sex1)&&phone2.equals(phone1)&&email2.equals(email1))
        {
        	DialogUtil.showDialog(this, "您的个人资料没有发生改变", false);
            return false;
        }
        
        return true;
    }
	private boolean validate2()
    {
    	if (!isNetworkAvailable(Data.this))
        {
    		DialogUtil.showDialog(this, "当前无可用网络", false);
            return false;
        }
        
        return true;
    }
}
