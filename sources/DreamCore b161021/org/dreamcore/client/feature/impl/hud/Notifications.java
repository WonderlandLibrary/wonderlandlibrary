package org.dreamcore.client.feature.impl.hud;

import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;

public class Notifications extends Feature {

    public static BooleanSetting state;

    public Notifications() {
        super("Notifications", "Показывает необходимую информацию о модулях", Type.Hud);
        state = new BooleanSetting("Module State", true, () -> true);
        addSettings(state);
    }
}