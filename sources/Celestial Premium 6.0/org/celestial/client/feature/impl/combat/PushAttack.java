/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class PushAttack
extends Feature {
    private final NumberSetting clickCoolDown = new NumberSetting("Click CoolDown", 1.0f, 0.5f, 1.0f, 0.1f, () -> true);

    public PushAttack() {
        super("PushAttack", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0438\u0442\u044c \u043d\u0430 \u041b\u041a\u041c \u043d\u0435 \u0441\u043c\u043e\u0442\u0440\u044f \u043d\u0430 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432", Type.Combat);
        this.addSettings(this.clickCoolDown);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (PushAttack.mc.player.getCooledAttackStrength(0.0f) == this.clickCoolDown.getCurrentValue() && PushAttack.mc.gameSettings.keyBindAttack.pressed) {
            mc.clickMouse();
        }
    }
}

