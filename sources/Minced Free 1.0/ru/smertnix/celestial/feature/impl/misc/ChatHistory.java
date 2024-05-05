package ru.smertnix.celestial.feature.impl.misc;

import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class ChatHistory
        extends Feature {
    public ChatHistory() {
        super("ChatHistory", "Позволяет сохранять историю чата", FeatureCategory.Util);
    }
}
