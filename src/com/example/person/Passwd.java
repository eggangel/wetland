package com.example.person;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.example.person.Data.Up;
import com.example.wetland.Code;
import com.example.wetland.DialogUtil;
import com.example.wetland.JSONParser;
import com.example.wetland.R;

public class Passwd extends Activity{
	private String UID;
	private String username;
	private String uriString;
	private static String url = "http://www.wsthome.com/mysql_test/passwd.php";
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private Button handin,back;
	private EditText oldpd,newpd;
	private Handler handler;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_passwd);
		Intent intent=getIntent();//getIntent������Ŀ�а�����ԭʼintent����������������������intent��ֵ��һ��Intent���͵ı���intent  
        Bundle bundle=intent.getExtras();//.getExtras()�õ�intent�������Ķ�������  
        UID=bundle.getString("UID");//getString()����ָ��key��ֵ  
        username=bundle.getString("username");
        uriString = bundle.getString("uri");
        
        oldpd = (EditText)findViewById(R.id.oldpd);
        newpd = (EditText)findViewById(R.id.newpd);
        oldpd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				oldpd.setText(oldpd.getText().toString());
				oldpd.setSelectAllOnFocus(true);
				oldpd.selectAll();
			}
		});
        newpd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				newpd.setText(newpd.getText().toString());
				newpd.setSelectAllOnFocus(true);
				newpd.selectAll();
			}
		});
        
        handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				String success = msg.getData().getString("success");
				if(success.equals("0")||success.equals("-2"))
	            {
	            	Toast.makeText(Passwd.this, success, Toast.LENGTH_SHORT).show();
	            }
				else if(success.equals("-1"))
				{
					Toast.makeText(Passwd.this, "������������������룡", Toast.LENGTH_SHORT).show();
				}
	            else {
	            	Toast.makeText(Passwd.this, "�����޸ĳɹ���", Toast.LENGTH_SHORT).show();
				}
			}
		};
        
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
				// TODO �Զ����ɵķ������
				if(validate())
				{
					new Up().execute();
				}
			}
		});
		
		back = (Button)findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(Passwd.this,Person.class);
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
        	Intent intent = new Intent(Passwd.this,Person.class);
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
            pDialog = new ProgressDialog(Passwd.this);
            pDialog.setMessage("���ڻ�ȡ����ǩ��..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
        	params.add(new BasicNameValuePair("UID", UID));
        	params.add(new BasicNameValuePair("newpd", newpd.getText().toString()));
        	params.add(new BasicNameValuePair("oldpd", oldpd.getText().toString()));
        	params.add(new BasicNameValuePair("username", username));
            try{
             JSONObject json = jsonParser.makeHttpRequest(url,
                     "POST", params);
             String success = json.getString("success");
             Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("success",success);
			msg.setData(bundle);
			handler.sendMessage(msg);
             return "";
            }catch(Exception e){
                e.printStackTrace(); 
                return "-1";          
            }
        }
        protected void onPostExecute(String message) {                  
            pDialog.dismiss();
           //message Ϊ����doInbackground�ķ���ֵ
            //Toast.makeText(getApplicationContext(), message, 8000).show();
        }
    }
 
	public boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// ��ȡNetworkInfo����
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					System.out.println(i + "===״̬==="
							+ networkInfo[i].getState());
					System.out.println(i + "===����==="
							+ networkInfo[i].getTypeName());
					// �жϵ�ǰ����״̬�Ƿ�Ϊ����״̬
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
    	if (!isNetworkAvailable(Passwd.this))
        {
    		DialogUtil.showDialog(this, "��ǰ�޿�������", false);
            return false;
        }
    	String oldpd1 = oldpd.getText().toString().trim();
        if (oldpd1.equals(""))
        {
            DialogUtil.showDialog(this, "����û����д������", false);
            return false;
        }
        String newpd1 = newpd.getText().toString().trim();
        if (newpd1.equals(""))
        {
            DialogUtil.showDialog(this, "����û����д������", false);
            return false;
        }
        if (newpd1.equals(oldpd1))
        {
        	DialogUtil.showDialog(this, "�¾����벻����ͬ����������д", false);
            return false;
        }
        
        String txt = newpd.getText().toString();
		char[] tran = txt.toCharArray();
		String b = String.valueOf(tran[0]);
		Pattern p = Pattern.compile(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
		Matcher m = p.matcher(txt);
		Pattern p1=Pattern.compile("[a-zA-Z]");
	    Matcher m1=p1.matcher(b);
		if (!m.matches()) {
			DialogUtil.showDialog(this, "������û��ͬʱ������ĸ������", false);
			return false;
		}
		if (!m1.matches()) {
			DialogUtil.showDialog(this, "��������λ������ĸ", false);
			return false;
		}
		if (txt.length()<6) {
			DialogUtil.showDialog(this, "����������6���ַ�", false);
			return false;
		}
        
        return true;
    }
}
