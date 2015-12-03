package com.citylinkdata.mycard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rockgarden.myapp.R;
import com.citylinkdata.mycard.model.Record;

import java.util.List;

/**
 * Created by rockgarden on 15/11/26.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private static List<Record> mRecords;
    private Context context;
    private boolean withHeader;

    public RecordAdapter(List<Record> records, Context context) {
        this.context = context;
        mRecords = records;
        withHeader = false;
    }

    public RecordAdapter(List<Record> records, Context context, boolean withHeader) {
        this.context = context;
        mRecords = records;
        withHeader = withHeader;
    }

    private static OnItemClickListener onItemClickListener;
//    private static OnItemTouchListener onItemTouchListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onItemLongClick(View view, int position);
    }

    public interface OnViewTouchListener {
        void setMotionEvent(RecyclerView recycler, MotionEvent event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

//    public void setOnItemTouchListener(OnItemTouchListener touchListener) {
//        OnItemTouchListener = touchListener;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recordTextView;
        public Button messageButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            recordTextView = (TextView) itemView.findViewById(R.id.tv_record_time);
            messageButton = (Button) itemView.findViewById(R.id.btn_record_result);
            if (onItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(itemView, getLayoutPosition() + 1);
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onItemLongClick(itemView, getLayoutPosition() + 1);
                        return false;
                    }
                });
            }
        }
    }

    public class ViewHolderOnClick extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView recordTextView;
        public Button messageButton;

        public ViewHolderOnClick(View itemView) {
            super(itemView);
            recordTextView = (TextView) itemView.findViewById(R.id.tv_record_time);
            messageButton = (Button) itemView.findViewById(R.id.btn_record_result);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            Record pos = mRecords.get(position);
            Toast.makeText(context, recordTextView.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.cardview_content_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecordAdapter.ViewHolder viewHolder, int position) {
        Record record = mRecords.get(position);
        TextView textView = viewHolder.recordTextView;
        textView.setText(record.getTime());
        Button button = viewHolder.messageButton;
        if (record.isSuccess()) {
            button.setText("Success");
            button.setEnabled(true);
        } else {
            button.setText("Failure");
            button.setEnabled(false);
        }
        //here can add onItemClickListener on viewHolder.itemView
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addData(int position) {
        mRecords.add(position, new Record("new", false));
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mRecords.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mRecords.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Record> list) {
        mRecords.addAll(list);
        notifyDataSetChanged();
    }
}
