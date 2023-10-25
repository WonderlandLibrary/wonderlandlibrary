package org.dreamcore.client.feature.impl.combat;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class KeepSprint extends Feature {

    public static NumberSetting speed;
    public static BooleanSetting setSprinting;

    public KeepSprint() {
        super("KeepSprint", "Повзоляет редактировать скорость игрока при ударе", Type.Combat);
        speed = new NumberSetting("Keep Speed", 1, 0.5F, 2, 0.01F, () -> true);
        setSprinting = new BooleanSetting("Set Sprinting", true, () -> true);
        addSettings(setSprinting, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(speed.getNumberValue(), 2));
    }
}