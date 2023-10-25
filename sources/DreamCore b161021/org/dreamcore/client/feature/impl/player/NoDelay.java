package org.dreamcore.client.feature.impl.player;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;

public class NoDelay extends Feature {

    public BooleanSetting rightClickDelay = new BooleanSetting("NoRightClickDelay", true, () -> true);
    public BooleanSetting leftClickDelay = new BooleanSetting("NoLeftClickDelay", false, () -> true);
    public BooleanSetting jumpDelay = new BooleanSetting("NoJumpDelay", true, () -> true);
    public BooleanSetting blockHitDelay = new BooleanSetting("NoBlockHitDelay", false, () -> true);

    public NoDelay() {
        super("NoDelay", "Убирает задержку", Type.Player);
        addSettings(rightClickDelay, leftClickDelay, jumpDelay, blockHitDelay);
    }


    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        if (!getState())
            return;

        if (rightClickDelay.getBoolValue()) {
            mc.rightClickDelayTimer = 0;
        }

        if (leftClickDelay.getBoolValue()) {
            mc.leftClickCounter = 0;
        }

        if (jumpDelay.getBoolValue()) {
            mc.player.jumpTicks = 0;
        }

        if (blockHitDelay.getBoolValue()) {
            mc.playerController.blockHitDelay = 0;
        }

    }

}
