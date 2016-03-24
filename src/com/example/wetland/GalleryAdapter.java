package com.example.wetland;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {

	private Context context = null;
	// ����һ�����飬������Ҫ��ʾ��ͼƬ��Դ
	private int images[] = { R.drawable.meinv1, R.drawable.meinv2,
			R.drawable.meinv3, R.drawable.meinv4 };

	public GalleryAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {// ȡ����ʾ������������������Ϊ���
		return Integer.MAX_VALUE;// ��Դ����
	}

	@Override
	public Object getItem(int position) {// ȡ����ʾ��
		return images[position % images.length];
	}

	@Override
	public long getItemId(int position) {// ȡ�����ID
		return images[position % images.length];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView image = new ImageView(this.context);// ����ImageView���
		image.setImageResource(images[position % images.length]);// ��ָ������Դ���õ�ImageView�����
		image.setScaleType(ImageView.ScaleType.FIT_XY);// ����ͼƬ���ݳߴ���ʾ
		image.setLayoutParams(new Gallery.LayoutParams(// ����ͼƬ�Ŀ��
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return image;
	}
}
