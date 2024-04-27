/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.features.special;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R$\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001e\u0010\u000e\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/features/special/AutoReconnect;", "", "()V", "MAX", "", "MIN", "value", "delay", "getDelay", "()I", "setDelay", "(I)V", "<set-?>", "", "isEnabled", "()Z", "LiKingSense"})
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

