package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.highjump.ModeHighJump;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;

public class HighJump extends AbstractModule {

    private final ArraySetting mode;

    private ModeHighJump modeHighJump;

    public HighJump() {
        super("HighJump", "Jumps high.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode",  "Vulcan", "Vulcan", "Vulcan Damage")

        );

        ModeHighJump.init();
        this.modeHighJump = ModeHighJump.getMode(mode.get());
    }

    @Override
    public void onEnable() {
        this.modeHighJump = ModeHighJump.getMode(mode.get());
        this.modeHighJump.setParent(this);
        this.modeHighJump.onEnable();
        Raze.INSTANCE.managerRegistry.eventManager.subscribe(this.modeHighJump);
    }

    @Override
    public void onDisable() {
        this.modeHighJump.onDisable();
        Raze.INSTANCE.managerRegistry.eventManager.unsubscribe(this.modeHighJump);
    }
}