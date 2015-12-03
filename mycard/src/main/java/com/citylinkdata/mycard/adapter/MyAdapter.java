package com.citylinkdata.mycard.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rockgarden.myapp.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> lists;

	public MyAdapter(Context context, ArrayList<String> list) {
		mContext = context;
		lists = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.cardview_content_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String s = lists.get(position);
		holder.tv_text.setText(s);
		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		@Bind(R.id.tv_record_time)
		TextView tv_text;
		@Bind(R.id.btn_record_result)
		Button btn;
		private Context mContext;

		@OnClick(R.id.btn_record_result)
		public void onClick(View arg0) {
			Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
		}

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
