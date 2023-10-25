/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.events;

public class WindowEvent {
    public static final int RESIZE = 511;
    public static final int MOVE = 512;
    public static final int RESCALE = 513;
    public static final int CLOSE = 521;
    public static final int DESTROY = 522;
    public static final int MINIMIZE = 531;
    public static final int MAXIMIZE = 532;
    public static final int RESTORE = 533;
    public static final int _FOCUS_MIN = 541;
    public static final int FOCUS_LOST = 541;
    public static final int FOCUS_GAINED = 542;
    public static final int FOCUS_GAINED_FORWARD = 543;
    public static final int FOCUS_GAINED_BACKWARD = 544;
    public static final int _FOCUS_MAX = 544;
    public static final int FOCUS_DISABLED = 545;
    public static final int FOCUS_UNGRAB = 546;

    public static String getEventName(int n) {
        switch (n) {
            case 511: {
                return "RESIZE";
            }
            case 512: {
                return "MOVE";
            }
            case 513: {
                return "RESCALE";
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

