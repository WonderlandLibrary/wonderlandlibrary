/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.EntityFireball
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.jetbrains.annotations.NotNull;

@Info(name="AntiFireBall", spacedName="Anti Fire Ball", description="Automatically punch fireballs away from you.", category=Category.COMBAT, cnName="\u53cd\u706b\u7130\u5f39")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0003R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lnet/dev/important/modules/module/modules/combat/AntiFireBall;", "Lnet/dev/important/modules/module/Module;", "()V", "rotationValue", "Lnet/dev/important/value/BoolValue;", "swingValue", "Lnet/dev/important/value/ListValue;", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class AntiFireBall
extends Module {
    @NotNull
    private final MSTimer timer = new MSTimer();
    @NotNull
    private final ListValue swingValue;
    @NotNull
    private final BoolValue rotationValue;

    public AntiFireBall() {
        String[] stringArray = new String[]{"Normal", "Packet", "None"};
        this.swingValue = new ListValue("Swing", stringArray, "Normal");
        this.rotationValue = new BoolValue("Rotation", true);
    }

    @EventTarget
    private final void onUpdate(UpdateEvent event) {
        for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityFireball) || !((double)MinecraftInstance.mc.field_71439_g.func_70032_d(entity) < 5.5) || !this.timer.hasTimePassed(300L)) continue;
            if (((Boolean)this.rotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(RotationUtils.getRotations(entity));
            }
            MinecraftInstance.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
            if (((String)this.swingValue.get()).equals("Normal")) {
                MinecraftInstance.mc.field_71439_g.func_71038_i();
            } else if (((String)this.swingValue.get()).equals("Packet")) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
            }
            this.timer.reset();
            break;
        }
    }
}

