package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author phatom | dirt | deleteboys | lmao | kroko
 * @created on 18.09.2020 : 21:29
 */

@ModuleInfo(name = "MemoryCleaner", description = "Its holds your memory clean", category = Module.Category.RENDER)
public class MemoryCleaner extends Module {

    public Setting memorySize = new Setting("MemorySize", new String[]{"Byte", "Kilobyte", "Megabyte", "Gigabyte", "Terabyte", "Petabyte", "Exabyte"}, "Gigabyte", this);
    public Setting minutes = new Setting("Minutes", 3, 1, 10, true, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {

            setInfo(memorySize.getCurrentMode() + "," + minutes.getCurrentValue());

            if (timeHelper.hasReached((long) (60000 * minutes.getCurrentValue()))) {

                long totalMemory = Runtime.getRuntime().totalMemory();

                String mode = memorySize.getCurrentMode();
                int memo = mode.equalsIgnoreCase("Byte") ? 0 : mode.equalsIgnoreCase("Kilobyte") ? 1 : mode.equalsIgnoreCase("Megabyte") ? 2 : mode.equalsIgnoreCase("Gigabyte") ? 3 : mode.equalsIgnoreCase("Terabyte") ? 4 : mode.equalsIgnoreCase("Petabyte") ? 5 : mode.equalsIgnoreCase("Exabyte") ? 6 : 0;

                Thread clearMemory = new Thread("clearMemory") {
                    public void run() {
                        System.gc();
                        this.stop();
                    }
                };
                clearMemory.start();

                long currentMemory = Runtime.getRuntime().totalMemory();
                long removedMemory = Math.abs(totalMemory - currentMemory);

                double memory = Math.round((removedMemory / Math.pow(1024.0, memo) * 100));
                double roundedMemory = memory / 100;
                String gigabyte = roundedMemory + "§f" + memorySize.getCurrentMode().substring(0, 1).toUpperCase() + "B";

                sendmsg("§aCleared §e" + (memo != 0 ? gigabyte : removedMemory + "§fB"), true);

                timeHelper.reset();
            }
        }

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
