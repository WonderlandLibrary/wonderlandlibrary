package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.NumberSetting;

public class HitBox extends Feature {

    public static NumberSetting hitboxSize = new NumberSetting("HitBox Size", 0.25f, 0.1f, 2.f, 0.1f, () -> true);

    public HitBox() {
        super("HitBox", "Увеличивает хитбокс у игрока.", FeatureCategory.Combat);
        addSettings(hitboxSize);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        setSuffix("" + hitboxSize.getNumberValue());
    }
}
