package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.noslow.ModeNoSlow;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;

public class NoSlow extends AbstractModule {
    //TODO: Recode NoSlow

    private final ArraySetting mode;

    public static boolean isNoSlow = false;

    private ModeNoSlow modeNoSlow;

    public NoSlow() {
        super("NoSlow", "When using items, you don't get slowed down.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Vanilla", "Vanilla", "NCP", "Hypixel", "Old Intave", "MineMenClub", "Spoof", "Matrix")

        );

        ModeNoSlow.init();
        this.modeNoSlow = ModeNoSlow.getMode(mode.get());
    }

    @Override
    public void onEnable() {
        isNoSlow = true;

        this.modeNoSlow = ModeNoSlow.getMode(mode.get());
        this.modeNoSlow.setParent(this);
        this.modeNoSlow.onEnable();
        Raze.INSTANCE.managerRegistry.eventManager.subscribe(this.modeNoSlow);
    }


    @Override
    public void onDisable() {
        isNoSlow = false;

        this.modeNoSlow.onDisable();
        Raze.INSTANCE.managerRegistry.eventManager.unsubscribe(this.modeNoSlow);
    }

}