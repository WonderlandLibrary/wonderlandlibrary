/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.accessor;

import net.minecraft.util.math.BlockPos;

public interface IPlayerControllerMP {
    public void setIsHittingBlock(boolean var1);

    public BlockPos getCurrentBlock();

    public void callSyncCurrentPlayItem();
}

