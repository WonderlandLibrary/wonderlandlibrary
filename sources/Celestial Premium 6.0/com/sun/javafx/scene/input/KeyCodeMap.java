/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.KeyCode
 */
package com.sun.javafx.scene.input;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;

public final class KeyCodeMap {
    private static final Map<Integer, KeyCode> charMap = new HashMap<Integer, KeyCode>(KeyCode.values().length);

    KeyCodeMap() {
    }

    public static KeyCode valueOf(int n) {
        return charMap.get(n);
    }

    static {
        for (KeyCode keyCode : KeyCode.values()) {
            charMap.put(keyCode.getCode(), keyCode);
        }
    }
}

