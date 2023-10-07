package dev.rise.module.impl.ghost;

import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;

@ModuleInfo(name = "NoClickDelay", description = "Removes the 1.8 click delay when missing an attack", category = Category.LEGIT)
public class NoClickDelay extends Module {

    @Override
    public void onUpdate(final UpdateEvent event) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            if (!mc.inGameHasFocus) return;

            mc.leftClickCounter = 0;
        }
    }
}
