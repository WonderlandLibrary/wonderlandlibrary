/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Trigger", description="Automatically attacks the entity you are looking at.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Trigger;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delay", "", "lastSwing", "maxCPS", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "minCPS", "onRender", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "KyinoClient"})
public final class Trigger
extends Module {
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 2000){
        final /* synthetic */ Trigger this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)Trigger.access$getMinCPS$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            Trigger.access$setDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)Trigger.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 2000){
        final /* synthetic */ Trigger this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)Trigger.access$getMaxCPS$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            Trigger.access$setDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)Trigger.access$getMaxCPS$p(this.this$0).get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private long delay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
    private long lastSwing;

    @EventTarget
    public final void onRender(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Trigger.access$getMc$p$s1046033730().field_71476_x != null && System.currentTimeMillis() - this.lastSwing >= this.delay && EntityUtils.isSelected(Trigger.access$getMc$p$s1046033730().field_71476_x.field_72308_g, true)) {
            KeyBinding keyBinding = Trigger.access$getMc$p$s1046033730().field_71474_y.field_74312_F;
            Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindAttack");
            KeyBinding.func_74507_a((int)keyBinding.func_151463_i());
            this.lastSwing = System.currentTimeMillis();
            this.delay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ IntegerValue access$getMinCPS$p(Trigger $this) {
        return $this.minCPS;
    }

    public static final /* synthetic */ long access$getDelay$p(Trigger $this) {
        return $this.delay;
    }

    public static final /* synthetic */ void access$setDelay$p(Trigger $this, long l) {
        $this.delay = l;
    }

    public static final /* synthetic */ IntegerValue access$getMaxCPS$p(Trigger $this) {
        return $this.maxCPS;
    }
}

