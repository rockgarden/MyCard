package com.rockgarden;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.TagTechnology;

import java.util.Arrays;

/**
 * Created by rockgarden on 15/12/26.
 */
public class NfcUtil {
    // 判断是否是某种类型的NFC Technology
    static public boolean hasTech(Tag tag, String klassName) {
        for (String tech : tag.getTechList()) {
            if (tech.equals(klassName)) {
                return true;
            }
        }
        return false;
    }

    static public boolean hasTech(Tag tag, Class<? extends TagTechnology> tech) {
        return hasTech(tag, tech.getCanonicalName());
    }

    // checkTagTechList
    private int checkTagTechType(String[] techList) {
        if (Arrays.asList(techList).contains(
                MifareClassic.class.getName())) {
            return 1;
        }
        if (Arrays.asList(techList).contains(
                IsoDep.class.getName())) {
            return 2;
        }
        return 0;
    }
}
