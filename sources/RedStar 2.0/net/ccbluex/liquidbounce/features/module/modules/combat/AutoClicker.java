package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoClicker", description="Constantly clicks when holding down a mouse button.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\n\n\b\n\n\u0000\n\t\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020HJ020HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0\nX¢\n\u0000R0\nX¢\n\u0000R\f0X¢\n\u0000R\r0X¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoClicker;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "jitterValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "leftDelay", "", "leftLastSwing", "leftValue", "maxCPSValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "minCPSValue", "rightDelay", "rightLastSwing", "rightValue", "onRender", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class AutoClicker
extends Module {
    private final IntegerValue maxCPSValue = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final AutoClicker this$0;

        protected void onChanged(int oldValue, int newValue) {
            int minCPS = ((Number)AutoClicker.access$getMinCPSValue$p(this.this$0).get()).intValue();
            if (minCPS > newValue) {
                this.set(minCPS);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minCPSValue = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final AutoClicker this$0;

        protected void onChanged(int oldValue, int newValue) {
            int maxCPS = ((Number)AutoClicker.access$getMaxCPSValue$p(this.this$0).get()).intValue();
            if (maxCPS < newValue) {
                this.set(maxCPS);
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

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (((Boolean)this.jitterValue.get()).booleanValue() && (((Boolean)this.leftValue.get()).booleanValue() && MinecraftInstance.mc.getGameSettings().getKeyBindAttack().isKeyDown() && MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() == 0.0f || ((Boolean)this.rightValue.get()).booleanValue() && MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().isKeyDown() && !thePlayer.isUsingItem())) {
            if (Random.Default.nextBoolean()) {
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setRotationYaw(iEntityPlayerSP2.getRotationYaw() + (Random.Default.nextBoolean() ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
            }
            if (Random.Default.nextBoolean()) {
                IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                iEntityPlayerSP3.setRotationPitch(iEntityPlayerSP3.getRotationPitch() + (Random.Default.nextBoolean() ? -RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f) : RandomUtils.INSTANCE.nextFloat(0.0f, 1.0f)));
                if (thePlayer.getRotationPitch() > (float)90) {
                    thePlayer.setRotationPitch(90.0f);
                } else if (thePlayer.getRotationPitch() < (float)-90) {
                    thePlayer.setRotationPitch(-90.0f);
                }
            }
        }
    }

    public static final IntegerValue access$getMinCPSValue$p(AutoClicker $this) {
        return $this.minCPSValue;
    }

    public static final IntegerValue access$getMaxCPSValue$p(AutoClicker $this) {
        return $this.maxCPSValue;
    }
}
