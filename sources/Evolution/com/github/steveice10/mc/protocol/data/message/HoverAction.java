/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

public enum HoverAction {
    SHOW_TEXT,
    SHOW_ITEM,
    SHOW_ACHIEVEMENT,
    SHOW_ENTITY;


    public static HoverAction byName(String name) {
        name = name.toLowerCase();
        HoverAction[] hoverActionArray = HoverAction.values();
        int n = hoverActionArray.length;
        int n2 = 0;
        while (n2 < n) {
            HoverAction action = hoverActionArray[n2];
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

