/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class KeepSprint
extends Feature {
    public static NumberSetting speed;
    public static BooleanSetting setSprinting;

    public KeepSprint() {
        super("KeepSprint", "\u041f\u043e\u0432\u0437\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430 \u043f\u0440\u0438 \u0443\u0434\u0430\u0440\u0435", Type.Combat);
        speed = new NumberSetting("Keep Speed", 1.0f, 0.5f, 2.0f, 0.01f, () -> true);
        setSprinting = new BooleanSetting("Set Sprinting", true, () -> true);
        this.addSettings(setSprinting, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(speed.getCurrentValue(), speed.getCurrentValue() == 1.0f || speed.getCurrentValue() == 2.0f ? 1 : 2));
    }
}

