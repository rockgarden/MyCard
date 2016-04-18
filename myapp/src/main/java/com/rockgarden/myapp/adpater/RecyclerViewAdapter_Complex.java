package com.rockgarden.myapp.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.fragment.ItemFragment;
import com.rockgarden.myapp.model.Item.DummyItem;
import com.rockgarden.myapp.model.Item.ImageItem;

import java.util.List;

/**
 *
 */
public class RecyclerViewAdapter_Complex extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> mValues;
    private ItemFragment.FragmentInteractionListener fragmentInteractionListener;
    private ImageItemListener imageItemListener;
    private final int TEXT = 0, IMAGE = 1;

    public RecyclerViewAdapter_Complex(List<Object> items, ItemFragment.FragmentInteractionListener fragmentInteractionListener, ImageItemListener imageItemListener) {
        mValues = items;
        this.fragmentInteractionListener = fragmentInteractionListener;
        this.imageItemListener = imageItemListener;
    }

    public void setListener(ImageItemListener listener) {
        imageItemListener = listener;
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
                ImageItem imageItem = (ImageItem) mValues.get(position);
                if (imageItem != null) {
                    vh2.setData(imageItem);
                }
                break;
            default:
                ViewHolderImage vh = (ViewHolderImage) viewHolder;
                configureViewHolderImage(vh);
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

    private void configureViewHolderImage(ViewHolderImage vh) {
        //vh.getLabel().setText((CharSequence) items.get(position));
        vh.setImageView(null);
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


    public class ViewHolderImage extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private ImageView imageView;
        public ImageItem item;

        public ViewHolderImage(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image);
        }

        // 提供配置model的方法,可简化onBindViewHolder代码
        public void setData(ImageItem item) {
            this.item = item;
            // FIXME:旋转屏幕OutOfMemoryError
            imageView.setImageResource(item.getDrawableResource());
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void onClick(View v) {
            if (imageItemListener != null) {
                imageItemListener.onImageItemClick(item);
            }
        }
    }

    public interface ImageItemListener {
        void onImageItemClick(ImageItem item);
    }
}
