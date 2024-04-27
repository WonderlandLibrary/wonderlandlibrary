/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="AutoWalk", description="Automatically makes you walk.", category=ModuleCategory.MOVEMENT)
public final class AutoWalk
extends Module {
    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        AutoWalk.access$getMc$p$s1046033730().getGameSettings().getKeyBindForward().setPressed(true);
    }

    @Override
    public void onDisable() {
        if (!AutoWalk.access$getMc$p$s1046033730().getGameSettings().isKeyDown(AutoWalk.access$getMc$p$s1046033730().getGameSettings().getKeyBindForward())) {
            AutoWalk.access$getMc$p$s1046033730().getGameSettings().getKeyBindForward().setPressed(false);
        }
    }

    public static final IMinecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

