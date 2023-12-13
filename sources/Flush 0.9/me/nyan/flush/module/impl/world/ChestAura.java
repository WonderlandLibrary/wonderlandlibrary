package me.nyan.flush.module.impl.world;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventWorldChange;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.combat.CombatUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class ChestAura extends Module {
    private final NumberSetting range = new NumberSetting("Range", this, 4, 1, 6, 0.1);
    private final BooleanSetting rotations = new BooleanSetting("Rotations", this, true),
            throughWalls = new BooleanSetting("Through Walls", this, true),
            noSwing = new BooleanSetting("No Swing", this, false);

    private final ArrayList<BlockPos> clickedBlocks = new ArrayList<>();

    public ChestAura() {
        super("ChestAura", Category.WORLD);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        clickedBlocks.clear();
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest)
            return;

        float range = this.range.getValueFloat();
        findBlocks:
        for (float x = -range; x < range; ++x) {
            for (float y = range; y > -range; --y) {
                for (float z = -range; z < range; ++z) {
                    BlockPos blockPos = new BlockPos((int) mc.thePlayer.posX + x, (int) mc.thePlayer.posY + y, (int) mc.thePlayer.posZ + z);
                    Block block = blockPos.getBlock();

                    if (!(block instanceof BlockChest) || clickedBlocks.contains(blockPos) || (!throughWalls.getValue() &&
                            mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(),
                                    mc.thePlayer.posZ), new Vec3(blockPos)) != null))
                        continue;

                    if (rotations.getValue()) {
                        float[] rotations = CombatUtils.getRotations(new Vec3(blockPos));
                        e.setYaw(rotations[0]);
                        e.setPitch(rotations[1]);
                    }

                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockPos, EnumFacing.DOWN, new Vec3(blockPos))) {
                        if (!noSwing.getValue())
                            mc.thePlayer.swingItem();

                        clickedBlocks.add(blockPos);
                        break findBlocks;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChangeWorld(EventWorldChange e) {
        clickedBlocks.clear();
    }
}