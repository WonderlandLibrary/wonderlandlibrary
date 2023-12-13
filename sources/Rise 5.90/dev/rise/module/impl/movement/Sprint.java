/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.util.InstanceAccess;
import dev.rise.util.player.MoveUtil;
import org.lwjgl.input.Keyboard;

/**
 * Makes you automatically sprint without pressing the sprint key.
 */
@ModuleInfo(name = "Sprint", description = "Sprints for you", category = Category.MOVEMENT)
public final class Sprint extends Module implements InstanceAccess {

    private final BooleanSetting multiDir = new BooleanSetting("All Directions", this, false);
    private final BooleanSetting onlyInAir = new BooleanSetting("Only In Air", this, false);
    private int onGroundTicks;

    @Override
    public void onUpdateAlwaysInGui() {
        onlyInAir.hidden = !multiDir.isEnabled();
    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        if (mc.thePlayer.onGround) {
            onGroundTicks += 1;
        } else {
            onGroundTicks = 0;
        }

        mc.gameSettings.keyBindSprint.setKeyPressed(true);

        mc.thePlayer.allowMultiDirSprint = multiDir.isEnabled()
                && !mc.thePlayer.isSneaking()
                && MoveUtil.isMoving()
                && !mc.thePlayer.isCollidedHorizontally
                && (!onlyInAir.isEnabled() || onGroundTicks < 2);
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.keyBindSprint.setKeyPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode()));
        mc.thePlayer.allowMultiDirSprint = false;
    }
}
