package com.rockgarden.myapp.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.fragment.ItemFragment.OnListFragmentInteractionListener;
import com.rockgarden.myapp.model.Item.DummyItem;
import com.rockgarden.myapp.model.Item.ImageItem;

import java.util.List;

/**
 */
public class RecyclerViewAdapter_Complex extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final int TEXT = 0, IMAGE = 1;

    public RecyclerViewAdapter_Complex(List<Object> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.get(position) instanceof DummyItem) {
            return TEXT;
        } else if (mValues.get(position) instanceof ImageItem) {
            return IMAGE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TEXT:
                View v1 = inflater.inflate(R.layout.item_text, parent, false);
                viewHolder = new ViewHolderText(v1);
                break;
            case IMAGE:
                View v2 = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ViewHolderImage(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new ViewHolderImage(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case TEXT:
                final ViewHolderText vh1 = (ViewHolderText) viewHolder;
                configureViewHolderText(vh1, position);
                break;
            case IMAGE:
                ViewHolderImage vh2 = (ViewHolderImage) viewHolder;
                configureViewHolderImage(vh2);
                break;
            default:
                ViewHolderImage vh = (ViewHolderImage) viewHolder;
                configureViewHolderImage(vh, position);
                break;
        }
    }

    private void configureViewHolderText(ViewHolderText vh1, int position) {
        DummyItem dummyItem = (DummyItem) mValues.get(position);
        if (dummyItem != null) {
            vh1.getIdView().setText("ID:" + dummyItem.id);
            vh1.getContentView().setText("Age and Sex: " + dummyItem.details);
        }
    }

    private void configureViewHolderImage(ViewHolderImage vh2) {
        vh2.getImageView().setImageResource(R.drawable.guide1);
    }

    private void configureViewHolderImage(ViewHolderImage vh, int position) {
        //vh.getLabel().setText((CharSequence) items.get(position));
        vh.getImageView().setImageResource(R.drawable.guide2);
    }

    public class ViewHolderText extends RecyclerView.ViewHolder {
        public final View view;
        public TextView idView;
        public TextView contentView;

        public ViewHolderText(View view) {
            super(view);
            this.view = view;
            idView = (TextView) view.findViewById(R.id.id);
            contentView = (TextView) view.findViewById(R.id.content);
        }

        public TextView getIdView() {
            return idView;
        }

        public void setIdView(TextView idView) {
            this.idView = idView;
        }

        public TextView getContentView() {
            return contentView;
        }

        public void setContentView(TextView contentView) {
            this.contentView = contentView;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }

    public class ViewHolderImage extends RecyclerView.ViewHolder {

        private final View view;
        private ImageView imageView;

        public ViewHolderImage(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.image);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
