package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.client.settings.KeyBinding;

public class Sprint extends AbstractModule {

    private final ArraySetting mode;

    public Sprint() {
        super("Sprint", "Sprints everytime you walk.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Legit", "Legit", "All Directions")

        );
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {

        if (!MoveUtil.isMoving() || mc.thePlayer.isSprinting()) return;

        if (eventMotion.getState() == Event.State.PRE) {
            switch (mode.get()) {

                case "Legit":
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
                    break;

                case "All Directions":
                    mc.thePlayer.setSprinting(true);
                    break;

            }
        }
    }
}
