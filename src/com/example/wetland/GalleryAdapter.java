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
	// 定义一个数组，用来存要显示的图片资源
	private int images[] = { R.drawable.meinv1, R.drawable.meinv2,
			R.drawable.meinv3, R.drawable.meinv4 };

	public GalleryAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {// 取得显示的内容数量，这里设为最大
		return Integer.MAX_VALUE;// 资源数量
	}

	@Override
	public Object getItem(int position) {// 取得显示项
		return images[position % images.length];
	}

	@Override
	public long getItemId(int position) {// 取得项的ID
		return images[position % images.length];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView image = new ImageView(this.context);// 创建ImageView组件
		image.setImageResource(images[position % images.length]);// 将指定的资源设置到ImageView组件中
		image.setScaleType(ImageView.ScaleType.FIT_XY);// 设置图片根据尺寸显示
		image.setLayoutParams(new Gallery.LayoutParams(// 设置图片的宽高
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return image;
	}
}
