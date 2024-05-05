package ru.smertnix.celestial.feature.impl.player;

import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class ItemScroller extends Feature {


    public ItemScroller() {
        super("Item Scroller", "Позволяет скроллить предметы", FeatureCategory.Util);

        addSettings();

    }
}