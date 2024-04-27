/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.jetbrains.annotations.NotNull;

@Info(name="KeepSprint", spacedName="Keep Sprint", description="Keep you sprint. Hypixel auto ban.", category=Category.MOVEMENT, cnName="\u66f4\u597d\u7684\u75be\u8dd1")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/movement/KeepSprint;", "Lnet/dev/important/modules/module/Module;", "()V", "onPacket", "", "event", "Lnet/dev/important/event/PacketEvent;", "LiquidBounce"})
public final class KeepSprint
extends Module {
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C0BPacketEntityAction && ((C0BPacketEntityAction)packet).func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
            event.cancelEvent();
        }
    }
}

