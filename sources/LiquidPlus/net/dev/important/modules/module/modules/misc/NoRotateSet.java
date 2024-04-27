/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.value.BoolValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;

@Info(name="NoRotateSet", spacedName="No Rotate Set", description="Prevents the server from rotating your head.", category=Category.MISC, cnName="\u7981\u6b62\u4fee\u6539\u8f6c\u5934")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/modules/module/modules/misc/NoRotateSet;", "Lnet/dev/important/modules/module/Module;", "()V", "confirmValue", "Lnet/dev/important/value/BoolValue;", "illegalRotationValue", "noZeroValue", "onPacket", "", "event", "Lnet/dev/important/event/PacketEvent;", "LiquidBounce"})
public final class NoRotateSet
extends Module {
    @NotNull
    private final BoolValue confirmValue = new BoolValue("Confirm", true);
    @NotNull
    private final BoolValue illegalRotationValue = new BoolValue("ConfirmIllegalRotation", false);
    @NotNull
    private final BoolValue noZeroValue = new BoolValue("NoZero", false);

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        if (packet instanceof S08PacketPlayerPosLook) {
            if (((Boolean)this.noZeroValue.get()).booleanValue() && ((S08PacketPlayerPosLook)packet).func_148931_f() == 0.0f && ((S08PacketPlayerPosLook)packet).func_148930_g() == 0.0f) {
                return;
            }
            if ((((Boolean)this.illegalRotationValue.get()).booleanValue() || ((S08PacketPlayerPosLook)packet).func_148930_g() <= 90.0f && ((S08PacketPlayerPosLook)packet).func_148930_g() >= -90.0f && RotationUtils.serverRotation != null && !(((S08PacketPlayerPosLook)packet).func_148931_f() == RotationUtils.serverRotation.getYaw()) && !(((S08PacketPlayerPosLook)packet).func_148930_g() == RotationUtils.serverRotation.getPitch())) && ((Boolean)this.confirmValue.get()).booleanValue()) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(((S08PacketPlayerPosLook)packet).func_148931_f(), ((S08PacketPlayerPosLook)packet).func_148930_g(), MinecraftInstance.mc.field_71439_g.field_70122_E));
            }
            ((S08PacketPlayerPosLook)packet).field_148936_d = MinecraftInstance.mc.field_71439_g.field_70177_z;
            ((S08PacketPlayerPosLook)packet).field_148937_e = MinecraftInstance.mc.field_71439_g.field_70125_A;
        }
    }
}

