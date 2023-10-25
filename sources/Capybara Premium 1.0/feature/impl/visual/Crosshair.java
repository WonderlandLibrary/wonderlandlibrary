package fun.expensive.client.feature.impl.visual;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;

public class Crosshair extends Feature {
    public static BooleanSetting smart = new BooleanSetting("Smart", false, () -> true);

    public Crosshair() {
        super("Crosshair", "", FeatureCategory.Visuals);
        addSettings(smart);
    }

   
}
