/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.random.Random
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
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

@ModuleInfo(name="AutoClicker", description="Constantly clicks when holding down a mouse button.", category=ModuleCategory.COMBAT)
public final class AutoClicker
extends Module {
    private final IntegerValue minCPSValue;
    private long leftDelay;
    private final BoolValue leftValue;
    private final BoolValue rightValue;
    private long rightLastSwing;
    private final IntegerValue maxCPSValue = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final AutoClicker this$0;

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }
        {
            this.this$0 = autoClicker;
            super(string, n, n2, n3);
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)AutoClicker.access$getMinCPSValue$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
        }

        static {
        }
    };
    private long leftLastSwing;
    private final BoolValue jitterValue;
    private long rightDelay;

    @EventTarget
    public final void onRender(Render3DEvent render3DEvent) {
        if (MinecraftInstance.mc.getGameSettings().getKeyBindAttack().isKeyDown() && ((Boolean)this.leftValue.get()).booleanValue() && System.currentTimeMillis() - this.leftLastSwing >= this.leftDelay && MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.0f) {
            MinecraftInstance.mc.getGameSettings().getKeyBindAttack().onTick(MinecraftInstance.mc.getGameSettings().getKeyBindAttack().getKeyCode());
            this.leftLastSwing = System.currentTimeMillis();
            this.leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
        }
        if (MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().isKeyDown()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP.isUsingItem() && ((Boolean)this.rightValue.get()).booleanValue() && System.currentTimeMillis() - this.rightLastSwing >= this.rightDelay) {
                MinecraftInstance.mc.getGameSettings().getKeyBindAttack().onTick(MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().getKeyCode());
                this.rightLastSwing = System.currentTimeMillis();
                this.rightDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
            }
        }
    }

    public static final IntegerValue access$getMaxCPSValue$p(AutoClicker autoClicker) {
        return autoClicker.maxCPSValue;
    }

    public static final IntegerValue access$getMinCPSValue$p(AutoClicker autoClicker) {
        return autoClicker.minCPSValue;
    }

    public AutoClicker() {
        this.minCPSValue = new IntegerValue(this, "MinCPS", 5, 1, 20){
            final AutoClicker this$0;

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)AutoClicker.access$getMaxCPSValue$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
            }
            {
                this.this$0 = autoClicker;
                super(string, n, n2, n3);
            }
        };
        this.rightValue = new BoolValue("Right", true);
        this.leftValue = new BoolValue("Left", true);
        this.jitterValue = new BoolValue("Jitter", false);
        this.rightDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
        this.leftDelay = TimeUtils.randomClickDelay(((Number)this.minCPSValue.get()).intValue(), ((Number)this.maxCPSValue.get()).intValue());
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.jitterValue.get()).booleanValue() && (((Boolean)this.leftValue.get()).booleanValue() && MinecraftInstance.mc.getGameSettings().getKeyBindAttack().isKeyDown() && MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.0f || ((Boolean)this.rightValue.get()).booleanValue() && MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().isKeyDown() && !iEntityPlayerSP2.isUsingItem())) {
            if (Random.Default.nextBoolean()) {
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setRotationYaw(iEntityPlayerSP3.getRotationYaw() + (Random.Default.nextBoolean() ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
            }
            if (Random.Default.nextBoolean()) {
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                iEntityPlayerSP4.setRotationPitch(iEntityPlayerSP4.getRotationPitch() + (Random.Default.nextBoolean() ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
                if (iEntityPlayerSP2.getRotationPitch() > (float)90) {
                    iEntityPlayerSP2.setRotationPitch(90.0f);
                } else if (iEntityPlayerSP2.getRotationPitch() < (float)-90) {
                    iEntityPlayerSP2.setRotationPitch(-90.0f);
                }
            }
        }
    }
}

