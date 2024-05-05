// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.feature.Feature;

public class Spider extends Feature
{
    TimerHelper spiderTimer;
    public NumberSetting climbSpeed;
    
    public Spider() {
        super("Spider", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u0437\u0431\u0438\u0440\u0430\u0442\u044c\u0441\u044f \u0432\u0432\u0435\u0440\u0445 \u043f\u043e \u0441\u0442\u0435\u043d\u0430\u043c", Type.Movement);
        this.spiderTimer = new TimerHelper();
        this.climbSpeed = new NumberSetting("Climb Speed", 1.0f, 0.0f, 5.0f, 0.1f, () -> true);
        this.addSettings(this.climbSpeed);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        this.setSuffix("" + this.climbSpeed.getCurrentValue());
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion eventPreMotion) {
        if (MovementHelper.isMoving() && Spider.mc.h.A && this.spiderTimer.hasReached(this.climbSpeed.getCurrentValue() * 100.0f)) {
            eventPreMotion.setOnGround(true);
            Spider.mc.h.cu();
            this.spiderTimer.reset();
        }
    }
}
