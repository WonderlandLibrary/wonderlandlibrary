// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class Strafe extends Feature
{
    public Strafe() {
        super("Strafe", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0441\u0442\u0440\u0435\u0439\u0444\u0438\u0442\u044c \u043d\u0430 \u043c\u0430\u0442\u0440\u0438\u043a\u0441\u0435", Type.Movement);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (!this.getState()) {
            return;
        }
        if (MovementHelper.isMoving() && MovementHelper.getSpeed() < 0.2177f) {
            MovementHelper.strafe2();
            if (Math.abs(Strafe.mc.h.e.a) < 0.1 && Strafe.mc.t.T.i) {
                MovementHelper.strafe2();
            }
            if (Strafe.mc.h.z) {
                MovementHelper.strafe2();
            }
        }
    }
}
