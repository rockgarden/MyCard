package com.rockgarden.myapp.adpater;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Create the Record adapter extending from RecyclerView.Adapter
 * Created by rockgarden on 15/11/26.
 */
public class RecyclerViewAdapter_Record extends RecyclerView.Adapter {
    private List<Record> mRecords;
    ; //store array
    private Context context;
    private boolean withHeader;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // 将context传递到构造函数constructor中
    public RecyclerViewAdapter_Record(Context context) {
        this.context = context;
        mRecords = new ArrayList<>();
        withHeader = false;
    }

    public RecyclerViewAdapter_Record(List<Record> records, Context context, boolean withHeader) {
        this.context = context;
        mRecords = records;
        withHeader = withHeader;
    }

    /**
     * 自定义OnItemClickListener
     * by this method, we can attach a click handler to the adapter in RecyclerView.class
     */
    private static OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onItemLongClick(View view, int position);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * 继承OnItemTouchListener
     * TODO: 实现一个sample
     */
    private static RecyclerView.OnItemTouchListener onItemTouchListener;

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener touchListener) {
        this.onItemTouchListener = touchListener;
    }

    /**
     * 自定义OnViewTouchListener
     * TODO: 实现一个sample
     */
    public interface OnViewTouchListener {
        void setMotionEvent(RecyclerView recycler, MotionEvent event);
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mRecords.add(null);
                    notifyItemInserted(mRecords.size() - 1);
                }
            });
        } else {
            mRecords.remove(mRecords.size() - 1);
            notifyItemRemoved(mRecords.size());
        }
    }

    /**
     * 定义RecordViewHolder
     * 为数据项中的每一个视图提供直接引用
     * Provide a direct reference to each of the views within a data item
     * 用于缓存item中视图便于快速访问
     * Used to cache the views within the item layout for fast access
     * 使用侦听器实现单击处理
     * Attaching Click Handlers using Listeners
     */
    public class RecordViewHolder extends RecyclerView.ViewHolder {
        public TextView recordTextView;
        public Button messageButton;

        // 构造函数item视图
        public RecordViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used to access the context from any ViewHolder instance.
            // 将ItemView存储在公用final成员变量,这样任何ViewHolder实例都可以来访问上下文
            super(itemView);

            recordTextView = (TextView) itemView.findViewById(R.id.tv_record_time);
            messageButton = (Button) itemView.findViewById(R.id.btn_record_result);
            if (onItemClickListener != null) {
                // TODO:实现messageButton的Click
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(itemView, getLayoutPosition());
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onItemLongClick(itemView, getLayoutPosition());
                        return false;
                    }
                });
            }
        }
    }

    /**
     * Simple Click Handler within ViewHolder
     */
    // Used to cache the views within the item layout for fast access
    public class ViewHolderOnClick extends RecordViewHolder implements View.OnClickListener {
        public TextView recordTextView;
        public Button messageButton;
        private Context context;

        public ViewHolderOnClick(View itemView) {
            super(itemView);
            recordTextView = (TextView) itemView.findViewById(R.id.tv_record_time);
            messageButton = (Button) itemView.findViewById(R.id.btn_record_result);
            // Store the context
            this.context = context;
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); //gets item position
            Record pos = mRecords.get(position);
            Toast.makeText(context, recordTextView.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mRecords.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    // involves inflating a layout from XML and returning the holder
    // 为Item创建视图
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext(); //获取上下文Activity.this
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_ITEM) {
            View view = inflater.inflate(R.layout.item_record, parent, false);
            viewHolder = new RecordViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.progress_item, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder; //Return a new holder instance
    }

    // 通过holder填充数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RecordViewHolder) {
            Record record = mRecords.get(position);
            ((RecordViewHolder) viewHolder).recordTextView.setText(record.getTime());
            Button button = ((RecordViewHolder) viewHolder).messageButton;
            if (record.isSuccess()) {
                button.setText("Success");
                button.setEnabled(true);
            } else {
                button.setText("Failure");
                button.setEnabled(false);
            }
            // here can add onItemClickListener on viewHolder.itemView
            // 如果设置了回调，则设置点击事件
            // 实现itemView的Click
            // FIXME:多处实现onItemClickListener有冲突吗?
            if (onItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = viewHolder.getLayoutPosition();
                        onItemClickListener.onItemClick(viewHolder.itemView, pos);
                    }
                });

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = viewHolder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(viewHolder.itemView, pos);
                        return false;
                    }
                });
            }
        } else {
            //((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }
    }

    // TODO:作用?
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        if (mRecords != null)
            return mRecords.size();
        else
            return 0;
    }

    /**
     * 清除数据
     */
    public void clear() {
        mRecords.clear();
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     * 初次加载数据
     *
     * @param list
     */
    public void addAll(List<Record> list) {
        mRecords.clear();
        mRecords.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 加载更多数据
     *
     * @param list
     */
    public void addItemMore(List<Record> list) {
        mRecords.addAll(list);
        notifyItemRangeChanged(0, mRecords.size());
    }

    /**
     * 删除指定数据
     *
     * @param position
     */
    public void removeData(int position) {
        try {
            mRecords.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeRemoved(position, 1);
        } catch (IndexOutOfBoundsException e) {
            notifyDataSetChanged();
            e.printStackTrace();
        }
    }

    public void updateAll(List<Record> list) {
        // record this value before making any changes to the existing list
        int curSize = getItemCount();
        // replace this line with wherever you get new records
        List<Record> newItems = Record.createRecordList(20);
        // update the existing list
        mRecords.addAll(newItems);
        // curSize should represent the first element that got added
        // newItems.size() represents the itemCount
        notifyItemRangeInserted(curSize, newItems.size());
    }

    public void insertData(int position) {
        mRecords.add(position, new Record("new", false));
        notifyItemInserted(position);
    }

    public void insertMoreData(int position) {
        mRecords.addAll(Record.createRecordList(5));
        notifyItemInserted(position);
    }

}
