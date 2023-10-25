/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.api.utils.Helper;
import baritone.api.utils.IPlayerContext;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public final class BlockBreakHelper
implements Helper {
    private final IPlayerContext ctx;
    private boolean didBreakLastTick;

    BlockBreakHelper(IPlayerContext ctx) {
        this.ctx = ctx;
    }

    public void stopBreakingBlock() {
        if (this.ctx.player() != null && this.didBreakLastTick) {
            if (!this.ctx.playerController().hasBrokenBlock()) {
                this.ctx.playerController().setHittingBlock(true);
            }
            this.ctx.playerController().resetBlockRemoving();
            this.didBreakLastTick = false;
        }
    }

    public void tick(boolean isLeftClick) {
        boolean isBlockTrace;
        RayTraceResult trace = this.ctx.objectMouseOver();
        boolean bl = isBlockTrace = trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK;
        if (isLeftClick && isBlockTrace) {
            if (!this.didBreakLastTick) {
                this.ctx.playerController().syncHeldItem();
                this.ctx.playerController().clickBlock(trace.getBlockPos(), trace.sideHit);
                this.ctx.player().swingArm(EnumHand.MAIN_HAND);
            }
            if (this.ctx.playerController().onPlayerDamageBlock(trace.getBlockPos(), trace.sideHit)) {
                this.ctx.player().swingArm(EnumHand.MAIN_HAND);
            }
            this.ctx.playerController().setHittingBlock(false);
            this.didBreakLastTick = true;
        } else if (this.didBreakLastTick) {
            this.stopBreakingBlock();
            this.didBreakLastTick = false;
        }
    }
}

