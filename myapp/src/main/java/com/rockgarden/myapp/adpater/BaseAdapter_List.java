package com.rockgarden.myapp.adpater;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 简化的EndlessScroll实现
 */
public class BaseAdapter_List extends BaseAdapter {
    EndlessScrollListener endlessScrollListener;
    private Context mContext;
    private ArrayList<String> lists;

    public void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

    public BaseAdapter_List(Context context, ArrayList<String> list) {
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
        final int VISIBLE_THRESHOLD = 5;

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.cardview_content_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String s = lists.get(position);
        holder.tv_text.setText(s);
        holder.btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
            }
        });
        // you can cache getItemCount() in a member variable for more performance tuning
        if (position == getCount() - VISIBLE_THRESHOLD) {
            if (endlessScrollListener != null) {
                endlessScrollListener.onLoadMore(position);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_card_record_time)
        TextView tv_text;
        @BindView(R.id.btn_card_record_result)
        Button btn;
        private Context mContext;

        @OnClick(R.id.btn_card_record_result)
        public void onClick(View arg0) {
            Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // TODO:实现LoadMore
    public interface EndlessScrollListener {
        /**
         * Loads more data.
         *
         * @param position
         * @return true loads data actually, false otherwise.
         */
        boolean onLoadMore(int position);
    }

}
