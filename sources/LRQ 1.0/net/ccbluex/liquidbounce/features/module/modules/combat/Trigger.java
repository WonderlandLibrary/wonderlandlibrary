/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

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

@ModuleInfo(name="Trigger", description="Automatically attacks the entity you are looking at.", category=ModuleCategory.COMBAT)
public final class Trigger
extends Module {
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
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
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
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
    public final void onRender(Render3DEvent event) {
        IMovingObjectPosition objectMouseOver = MinecraftInstance.mc.getObjectMouseOver();
        if (objectMouseOver != null && System.currentTimeMillis() - this.lastSwing >= this.delay && EntityUtils.isSelected(objectMouseOver.getEntityHit(), true)) {
            MinecraftInstance.mc.getGameSettings().getKeyBindAttack().onTick(MinecraftInstance.mc.getGameSettings().getKeyBindAttack().getKeyCode());
            this.lastSwing = System.currentTimeMillis();
            this.delay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
        }
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

