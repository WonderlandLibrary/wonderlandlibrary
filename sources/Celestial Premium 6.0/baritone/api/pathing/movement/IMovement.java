/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.movement;

import baritone.api.pathing.movement.MovementStatus;
import baritone.api.utils.BetterBlockPos;
import net.minecraft.util.math.BlockPos;

public interface IMovement {
    public double getCost();

    public MovementStatus update();

    public void reset();

    public void resetBlockCache();

    public boolean safeToCancel();

    public boolean calculatedWhileLoaded();

    public BetterBlockPos getSrc();

    public BetterBlockPos getDest();

    public BlockPos getDirection();
}

