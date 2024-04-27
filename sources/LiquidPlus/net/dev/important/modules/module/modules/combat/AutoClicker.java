/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.timer.TimeUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoClicker", spacedName="Auto Clicker", description="Constantly clicks when holding down a mouse button.", category=Category.COMBAT, cnName="\u8fde\u70b9\u5668")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0014H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lnet/dev/important/modules/module/modules/combat/AutoClicker;", "Lnet/dev/important/modules/module/Module;", "()V", "jitterValue", "Lnet/dev/important/value/BoolValue;", "leftDelay", "", "leftLastSwing", "leftValue", "maxCPSValue", "Lnet/dev/important/value/IntegerValue;", "minCPSValue", "rightDelay", "rightLastSwing", "rightValue", "onRender", "", "event", "Lnet/dev/important/event/Render3DEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class AutoClicker
extends Module {
    @NotNull
    private final IntegerValue maxCPSValue = new IntegerValue(this){
        final /* synthetic */ AutoClicker this$0;
        {
            this.this$0 = $receiver;
            super("MaxCPS", 8, 1, 20);
        }

        protected void onChanged(int oldValue, int newValue) {
            int minCPS2 = ((Number)AutoClicker.access$getMinCPSValue$p(this.this$0).get()).intValue();
            if (minCPS2 > newValue) {
                this.set(minCPS2);
            }
        }
    };
    @NotNull
    private final IntegerValue minCPSValue = new IntegerValue(this){
        final /* synthetic */ AutoClicker this$0;
        {
            this.this$0 = $receiver;
            super("MinCPS", 5, 1, 20);
        }

        protected void onChanged(int oldValue, int newValue) {
            int maxCPS2 = ((Number)AutoClicker.access$getMaxCPSValue$p(this.this$0).get()).intValue();
            if (maxCPS2 < newValue) {
                this.set(maxCPS2);
            }
        }
    };
    @NotNull
    private final BoolValue rightValue = new BoolValue("Right", true);
    @NotNull
    private final BoolValue leftValue = new BoolValue("Left", true);
    @NotNull
    private final BoolValue jitterValue = new BoolValue("Jitter", false);
    private long rightDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
    private long rightLastSwing;
    private long leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
    private long leftLastSwing;

    @EventTarget
    public final void onRender(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71474_y.field_74312_F.func_151470_d() && ((Boolean)this.leftValue.get()).booleanValue() && System.currentTimeMillis() - this.leftLastSwing >= this.leftDelay && MinecraftInstance.mc.field_71442_b.field_78770_f == 0.0f) {
            KeyBinding.func_74507_a((int)MinecraftInstance.mc.field_71474_y.field_74312_F.func_151463_i());
            this.leftLastSwing = System.currentTimeMillis();
            this.leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
        }
        if (MinecraftInstance.mc.field_71474_y.field_74313_G.func_151470_d() && !MinecraftInstance.mc.field_71439_g.func_71039_bw() && ((Boolean)this.rightValue.get()).booleanValue() && System.currentTimeMillis() - this.rightLastSwing >= this.rightDelay) {
            KeyBinding.func_74507_a((int)MinecraftInstance.mc.field_71474_y.field_74313_G.func_151463_i());
            this.rightLastSwing = System.currentTimeMillis();
            this.rightDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.jitterValue.get()).booleanValue() && (((Boolean)this.leftValue.get()).booleanValue() && MinecraftInstance.mc.field_71474_y.field_74312_F.func_151470_d() && MinecraftInstance.mc.field_71442_b.field_78770_f == 0.0f || ((Boolean)this.rightValue.get()).booleanValue() && MinecraftInstance.mc.field_71474_y.field_74313_G.func_151470_d() && !MinecraftInstance.mc.field_71439_g.func_71039_bw())) {
            EntityPlayerSP entityPlayerSP;
            if (Random.Default.nextBoolean()) {
                entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP.field_70177_z = entityPlayerSP.field_70177_z + (Random.Default.nextBoolean() ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
            }
            if (Random.Default.nextBoolean()) {
                entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP.field_70125_A = entityPlayerSP.field_70125_A + (Random.Default.nextBoolean() ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
                if (MinecraftInstance.mc.field_71439_g.field_70125_A > 90.0f) {
                    MinecraftInstance.mc.field_71439_g.field_70125_A = 90.0f;
                } else if (MinecraftInstance.mc.field_71439_g.field_70125_A < -90.0f) {
                    MinecraftInstance.mc.field_71439_g.field_70125_A = -90.0f;
                }
            }
        }
    }

    public static final /* synthetic */ IntegerValue access$getMinCPSValue$p(AutoClicker $this) {
        return $this.minCPSValue;
    }

    public static final /* synthetic */ IntegerValue access$getMaxCPSValue$p(AutoClicker $this) {
        return $this.maxCPSValue;
    }
}

