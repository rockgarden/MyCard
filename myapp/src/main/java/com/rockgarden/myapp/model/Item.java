package com.rockgarden.myapp.model;

import android.support.annotation.DrawableRes;

import com.rockgarden.myapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Item {

    public static final List<Object> ITEMS = new ArrayList<>();
    public static final Map<String, Object> ITEM_MAP = new HashMap<String, Object>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        addItem(new DummyItem("101", "Wang", "age 18; sex female"));
        addItem(new DummyItem("102", "Rob Stark", "age 17; sex male"));
        addItem(new ImageItem("103", R.drawable.guide1, "Sun"));
        addItem(new DummyItem("104", "Jon Snow", "age 18; sex male"));
        addItem(new ImageItem("105", R.drawable.guide2,"moon"));
        addItem(new DummyItem("106", "Tyrion Lanister", "age 16; sex male"));
        addItem(new ImageItem("107", R.drawable.guide3,"moon"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static void addItem(ImageItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static class ImageItem {
        private int mDrawableRes;
        public final String id;
        public final String title;

        public ImageItem(String id, @DrawableRes int drawable, String title) {
            mDrawableRes = drawable;
            this.id = id;
            this.title = title;
        }

        public int getDrawableResource() {
            return mDrawableRes;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
