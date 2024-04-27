/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="Rotations", description="Allows you to see server-sided head and body rotations.", category=ModuleCategory.RENDER)
public final class Rotations
extends Module {
    private Float playerYaw;
    private final BoolValue bodyValue = new BoolValue("Body", true);

    private final boolean getState(Class clazz) {
        return LiquidBounce.INSTANCE.getModuleManager().get(clazz).getState();
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        block1: {
            if (RotationUtils.serverRotation == null || ((Boolean)this.bodyValue.get()).booleanValue()) break block1;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP != null) {
                iEntityPlayerSP.setRotationYawHead(RotationUtils.serverRotation.getYaw());
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (!((Boolean)this.bodyValue.get()).booleanValue() || this != null || iEntityPlayerSP == null) {
            return;
        }
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayerPosLook(iPacket) || MinecraftInstance.classProvider.isCPacketPlayerLook(iPacket)) {
            ICPacketPlayer iCPacketPlayer = iPacket.asCPacketPlayer();
            this.playerYaw = Float.valueOf(iCPacketPlayer.getYaw());
            iEntityPlayerSP.setRenderYawOffset(iCPacketPlayer.getYaw());
            iEntityPlayerSP.setRotationYawHead(iCPacketPlayer.getYaw());
        } else {
            if (this.playerYaw != null) {
                Float f = this.playerYaw;
                if (f == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.setRenderYawOffset(f.floatValue());
            }
            iEntityPlayerSP.setRotationYawHead(iEntityPlayerSP.getRenderYawOffset());
        }
    }
}

