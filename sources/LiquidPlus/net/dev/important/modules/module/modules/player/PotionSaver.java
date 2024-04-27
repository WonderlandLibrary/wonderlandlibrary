/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

@Info(name="PotionSaver", spacedName="Potion Saver", description="Freezes all potion effects while you are standing still.", category=Category.PLAYER, cnName="\u51bb\u7ed3\u836f\u6548")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/player/PotionSaver;", "Lnet/dev/important/modules/module/Module;", "()V", "onPacket", "", "e", "Lnet/dev/important/event/PacketEvent;", "LiquidBounce"})
public final class PotionSaver
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent e) {
        Intrinsics.checkNotNullParameter(e, "e");
        Packet<?> packet = e.getPacket();
        if (!(!(packet instanceof C03PacketPlayer) || packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C03PacketPlayer.C05PacketPlayerLook || MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71439_g.func_71039_bw())) {
            e.cancelEvent();
        }
    }
}

