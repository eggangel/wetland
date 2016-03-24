package com.example.wetland;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.R.integer;
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
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    EditText name;
    EditText password;
	Button res_button,goback;
	ImageView vc_image;
	String getCode=null;
    EditText vc_code;
    TextView denglu;
    private boolean isExit;
    private Handler handler;
    private Handler handler2;
	
	private static String url = "http://www.wsthome.com/mysql_test/test.php";
    private static final String TAG_MESSAGE = "message";
    //private static final String TAG_SUCCESS = "success";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_main);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isExit = false;
            }
        };
        
        handler2 = new Handler()
        {
        	@Override
        	public void handleMessage(Message msg)
        	{
        		super.handleMessage(msg);
        		String success = msg.getData().getString("success");
        		if(success.equals("0"))
                {
                	Toast.makeText(Register.this, "用户名已使用，请更换用户名！", Toast.LENGTH_SHORT).show();
                	name.requestFocus();
                	vc_image.setImageBitmap(Code.getInstance().getBitmap());
    				getCode=Code.getInstance().getCode();
                }
                if(success.equals("1"))
                {
                	Toast.makeText(getApplicationContext(), "注册成功！", 8000).show();
					Intent intent = new Intent();
					intent.setClass(Register.this, Login.class);
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					finish();
                }
        	}
        };
        
        goback = (Button)findViewById(R.id.goback);
        goback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Register.this, Login.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
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
        
        
        vc_image=(ImageView)findViewById(R.id.vc_image);
		vc_image.setImageBitmap(Code.getInstance().getBitmap());
		vc_code=(EditText) findViewById(R.id.vc_code);
		vc_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				vc_code.setText(vc_code.getText().toString());
				vc_code.setSelectAllOnFocus(true);
				vc_code.selectAll();
			}
		});
		
		getCode=Code.getInstance().getCode(); //获取显示的验证码
		Log.e("info", getCode+"----");
		
		
		vc_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vc_image.setImageBitmap(Code.getInstance().getBitmap());
				getCode=Code.getInstance().getCode();
			}
		});
        
        name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);
        res_button=(Button)findViewById(R.id.res_button);
        
        res_button.setOnTouchListener(new OnTouchListener() {  
            
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
        
        res_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
				if(validate()){                
	                new Up().execute(); 
					
	            }
            }
        });
    }

	private boolean validate() {
		if (!isNetworkAvailable(Register.this)) {
			DialogUtil.showDialog(this, "当前无可用网络", false);
			return false;
		}

		String name1 = name.getText().toString().trim();
		if (name1.equals("")) {
			DialogUtil.showDialog(this, "您还没有填写用户名", false);
			return false;
		}
		String password1 = password.getText().toString().trim();
		if (password1.equals("")) {
			DialogUtil.showDialog(this, "您还没有填写密码", false);
			return false;
		}
		String v_code = vc_code.getText().toString().trim();
		if (v_code == null || v_code.equals("")) {
			DialogUtil.showDialog(this, "您还没有填写验证码", false);
			return false;
		}
		if (!v_code.equals(getCode)) {
			DialogUtil.showDialog(this, "验证码错误", false);
			vc_image.setImageBitmap(Code.getInstance().getBitmap());
			getCode=Code.getInstance().getCode();
			vc_code.setText(vc_code.getText().toString());
			vc_code.setSelectAllOnFocus(false);
			vc_code.selectAll();
			vc_code.clearFocus();
			return false;
		}

		String txt = password.getText().toString();
		char[] tran = txt.toCharArray();
		String b = String.valueOf(tran[0]);
		Pattern p = Pattern.compile(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
		Matcher m = p.matcher(txt);
		Pattern p1=Pattern.compile("[a-zA-Z]");
	    Matcher m1=p1.matcher(b);
		if (!m.matches()) {
			DialogUtil.showDialog(this, "没有同时包含字母和数字", false);
			vc_image.setImageBitmap(Code.getInstance().getBitmap());
			getCode=Code.getInstance().getCode();
			password.requestFocus();
			return false;
		}
		if (!m1.matches()) {
			DialogUtil.showDialog(this, "密码首位不是字母", false);
			vc_image.setImageBitmap(Code.getInstance().getBitmap());
			getCode=Code.getInstance().getCode();
			password.requestFocus();
			return false;
		}
		if (txt.length()<6) {
			DialogUtil.showDialog(this, "密码少于6个字符", false);
			vc_image.setImageBitmap(Code.getInstance().getBitmap());
			getCode=Code.getInstance().getCode();
			password.requestFocus();
			return false;
		}

		return true;
	}
    class Up extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("正在注册中..");
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
            params.add(new BasicNameValuePair("flag", "1"));
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
