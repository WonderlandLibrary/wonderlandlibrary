/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.events;

public class ViewEvent {
    public static final int ADD = 411;
    public static final int REMOVE = 412;
    public static final int REPAINT = 421;
    public static final int RESIZE = 422;
    public static final int MOVE = 423;
    public static final int FULLSCREEN_ENTER = 431;
    public static final int FULLSCREEN_EXIT = 432;

    public static String getTypeString(int n) {
        String string = "UNKNOWN";
        switch (n) {
            case 411: {
                string = "ADD";
                break;
            }
            case 412: {
                string = "REMOVE";
                break;
            }
            case 421: {
                string = "REPAINT";
                break;
            }
            case 422: {
                string = "RESIZE";
                break;
            }
            case 423: {
                string = "MOVE";
                break;
            }
            case 431: {
                string = "FULLSCREEN_ENTER";
                break;
            }
            case 432: {
                string = "FULLSCREEN_EXIT";
                break;
            }
            default: {
                System.err.println("Unknown view event type: " + n);
            }
        }
        return string;
    }
}

