/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.hud;

import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.components.draggable.DraggableModule;
import org.celestial.client.ui.components.draggable.impl.IndicatorsComponent;

public class Indicators
extends Feature {
    public static BooleanSetting glow;
    public static BooleanSetting shadow;
    public static NumberSetting shadowRadius;

    public Indicators() {
        super("Indicators", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043d\u0435\u043a\u043e\u0442\u043e\u0440\u0443\u044e \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043d\u0430 \u044d\u043a\u0440\u0430\u043d\u0435", Type.Hud);
        glow = new BooleanSetting("Glow", true, () -> true);
        shadow = new BooleanSetting("Shadow", false, () -> true);
        shadowRadius = new NumberSetting("Shadow Radius", 50.0f, 25.0f, 100.0f, 5.0f, () -> shadow.getCurrentValue());
        this.addSettings(glow, shadow, shadowRadius);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        for (DraggableModule draggableModule : Celestial.instance.draggableManager.getMods()) {
            if (!this.getState() || !(draggableModule instanceof IndicatorsComponent)) continue;
            draggableModule.draw();
        }
    }
}

