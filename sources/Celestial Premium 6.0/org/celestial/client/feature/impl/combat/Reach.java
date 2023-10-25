/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class Reach
extends Feature {
    public static NumberSetting reachValue;

    public Reach() {
        super("Reach", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0434\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044e \u0443\u0434\u0430\u0440\u0430", Type.Combat);
        reachValue = new NumberSetting("Expand", 3.2f, 3.0f, 5.0f, 0.01f, () -> true);
        this.addSettings(reachValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(reachValue.getCurrentValue(), 1));
    }
}

