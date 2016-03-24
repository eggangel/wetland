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

public class Location extends Activity{
	private Button handin,back;
	private String UID;
	private String username;
	private String uriString;
	private static String url = "http://www.wsthome.com/mysql_test/address.php";
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private EditText address;
	int flag;
	String address1;
	Handler handler,handler2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_location);
		Intent intent=getIntent();//getIntent������Ŀ�а�����ԭʼintent����������������������intent��ֵ��һ��Intent���͵ı���intent  
        Bundle bundle=intent.getExtras();//.getExtras()�õ�intent�������Ķ�������  
        UID=bundle.getString("UID");//getString()����ָ��key��ֵ  
        username=bundle.getString("username");
        uriString = bundle.getString("uri");
		address = (EditText)findViewById(R.id.address);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				String success = msg.getData().getString("success");
				address1 = msg.getData().getString("address");
				if(success.equals("1"))
				{
					address.setText(address1);
				}
				if(success.equals("-2")){
					Toast.makeText(Location.this, "������Ϣ��ȡʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
	            	Toast.makeText(Location.this, "�ϴ�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
	            }
	            else {
	            	address1 = address.getText().toString();
	            	Toast.makeText(Location.this, "�ϴ��ɹ���", Toast.LENGTH_SHORT).show();
				}

			}
		};
		
		if(validate2())
		{
			flag = 1;
			new Up().execute();
		}
		address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				address.setText(address.getText().toString());
				address.setSelectAllOnFocus(true);
				address.selectAll();
			}
		});
		
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
					flag = 2;
					new Up().execute();
				}
			}
		});
		
		back = (Button)findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(Location.this,Person.class);
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
        	Intent intent = new Intent(Location.this,Person.class);
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
            pDialog = new ProgressDialog(Location.this);
            pDialog.setMessage("���ڻ�ȡ����ǩ��..");
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
                 String address1 = json.getString("address");
                 Message msg = new Message();
                 Bundle bundle = new Bundle();
                 bundle.putString("success", success);
                 bundle.putString("address", address1);
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
            	params.add(new BasicNameValuePair("UID", UID));
            	params.add(new BasicNameValuePair("address", address.getText().toString().trim()));
                try{
                 JSONObject json = jsonParser.makeHttpRequest(url,
                         "POST", params);
                 String success = json.getString("success");
                 Message msg = new Message();
                 Bundle bundle = new Bundle();
                 bundle.putString("success", success);
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
    	if (!isNetworkAvailable(Location.this))
        {
    		DialogUtil.showDialog(this, "��ǰ�޿�������", false);
            return false;
        }
    	String address2 = address.getText().toString().trim();
        if (address2.equals(""))
        {
            DialogUtil.showDialog(this, "����û����д��ַ", false);
            return false;
        }
        if (address2.equals(address1))
        {
        	DialogUtil.showDialog(this, "����д�ĵ�ַû�з����ı�", false);
            return false;
        }
        
        return true;
    }
	
	private boolean validate2()
    {
    	if (!isNetworkAvailable(Location.this))
        {
    		DialogUtil.showDialog(this, "��ǰ�޿�������", false);
            return false;
        }
        
        return true;
    }
}
