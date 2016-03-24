package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;



















import com.example.wetland.JSONParser2;
import com.example.wetland.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Test extends Activity implements OnClickListener {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	private ProgressDialog pDialog;
	private static String url = "http://www.wsthome.com/mysql_test/ques.php";
	private static final String TAG_MESSAGE = "message";
	String exchange[][] = new String[8][100];
	JSONParser2 jsonParser2 = new JSONParser2();
	/////////////////////////////////////////////////////////////////////////
	String test[][] = new String[8][16];
	int length = 0;
/////////////////////////////////////////////////////////////////////////////
	Map<String, String> map;
	private int buffer = 0;
	private int id;

	private TextView stateView, stateprogressView, questionView; // 各种状态信息
	private Button aswA, aswB, aswC, aswD; // 4个答案选项按钮
	private ProgressBar timeprogress; // 时间进度条
	private int wr = 0; // 答错的题数
	private int tr = 0; // 答对的题数
	private int qnumber = 1; // 当前题目的题号
	private int statenum = 1; // 当前关数
	private final static int sum = 5; // 总共需要答对的题数
	private final static int wrsum = 3; // 总共可答错的次数
	private final static int LASTSTATE = 2; // 最终关数
	private final static int CHANGE_QUESTION = 1; // 变换游戏界面题目的标识符
	private final static int SETPROGRESS = 2; // 表示设置时间进度条的标识符
	private final static int RESTARTGAME = 3; // 重新开始游戏的标识符
	private final static int TIMEOUT = 4; // 显示答案时间的标识符
	private final static int CHANGETEST = 5;
	private static boolean OVERTIME = false; // 是否已经超时标识符
	// 用mainMap来存储该题对应的信息
	private Map<String, String> mainMap = new HashMap<String, String>();
	private boolean flag = false; // 此题是否答对
	private int progressBarValue = 0; // 表示时间进度条的进度
	private final static int TOTALPROGRESS = 300; // 设置时间进度条的最大值
	private Timer timer; // 设置一个定时器
	private Random random = new Random(); // 设置一个随机数来随机抽取题目
	private int[] QuestionNum = new int[8]; // 每一关题目的序列号

	private Handler handler2;
	// 用线程和handler来处理消息
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				stateView.setText("第" + statenum + "关");
				stateprogressView.setText(tr + "/" + sum + "\n" + wr + "/"
						+ wrsum);
				questionView.setText(qnumber + ":" + mainMap.get("question"));
				aswA.setText("A." + mainMap.get("a"));
				aswB.setText("B." + mainMap.get("b"));
				aswC.setText("C." + mainMap.get("c"));
				if(mainMap.get("d").equals(""))
				{
					aswD.setVisibility(View.INVISIBLE);
				}
				else
				{
					aswD.setVisibility(View.VISIBLE);
					aswD.setText("D." + mainMap.get("d"));
				}
				
				break;
			case SETPROGRESS:
				int progress = (Integer) msg.obj;
				timeprogress.setProgress(progress);
				break;
			case RESTARTGAME:
				timer.cancel();
				OVERTIME = true; // 设置为超时
				new ShowTimeOverDialog().showdialog();
				break;
			case TIMEOUT:

				String Trueanswer = mainMap.get("answer");
				if (Trueanswer.equals("a")) {
					if(buffer == 1)
					{
						aswA.setBackgroundColor(Color.YELLOW);
					}
					if(buffer == 2)
					{
						aswB.setBackgroundColor(Color.BLACK);
						aswB.setTextColor(Color.WHITE);
					}
					if(buffer == 3)
					{
						aswC.setBackgroundColor(Color.BLACK);
						aswC.setTextColor(Color.WHITE);
					}
					if(buffer == 4)
					{
						aswD.setBackgroundColor(Color.BLACK);
						aswD.setTextColor(Color.WHITE);
					}
					questionView.append("\n答案：A.");
					aswA.setTextColor(Color.RED);
				}
				if (Trueanswer.equals("b")) {
					if(buffer == 1)
					{
						aswA.setBackgroundColor(Color.BLACK);
						aswA.setTextColor(Color.WHITE);
					}
					if(buffer == 2)
					{
						aswB.setBackgroundColor(Color.YELLOW);
					}
					if(buffer == 3)
					{
						aswC.setBackgroundColor(Color.BLACK);
						aswC.setTextColor(Color.WHITE);
					}
					if(buffer == 4)
					{
						aswD.setBackgroundColor(Color.BLACK);
						aswD.setTextColor(Color.WHITE);
					}
					questionView.append("\n答案：B.");
					aswB.setTextColor(Color.RED);
				}
				if (Trueanswer.equals("c")) {
					if(buffer == 1)
					{
						aswA.setBackgroundColor(Color.BLACK);
						aswA.setTextColor(Color.WHITE);
					}
					if(buffer == 2)
					{
						aswB.setBackgroundColor(Color.BLACK);
						aswB.setTextColor(Color.WHITE);

					}
					if(buffer == 3)
					{
						aswC.setBackgroundColor(Color.YELLOW);
					}
					if(buffer == 4)
					{
						aswD.setBackgroundColor(Color.BLACK);
						aswD.setTextColor(Color.WHITE);
					}
					questionView.append("\n答案：C.");
					aswC.setTextColor(Color.RED);
				}
				if (Trueanswer.equals("d")) {
					if(buffer == 1)
					{
						aswA.setBackgroundColor(Color.BLACK);
						aswA.setTextColor(Color.WHITE);
					}
					if(buffer == 2)
					{
						aswB.setBackgroundColor(Color.BLACK);
						aswB.setTextColor(Color.WHITE);

					}
					if(buffer == 3)
					{
						aswC.setBackgroundColor(Color.BLACK);
						aswC.setTextColor(Color.WHITE);
					}
					if(buffer == 4)
					{
						aswD.setBackgroundColor(Color.YELLOW);
					}
					questionView.append("\n答案：D.");
					aswD.setTextColor(Color.RED);
				}
				break;
			case CHANGETEST:
				aswA.setTextColor(Color.BLACK);
				aswB.setTextColor(Color.BLACK);
				aswC.setTextColor(Color.BLACK);
				aswD.setTextColor(Color.BLACK);
				String color = "#33CCFF";
				int cor = Color.parseColor(color);
				aswA.setBackgroundColor(cor);
				aswB.setBackgroundColor(cor);
				aswC.setBackgroundColor(cor);
				aswD.setBackgroundColor(cor);
		    	if (flag) { // 如果答对，对应参数进行改变
					tr++;
					qnumber++;
					if (tr == sum) {
						if (statenum == LASTSTATE) {
							GoOverGame();
						} else {
							GoToNextState();
						}
					} else {
						new Thread(new StartGame()).start();
					}
				} else {
					wr++;
					qnumber++;
					if (wr == wrsum) { // 当错误题量达到上限，弹出游戏结束对话框
						new ShowGameOverDialog().showdialog();
					} else { // 否则更换题目
						new Thread(new StartGame()).start();
					}
				}
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ggg);
		stateView = (TextView) this.findViewById(R.id.statetext);
		stateprogressView = (TextView) this.findViewById(R.id.stateprogress);
		questionView = (TextView) this.findViewById(R.id.questiontext);
		aswA = (Button) this.findViewById(R.id.aswA);
		aswA.setAlpha((float) 0.5);
		aswA.setOnClickListener(this);
		aswB = (Button) this.findViewById(R.id.aswB);
		aswB.setAlpha((float) 0.5);
		aswB.setOnClickListener(this);
		aswC = (Button) this.findViewById(R.id.aswC);
		aswC.setAlpha((float) 0.5);
		aswC.setOnClickListener(this);
		aswD = (Button) this.findViewById(R.id.aswD);
		aswD.setAlpha((float) 0.5);
		aswD.setOnClickListener(this);
		timeprogress = (ProgressBar) this.findViewById(R.id.progressBar1);
		timeprogress.setMax(TOTALPROGRESS);
		InitialQNum(); // 初始化题号序列数组
		new Up().execute();
		//////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////
		handler2 = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg){
				if(msg.what == 1)
				{
					TestNum();//初始化题目序列数组
					new Thread(new StartGame()).start();
					timer = new Timer(true);
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (progressBarValue == TOTALPROGRESS) {
								// 超出游戏时间，弹出对话框提示玩家
								handler.sendEmptyMessage(RESTARTGAME);
							} else {
								// 将信息传送给handler来更新进度条
								Message message = Message.obtain();
								message.obj = progressBarValue;
								message.what = SETPROGRESS;
								handler.sendMessage(message);
								// 时间进度自增
								progressBarValue++;
							}
						}
					}, 0, 1000);
				}
			}
		};
		
		
		
		
	}

	// 初始化QuestionNum数组,随机抽取
	private void InitialQNum() {
		int count = 0;
		while (count < 8) {
			boolean flag1 = true; // 标志是否重复
			int cur = Math.abs(random.nextInt() % 8) + 1;
			for (int i = 0; i < count; i++) {
				if (cur == QuestionNum[i]) {
					flag1 = false;
					break;
				}
			}
			if (flag1) {
				QuestionNum[count] = cur;
				count++;
			}
		}
	}
	
	// 初始化TestNum数组,获取题目序列
			private void TestNum() {
				int count = 0;
				for(int i=0;i<length;i++)
				{
					exchange[0][i] = i+1+"";
				}
				while (count < 16) {
					boolean flag1 = true; // 标志是否重复
					int cur = Math.abs(random.nextInt() % length) + 1;
					for (int i = 0; i < count; i++) {
						if (test[0][i].equals(cur+"")) {
							flag1 = false;
							break;
						}
					}
					if (flag1) {
						test[0][count] = cur+"";
						test[1][count] = exchange[1][cur-1];
						test[2][count] = exchange[2][cur-1];
						test[3][count] = exchange[3][cur-1];
						test[4][count] = exchange[4][cur-1];
						test[5][count] = exchange[5][cur-1];
						test[6][count] = exchange[6][cur-1];
						test[7][count] = exchange[7][cur-1];
						count++;
					}
				}
				int l = 16;//题目数量！！！
				for(int i=0;i<l/2;i++)
				{
					test[0][i] = String.valueOf(i+1);
					test[1][i] = String.valueOf(1);
				}
				for(int i=l/2;i<l;i++)
				{
					test[0][i] = String.valueOf(i+1);
					test[1][i] = String.valueOf(2);
				}
			}

	public class StartGame implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			buffer = 0;
			Map<String, String> map = new HashMap<String, String>();
			// 用MakeIntToString工具类来转换字符，并选择对应题目
			String str = MakeIntToString.getString(QuestionNum[qnumber - 1]
					+ (statenum - 1) * 8);
			String str1 = String.valueOf(statenum);
			String[] strs = new String[] { str, str1 };
			
			id = Integer.parseInt(strs[0]);
			String columname = "id";
			String columvalue = test[0][id - 1];
			map.put(columname, columvalue);

			columname = "state";
			columvalue = test[1][id - 1];
			map.put(columname, columvalue);

			columname = "a";
			columvalue = test[2][id - 1];
			map.put(columname, columvalue);

			columname = "b";
			columvalue = test[3][id - 1];
			map.put(columname, columvalue);

			columname = "c";
			columvalue = test[4][id - 1];
			map.put(columname, columvalue);

			columname = "d";
			columvalue = test[5][id - 1];
			if (columvalue == null) {
				columvalue = "";
			}
			map.put(columname, columvalue);

			columname = "answer";
			columvalue = test[6][id - 1];
			map.put(columname, columvalue);

			columname = "question";
			columvalue = test[7][id - 1];
			map.put(columname, columvalue);
			
			// 用message来向主线程传递信息并处理
			Message message = Message.obtain();
			message.obj = map; // 将map信息放入message中
			message.what = CHANGE_QUESTION; // 设定message的标示符
			handler.sendMessage(message); // 向主线程中的handler发送信息
		}

	}

	// 游戏进入下一关
	private void GoToNextState() {
		if (OVERTIME) {
			return;
		}
		timer.cancel(); // 关闭时钟
		statenum++; // 关数自增
		qnumber = 1; // 题号重置为1
		wr = 0; // 答错重置
		tr = 0; // 答对重置
		InitialQNum(); // 重新抽取随机数组为题目序列
		progressBarValue = 0; // 将时间进度重置为0
		Toast.makeText(Test.this, "恭喜你进入第" + statenum + "关！", 0)
				.show();
		new Thread(new StartGame()).start();
		timer = null;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressBarValue == TOTALPROGRESS) {
					// 超出游戏时间，弹出对话框提示玩家
					handler.sendEmptyMessage(RESTARTGAME);
				} else {
					// 将信息传送给handler来更新进度条
					Message message = Message.obtain();
					message.obj = progressBarValue;
					message.what = SETPROGRESS;
					handler.sendMessage(message);
					// 时间进度自增
					progressBarValue++;
				}
			}
		}, 0, 1000);
	}

	// 重新开始游戏
	private class RestartGame {
		public RestartGame() {

		}

		public void restart() {
			statenum = 1;
			qnumber = 1; // 重置题号为1
			wr = 0;
			tr = 0;
			progressBarValue = 0;
			InitialQNum();
			TestNum();
			timer = null;
			timer = new Timer(true);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (progressBarValue == TOTALPROGRESS) {
						// 超出游戏时间，弹出对话框提示玩家
						handler.sendEmptyMessage(RESTARTGAME);
					} else {
						// 将信息传送给handler来更新进度条
						Message message = Message.obtain();
						message.obj = progressBarValue;
						message.what = SETPROGRESS;
						handler.sendMessage(message);
						// 时间进度自增
						progressBarValue++;
					}
				}
			}, 0, 1000);
			new Thread(new StartGame()).start();
		}
	}

	// 游戏超时弹出对话框
	public class ShowTimeOverDialog {
		public ShowTimeOverDialog() {

		}

		public void showdialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Test.this);
			builder.setTitle("提示");
			builder.setMessage("对不起，您没有在规定时间内完成答题！");
			builder.setPositiveButton("重新开始",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							OVERTIME = false; // 设置为不超时
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("主界面",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Test.this.finish();
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();

		}
	}

	// 游戏失败时弹出的对话框
	public class ShowGameOverDialog {

		public ShowGameOverDialog() {

		}

		public void showdialog() {
			timer.cancel();
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Test.this);
			builder.setTitle("提示");
			builder.setMessage("对不起，你闯关失败了！");
			builder.setPositiveButton("重新闯关",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("主界面",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Test.this.finish();
						}
					});
			builder.setCancelable(false);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}

	private void GoOverGame() {
		if (OVERTIME) {
			return;
		}
		timer.cancel();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				Test.this);
		builder.setTitle("提示");
		builder.setMessage("恭喜您通关！！");
		builder.setPositiveButton("谦让谦让",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Test.this.finish();
					}
				});
		builder.setCancelable(false);
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onBackPressed() { // 按返回键时触发事件
		// TODO Auto-generated method stub
		super.onBackPressed();
		timer.cancel(); // 将时钟取消并置空
		timer = null;
		Test.this.finish();
	}
	
	public class ShowAnswer implements Runnable
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = Message.obtain();
			message.what = TIMEOUT; // 设定message的标示符
			handler.sendMessage(message); // 向主线程中的handler发送信息

		}
	}
	Vibrator vibrator;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aswA:
			buffer = 1;
			// 返回当前是否答对
			new Thread(new ShowAnswer()).start();
			new Thread(new Runnable(){   

			    public void run(){  
			        try {
			        	flag = new JudgeAnswer(Test.this).judgeit("a", mainMap);
			        	if(flag == false)
			        	{
			    			vibrator = (Vibrator)getSystemService(Test.VIBRATOR_SERVICE);
			    			vibrator.vibrate(500);
			        	}
			        	Thread.currentThread();
						Thread.sleep(1000);
						handler.sendEmptyMessage(CHANGETEST);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}   
			    }   

			}).start(); 
			break;
		case R.id.aswB:
			buffer = 2;
			new Thread(new ShowAnswer()).start();
			new Thread(new Runnable(){   

			    public void run(){  
			        try {
			        	flag = new JudgeAnswer(Test.this).judgeit("b", mainMap);
			        	if(flag == false)
			        	{
			    			vibrator = (Vibrator)getSystemService(Test.VIBRATOR_SERVICE);
			    			vibrator.vibrate(500);
			        	}
			        	Thread.currentThread();
						Thread.sleep(1000);
						handler.sendEmptyMessage(CHANGETEST);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}   
			    }   

			}).start(); 
			break;
		case R.id.aswC:
			buffer = 3;
			new Thread(new ShowAnswer()).start();
			new Thread(new Runnable(){   

			    public void run(){  
			        try {
			        	flag = new JudgeAnswer(Test.this).judgeit("c", mainMap);
			        	if(flag == false)
			        	{
			    			vibrator = (Vibrator)getSystemService(Test.VIBRATOR_SERVICE);
			    			vibrator.vibrate(500);
			        	}
			        	Thread.currentThread();
						Thread.sleep(1000);
						handler.sendEmptyMessage(CHANGETEST);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}   
			    }   

			}).start(); 
			break;
		case R.id.aswD:
			buffer = 4;
			new Thread(new ShowAnswer()).start();
			new Thread(new Runnable(){   

			    public void run(){  
			        try {
			        	flag = new JudgeAnswer(Test.this).judgeit("d", mainMap);
			        	if(flag == false)
			        	{
			    			vibrator = (Vibrator)getSystemService(Test.VIBRATOR_SERVICE);
			    			vibrator.vibrate(500);
			        	}
			        	Thread.currentThread();
						Thread.sleep(1000);
						handler.sendEmptyMessage(CHANGETEST);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}   
			    }   

			}).start(); 
			break;
		}
	}
	
	
	class Up extends AsyncTask<String, String, String> {
		

		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Test.this);
			pDialog.setMessage("正在下载..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// params.add(new BasicNameValuePair("flag", "-1"));

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
					if(l == 0)
					{
						length = Integer.parseInt(jsob.getString("length"));
					}

					exchange[0][l] = jsob.getString("id");
					exchange[1][l] = jsob.getString("state");
					exchange[2][l] = jsob.getString("a");
					exchange[3][l] = jsob.getString("b");
					exchange[4][l] = jsob.getString("c");
					exchange[5][l] = jsob.getString("d");
					exchange[6][l] = jsob.getString("answer");
					exchange[7][l] = jsob.getString("question");
					l++;
					i--;
				}
				Message message = Message.obtain();
				message.what = 1;
				handler2.sendMessage(message);
				
				return "1";
			} catch (Exception e) {
				e.printStackTrace();
				Message message = Message.obtain();
				message.what = 0;
				handler2.sendMessage(message);
				return "0";
			}
		}

		protected void onPostExecute(String message) {
			pDialog.dismiss();
			// message 为接收doInbackground的返回值
			// Toast.makeText(getApplicationContext(), message, 8000).show();
		}
	}

}
