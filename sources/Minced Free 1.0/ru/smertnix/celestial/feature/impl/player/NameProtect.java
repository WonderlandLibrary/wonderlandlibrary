package ru.smertnix.celestial.feature.impl.player;

import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.utils.other.NameUtils;

public class NameProtect extends Feature {
    public static BooleanSetting streamer = new BooleanSetting("Streamer", true, () -> true);
    public static BooleanSetting friends2 = new BooleanSetting("Friends Spoof", true, () -> !streamer.getBoolValue());

    public NameProtect() {
        super("Name Protect", "Скрывает информацию", FeatureCategory.Player);
        addSettings(streamer,friends2);
    }
}
