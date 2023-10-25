/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class FastClimb
extends Feature {
    public static ListSetting ladderMode;
    public static NumberSetting ladderSpeed;

    public FastClimb() {
        super("FastClimb", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u0437\u0430\u0431\u0438\u0440\u0430\u0442\u044c\u0441\u044f \u043f\u043e \u043b\u0435\u0441\u0442\u043d\u0438\u0446\u0430\u043c \u0438 \u043b\u0438\u0430\u043d\u0430\u043c", Type.Movement);
        ladderMode = new ListSetting("FastClimb Mode", "Old Matrix", () -> true, "Old Matrix", "Vanilla");
        ladderSpeed = new NumberSetting("Ladder Speed", 0.5f, 0.1f, 2.0f, 0.1f, () -> FastClimb.ladderMode.currentMode.equals("Vanilla"));
        this.addSettings(ladderMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix(ladderMode.getCurrentMode());
        if (FastClimb.mc.player == null || FastClimb.mc.world == null) {
            return;
        }
        switch (ladderMode.getOptions()) {
            case "Old Matrix": {
                if (!FastClimb.mc.player.isOnLadder() || !FastClimb.mc.player.isCollidedHorizontally || !MovementHelper.isMoving()) break;
                FastClimb.mc.player.motionY += (double)0.312f;
                event.setOnGround(true);
                break;
            }
            case "Vanilla": {
                if (!FastClimb.mc.player.isOnLadder() || !FastClimb.mc.player.isCollidedHorizontally || !MovementHelper.isMoving()) break;
                FastClimb.mc.player.motionY += (double)ladderSpeed.getCurrentValue();
            }
        }
    }
}

