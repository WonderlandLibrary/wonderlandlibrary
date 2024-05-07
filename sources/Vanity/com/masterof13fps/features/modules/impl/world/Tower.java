package com.masterof13fps.features.modules.impl.world;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventMotion;
import com.masterof13fps.manager.eventmanager.impl.EventPacket;
import com.masterof13fps.features.modules.Category;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "Tower", category = Category.WORLD, description = "Automatically towers yourself with blocks into the air")
public class Tower extends Module {

    Setting slowDown = new Setting("Slow", this, false);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private EnumFacing getFacingDirection(final BlockPos pos) {
        EnumFacing direction = null;
        if (!Tower.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.UP;
        } else if (!Tower.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.DOWN;
        } else if (!Tower.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.EAST;
        } else if (!Tower.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.WEST;
        } else if (!Tower.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!Tower.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventMotion) {
            if(((EventMotion) event).getType() == EventMotion.Type.PRE) {
                final BlockPos pos = new BlockPos(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY - 1.0, Tower.mc.thePlayer.posZ);
                final EnumFacing face = getFacingDirection(pos);
                try {
                    boolean slow = slowDown.isToggled();
                    if (timeHelper.hasReached(slow ? 150 : 75) && Tower.mc.thePlayer.getCurrentEquippedItem().getItem() != null && Tower.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                        Tower.mc.thePlayer.setPosition(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY + 1.1, Tower.mc.thePlayer.posZ);
                        final float[] rotations = BlockHelper.getBlockRotations(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY - 1.0, Tower.mc.thePlayer.posZ);
                        if (!Tower.mc.thePlayer.onGround) {
                            mc.rightClickMouse();
                            Tower.mc.thePlayer.swingItem();
                        }
                        timeHelper.reset();
                    }
                } catch (Exception ignored) {
                }
            }
        }
        if(event instanceof EventPacket) {
            if(((EventPacket) event).getType() == EventPacket.Type.SEND) {
                if (((EventPacket) event).getPacket() instanceof C03PacketPlayer) {
                    final C03PacketPlayer player = (C03PacketPlayer) ((EventPacket) event).getPacket();
                    final float[] rotations = BlockHelper.getBlockRotations(Tower.mc.thePlayer.posX, Tower.mc.thePlayer.posY - 1.0, Tower.mc.thePlayer.posZ);
                    player.yaw = rotations[0];
                    player.pitch = rotations[1];
                }
            }
        }
    }
}