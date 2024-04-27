/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.AttackEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="SuperKnockback", spacedName="Super Knockback", description="Increases knockback dealt to other entities.", category=Category.COMBAT, cnName="\u8d85\u7ea7\u51fb\u9000")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\u0004\u0018\u00010\t8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0014"}, d2={"Lnet/dev/important/modules/module/modules/combat/SuperKnockback;", "Lnet/dev/important/modules/module/Module;", "()V", "delay", "Lnet/dev/important/value/IntegerValue;", "hurtTimeValue", "modeValue", "Lnet/dev/important/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "getTimer", "()Lnet/dev/important/utils/timer/MSTimer;", "onAttack", "", "event", "Lnet/dev/important/event/AttackEvent;", "LiquidBounce"})
public final class SuperKnockback
extends Module {
    @NotNull
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final IntegerValue delay;
    @NotNull
    private final MSTimer timer;

    public SuperKnockback() {
        String[] stringArray = new String[]{"ExtraPacket", "WTap", "Packet"};
        this.modeValue = new ListValue("Mode", stringArray, "ExtraPacket");
        this.delay = new IntegerValue("Delay", 0, 0, 500, "ms");
        this.timer = new MSTimer();
    }

    @NotNull
    public final MSTimer getTimer() {
        return this.timer;
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        block17: {
            Intrinsics.checkNotNullParameter(event, "event");
            if (!(event.getTargetEntity() instanceof EntityLivingBase)) break block17;
            if (((EntityLivingBase)event.getTargetEntity()).field_70737_aN > ((Number)this.hurtTimeValue.get()).intValue() || !this.timer.hasTimePassed(((Number)this.delay.get()).intValue())) {
                return;
            }
            String string = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            switch (string) {
                case "extrapacket": {
                    if (MinecraftInstance.mc.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc.field_71439_g.func_70031_b(true);
                    }
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)MinecraftInstance.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)MinecraftInstance.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)MinecraftInstance.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)MinecraftInstance.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                    MinecraftInstance.mc.field_71439_g.field_175171_bO = true;
                    break;
                }
                case "wtap": {
                    if (MinecraftInstance.mc.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc.field_71439_g.func_70031_b(false);
                    }
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)MinecraftInstance.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                    MinecraftInstance.mc.field_71439_g.field_175171_bO = true;
                    break;
                }
                case "packet": {
                    if (MinecraftInstance.mc.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc.field_71439_g.func_70031_b(true);
                    }
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)MinecraftInstance.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)MinecraftInstance.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                    MinecraftInstance.mc.field_71439_g.field_175171_bO = true;
                }
            }
            this.timer.reset();
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

