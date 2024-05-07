package com.masterof13fps.features.modules.impl.world;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "Nuker", category = Category.WORLD, description = "Automatically breaks blocks near your feet")
public class Nuker extends Module {

    Setting mode = new Setting("Mode", this, "Packet", new String[]{"Packet", "Swing"});
    Setting range = new Setting("Range", this, 3.5, 0, 5, false);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (mc.thePlayer.capabilities.isCreativeMode) {
                for (int y = (int) range.getCurrentValue(); y >= (int) (-range.getCurrentValue()); --y) {
                    for (int z = (int) (-range.getCurrentValue()); (float) z <= range.getCurrentValue(); ++z) {
                        for (int x = (int) (-range.getCurrentValue()); (float) x <= range.getCurrentValue(); ++x) {
                            int xPos = (int) Math.round(mc.thePlayer.posX + (double) x);
                            int yPos = (int) Math.round(mc.thePlayer.posY + (double) y);
                            int zPos = (int) Math.round(mc.thePlayer.posZ + (double) z);
                            BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                            IBlockState state = mc.theWorld.getBlockState(blockPos);

                            switch (mode.getCurrentMode()) {
                                case "Packet": {
                                    if (state.getBlock().getMaterial() != Material.air && mc.thePlayer.getDistance(xPos, yPos, zPos) < range.getCurrentValue()) {
                                        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                                        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                                    }
                                    break;
                                }
                                case "Swing": {
                                    if (state.getBlock().getMaterial() != Material.air && mc.thePlayer.getDistance(xPos, yPos, zPos) < range.getCurrentValue()) {
                                        mc.thePlayer.swingItem();

                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
