/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Nuker
extends Feature {
    private final BooleanSetting sendRotations;
    private final BooleanSetting sortTrashBlocks;
    private final BooleanSetting autoNoBreakDelay;
    private final NumberSetting nukerHorizontal;
    private final NumberSetting nukerVertical;
    private final BooleanSetting nukerESP = new BooleanSetting("Nuker ESP", true, () -> true);
    private final ColorSetting color = new ColorSetting("Nuker Color", new Color(0xFFFFFF).getRGB(), this.nukerESP::getCurrentValue);
    private BlockPos blockPos;

    public Nuker() {
        super("Nuker", "\u0420\u0443\u0448\u0438\u0442 \u0431\u043b\u043e\u043a\u0438 \u0432\u043e\u043a\u0440\u0443\u0433 \u0442\u0435\u0431\u044f", Type.Misc);
        this.sendRotations = new BooleanSetting("Send Rotations", true, () -> true);
        this.sortTrashBlocks = new BooleanSetting("Sort trash blocks", true, () -> true);
        this.autoNoBreakDelay = new BooleanSetting("No Delay", true, () -> true);
        this.nukerHorizontal = new NumberSetting("Nuker Horizontal", 3.0f, 1.0f, 5.0f, 1.0f, () -> true);
        this.nukerVertical = new NumberSetting("Nuker Vertical", 3.0f, 1.0f, 5.0f, 1.0f, () -> true);
        this.addSettings(this.color, this.nukerESP, this.sendRotations, this.sortTrashBlocks, this.autoNoBreakDelay, this.nukerHorizontal, this.nukerVertical);
    }

    private boolean canNuker(BlockPos pos) {
        IBlockState blockState = Nuker.mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, Nuker.mc.world, pos) != -1.0f;
    }

    private BlockPos getPositionXYZ() {
        BlockPos blockPos = null;
        float vDistance = this.nukerVertical.getCurrentValue();
        float hDistance = this.nukerHorizontal.getCurrentValue();
        for (float x = 0.0f; x <= hDistance; x += 1.0f) {
            for (float y = 0.0f; y <= vDistance; y += 1.0f) {
                for (float z = 0.0f; z <= hDistance; z += 1.0f) {
                    for (int reversedX = 0; reversedX <= 1; ++reversedX) {
                        for (int reversedZ = 0; reversedZ <= 1; ++reversedZ) {
                            BlockPos pos = new BlockPos(Nuker.mc.player.posX + (double)x, Nuker.mc.player.posY + (double)y, Nuker.mc.player.posZ + (double)z);
                            if (Nuker.mc.world.getBlockState(pos).getBlock() != Blocks.AIR && this.canNuker(pos)) {
                                blockPos = pos;
                            }
                            z = -z;
                        }
                        x = -x;
                    }
                }
            }
        }
        return blockPos;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (Nuker.mc.player == null || Nuker.mc.world == null) {
            return;
        }
        if (!this.nukerESP.getCurrentValue()) {
            return;
        }
        if ((Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRASS || Nuker.mc.world.getBlockState(this.blockPos).getBlock() instanceof BlockLiquid || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.MONSTER_EGG || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.DIRT || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRAVEL || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.WATER || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.LAVA) && this.sortTrashBlocks.getCurrentValue()) {
            return;
        }
        this.blockPos = this.getPositionXYZ();
        Color nukerColor = new Color(this.color.getColor());
        BlockPos blockPos = new BlockPos(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
        RenderHelper.blockEsp(blockPos, nukerColor, true);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        if (Nuker.mc.player == null || Nuker.mc.world == null) {
            return;
        }
        if (this.autoNoBreakDelay.getCurrentValue()) {
            Nuker.mc.playerController.blockHitDelay = 0;
        }
        this.blockPos = this.getPositionXYZ();
        float[] rotations = RotationHelper.getRotationVector(new Vec3d((float)this.blockPos.getX() + 0.5f, (float)this.blockPos.getY() + 0.5f, (float)this.blockPos.getZ() + 0.5f));
        if ((Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRASS || Nuker.mc.world.getBlockState(this.blockPos).getBlock() instanceof BlockLiquid || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.MONSTER_EGG || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.DIRT || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRAVEL || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.WATER || Nuker.mc.world.getBlockState(this.blockPos).getBlock() == Blocks.LAVA) && this.sortTrashBlocks.getCurrentValue()) {
            return;
        }
        if ((Nuker.mc.player.getHeldItemOffhand().getItem() instanceof ItemTool || Nuker.mc.player.getHeldItemMainhand().getItem() instanceof ItemTool || Nuker.mc.player.isCreative()) && this.blockPos != null) {
            if (this.sendRotations.getCurrentValue()) {
                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
                Nuker.mc.player.renderYawOffset = rotations[0];
                Nuker.mc.player.rotationYawHead = rotations[0];
                Nuker.mc.player.rotationPitchHead = rotations[1];
            }
            if (this.canNuker(this.blockPos)) {
                Nuker.mc.playerController.onPlayerDamageBlock(this.blockPos, Nuker.mc.player.getHorizontalFacing());
                Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
}

