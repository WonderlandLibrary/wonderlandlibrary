/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.Value;

import java.util.ArrayList;
import java.util.List;
import me.Tengoku.Terror.Value.Value;

public class ValueManager {
    public static ArrayList values = new ArrayList();

    public static List getValueByModName(String string) {
        ArrayList<Value> arrayList = new ArrayList<Value>();
        for (Value value : values) {
            if (!value.getValType().equals(string)) continue;
            arrayList.add(value);
        }
        return arrayList;
    }

    public static Value getValue(String string) {
        for (Value value : values) {
            if (!value.getName().equalsIgnoreCase(string)) continue;
            return value;
        }
        return null;
    }
}

