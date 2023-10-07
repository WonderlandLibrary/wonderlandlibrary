/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="SafeWalk", description="Prevents you from falling down as if you were sneaking.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/SafeWalk;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airSafeValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onMove", "", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "KyinoClient"})
public final class SafeWalk
extends Module {
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.airSafeValue.get()).booleanValue() || SafeWalk.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            event.setSafeWalk(true);
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

