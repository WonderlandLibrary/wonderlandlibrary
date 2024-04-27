package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Trigger", description="Automatically attacks the entity you are looking at.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\n\b\n\t\n\b\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\t0\n20\fHR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000¨\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Trigger;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delay", "", "lastSwing", "maxCPS", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "minCPS", "onRender", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "Pride"})
public final class Trigger
extends Module {
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final Trigger this$0;

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
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final Trigger this$0;

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
        IMovingObjectPosition objectMouseOver = MinecraftInstance.mc.getObjectMouseOver();
        if (objectMouseOver != null && System.currentTimeMillis() - this.lastSwing >= this.delay && EntityUtils.isSelected(objectMouseOver.getEntityHit(), true)) {
            MinecraftInstance.mc.getGameSettings().getKeyBindAttack().onTick(MinecraftInstance.mc.getGameSettings().getKeyBindAttack().getKeyCode());
            this.lastSwing = System.currentTimeMillis();
            this.delay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
        }
    }

    public static final IntegerValue access$getMinCPS$p(Trigger $this) {
        return $this.minCPS;
    }

    public static final long access$getDelay$p(Trigger $this) {
        return $this.delay;
    }

    public static final void access$setDelay$p(Trigger $this, long l) {
        $this.delay = l;
    }

    public static final IntegerValue access$getMaxCPS$p(Trigger $this) {
        return $this.maxCPS;
    }
}
