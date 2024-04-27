package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Nuker;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FastBreak", description="Allows you to break blocks faster.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020\bHR0X¢\n\u0000¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/FastBreak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "breakDamage", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class FastBreak
extends Module {
    private final FloatValue breakDamage = new FloatValue("BreakDamage", 0.8f, 0.1f, 1.0f);

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        MinecraftInstance.mc.getPlayerController().setBlockHitDelay(0);
        if (MinecraftInstance.mc.getPlayerController().getCurBlockDamageMP() > ((Number)this.breakDamage.get()).floatValue()) {
            MinecraftInstance.mc.getPlayerController().setCurBlockDamageMP(1.0f);
        }
        if (Fucker.INSTANCE.getCurrentDamage() > ((Number)this.breakDamage.get()).floatValue()) {
            Fucker.INSTANCE.setCurrentDamage(1.0f);
        }
        if (Nuker.Companion.getCurrentDamage() > ((Number)this.breakDamage.get()).floatValue()) {
            Nuker.Companion.setCurrentDamage(1.0f);
        }
    }
}
