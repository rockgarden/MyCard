package com.rockgarden.myapp.adpater;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.activity.PictureDetailActivity;
import com.rockgarden.myapp.fragment.PicturesFragment;
import com.rockgarden.myapp.model.Pictures;

import java.util.List;

/**
 * Created by rockgarden on 16/4/13.
 */
public class RecyclerViewAdapter_Picture
        extends RecyclerView.Adapter<RecyclerViewAdapter_Picture.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<String> mValues;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public String getValueAt(int position) {
        return mValues.get(position);
    }

    public RecyclerViewAdapter_Picture(Context context, List<String> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_picture, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mBoundString = mValues.get(position);
        holder.mTextView.setText(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PictureDetailActivity.class);
                intent.putExtra(PictureDetailActivity.EXTRA_NAME, holder.mBoundString);
                context.startActivity(intent);
            }
        });

        Glide.with(holder.mImageView.getContext())
                .load(Pictures.getRandomCheeseDrawable())
                .fitCenter()
                .into(holder.mImageView);

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeData(position);
                addData(0);
                updateData(100);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void removeData(int position) {
        try {
            mValues.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e) {
            notifyDataSetChanged();
            e.printStackTrace();
        }
    }

    public void addData(int position) {
        try {
            mValues.add(position, "new");
            notifyItemInserted(position);
        } catch (IndexOutOfBoundsException e) {
            notifyDataSetChanged();
            e.printStackTrace();
        }
    }

    public void updateData(int number) {
        // record this value before making any changes to the existing list
        int curSize = getItemCount();
        // replace this line with wherever you get new records
        List<String> newItems = PicturesFragment.getRandomSublist(Pictures.sPictureStrings, number);
        // update the existing list
        mValues.addAll(newItems);
        // curSize should represent the first element that got added
        // newItems.size() represents the itemCount
        notifyItemRangeInserted(curSize, newItems.size());
    }
}
