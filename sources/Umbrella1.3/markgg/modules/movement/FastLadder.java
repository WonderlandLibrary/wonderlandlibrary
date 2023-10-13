/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package markgg.modules.movement;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.utilities.movement.MovementUtil;
import org.lwjgl.input.Keyboard;

public class FastLadder
extends Module {
    private int ticks = 0;

    public FastLadder() {
        super("FastLadder", 0, Module.Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.isEnabled()) {
            ++this.ticks;
            if (MovementUtil.isOnLadder() && Keyboard.isKeyDown((int)MovementUtil.getForwardCode())) {
                MovementUtil.setMotionY(0.25);
            } else if (MovementUtil.isOnLadder() && !Keyboard.isKeyDown((int)MovementUtil.getForwardCode())) {
                MovementUtil.setMotionY(-0.25);
            }
        }
    }
}

