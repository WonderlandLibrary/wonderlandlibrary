/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.process.PathingCommand;

public interface IBaritoneProcess {
    public static final double DEFAULT_PRIORITY = -1.0;

    public boolean isActive();

    public PathingCommand onTick(boolean var1, boolean var2);

    public boolean isTemporary();

    public void onLostControl();

    default public double priority() {
        return -1.0;
    }

    default public String displayName() {
        if (!this.isActive()) {
            return "INACTIVE";
        }
        return this.displayName0();
    }

    public String displayName0();
}

