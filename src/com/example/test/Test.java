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

	private TextView stateView, stateprogressView, questionView; // ����״̬��Ϣ
	private Button aswA, aswB, aswC, aswD; // 4����ѡ�ť
	private ProgressBar timeprogress; // ʱ�������
	private int wr = 0; // ��������
	private int tr = 0; // ��Ե�����
	private int qnumber = 1; // ��ǰ��Ŀ�����
	private int statenum = 1; // ��ǰ����
	private final static int sum = 5; // �ܹ���Ҫ��Ե�����
	private final static int wrsum = 3; // �ܹ��ɴ��Ĵ���
	private final static int LASTSTATE = 2; // ���չ���
	private final static int CHANGE_QUESTION = 1; // �任��Ϸ������Ŀ�ı�ʶ��
	private final static int SETPROGRESS = 2; // ��ʾ����ʱ��������ı�ʶ��
	private final static int RESTARTGAME = 3; // ���¿�ʼ��Ϸ�ı�ʶ��
	private final static int TIMEOUT = 4; // ��ʾ��ʱ��ı�ʶ��
	private final static int CHANGETEST = 5;
	private static boolean OVERTIME = false; // �Ƿ��Ѿ���ʱ��ʶ��
	// ��mainMap���洢�����Ӧ����Ϣ
	private Map<String, String> mainMap = new HashMap<String, String>();
	private boolean flag = false; // �����Ƿ���
	private int progressBarValue = 0; // ��ʾʱ��������Ľ���
	private final static int TOTALPROGRESS = 300; // ����ʱ������������ֵ
	private Timer timer; // ����һ����ʱ��
	private Random random = new Random(); // ����һ��������������ȡ��Ŀ
	private int[] QuestionNum = new int[8]; // ÿһ����Ŀ�����к�

	private Handler handler2;
	// ���̺߳�handler��������Ϣ
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_QUESTION:
				mainMap = (Map<String, String>) msg.obj;
				stateView.setText("��" + statenum + "��");
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
				OVERTIME = true; // ����Ϊ��ʱ
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
					questionView.append("\n�𰸣�A.");
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
					questionView.append("\n�𰸣�B.");
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
					questionView.append("\n�𰸣�C.");
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
					questionView.append("\n�𰸣�D.");
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
		    	if (flag) { // �����ԣ���Ӧ�������иı�
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
					if (wr == wrsum) { // �����������ﵽ���ޣ�������Ϸ�����Ի���
						new ShowGameOverDialog().showdialog();
					} else { // ���������Ŀ
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
		InitialQNum(); // ��ʼ�������������
		new Up().execute();
		//////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////
		handler2 = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg){
				if(msg.what == 1)
				{
					TestNum();//��ʼ����Ŀ��������
					new Thread(new StartGame()).start();
					timer = new Timer(true);
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (progressBarValue == TOTALPROGRESS) {
								// ������Ϸʱ�䣬�����Ի�����ʾ���
								handler.sendEmptyMessage(RESTARTGAME);
							} else {
								// ����Ϣ���͸�handler�����½�����
								Message message = Message.obtain();
								message.obj = progressBarValue;
								message.what = SETPROGRESS;
								handler.sendMessage(message);
								// ʱ���������
								progressBarValue++;
							}
						}
					}, 0, 1000);
				}
			}
		};
		
		
		
		
	}

	// ��ʼ��QuestionNum����,�����ȡ
	private void InitialQNum() {
		int count = 0;
		while (count < 8) {
			boolean flag1 = true; // ��־�Ƿ��ظ�
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
	
	// ��ʼ��TestNum����,��ȡ��Ŀ����
			private void TestNum() {
				int count = 0;
				for(int i=0;i<length;i++)
				{
					exchange[0][i] = i+1+"";
				}
				while (count < 16) {
					boolean flag1 = true; // ��־�Ƿ��ظ�
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
				int l = 16;//��Ŀ����������
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
			// ��MakeIntToString��������ת���ַ�����ѡ���Ӧ��Ŀ
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
			
			// ��message�������̴߳�����Ϣ������
			Message message = Message.obtain();
			message.obj = map; // ��map��Ϣ����message��
			message.what = CHANGE_QUESTION; // �趨message�ı�ʾ��
			handler.sendMessage(message); // �����߳��е�handler������Ϣ
		}

	}

	// ��Ϸ������һ��
	private void GoToNextState() {
		if (OVERTIME) {
			return;
		}
		timer.cancel(); // �ر�ʱ��
		statenum++; // ��������
		qnumber = 1; // �������Ϊ1
		wr = 0; // �������
		tr = 0; // �������
		InitialQNum(); // ���³�ȡ�������Ϊ��Ŀ����
		progressBarValue = 0; // ��ʱ���������Ϊ0
		Toast.makeText(Test.this, "��ϲ������" + statenum + "�أ�", 0)
				.show();
		new Thread(new StartGame()).start();
		timer = null;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressBarValue == TOTALPROGRESS) {
					// ������Ϸʱ�䣬�����Ի�����ʾ���
					handler.sendEmptyMessage(RESTARTGAME);
				} else {
					// ����Ϣ���͸�handler�����½�����
					Message message = Message.obtain();
					message.obj = progressBarValue;
					message.what = SETPROGRESS;
					handler.sendMessage(message);
					// ʱ���������
					progressBarValue++;
				}
			}
		}, 0, 1000);
	}

	// ���¿�ʼ��Ϸ
	private class RestartGame {
		public RestartGame() {

		}

		public void restart() {
			statenum = 1;
			qnumber = 1; // �������Ϊ1
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
						// ������Ϸʱ�䣬�����Ի�����ʾ���
						handler.sendEmptyMessage(RESTARTGAME);
					} else {
						// ����Ϣ���͸�handler�����½�����
						Message message = Message.obtain();
						message.obj = progressBarValue;
						message.what = SETPROGRESS;
						handler.sendMessage(message);
						// ʱ���������
						progressBarValue++;
					}
				}
			}, 0, 1000);
			new Thread(new StartGame()).start();
		}
	}

	// ��Ϸ��ʱ�����Ի���
	public class ShowTimeOverDialog {
		public ShowTimeOverDialog() {

		}

		public void showdialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Test.this);
			builder.setTitle("��ʾ");
			builder.setMessage("�Բ�����û���ڹ涨ʱ������ɴ��⣡");
			builder.setPositiveButton("���¿�ʼ",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							OVERTIME = false; // ����Ϊ����ʱ
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("������",
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

	// ��Ϸʧ��ʱ�����ĶԻ���
	public class ShowGameOverDialog {

		public ShowGameOverDialog() {

		}

		public void showdialog() {
			timer.cancel();
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Test.this);
			builder.setTitle("��ʾ");
			builder.setMessage("�Բ����㴳��ʧ���ˣ�");
			builder.setPositiveButton("���´���",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new RestartGame().restart();
						}
					});
			builder.setNegativeButton("������",
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
		builder.setTitle("��ʾ");
		builder.setMessage("��ϲ��ͨ�أ���");
		builder.setPositiveButton("ǫ��ǫ��",
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
	public void onBackPressed() { // �����ؼ�ʱ�����¼�
		// TODO Auto-generated method stub
		super.onBackPressed();
		timer.cancel(); // ��ʱ��ȡ�����ÿ�
		timer = null;
		Test.this.finish();
	}
	
	public class ShowAnswer implements Runnable
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = Message.obtain();
			message.what = TIMEOUT; // �趨message�ı�ʾ��
			handler.sendMessage(message); // �����߳��е�handler������Ϣ

		}
	}
	Vibrator vibrator;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aswA:
			buffer = 1;
			// ���ص�ǰ�Ƿ���
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
						// TODO �Զ����ɵ� catch ��
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
						// TODO �Զ����ɵ� catch ��
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
						// TODO �Զ����ɵ� catch ��
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
						// TODO �Զ����ɵ� catch ��
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
			pDialog.setMessage("��������..");
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
			// message Ϊ����doInbackground�ķ���ֵ
			// Toast.makeText(getApplicationContext(), message, 8000).show();
		}
	}

}
