/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.special;

public final class AutoReconnect {
    public static final int MAX = 60000;
    public static final int MIN = 1000;
    private static boolean isEnabled;
    private static int delay;
    public static final AutoReconnect INSTANCE;

    public final boolean isEnabled() {
        return isEnabled;
    }

    public final int getDelay() {
        return delay;
    }

    public final void setDelay(int value) {
        isEnabled = value < 60000;
        delay = value;
    }

    private AutoReconnect() {
    }

    static {
        AutoReconnect autoReconnect;
        INSTANCE = autoReconnect = new AutoReconnect();
        isEnabled = true;
        delay = 5000;
    }
}

