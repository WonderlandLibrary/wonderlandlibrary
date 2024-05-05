/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.ccbluex.liquidbounce.value.Value;

public final class Manager {
    private static final List<Value> settingList = new CopyOnWriteArrayList<Value>();

    public static void put(Value setting) {
        settingList.add(setting);
    }

    public static List<Value> getSettingList() {
        return settingList;
    }
}

