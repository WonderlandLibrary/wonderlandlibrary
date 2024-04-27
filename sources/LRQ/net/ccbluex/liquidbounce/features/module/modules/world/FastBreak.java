/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Nuker;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="FastBreak", description="Allows you to break blocks faster.", category=ModuleCategory.WORLD)
public final class FastBreak
extends Module {
    private final FloatValue breakDamage = new FloatValue("BreakDamage", 0.8f, 0.1f, 1.0f);

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
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

