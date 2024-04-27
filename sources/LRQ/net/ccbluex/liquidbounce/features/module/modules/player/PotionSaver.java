/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="PotionSaver", description="Freezes all potion effects while you are standing still.", category=ModuleCategory.PLAYER, supportedVersions={MinecraftVersion.MC_1_8})
public final class PotionSaver
extends Module {
    @EventTarget
    public final void onPacket(PacketEvent e) {
        IPacket packet = e.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet) && !MinecraftInstance.classProvider.isCPacketPlayerPosition(packet) && !MinecraftInstance.classProvider.isCPacketPlayerPosLook(packet) && !MinecraftInstance.classProvider.isCPacketPlayerPosLook(packet) && MinecraftInstance.mc.getThePlayer() != null) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP.isUsingItem()) {
                e.cancelEvent();
            }
        }
    }
}

