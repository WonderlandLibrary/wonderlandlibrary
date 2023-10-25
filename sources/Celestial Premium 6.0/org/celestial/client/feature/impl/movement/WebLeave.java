/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class WebLeave
extends Feature {
    private final NumberSetting motionValue = new NumberSetting("Motion Value", 10.0f, 1.0f, 15.0f, 1.0f, () -> true);

    public WebLeave() {
        super("WebLeave", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u044b\u0441\u043e\u043a\u043e \u043f\u0440\u0438\u0433\u043d\u0443\u0442\u044c \u043f\u0440\u0438 \u043f\u043e\u0433\u0440\u0443\u0436\u0435\u043d\u0438\u0438 \u0432 \u043f\u0430\u0443\u0442\u0438\u043d\u0443", Type.Movement);
        this.addSettings(this.motionValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.setSuffix("" + this.motionValue.getCurrentValue());
        if (WebLeave.mc.player.isInWeb) {
            WebLeave.mc.player.isInWeb = false;
            WebLeave.mc.player.motionY *= WebLeave.mc.player.ticksExisted % 2 == 0 ? (double)(-this.motionValue.getCurrentValue() * 10.0f) : -0.05;
        }
    }
}

