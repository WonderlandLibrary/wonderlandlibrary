/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

public class MouseUtil {
    public static int convertToMouse(int key) {
        switch (key) {
            case -2: {
                return 0;
            }
            case -3: {
                return 1;
            }
            case -4: {
                return 2;
            }
            case -5: {
                return 3;
            }
            case -6: {
                return 4;
            }
        }
        return -1;
    }
}

