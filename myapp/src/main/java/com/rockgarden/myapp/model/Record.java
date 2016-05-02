package com.rockgarden.myapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of Record
 * Created by rockgarden on 15/11/26.
 */
public class Record {
    private String mTime;
    private boolean mResult;

    public Record(String name, boolean online) {
        mTime = name;
        mResult = isSuccess();
    }

    public String getTime() {
        return mTime;
    }

    public boolean isSuccess() {
        return mResult;
    }

    private static int lastRecordsId = 0;

    public static List<Record> createRecordList(int numRecords) {
        List<Record> records = new ArrayList<Record>();

        for (int i = 1; i <= numRecords; i++) {
            records.add(new Record("Record " + i++, i <= numRecords / 2));
            //records.add(new Record("Record " + ++lastRecordsId, i <= numRecords / 2));
        }
        return records;
    }

}
