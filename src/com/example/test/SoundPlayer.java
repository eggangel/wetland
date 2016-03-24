package com.example.test;

import java.net.ContentHandler;
import java.util.HashMap;
import java.util.Map;



import com.example.wetland.R;

import android.R.integer;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.StaticLayout;

public class SoundPlayer {

	private static Context context;// �����Ķ���
	private static boolean playflag = true;// �Ƿ񲥷���Ч
	private static Map<Integer, Integer> soundMap; // R.raw.�е�id��soundPool�е�id�ļ�ֵ��ӦMap
	private static SoundPool soundPool; // ���ֳض���

	public static void init(Context c) {
		context = c;
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		soundMap = new HashMap<Integer, Integer>();
		soundMap.put(R.raw.gang1, soundPool.load(context, R.raw.gang1, 1));
		soundMap.put(R.raw.gang2, soundPool.load(context, R.raw.gang2, 1));
	}

	public static void playsound(int rID) {
		if (!playflag) {
			return;
		} else {
			Integer soundID = soundMap.get(rID);
			if (soundID != null) {
				soundPool.play(soundID, 1, 1, 1, 0, 1);
			}
		}
	}
	public static boolean getplayflag(){
		return playflag;
	}

	public static void setplayflag(boolean flag) {
		playflag = flag;
	}
	
	public static void Releasesoundplayer(){
		soundPool.release();
	}

}
