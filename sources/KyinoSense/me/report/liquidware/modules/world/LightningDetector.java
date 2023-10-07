/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="LightningDetector", description="Lightning x y z", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lme/report/liquidware/modules/world/LightningDetector;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onPacket", "", "packetEvent", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class LightningDetector
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent packetEvent) {
        Packet<?> packetIn;
        Intrinsics.checkParameterIsNotNull(packetEvent, "packetEvent");
        if (packetEvent.getPacket() instanceof S2CPacketSpawnGlobalEntity && ((S2CPacketSpawnGlobalEntity)(packetIn = packetEvent.getPacket())).func_149053_g() == 1) {
            int x = (int)((double)((S2CPacketSpawnGlobalEntity)packetIn).func_149051_d() / 32.0);
            int y = (int)((double)((S2CPacketSpawnGlobalEntity)packetIn).func_149050_e() / 32.0);
            int n = (int)((double)((S2CPacketSpawnGlobalEntity)packetIn).func_149049_f() / 32.0);
        }
    }
}

