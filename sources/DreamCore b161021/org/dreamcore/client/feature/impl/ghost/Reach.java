package org.dreamcore.client.feature.impl.ghost;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

public class Reach extends Feature {

    public static NumberSetting reachValue;

    public Reach() {
        super("Reach", "Увеличивает дистанцию удара", Type.Ghost);
        reachValue = new NumberSetting("Expand", 3.2F, 3, 5, 0.1F, () -> true);
        addSettings(reachValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(reachValue.getNumberValue(), 1));
    }
}