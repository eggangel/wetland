package com.example.test;


import com.example.person.Person;
import com.example.talk.Talk;
import com.example.wetland.Content;
import com.example.wetland.R;

import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class TestControl extends Activity implements View.OnClickListener {

	private Button button;
	private Button button2;
	private Button button3;
	private Button bt1,bt2,bt3,bt4,bt5;
	private boolean isExit;
	private Handler handler;

	private CharSequence items[] = { "��ʼ��Ϸ", "��Ч����", "������Ϸ" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				isExit = false;
			}
		};
		
		setTitle("����ʪ�ء�ѡ���ʴ�");
		SoundPlayer.init(TestControl.this);
		Mediaplayer.init(TestControl.this);
		if (Mediaplayer.getplayflag()) {
			Mediaplayer.PlayBackgroundMusic();
		}
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(this);
		button.setOnTouchListener(new OnTouchListener() {  
            
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                // TODO Auto-generated method stub  
                if(event.getAction()==MotionEvent.ACTION_DOWN){  
                    v.setBackgroundResource(R.drawable.start_press);  
                }else if(event.getAction()==MotionEvent.ACTION_UP){  
                    v.setBackgroundResource(R.drawable.start);  
                }  
                return false;  
            }  
        }); 
		button2 = (Button) this.findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button2.setOnTouchListener(new OnTouchListener() {  
            
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                // TODO Auto-generated method stub  
                if(event.getAction()==MotionEvent.ACTION_DOWN){  
                    v.setBackgroundResource(R.drawable.music_press);  
                }else if(event.getAction()==MotionEvent.ACTION_UP){  
                    v.setBackgroundResource(R.drawable.music);  
                }  
                return false;  
            }  
        }); 
		button3 = (Button) this.findViewById(R.id.button3);
		button3.setOnClickListener(this);
		button3.setOnTouchListener(new OnTouchListener() {  
            
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                // TODO Auto-generated method stub  
                if(event.getAction()==MotionEvent.ACTION_DOWN){  
                    v.setBackgroundResource(R.drawable.out_press);  
                }else if(event.getAction()==MotionEvent.ACTION_UP){  
                    v.setBackgroundResource(R.drawable.out);  
                }  
                return false;  
            }  
        }); 
		
		bt1 = (Button)findViewById(R.id.bt1);
		bt1.setOnClickListener(this);
		
		bt2 = (Button)findViewById(R.id.bt2);
		bt2.setOnClickListener(this);
		
		bt3 = (Button)findViewById(R.id.bt3);
		bt3.setOnClickListener(this);
		bt3.setBackgroundResource(R.drawable.button3_choose);
		
		bt4 = (Button)findViewById(R.id.bt4);
		bt4.setOnClickListener(this);
		
		bt5 = (Button)findViewById(R.id.bt5);
		bt5.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
		Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
		Intent intent=getIntent();//getIntent������Ŀ�а�����ԭʼintent����������������������intent��ֵ��һ��Intent���͵ı���intent  
        Bundle bundle=intent.getExtras();//.getExtras()�õ�intent�������Ķ�������  
        String str=bundle.getString("user");//getString()����ָ��key��ֵ  
        String uriString = bundle.getString("uri");
		intent = new Intent();
		intent.setClass(TestControl.this, Content.class);
		intent.putExtra("str", str);
		intent.putExtra("uri", uriString);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings1:
			onClick(button);
			break;
		case R.id.action_settings2:
			onClick(button2);
			break;
		case R.id.action_settings3:
			onClick(button3);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle bundle;
		String str,uriString,UID;
		switch (v.getId()) {
		case R.id.bt1:
			SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
			Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
			intent=getIntent();//getIntent������Ŀ�а�����ԭʼintent����������������������intent��ֵ��һ��Intent���͵ı���intent  
	        bundle=intent.getExtras();//.getExtras()�õ�intent�������Ķ�������  
	        str=bundle.getString("user");//getString()����ָ��key��ֵ  
	        uriString = bundle.getString("uri");
			intent = new Intent();
			intent.setClass(TestControl.this, Content.class);
			intent.putExtra("str", str);
			intent.putExtra("uri", uriString);
			intent.putExtra("getin", -1);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			break;
		case R.id.bt2:
			SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
			Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
			intent = getIntent();
			bundle=intent.getExtras();//.getExtras()�õ�intent�������Ķ�������  
	        str=bundle.getString("user");//getString()����ָ��key��ֵ  
	        uriString = bundle.getString("uri");
	        UID = bundle.getString("UID");
	        intent = new Intent();
	        intent.setClass(TestControl.this, Content.class);
	        intent.putExtra("UID", UID);
	        intent.putExtra("str", str);
	        intent.putExtra("uri", uriString);
	        intent.putExtra("getin", 3);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			break;
		case R.id.bt3:
			break;
		case R.id.bt4:
			SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
			Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
			intent = getIntent();
			bundle=intent.getExtras();//.getExtras()�õ�intent�������Ķ�������  
	        str=bundle.getString("user");//getString()����ָ��key��ֵ  
	        uriString = bundle.getString("uri");
	        UID = bundle.getString("UID");
	        intent = new Intent();
	        intent.setClass(TestControl.this, Talk.class);
	        intent.putExtra("UID", UID);
	        intent.putExtra("user", str);
	        intent.putExtra("uri", uriString);
	        intent.putExtra("TalkID", 3);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			break;
		case R.id.bt5:
			SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
			Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
			intent=getIntent();//getIntent������Ŀ�а�����ԭʼintent����������������������intent��ֵ��һ��Intent���͵ı���intent  
	        bundle=intent.getExtras();//.getExtras()�õ�intent�������Ķ�������  
	        str=bundle.getString("user");//getString()����ָ��key��ֵ  
	        uriString = bundle.getString("uri");
	        UID = bundle.getString("UID");
	        intent = new Intent();
	        intent.setClass(TestControl.this, Person.class);
	        intent.putExtra("username", str);
	        intent.putExtra("uri", uriString);
	        intent.putExtra("UID", UID);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			break;
			
		case R.id.button1:
			// ������Ч
			SoundPlayer.playsound(R.raw.gang2);
			Intent intent1=new Intent(TestControl.this,Test.class);
			startActivity(intent1);    //������Ϸ����
			// ����Activity֮����л�Ч��
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button2:
			Intent intent2 = new Intent(TestControl.this, VolumControl.class);
			startActivity(intent2);
			// ����Activity֮����л�Ч��
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button3:
			//�öԻ������ʽ��ʾȷ��Ҫ��Ҫ�˳���Ϸ
			AlertDialog.Builder builder=new AlertDialog.Builder(this);   
			builder.setTitle("��ʾ");
			builder.setMessage("��ȷ��Ҫ�˳���Ϸô��");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					onBackPressed();// ���ð����ؼ�ʱ�������¼�����
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
			builder.setCancelable(false);
			AlertDialog dialog=builder.create();
			dialog.show();
			break;

		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(!isExit){
                isExit = true;
                handler.sendEmptyMessageDelayed(0, 1500);
                Toast.makeText(this, "�ٰ�һ���˳�����", 1000).show();
                return false;
            }else{
            	SoundPlayer.Releasesoundplayer();// �˳�Activityʱ�ͷ�soundpool�е���Դ
    			Mediaplayer.ReleaseMediaplayer();// �˳�Activityʱ�ͷ�mediaplayer�е���Դ
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
