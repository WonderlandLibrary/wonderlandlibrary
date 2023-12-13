package vip.radium.module.impl.misc;

import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;

@ModuleInfo(label = "Memory Fix", category = ModuleCategory.MISCELLANEOUS)
public final class MemoryFix extends Module {

    public MemoryFix() {
        toggle();
        setHidden(true);
    }

    public static boolean cancelGarbageCollection() {
        return ModuleManager.getInstance(MemoryFix.class).isEnabled();
    }
}
