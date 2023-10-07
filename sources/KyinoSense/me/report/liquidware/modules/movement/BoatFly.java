/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.Vec3;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="BoatFly", description=":/", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lme/report/liquidware/modules/movement/BoatFly;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoHit", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "delay", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hBoost", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "hitTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "jumpState", "", "lastRide", "", "timer", "vBoost", "onEnable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class BoatFly
extends Module {
    private final FloatValue hBoost = new FloatValue("HBoost", 3.0f, 0.0f, 6.0f);
    private final FloatValue vBoost = new FloatValue("VBoost", 3.0f, 0.0f, 6.0f);
    private final IntegerValue delay = new IntegerValue("Delay", 200, 100, 500);
    private final BoolValue autoHit = new BoolValue("AutoHit", true);
    private int jumpState = 1;
    private final MSTimer timer = new MSTimer();
    private final MSTimer hitTimer = new MSTimer();
    private boolean lastRide;

    @Override
    public void onEnable() {
        this.jumpState = 1;
        this.lastRide = false;
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        EntityPlayerSP entityPlayerSP = BoatFly.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_70115_ae() && this.jumpState == 1) {
            if (!this.lastRide) {
                this.timer.reset();
            }
            if (this.timer.hasTimePassed(((Number)this.delay.get()).intValue())) {
                this.jumpState = 2;
                BoatFly.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e = true;
            }
        } else if (this.jumpState == 2) {
            EntityPlayerSP entityPlayerSP2 = BoatFly.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            if (!entityPlayerSP2.func_70115_ae()) {
                BoatFly.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e = false;
                double radiansYaw = (double)BoatFly.access$getMc$p$s1046033730().field_71439_g.field_70177_z * Math.PI / (double)180;
                double d = ((Number)this.hBoost.get()).doubleValue();
                EntityPlayerSP entityPlayerSP3 = BoatFly.access$getMc$p$s1046033730().field_71439_g;
                boolean bl = false;
                double d2 = Math.sin(radiansYaw);
                entityPlayerSP3.field_70159_w = d * -d2;
                d = ((Number)this.hBoost.get()).doubleValue();
                entityPlayerSP3 = BoatFly.access$getMc$p$s1046033730().field_71439_g;
                bl = false;
                d2 = Math.cos(radiansYaw);
                entityPlayerSP3.field_70179_y = d * d2;
                BoatFly.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.vBoost.get()).floatValue();
                this.jumpState = 1;
                this.timer.reset();
                this.hitTimer.reset();
            }
        }
        EntityPlayerSP entityPlayerSP4 = BoatFly.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
        this.lastRide = entityPlayerSP4.func_70115_ae();
        if (((Boolean)this.autoHit.get()).booleanValue()) {
            EntityPlayerSP entityPlayerSP5 = BoatFly.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
            if (!entityPlayerSP5.func_70115_ae() && this.hitTimer.hasTimePassed(1500L)) {
                for (Entity entity : BoatFly.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
                    if (!(entity instanceof EntityBoat) || !(BoatFly.access$getMc$p$s1046033730().field_71439_g.func_70032_d(entity) < (float)3)) continue;
                    Minecraft minecraft = BoatFly.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(entity, new Vec3(0.5, 0.5, 0.5)));
                    Minecraft minecraft2 = BoatFly.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                    minecraft2.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(entity, C02PacketUseEntity.Action.INTERACT));
                    this.hitTimer.reset();
                }
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

