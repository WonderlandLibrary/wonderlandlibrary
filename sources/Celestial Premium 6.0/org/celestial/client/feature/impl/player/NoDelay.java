/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;

public class NoDelay
extends Feature {
    public BooleanSetting rightClickDelay = new BooleanSetting("NoRightClickDelay", true, () -> true);
    public BooleanSetting leftClickDelay = new BooleanSetting("NoLeftClickDelay", false, () -> true);
    public BooleanSetting jumpDelay = new BooleanSetting("NoJumpDelay", true, () -> true);
    public BooleanSetting blockHitDelay = new BooleanSetting("NoBlockHitDelay", false, () -> true);

    public NoDelay() {
        super("NoDelay", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u0434\u0435\u0440\u0436\u043a\u0443", Type.Player);
        this.addSettings(this.rightClickDelay, this.leftClickDelay, this.jumpDelay, this.blockHitDelay);
    }

    @Override
    public void onDisable() {
        NoDelay.mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!this.getState()) {
            return;
        }
        if (this.rightClickDelay.getCurrentValue()) {
            NoDelay.mc.rightClickDelayTimer = 0;
        }
        if (this.leftClickDelay.getCurrentValue()) {
            NoDelay.mc.leftClickCounter = 0;
        }
        if (this.jumpDelay.getCurrentValue()) {
            NoDelay.mc.player.jumpTicks = 0;
        }
        if (this.blockHitDelay.getCurrentValue()) {
            NoDelay.mc.playerController.blockHitDelay = 0;
        }
    }
}

