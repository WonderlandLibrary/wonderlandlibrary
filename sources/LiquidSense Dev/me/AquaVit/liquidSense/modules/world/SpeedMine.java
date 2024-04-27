package me.AquaVit.liquidSense.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import java.lang.System;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

@ModuleInfo(name = "SpeedMine", description = "SpeedMine", category = ModuleCategory.WORLD)
public class SpeedMine extends Module {
    private boolean bzs = false;
    private float bzx = 0.0f;
    public BlockPos blockPos;
    public EnumFacing facing;

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        if(packet instanceof C07PacketPlayerDigging && mc.playerController != null) {
            C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)packet;
            if(c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                bzs = true;
                blockPos = c07PacketPlayerDigging.getPosition();
                facing = c07PacketPlayerDigging.getFacing();
                bzx = 0.0f;
            }
            else if(c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                bzs = false;
                blockPos = null;
                facing = null;
            }
        }

    }


    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(mc.playerController.extendedReach()) {
            mc.playerController.blockHitDelay = 0;
        }
        else if(bzs) {
            Block block = mc.theWorld.getBlockState(blockPos).getBlock();
            bzx += (block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, blockPos) * 1.4);
            if (bzx >= 1.0f) {
                mc.theWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 11);
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, facing));
                bzx = 0.0f;
                bzs = false;
            }
        }

    }
}
