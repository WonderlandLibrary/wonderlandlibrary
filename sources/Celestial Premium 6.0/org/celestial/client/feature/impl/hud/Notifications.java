/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.hud;

import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;

public class Notifications
extends Feature {
    public static BooleanSetting state;
    public static BooleanSetting timePeriod;
    public static ListSetting backGroundMode;

    public Notifications() {
        super("Notifications", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u0443\u044e \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u043c\u043e\u0434\u0443\u043b\u044f\u0445", Type.Hud);
        state = new BooleanSetting("Module State", true, () -> true);
        timePeriod = new BooleanSetting("Time Period", false, () -> true);
        this.addSettings(backGroundMode, state, timePeriod);
    }

    @Override
    public void onEnable() {
        this.toggle();
        super.onEnable();
    }

    static {
        backGroundMode = new ListSetting("Background Mode", "Rect", "Rect", "Blur");
    }
}

