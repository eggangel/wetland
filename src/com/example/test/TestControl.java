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

	private CharSequence items[] = { "开始游戏", "音效设置", "结束游戏" };

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
		
		setTitle("乐游湿地·选择问答");
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
		SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
		Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
        String str=bundle.getString("user");//getString()返回指定key的值  
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
			SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
			Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
			intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
	        bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
	        str=bundle.getString("user");//getString()返回指定key的值  
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
			SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
			Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
			intent = getIntent();
			bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
	        str=bundle.getString("user");//getString()返回指定key的值  
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
			SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
			Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
			intent = getIntent();
			bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
	        str=bundle.getString("user");//getString()返回指定key的值  
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
			SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
			Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
			intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
	        bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
	        str=bundle.getString("user");//getString()返回指定key的值  
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
			// 播放音效
			SoundPlayer.playsound(R.raw.gang2);
			Intent intent1=new Intent(TestControl.this,Test.class);
			startActivity(intent1);    //进入游戏界面
			// 设置Activity之间的切换效果
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button2:
			Intent intent2 = new Intent(TestControl.this, VolumControl.class);
			startActivity(intent2);
			// 设置Activity之间的切换效果
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.button3:
			//用对话框的形式提示确定要不要退出游戏
			AlertDialog.Builder builder=new AlertDialog.Builder(this);   
			builder.setTitle("提示");
			builder.setMessage("你确定要退出游戏么？");
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					onBackPressed();// 调用按返回键时触发的事件处理
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
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
                Toast.makeText(this, "再按一次退出程序", 1000).show();
                return false;
            }else{
            	SoundPlayer.Releasesoundplayer();// 退出Activity时释放soundpool中的资源
    			Mediaplayer.ReleaseMediaplayer();// 退出Activity时释放mediaplayer中的资源
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
