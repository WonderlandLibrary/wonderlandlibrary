package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.longjump.ModeLongJump;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;

public class LongJump extends AbstractModule {

    private final ArraySetting mode;

    private ModeLongJump modeLongJump;

    public LongJump() {
        super("LongJump", "Makes your jumps longer.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Vanilla", "Vanilla", "Vulcan", "Verus")

        );

        ModeLongJump.init();
        this.modeLongJump = ModeLongJump.getMode(mode.get());
        this.modeLongJump.setParent(this);
    }

    @Override
    public void onEnable() {
        this.modeLongJump = ModeLongJump.getMode(mode.get());
        this.modeLongJump.setParent(this);
        this.modeLongJump.onEnable();
        Raze.INSTANCE.managerRegistry.eventManager.subscribe(this.modeLongJump);
    }

    @Override
    public void onDisable() {
        this.modeLongJump.onDisable();
        Raze.INSTANCE.managerRegistry.eventManager.unsubscribe(this.modeLongJump);
    }
}