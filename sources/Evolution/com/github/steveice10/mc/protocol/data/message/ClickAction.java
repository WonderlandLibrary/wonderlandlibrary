/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

public enum ClickAction {
    RUN_COMMAND,
    SUGGEST_COMMAND,
    OPEN_URL,
    OPEN_FILE;


    public static ClickAction byName(String name) {
        name = name.toLowerCase();
        ClickAction[] clickActionArray = ClickAction.values();
        int n = clickActionArray.length;
        int n2 = 0;
        while (n2 < n) {
            ClickAction action = clickActionArray[n2];
            if (action.toString().equals(name)) {
                return action;
            }
            ++n2;
        }
        return null;
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}

