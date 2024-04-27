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
    private final IntegerValue minCPS;
    private long delay;
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final Trigger this$0;

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)Trigger.access$getMinCPS$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
            Trigger.access$setDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)Trigger.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
        {
            this.this$0 = trigger;
            super(string, n, n2, n3);
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }

        static {
        }
    };
    private long lastSwing;

    public static final long access$getDelay$p(Trigger trigger) {
        return trigger.delay;
    }

    public Trigger() {
        this.minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
            final Trigger this$0;

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)Trigger.access$getMaxCPS$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
                Trigger.access$setDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)Trigger.access$getMaxCPS$p(this.this$0).get()).intValue()));
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }

            static {
            }
            {
                this.this$0 = trigger;
                super(string, n, n2, n3);
            }
        };
        this.delay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
    }

    public static final void access$setDelay$p(Trigger trigger, long l) {
        trigger.delay = l;
    }

    public static final IntegerValue access$getMaxCPS$p(Trigger trigger) {
        return trigger.maxCPS;
    }

    public static final IntegerValue access$getMinCPS$p(Trigger trigger) {
        return trigger.minCPS;
    }

    @EventTarget
    public final void onRender(Render3DEvent render3DEvent) {
        IMovingObjectPosition iMovingObjectPosition = MinecraftInstance.mc.getObjectMouseOver();
        if (iMovingObjectPosition != null && System.currentTimeMillis() - this.lastSwing >= this.delay && EntityUtils.isSelected(iMovingObjectPosition.getEntityHit(), true)) {
            MinecraftInstance.mc.getGameSettings().getKeyBindAttack().onTick(MinecraftInstance.mc.getGameSettings().getKeyBindAttack().getKeyCode());
            this.lastSwing = System.currentTimeMillis();
            this.delay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
        }
    }
}

