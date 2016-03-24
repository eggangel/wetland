package com.artifex.mupdfdemo;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.example.wetland.Content;
import com.example.wetland.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ChoosePDFActivity extends ListActivity {
	static private File  mDirectory;
	static private Map<String, Integer> mPositions = new HashMap<String, Integer>();
	private File         mParent;
	private File []      mDirs;
	private File []      mFiles;
	private Handler	     mHandler;
	private Runnable     mUpdateFiles;
	private ChoosePDFAdapter adapter;
	private String username,uriString,UID;
	private boolean isExit;
	private Handler handler;
	int getin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		handler = new Handler(){
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            isExit = false;
	        }
	    };
		
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
        username=bundle.getString("username");//getString()返回指定key的值 
        UID=bundle.getString("UID");
        getin=bundle.getInt("getin");

		String storageState = Environment.getExternalStorageState();

		if (!Environment.MEDIA_MOUNTED.equals(storageState)
				&& !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.no_media_warning);
			builder.setMessage(R.string.no_media_hint);
			AlertDialog alert = builder.create();
			alert.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.dismiss),
					new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			alert.show();
			return;
		}

		if (mDirectory == null)
			mDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		// Create a list adapter...
		adapter = new ChoosePDFAdapter(getLayoutInflater());
		setListAdapter(adapter);

		// ...that is updated dynamically when files are scanned
		mHandler = new Handler();
		mUpdateFiles = new Runnable() {
			public void run() {
				Resources res = getResources();
				String appName = res.getString(R.string.app_name);
				String version = res.getString(R.string.version);
				String title = res.getString(R.string.picker_title_App_Ver_Dir);
				setTitle(String.format(title, appName, version, mDirectory));

				mParent = mDirectory.getParentFile();

				mDirs = mDirectory.listFiles(new FileFilter() {

					public boolean accept(File file) {
						return file.isDirectory();
					}
				});
				if (mDirs == null)
					mDirs = new File[0];

				mFiles = mDirectory.listFiles(new FileFilter() {

					public boolean accept(File file) {
						if (file.isDirectory())
							return false;
						String fname = file.getName().toLowerCase();
						if (fname.endsWith(".pdf"))
							return true;
						if (fname.endsWith(".xps"))
							return true;
						if (fname.endsWith(".cbz"))
							return true;
						if (fname.endsWith(".png"))
							return true;
						if (fname.endsWith(".jpe"))
							return true;
						if (fname.endsWith(".jpeg"))
							return true;
						if (fname.endsWith(".jpg"))
							return true;
						if (fname.endsWith(".jfif"))
							return true;
						if (fname.endsWith(".jfif-tbnl"))
							return true;
						if (fname.endsWith(".tif"))
							return true;
						if (fname.endsWith(".tiff"))
							return true;
						return false;
					}
				});
				if (mFiles == null)
					mFiles = new File[0];

				Arrays.sort(mFiles, new Comparator<File>() {
					public int compare(File arg0, File arg1) {
						return arg0.getName().compareToIgnoreCase(arg1.getName());
					}
				});

				Arrays.sort(mDirs, new Comparator<File>() {
					public int compare(File arg0, File arg1) {
						return arg0.getName().compareToIgnoreCase(arg1.getName());
					}
				});

				adapter.clear();
				if (mParent != null)
					adapter.add(new ChoosePDFItem(ChoosePDFItem.Type.PARENT, getString(R.string.parent_directory)));
				for (File f : mDirs)
					adapter.add(new ChoosePDFItem(ChoosePDFItem.Type.DIR, f.getName()));
				for (File f : mFiles)
					adapter.add(new ChoosePDFItem(ChoosePDFItem.Type.DOC, f.getName()));

				lastPosition();
			}
		};

		// Start initial file scan...
		mHandler.post(mUpdateFiles);

		// ...and observe the directory and scan files upon changes.
		FileObserver observer = new FileObserver(mDirectory.getPath(), FileObserver.CREATE | FileObserver.DELETE) {
			public void onEvent(int event, String path) {
				mHandler.post(mUpdateFiles);
			}
		};
		observer.startWatching();
	}

	private void lastPosition() {
		String p = mDirectory.getAbsolutePath();
		if (mPositions.containsKey(p))
			getListView().setSelection(mPositions.get(p));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		mPositions.put(mDirectory.getAbsolutePath(), getListView().getFirstVisiblePosition());

		if (position < (mParent == null ? 0 : 1)) {
			mDirectory = mParent;
			mHandler.post(mUpdateFiles);
			return;
		}

		position -= (mParent == null ? 0 : 1);

		if (position < mDirs.length) {
			mDirectory = mDirs[position];
			mHandler.post(mUpdateFiles);
			return;
		}

		position -= mDirs.length;

		uriString = mFiles[position].getAbsolutePath();
		Uri uri = Uri.parse(mFiles[position].getAbsolutePath());
		Intent intent = new Intent(this,MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		intent.putExtra("UID", UID);
		intent.putExtra("getin", getin*2);
		intent.putExtra("uri", uriString);
		intent.putExtra("username", username);
		startActivity(intent);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPositions.put(mDirectory.getAbsolutePath(), getListView().getFirstVisiblePosition());
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	if(keyCode==KeyEvent.KEYCODE_BACK){
	            if(!isExit){
	                isExit = true;
	                handler.sendEmptyMessageDelayed(0, 1500);
	                Toast.makeText(this, "再按一次退出PDF选择程序", 1000).show();
	                return false;
	            }else{
	            	Intent intent = new Intent(ChoosePDFActivity.this,Content.class);
	                intent.putExtra("str", username);
	                if(uriString == null)
	                {
	                	uriString = "-1";
	                }
	                intent.putExtra("uri", uriString);
	                intent.putExtra("getin", getin*2);
	        		intent.putExtra("uri", uriString);
	                intent.setAction(Intent.ACTION_VIEW);  
	        		startActivity(intent);  
	        		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	        		finish();
	            }
	        }
            
        }
        return super.onKeyDown(keyCode, event);
    }
}
