/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoClicker", description="Constantly clicks when holding down a mouse button.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0014H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoClicker;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "jitterValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "leftDelay", "", "leftLastSwing", "leftValue", "maxCPSValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "minCPSValue", "rightDelay", "rightLastSwing", "rightValue", "onRender", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoClicker
extends Module {
    private final IntegerValue maxCPSValue = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final /* synthetic */ AutoClicker this$0;

        protected void onChanged(int oldValue, int newValue) {
            int minCPS2 = ((Number)AutoClicker.access$getMinCPSValue$p(this.this$0).get()).intValue();
            if (minCPS2 > newValue) {
                this.set(minCPS2);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minCPSValue = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final /* synthetic */ AutoClicker this$0;

        protected void onChanged(int oldValue, int newValue) {
            int maxCPS2 = ((Number)AutoClicker.access$getMaxCPSValue$p(this.this$0).get()).intValue();
            if (maxCPS2 < newValue) {
                this.set(maxCPS2);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue rightValue = new BoolValue("Right", true);
    private final BoolValue leftValue = new BoolValue("Left", true);
    private final BoolValue jitterValue = new BoolValue("Jitter", false);
    private long rightDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
    private long rightLastSwing;
    private long leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
    private long leftLastSwing;

    @EventTarget
    public final void onRender(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        KeyBinding keyBinding = AutoClicker.access$getMc$p$s1046033730().field_71474_y.field_74312_F;
        Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindAttack");
        if (keyBinding.func_151470_d() && ((Boolean)this.leftValue.get()).booleanValue() && System.currentTimeMillis() - this.leftLastSwing >= this.leftDelay) {
            KeyBinding keyBinding2 = AutoClicker.access$getMc$p$s1046033730().field_71474_y.field_74312_F;
            Intrinsics.checkExpressionValueIsNotNull(keyBinding2, "mc.gameSettings.keyBindAttack");
            KeyBinding.func_74507_a((int)keyBinding2.func_151463_i());
            this.leftLastSwing = System.currentTimeMillis();
            this.leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
        }
        KeyBinding keyBinding3 = AutoClicker.access$getMc$p$s1046033730().field_71474_y.field_74313_G;
        Intrinsics.checkExpressionValueIsNotNull(keyBinding3, "mc.gameSettings.keyBindUseItem");
        if (keyBinding3.func_151470_d()) {
            EntityPlayerSP entityPlayerSP = AutoClicker.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (!entityPlayerSP.func_71039_bw() && ((Boolean)this.rightValue.get()).booleanValue() && System.currentTimeMillis() - this.rightLastSwing >= this.rightDelay) {
                KeyBinding keyBinding4 = AutoClicker.access$getMc$p$s1046033730().field_71474_y.field_74313_G;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding4, "mc.gameSettings.keyBindUseItem");
                KeyBinding.func_74507_a((int)keyBinding4.func_151463_i());
                this.rightLastSwing = System.currentTimeMillis();
                this.rightDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
            }
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block8: {
            block10: {
                block9: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    if (!((Boolean)this.jitterValue.get()).booleanValue()) break block8;
                    if (!((Boolean)this.leftValue.get()).booleanValue()) break block9;
                    KeyBinding keyBinding = AutoClicker.access$getMc$p$s1046033730().field_71474_y.field_74312_F;
                    Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindAttack");
                    if (keyBinding.func_151470_d()) break block10;
                }
                if (!((Boolean)this.rightValue.get()).booleanValue()) break block8;
                KeyBinding keyBinding = AutoClicker.access$getMc$p$s1046033730().field_71474_y.field_74313_G;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindUseItem");
                if (!keyBinding.func_151470_d()) break block8;
                EntityPlayerSP entityPlayerSP = AutoClicker.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (entityPlayerSP.func_71039_bw()) break block8;
            }
            if (Random.Default.nextBoolean()) {
                AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70177_z = AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70177_z + (Random.Default.nextBoolean() ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
            }
            if (Random.Default.nextBoolean()) {
                AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70125_A = AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70125_A + (Random.Default.nextBoolean() ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
                if (AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70125_A > (float)90) {
                    AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70125_A = 90.0f;
                } else if (AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70125_A < (float)-90) {
                    AutoClicker.access$getMc$p$s1046033730().field_71439_g.field_70125_A = -90.0f;
                }
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ IntegerValue access$getMinCPSValue$p(AutoClicker $this) {
        return $this.minCPSValue;
    }

    public static final /* synthetic */ IntegerValue access$getMaxCPSValue$p(AutoClicker $this) {
        return $this.maxCPSValue;
    }
}

