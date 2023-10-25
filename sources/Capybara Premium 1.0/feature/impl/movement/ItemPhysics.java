package fun.expensive.client.feature.impl.movement;


import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.NumberSetting;

public class ItemPhysics extends Feature {
    public static NumberSetting physicsSpeed;

    public ItemPhysics() {
        super("ItemPhysics", FeatureCategory.Visuals);
                physicsSpeed = new NumberSetting("Physics Speed", 0.5F, 0.1F, 5.0F, 0.5F, () -> true);
        addSettings(physicsSpeed);
    }
}
