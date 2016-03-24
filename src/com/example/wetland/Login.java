package com.example.wetland;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;











import android.R.bool;
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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity{
	private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    EditText name;
    EditText password;
	Button login_button;
	TextView zhuce;
	CheckBox auto;
	private boolean isExit;
    private Handler handler;
	private Handler handler2;
	private static String url = "http://www.wsthome.com/mysql_test/test.php";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        	Intent intent2=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
            Bundle bundle=intent2.getExtras();//.getExtras()得到intent所附带的额外数据  
            String logout=bundle.getString("logout");//getString()返回指定key的值 
            MyPreference.getInstance(Login.this).SetIsSavePwd(false);
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        
        if(MyPreference.getInstance(Login.this).IsSavePwd())
        {
        	Intent intent = new Intent(Login.this,Content.class);
        	intent.putExtra("str", MyPreference.getInstance(Login.this).getLoginName());
        	intent.putExtra("uri", "-1");
        	intent.putExtra("getin", -1);
        	startActivity(intent);
        	overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        	finish();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);
        
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isExit = false;
            }
        };

        
        name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);
        auto = (CheckBox)findViewById(R.id.auto);
        
        login_button=(Button)findViewById(R.id.login_button);
        login_button.setOnTouchListener(new OnTouchListener() {  
            
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                // TODO Auto-generated method stub  
                if(event.getAction()==MotionEvent.ACTION_DOWN){  
                    v.setBackgroundResource(R.drawable.background_login_dix_press);  
                }else if(event.getAction()==MotionEvent.ACTION_UP){  
                    v.setBackgroundResource(R.drawable.background_login);  
                }  
                return false;  
            }  
        });  
        login_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
				if(validate()){                
	                new Up().execute(); 
	                handler2 = new Handler()
	                {
	                	@Override
	                	public void handleMessage(Message msg)
	                	{
	                		super.handleMessage(msg);
	                		String success = msg.getData().getString("success");
	                		if(success.equals("0"))
	    	                {
	    	                	Toast.makeText(Login.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
	    	                }
	    	                if(success.equals("1"))
	    	                {
	    	                	if(auto.isChecked()){  
	    	                        MyPreference.getInstance(Login.this)  
	    	                            .SetLoginName(name.getText().toString().trim());  
	    	                        MyPreference.getInstance(Login.this)  
	    	                            .SetPassword(password.getText().toString().trim());  
	    	                        MyPreference.getInstance(Login.this)  
	    	                            .SetIsSavePwd(auto.isChecked());  
	    	                    }else{  
	    	                        MyPreference.getInstance(Login.this)  
	    	                            .SetLoginName("");  
	    	                        MyPreference.getInstance(Login.this)  
	    	                            .SetPassword("");  
	    	                        MyPreference.getInstance(Login.this)  
	    	                            .SetIsSavePwd(auto.isChecked());  
	    	                    }
	    	                	Toast.makeText(getApplicationContext(), "登录成功！", 8000).show();
	    	                	Intent intent = new Intent(Login.this,Content.class);
	    		                intent.putExtra("str", name.getText().toString());
	    		                intent.putExtra("uri", "-1");
	    		                intent.putExtra("getin", -1);
	    						startActivity(intent);
	    						overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	    						finish();
	    	                }
	                	}
	                };
	                
	            }
            }
        });
        
        name = (EditText)findViewById(R.id.name);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				name.setText(name.getText().toString());
				name.setSelectAllOnFocus(true);
				name.selectAll();
			}
		});
        
        password = (EditText)findViewById(R.id.password);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				password.setText(password.getText().toString());
				password.setSelectAllOnFocus(true);
				password.selectAll();
			}
		});
        
        zhuce=(TextView)findViewById(R.id.zhuce);
        zhuce.setText(Html.fromHtml("<u>如果您还没有注册，请点击这里</u>"));
        zhuce.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this,Register.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
			}
		});
    }
    private boolean validate()
    {
    	if (!isNetworkAvailable(Login.this))
        {
    		DialogUtil.showDialog(this, "当前无可用网络", false);
            return false;
        }
    	
        String name1 = name.getText().toString().trim();
        if (name1.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写用户名", false);
            return false;
        }
        String password1 = password.getText().toString().trim();
        if (password1.equals(""))
        {
            DialogUtil.showDialog(this, "您还没有填写密码", false);
            return false;
        }
        
        return true;
    }
    class Up extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("正在登录..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            String name1 = name.getText().toString();
            String password1 = password.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name1));
            params.add(new BasicNameValuePair("password", password1));
            params.add(new BasicNameValuePair("flag", "0"));
           try{
            JSONObject json = jsonParser.makeHttpRequest(url,
                    "POST", params);
            String message = json.getString(TAG_MESSAGE);
            String success = json.getString("success");
            Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("success", success);
			msg.setData(bundle);
			handler2.sendMessage(msg);
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

}
