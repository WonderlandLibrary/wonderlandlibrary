package fun.expensive.client.feature.impl.player;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;

public class NoPush extends Feature {
    public static BooleanSetting water = new BooleanSetting("Water", true, () -> true);
    public static BooleanSetting players = new BooleanSetting("Entity", true, () -> true);
    public static BooleanSetting blocks = new BooleanSetting("Blocks", true, () -> true);

    public NoPush() {
        super("NoPush", "Убирает отталкивание от игроков, воды и блоков", FeatureCategory.Player);
        addSettings(players, water, blocks);
    }
}
