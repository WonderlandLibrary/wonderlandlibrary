/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui;

import java.util.ArrayList;
import me.Tengoku.Terror.ui.Alt;

public class AltManager {
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;

    public void setLastAlt(Alt alt) {
        lastAlt = alt;
    }

    public ArrayList<Alt> getRegistry() {
        return registry;
    }

    static {
        registry = new ArrayList();
    }
}

