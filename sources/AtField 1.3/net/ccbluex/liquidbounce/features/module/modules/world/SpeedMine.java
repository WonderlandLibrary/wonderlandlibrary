/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.init.Blocks
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.init.Blocks;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="SpeedMine", description="faq", category=ModuleCategory.WORLD)
public final class SpeedMine
extends Module {
    private IEnumFacing facing;
    private float bzx;
    private WBlockPos blockPos;
    private boolean bzs;

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        if (MinecraftInstance.mc.getPlayerController().extendedReach()) {
            MinecraftInstance.mc.getPlayerController().setBlockHitDelay(0);
        } else if (this.bzs) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            WBlockPos wBlockPos = this.blockPos;
            if (wBlockPos == null) {
                Intrinsics.throwNpe();
            }
            IBlock iBlock = iWorldClient.getBlockState(wBlockPos).getBlock();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient2 == null) {
                Intrinsics.throwNpe();
            }
            IWorld iWorld = iWorldClient2;
            WBlockPos wBlockPos2 = this.blockPos;
            if (wBlockPos2 == null) {
                Intrinsics.throwNpe();
            }
            this.bzx += (float)((double)iBlock.getPlayerRelativeBlockHardness(iEntityPlayerSP, iWorld, wBlockPos2) * 1.4);
            if (this.bzx >= 1.0f) {
                IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient3 == null) {
                    Intrinsics.throwNpe();
                }
                iWorldClient3.setBlockState(this.blockPos, Blocks.field_150350_a.func_176223_P(), 11);
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                WBlockPos wBlockPos3 = this.blockPos;
                if (wBlockPos3 == null) {
                    Intrinsics.throwNpe();
                }
                IEnumFacing iEnumFacing = this.facing;
                if (iEnumFacing == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, wBlockPos3, iEnumFacing));
                this.bzx = 0.0f;
                this.bzs = false;
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayerDigging(iPacket)) {
            IPacket iPacket2 = iPacket.asCPacketPlayerDigging();
            if (iPacket.asCPacketPlayerDigging().getAction() == ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK) {
                this.bzs = true;
                this.blockPos = iPacket2.asCPacketPlayerDigging().getPosition();
                this.facing = iPacket.asCPacketPlayerDigging().getFacing();
                this.bzx = 0.0f;
            } else if (iPacket.asCPacketPlayerDigging().getAction() == ICPacketPlayerDigging.WAction.ABORT_DESTROY_BLOCK || iPacket.asCPacketPlayerDigging().getAction() == ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK) {
                this.bzs = false;
                this.blockPos = null;
                this.facing = null;
            }
        }
    }

    public final WBlockPos getBlockPos() {
        return this.blockPos;
    }

    public final IEnumFacing getFacing() {
        return this.facing;
    }

    public final void setFacing(@Nullable IEnumFacing iEnumFacing) {
        this.facing = iEnumFacing;
    }

    public final void setBlockPos(@Nullable WBlockPos wBlockPos) {
        this.blockPos = wBlockPos;
    }
}

