package ru.smertnix.celestial.feature.impl.visual;

import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;

public class Notifications extends Feature {
    public static BooleanSetting state;
    public static BooleanSetting shieldBreak;
    
    public Notifications() {
        super("Notifications", "Уведомляет вас о включении модуля", FeatureCategory.Render);
        state = new BooleanSetting("Module Debug", true, () -> true);
        shieldBreak = new BooleanSetting("ShieldBreak Debug", true, () -> true);
        addSettings(state, shieldBreak);
    }
}