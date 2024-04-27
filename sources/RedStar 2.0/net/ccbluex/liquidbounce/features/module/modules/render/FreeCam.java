package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="FreeCam", description="Allows you to move out of your body.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000<\n\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b0HJ\b0HJ020HJ02\b0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0\tX¢\n\u0000R0\tX¢\n\u0000R\f0\rX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/FreeCam;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "fakePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityOtherPlayerMP;", "flyValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "noClipValue", "oldX", "", "oldY", "oldZ", "speedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onDisable", "", "onEnable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class FreeCam
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 0.8f, 0.1f, 2.0f);
    private final BoolValue flyValue = new BoolValue("Fly", true);
    private final BoolValue noClipValue = new BoolValue("NoClip", true);
    private IEntityOtherPlayerMP fakePlayer;
    private double oldX;
    private double oldY;
    private double oldZ;

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        this.oldX = thePlayer.getPosX();
        this.oldY = thePlayer.getPosY();
        this.oldZ = thePlayer.getPosZ();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IEntityOtherPlayerMP playerMP = MinecraftInstance.classProvider.createEntityOtherPlayerMP(iWorldClient, thePlayer.getGameProfile());
        playerMP.setRotationYawHead(thePlayer.getRotationYawHead());
        playerMP.setRenderYawOffset(thePlayer.getRenderYawOffset());
        playerMP.setRotationYawHead(thePlayer.getRotationYawHead());
        playerMP.copyLocationAndAnglesFrom(thePlayer);
        IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient2 == null) {
            Intrinsics.throwNpe();
        }
        iWorldClient2.addEntityToWorld(-1000, playerMP);
        if (((Boolean)this.noClipValue.get()).booleanValue()) {
            thePlayer.setNoClip(true);
        }
        this.fakePlayer = playerMP;
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || this.fakePlayer == null) {
            return;
        }
        thePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, thePlayer.getRotationYaw(), thePlayer.getRotationPitch());
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
        thePlayer.setMotionX(0.0);
        thePlayer.setMotionY(0.0);
        thePlayer.setMotionZ(0.0);
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (((Boolean)this.noClipValue.get()).booleanValue()) {
            thePlayer.setNoClip(true);
        }
        thePlayer.setFallDistance(0.0f);
        if (((Boolean)this.flyValue.get()).booleanValue()) {
            float value = ((Number)this.speedValue.get()).floatValue();
            thePlayer.setMotionY(0.0);
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
            if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + (double)value);
            }
            if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() - (double)value);
            }
            MovementUtils.strafe(value);
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet) || MinecraftInstance.classProvider.isCPacketEntityAction(packet)) {
            event.cancelEvent();
        }
    }
}
