package fun.expensive.client.feature.impl.visual;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;

public class Cosmetics extends Feature {
    public static ListSetting wings = new ListSetting("Wings Mode", "White", () -> true, "White", "Gray");
    public static NumberSetting scaleWings = new NumberSetting("Scale", 0.3f, 0.4f, 2.0f, 0.1f, () -> true);

    public Cosmetics() {
        super("Cosmetics", FeatureCategory.Visuals);
        addSettings(wings, scaleWings);
    }
}
