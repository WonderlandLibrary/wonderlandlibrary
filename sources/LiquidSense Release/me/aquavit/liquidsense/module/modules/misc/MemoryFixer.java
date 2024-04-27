package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.timer.TimeUtils;

@ModuleInfo(name = "MemoryFixer", description = "MemoryFixer", category = ModuleCategory.MISC)
public class MemoryFixer extends Module {

    TimeUtils timer = new TimeUtils();
    @EventTarget
    public void onTick(){
        if(timer.hasReached(3000)) {
            System.gc();
            //Runtime.getRuntime().gc();
            timer.reset();
        }

    }
}
