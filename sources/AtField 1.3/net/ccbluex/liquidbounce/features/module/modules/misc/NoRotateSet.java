/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketPosLook;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="NoRotateSet", description="Prevents the server from rotating your head.", category=ModuleCategory.MISC)
public final class NoRotateSet
extends Module {
    private final BoolValue noZeroValue;
    private final BoolValue illegalRotationValue;
    private final BoolValue confirmValue = new BoolValue("Confirm", true);

    public NoRotateSet() {
        this.illegalRotationValue = new BoolValue("ConfirmIllegalRotation", false);
        this.noZeroValue = new BoolValue("NoZero", false);
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(packetEvent.getPacket())) {
            ISPacketPosLook iSPacketPosLook = packetEvent.getPacket().asSPacketPosLook();
            if (((Boolean)this.noZeroValue.get()).booleanValue() && iSPacketPosLook.getYaw() == 0.0f && iSPacketPosLook.getPitch() == 0.0f) {
                return;
            }
            if ((((Boolean)this.illegalRotationValue.get()).booleanValue() || iSPacketPosLook.getPitch() <= (float)90 && iSPacketPosLook.getPitch() >= (float)-90 && RotationUtils.serverRotation != null && iSPacketPosLook.getYaw() != RotationUtils.serverRotation.getYaw() && iSPacketPosLook.getPitch() != RotationUtils.serverRotation.getPitch()) && ((Boolean)this.confirmValue.get()).booleanValue()) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerLook(iSPacketPosLook.getYaw(), iSPacketPosLook.getPitch(), iEntityPlayerSP2.getOnGround()));
            }
            iSPacketPosLook.setYaw(iEntityPlayerSP2.getRotationYaw());
            iSPacketPosLook.setPitch(iEntityPlayerSP2.getRotationPitch());
        }
    }
}

