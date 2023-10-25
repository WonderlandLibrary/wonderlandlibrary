/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

class GlassEventUtils {
    private GlassEventUtils() {
    }

    public static String getMouseEventString(int n) {
        switch (n) {
            case 211: {
                return "BUTTON_NONE";
            }
            case 212: {
                return "BUTTON_LEFT";
            }
            case 213: {
                return "BUTTON_RIGHT";
            }
            case 214: {
                return "BUTTON_OTHER";
            }
            case 215: {
                return "BUTTON_BACK";
            }
            case 216: {
                return "BUTTON_FORWARD";
            }
            case 221: {
                return "DOWN";
            }
            case 222: {
                return "UP";
            }
            case 223: {
                return "DRAG";
            }
            case 224: {
                return "MOVE";
            }
            case 225: {
                return "ENTER";
            }
            case 226: {
                return "EXIT";
            }
            case 227: {
                return "CLICK";
            }
            case 228: {
                return "WHEEL";
            }
        }
        return "UNKNOWN";
    }

    public static String getViewEventString(int n) {
        switch (n) {
            case 411: {
                return "ADD";
            }
            case 412: {
                return "REMOVE";
            }
            case 421: {
                return "REPAINT";
            }
            case 422: {
                return "RESIZE";
            }
            case 423: {
                return "MOVE";
            }
            case 431: {
                return "FULLSCREEN_ENTER";
            }
            case 432: {
                return "FULLSCREEN_EXIT";
            }
        }
        return "UNKNOWN";
    }

    public static String getWindowEventString(int n) {
        switch (n) {
            case 511: {
                return "RESIZE";
            }
            case 512: {
                return "MOVE";
            }
            case 521: {
                return "CLOSE";
            }
            case 522: {
                return "DESTROY";
            }
            case 531: {
                return "MINIMIZE";
            }
            case 532: {
                return "MAXIMIZE";
            }
            case 533: {
                return "RESTORE";
            }
            case 541: {
                return "FOCUS_LOST";
            }
            case 542: {
                return "FOCUS_GAINED";
            }
            case 543: {
                return "FOCUS_GAINED_FORWARD";
            }
            case 544: {
                return "FOCUS_GAINED_BACKWARD";
            }
            case 545: {
                return "FOCUS_DISABLED";
            }
            case 546: {
                return "FOCUS_UNGRAB";
            }
        }
        return "UNKNOWN";
    }
}

