package fun.expensive.client.feature.impl.player;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.NumberSetting;

public class ItemScroller extends Feature {

    public static NumberSetting scrollerDelay;

    public ItemScroller() {
        super("ItemScroller", "Позволяет быстро лутать сундуки при нажатии на шифт и ЛКМ", FeatureCategory.Player);

        scrollerDelay = new NumberSetting("Scroller Delay", 80, 0, 1000, 1, () -> true);
        addSettings(scrollerDelay);

    }
}