/*
 * Decompiled with CFR 0.150.
 */
package baritone.behavior;

import baritone.Baritone;
import baritone.api.behavior.IBehavior;
import baritone.api.utils.IPlayerContext;

public class Behavior
implements IBehavior {
    public final Baritone baritone;
    public final IPlayerContext ctx;

    protected Behavior(Baritone baritone) {
        this.baritone = baritone;
        this.ctx = baritone.getPlayerContext();
        baritone.registerBehavior(this);
    }
}

