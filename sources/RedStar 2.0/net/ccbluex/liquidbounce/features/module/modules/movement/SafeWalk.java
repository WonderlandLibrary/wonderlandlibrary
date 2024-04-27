package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="SafeWalk", description="Prevents you from falling down as if you were sneaking.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020\bHR0X¢\n\u0000¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/SafeWalk;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airSafeValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onMove", "", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "Pride"})
public final class SafeWalk
extends Module {
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        block5: {
            block4: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (((Boolean)this.airSafeValue.get()).booleanValue()) break block4;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getOnGround()) break block5;
            }
            event.setSafeWalk(true);
        }
    }
}
