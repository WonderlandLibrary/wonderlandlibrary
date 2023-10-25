/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class NightMode
extends Feature {
    public static NumberSetting darkModifier;

    public NightMode() {
        super("NightMode", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0433\u0430\u043c\u043c\u0443 \u0442\u0435\u043c\u043d\u0435\u0435\u0435", Type.Visuals);
        darkModifier = new NumberSetting("Dark Modifier", 0.2f, 0.0f, 1.0f, 0.01f, () -> true);
        this.addSettings(darkModifier);
    }

    @Override
    public void onEnable() {
        NightMode.mc.gameSettings.gammaSetting = 1.0f;
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(darkModifier.getCurrentValue(), 2));
    }
}

