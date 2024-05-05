package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.step.ModeStep;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.NumberSetting;

public class Step extends AbstractModule {

    private final ArraySetting mode;
    public final NumberSetting height,timerSetting;

    private ModeStep modeStep;

    public Step() {
        super("Step", "Steps up.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Jump", "Jump", "Vanilla", "Motion", "Timer", "NCP", "Packet"),

                height = new NumberSetting(this, "Step Height", 0, 10, 1)
                        .setHidden(() -> !mode.compare("Vanilla")),

                timerSetting = new NumberSetting(this, "Timer", 0.3, 10, 2)
                        .setHidden(() -> !mode.compare("Timer"))

        );

        ModeStep.init();
        this.modeStep = ModeStep.getMode(mode.get());
    }

    @Override
    public void onEnable() {
        this.modeStep = ModeStep.getMode(mode.get());
        this.modeStep.setParent(this);
        this.modeStep.onEnable();
        Raze.INSTANCE.managerRegistry.eventManager.subscribe(this.modeStep);
    }

    @Override
    public void onDisable() {
        this.modeStep.onDisable();
        Raze.INSTANCE.managerRegistry.eventManager.unsubscribe(this.modeStep);
    }
}