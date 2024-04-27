/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityOtherPlayerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="FreeCam", description="Allows you to move out of your body.", category=ModuleCategory.RENDER)
public final class FreeCam
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 0.8f, 0.1f, 2.0f);
    private double oldY;
    private final BoolValue flyValue = new BoolValue("Fly", true);
    private final BoolValue noClipValue = new BoolValue("NoClip", true);
    private IEntityOtherPlayerMP fakePlayer;
    private double oldX;
    private double oldZ;

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        this.oldX = iEntityPlayerSP2.getPosX();
        this.oldY = iEntityPlayerSP2.getPosY();
        this.oldZ = iEntityPlayerSP2.getPosZ();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IEntityOtherPlayerMP iEntityOtherPlayerMP = MinecraftInstance.classProvider.createEntityOtherPlayerMP(iWorldClient, iEntityPlayerSP2.getGameProfile());
        iEntityOtherPlayerMP.setRotationYawHead(iEntityPlayerSP2.getRotationYawHead());
        iEntityOtherPlayerMP.setRenderYawOffset(iEntityPlayerSP2.getRenderYawOffset());
        iEntityOtherPlayerMP.setRotationYawHead(iEntityPlayerSP2.getRotationYawHead());
        iEntityOtherPlayerMP.copyLocationAndAnglesFrom(iEntityPlayerSP2);
        IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient2 == null) {
            Intrinsics.throwNpe();
        }
        iWorldClient2.addEntityToWorld(-1000, iEntityOtherPlayerMP);
        if (((Boolean)this.noClipValue.get()).booleanValue()) {
            iEntityPlayerSP2.setNoClip(true);
        }
        this.fakePlayer = iEntityOtherPlayerMP;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(iPacket) || MinecraftInstance.classProvider.isCPacketEntityAction(iPacket)) {
            packetEvent.cancelEvent();
        }
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null || this.fakePlayer == null) {
            return;
        }
        iEntityPlayerSP.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, iEntityPlayerSP.getRotationYaw(), iEntityPlayerSP.getRotationPitch());
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IEntityOtherPlayerMP iEntityOtherPlayerMP = this.fakePlayer;
        if (iEntityOtherPlayerMP == null) {
            Intrinsics.throwNpe();
        }
        iWorldClient.removeEntityFromWorld(iEntityOtherPlayerMP.getEntityId());
        this.fakePlayer = null;
        iEntityPlayerSP.setMotionX(0.0);
        iEntityPlayerSP.setMotionY(0.0);
        iEntityPlayerSP.setMotionZ(0.0);
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.noClipValue.get()).booleanValue()) {
            iEntityPlayerSP2.setNoClip(true);
        }
        iEntityPlayerSP2.setFallDistance(0.0f);
        if (((Boolean)this.flyValue.get()).booleanValue()) {
            float f = ((Number)this.speedValue.get()).floatValue();
            iEntityPlayerSP2.setMotionY(0.0);
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
            if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + (double)f);
            }
            if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() - (double)f);
            }
            MovementUtils.strafe(f);
        }
    }
}

