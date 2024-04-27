package net.ccbluex.liquidbounce.features.special;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\b\n\b\b\n\n\b\bÆ\u000020B\b¢R0XT¢\n\u0000R0XT¢\n\u0000R$020@FX¢\n\u0000\b\b\t\"\b\nR0\r2\f0\r@BX¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/special/AutoReconnect;", "", "()V", "MAX", "", "MIN", "value", "delay", "getDelay", "()I", "setDelay", "(I)V", "<set-?>", "", "isEnabled", "()Z", "Pride"})
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
