package com.rockgarden.myapp.model;

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
        addItem(new ImageItem("103", "Sun"));
        addItem(new DummyItem("104", "Jon Snow", "age 18; sex male"));
        addItem(new ImageItem("105", "moon"));
        addItem(new DummyItem("106", "Tyrion Lanister", "age 16; sex male"));
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
        public final String id;
        public final String name;

        public ImageItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
