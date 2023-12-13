package koks.manager.module.impl.combat;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 20:10
 */

@ModuleInfo(name = "Rotations", description = "Setup your KillAura Rotations here", category = Module.Category.COMBAT)
public class Rotations extends Module {

    public Setting smooth = new Setting("Smooth", false, this);
    public Setting precision = new Setting("Precision", 0.1F, 0.05F, 0.50F, false, this);
    public Setting accuracy = new Setting("Accuracy", 0.3F, 0.1F, 0.8F, false, this);
    public Setting predictionMultiplier = new Setting("Prediction Multiplier", 0.4F, 0.0F, 1.0F, false, this);
    public Setting lockView = new Setting("LockView", false, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventUpdate) {
            setToggled(false);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}