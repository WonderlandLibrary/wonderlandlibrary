package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;

@ModuleAnnotation(name = "FastPlace", category = ModuleCategory.MISC, clickG = "Places faster blocks")
public class FastPlace extends Module {

    public NumberSetting delay = new NumberSetting("delay", this, 0, 0, 3, 1);

    @EventTarget
    public void onRender(UpdateEvent e) {
        if (!this.isState()) return;
        MC.rightClickDelayTimer = delay.getValueInt();
        setDesc("Delay " + delay.getValue());
    }

    @Override
    public void onState() {
    }

    @Override
    public void onUndo() {
        MC.rightClickDelayTimer = 4;

    }
}
