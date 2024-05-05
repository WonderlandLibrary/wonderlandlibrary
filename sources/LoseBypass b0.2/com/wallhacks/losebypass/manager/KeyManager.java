/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.manager;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.InputEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.utils.MC;
import org.lwjgl.input.Mouse;

public class KeyManager
implements MC {
    boolean[] down = new boolean[5];

    public KeyManager() {
        LoseBypass.eventBus.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (KeyManager.mc.currentScreen != null) return;
        int i = 0;
        while (i < this.down.length) {
            if (Mouse.isButtonDown((int)i)) {
                if (!this.down[i]) {
                    this.onPress(-(2 + i));
                }
                this.down[i] = true;
            } else {
                this.down[i] = false;
            }
            ++i;
        }
    }

    private void onPress(int button) {
        LoseBypass.eventBus.post(new InputEvent(button));
    }
}

