/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.special;

public final class AutoReconnect {
    private static int delay;
    public static final int MAX;
    public static final int MIN;
    public static final AutoReconnect INSTANCE;
    private static boolean isEnabled;

    public final int getDelay() {
        return delay;
    }

    static {
        AutoReconnect autoReconnect;
        MIN = 1000;
        MAX = 60000;
        INSTANCE = autoReconnect = new AutoReconnect();
        isEnabled = true;
        delay = 5000;
    }

    public final void setDelay(int n) {
        isEnabled = n < 60000;
        delay = n;
    }

    private AutoReconnect() {
    }

    public final boolean isEnabled() {
        return isEnabled;
    }
}

