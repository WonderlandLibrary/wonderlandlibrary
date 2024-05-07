package com.masterof13fps.features.modules.impl.misc;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventTick;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.utils.time.TimeHelper;

@ModuleInfo(name = "MemoryCleaner", category = Category.MISC, description = "Cleans your RAM (for more performance)")
public class MemoryCleaner extends Module {
    TimeHelper timeHelper = new TimeHelper();
    long memoryBeforeCleaning;

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        timeHelper.reset();
        memoryBeforeCleaning = 0;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventTick && !(mc.theWorld == null)) {
            memoryBeforeCleaning = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
            if (timeHelper.hasReached(60000L)) {
                Thread cleanThread = new Thread(() -> {
                    System.gc();
                    long clearedMemory = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) - memoryBeforeCleaning;
                    if (clearedMemory > memoryBeforeCleaning) {
                        notify.notification("MemoryCleaner", "Zwischenspeicher (" + clearedMemory / 1024 + " MB) geleert!", NotificationType.INFO, 5);
                    }else{
                        notify.debug("Could not clean the memory - nothing to clean found. Trying again in 60 seconds ...");
                    }
                    timeHelper.reset();
                });
                cleanThread.start();
            }
        }
    }
}
