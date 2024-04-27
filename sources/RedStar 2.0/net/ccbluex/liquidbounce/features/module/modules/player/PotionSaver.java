package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="PotionSaver", description="Freezes all potion effects while you are standing still.", category=ModuleCategory.PLAYER, supportedVersions={MinecraftVersion.MC_1_8})
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J020H¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/PotionSaver;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onPacket", "", "e", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "Pride"})
public final class PotionSaver
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
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
