package org.dreamcore.client.feature.impl.combat;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.NumberSetting;

public class PushAttack extends Feature {

    private final NumberSetting clickCoolDown;

    public PushAttack() {
        super("PushAttack", "Позволяет бить на ЛКМ не смотря на использование предметов", Type.Combat);
        clickCoolDown = new NumberSetting("Click CoolDown", 1F, 0.5F, 1F, 0.1F, () -> true);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getCooledAttackStrength(0) == clickCoolDown.getNumberValue() && mc.gameSettings.keyBindAttack.pressed) {
            mc.clickMouse();
        }
    }
}
