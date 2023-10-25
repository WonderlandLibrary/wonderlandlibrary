package org.dreamcore.client.feature.impl.misc;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.RotationHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;

public class Nuker extends Feature {

    private final BooleanSetting sendRotations;
    private final BooleanSetting sortTrashBlocks;
    private final BooleanSetting autoNoBreakDelay;
    private final NumberSetting nukerHorizontal;
    private final NumberSetting nukerVertical;
    private final BooleanSetting nukerESP;
    private final ColorSetting color;

    private BlockPos blockPos;

    public Nuker() {
        super("Nuker", "Рушит блоки вокруг тебя", Type.Misc);
        this.nukerESP = new BooleanSetting("Nuker ESP", true, () -> true);
        this.color = new ColorSetting("Nuker Color", new Color(0xFFFFFF).getRGB(), this.nukerESP::getBoolValue);
        this.sendRotations = new BooleanSetting("Send Rotations", true, () -> true);
        this.sortTrashBlocks = new BooleanSetting("Sort trash blocks", true, () -> true);
        this.autoNoBreakDelay = new BooleanSetting("No Delay", true, () -> true);
        this.nukerHorizontal = new NumberSetting("Nuker Horizontal", 3, 1, 5, 1F, () -> true);
        this.nukerVertical = new NumberSetting("Nuker Vertical", 3, 1, 5, 1F, () -> true);
        addSettings(color, nukerESP, sendRotations, sortTrashBlocks, autoNoBreakDelay, nukerHorizontal, nukerVertical);
    }

    private boolean canNuker(BlockPos pos) {
        IBlockState blockState = mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }

    private BlockPos getPositionXYZ() {
        BlockPos blockPos = null;
        float vDistance = this.nukerVertical.getNumberValue();
        float hDistance = this.nukerHorizontal.getNumberValue();
        for (float x = 0; x <= hDistance; x++) {
            for (float y = 0; y <= vDistance; y++) {
                for (float z = 0; z <= hDistance; z++) {
                    for (int reversedX = 0; reversedX <= 1; reversedX++, x = -x) {
                        for (int reversedZ = 0; reversedZ <= 1; reversedZ++, z = -z) {
                            BlockPos pos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
                            if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR && this.canNuker(pos)) {
                                blockPos = pos;
                            }
                        }
                    }
                }
            }
        }
        return blockPos;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null)
            return;
        if (!this.nukerESP.getBoolValue()) {
            return;
        }
        if ((mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRASS || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.MONSTER_EGG || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.DIRT || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRAVEL || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.WATER || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.LAVA) && (this.sortTrashBlocks.getBoolValue())) {
            return;
        }
        this.blockPos = this.getPositionXYZ();
        Color nukerColor = new Color(color.getColorValue());
        BlockPos blockPos = new BlockPos(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
        RenderHelper.blockEsp(blockPos, nukerColor, true);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        if (mc.player == null || mc.world == null)
            return;
        if (autoNoBreakDelay.getBoolValue()) {
            mc.playerController.blockHitDelay = 0;
        }
        this.blockPos = this.getPositionXYZ();
        float[] rotations = RotationHelper.getRotationVector(new Vec3d(this.blockPos.getX() + 0.5f, this.blockPos.getY() + 0.5f, this.blockPos.getZ() + 0.5f), true, 2, 2, 360);
        if ((mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRASS || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.MONSTER_EGG || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.DIRT || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.GRAVEL || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.WATER || mc.world.getBlockState(this.blockPos).getBlock() == Blocks.LAVA) && (this.sortTrashBlocks.getBoolValue())) {
            return;
        }
        if (mc.player.getHeldItemOffhand().getItem() instanceof ItemTool || mc.player.getHeldItemMainhand().getItem() instanceof ItemTool || mc.player.isCreative()) {
            if (this.blockPos != null) {
                if (sendRotations.getBoolValue()) {
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                    mc.player.renderYawOffset = rotations[0];
                    mc.player.rotationYawHead = rotations[0];
                    mc.player.rotationPitchHead = rotations[1];
                }
                if (this.canNuker(this.blockPos)) {
                    mc.playerController.onPlayerDamageBlock(this.blockPos, mc.player.getHorizontalFacing());
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}
