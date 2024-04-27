/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="SafeWalk", description="Prevents you from falling down as if you were sneaking.", category=ModuleCategory.MOVEMENT)
public final class SafeWalk
extends Module {
    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        block5: {
            block4: {
                if (((Boolean)this.airSafeValue.get()).booleanValue()) break block4;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getOnGround()) break block5;
            }
            moveEvent.setSafeWalk(true);
        }
    }
}

