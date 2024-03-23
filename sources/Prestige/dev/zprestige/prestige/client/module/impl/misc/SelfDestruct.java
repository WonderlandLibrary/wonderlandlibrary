package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;

public class SelfDestruct extends Module {

    public SelfDestruct() {
        super("Self Destruct", Category.Misc, "Destructs the client to hopefully prevent detection when screenshared");
    }

    @Override
    public void onEnable() {
        Prestige.Companion.setSelfDestructed(true);
        getMc().setScreen(null);
        for (Module module : Prestige.Companion.getModuleManager().getModules()) {
            if (module.isEnabled()) {
                module.toggle();
            }
            module.clear();
            module.getKeybind().invokeValue(-1);
        }
        Prestige.Companion.getClickGUI().onSelfDestruct();
        try {
            System.gc();
            System.runFinalization();
            System.gc();
            Thread.sleep(100L);
            System.gc();
            System.runFinalization();
            Thread.sleep(200L);
            System.gc();
            System.runFinalization();
            Thread.sleep(300L);
            System.gc();
            System.runFinalization();
        }
        catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }
        System.gc();
    }
}
