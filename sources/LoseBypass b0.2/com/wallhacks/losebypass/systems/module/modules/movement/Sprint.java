/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.movement;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.systems.module.Module;
import net.minecraft.client.settings.KeyBinding;

@Module.Registration(name="Sprint", description="Holds the sprint key down for you", category=Module.Category.MOVEMENT)
public class Sprint
extends Module {
    @SubscribeEvent
    public void onTick(TickEvent event) {
        KeyBinding.setKeyBindState(Sprint.mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }
}

