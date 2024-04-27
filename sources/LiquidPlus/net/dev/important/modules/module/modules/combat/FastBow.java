/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.IntegerValue;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

@Info(name="FastBow", spacedName="Fast Bow", description="Turns your bow into a machine gun.", category=Category.COMBAT, cnName="\u5feb\u901f\u5c04\u7bad")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u000e"}, d2={"Lnet/dev/important/modules/module/modules/combat/FastBow;", "Lnet/dev/important/modules/module/Module;", "()V", "delay", "Lnet/dev/important/value/IntegerValue;", "packetsValue", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "getTimer", "()Lnet/dev/important/utils/timer/MSTimer;", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class FastBow
extends Module {
    @NotNull
    private final IntegerValue packetsValue = new IntegerValue("Packets", 20, 3, 20);
    @NotNull
    private final IntegerValue delay = new IntegerValue("Delay", 0, 0, 500, "ms");
    @NotNull
    private final MSTimer timer = new MSTimer();

    @NotNull
    public final MSTimer getTimer() {
        return this.timer;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!MinecraftInstance.mc.field_71439_g.func_71039_bw()) {
            return;
        }
        if (MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g() != null && MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(BlockPos.field_177992_a, 255, MinecraftInstance.mc.field_71439_g.func_71045_bC(), 0.0f, 0.0f, 0.0f));
            float yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : MinecraftInstance.mc.field_71439_g.field_70177_z;
            float pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : MinecraftInstance.mc.field_71439_g.field_70125_A;
            int n = 0;
            int n2 = ((Number)this.packetsValue.get()).intValue();
            while (n < n2) {
                int i = n++;
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, true));
            }
            if (this.timer.hasTimePassed(((Number)this.delay.get()).intValue())) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
                this.timer.reset();
            }
            MinecraftInstance.mc.field_71439_g.field_71072_f = MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g().func_77988_m() - 1;
        }
    }
}

