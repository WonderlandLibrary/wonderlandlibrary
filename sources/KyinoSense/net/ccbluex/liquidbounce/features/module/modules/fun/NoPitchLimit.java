/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoPitchLimit", description="Allows you to rotate your head indefinitely in every direction..", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/NoPitchLimit;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "serverSideValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onPacket", "", "e", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class NoPitchLimit
extends Module {
    private final BoolValue serverSideValue = new BoolValue("ServerSide", true);

    @EventTarget
    public final void onPacket(@NotNull PacketEvent e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        if (((Boolean)this.serverSideValue.get()).booleanValue()) {
            return;
        }
        Packet<?> packet = e.getPacket();
        if (packet instanceof C03PacketPlayer) {
            ((C03PacketPlayer)packet).field_149473_f = RangesKt.coerceAtMost(((C03PacketPlayer)packet).field_149473_f, 90.0f);
            ((C03PacketPlayer)packet).field_149473_f = RangesKt.coerceAtLeast(((C03PacketPlayer)packet).field_149473_f, -90.0f);
        }
    }
}

