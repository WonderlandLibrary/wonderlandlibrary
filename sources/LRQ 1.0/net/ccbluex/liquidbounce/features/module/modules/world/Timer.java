/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="Timer", description="Changes the speed of the entire game.", category=ModuleCategory.WORLD)
public final class Timer
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 2.0f, 0.1f, 10.0f);
    private final BoolValue onMoveValue = new BoolValue("OnMove", true);

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        if (MovementUtils.isMoving() || !((Boolean)this.onMoveValue.get()).booleanValue()) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.speedValue.get()).floatValue());
            return;
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
    }

    @EventTarget
    public final void onWorld(WorldEvent event) {
        if (event.getWorldClient() != null) {
            return;
        }
        this.setState(false);
    }
}

