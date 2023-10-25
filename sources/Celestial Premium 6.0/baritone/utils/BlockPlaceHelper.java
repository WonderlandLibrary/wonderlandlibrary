/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.Baritone;
import baritone.api.utils.Helper;
import baritone.api.utils.IPlayerContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class BlockPlaceHelper
implements Helper {
    private final IPlayerContext ctx;
    private int rightClickTimer;

    BlockPlaceHelper(IPlayerContext playerContext) {
        this.ctx = playerContext;
    }

    public void tick(boolean rightClickRequested) {
        if (this.rightClickTimer > 0) {
            --this.rightClickTimer;
            return;
        }
        RayTraceResult mouseOver = this.ctx.objectMouseOver();
        if (!rightClickRequested || this.ctx.player().isRowingBoat() || mouseOver == null || mouseOver.getBlockPos() == null || mouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }
        this.rightClickTimer = (Integer)Baritone.settings().rightClickSpeed.value;
        for (EnumHand hand : EnumHand.values()) {
            if (this.ctx.playerController().processRightClickBlock(this.ctx.player(), this.ctx.world(), mouseOver.getBlockPos(), mouseOver.sideHit, mouseOver.hitVec, hand) == EnumActionResult.SUCCESS) {
                this.ctx.player().swingArm(hand);
                return;
            }
            if (this.ctx.player().getHeldItem(hand).isEmpty() || this.ctx.playerController().processRightClick(this.ctx.player(), this.ctx.world(), hand) != EnumActionResult.SUCCESS) continue;
            return;
        }
    }
}

