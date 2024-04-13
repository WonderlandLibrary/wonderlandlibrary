package wtf.dawn.module.impl.ghost;

import wtf.dawn.module.Category;
import wtf.dawn.module.Module;
import wtf.dawn.module.ModuleInfo;
import wtf.dawn.settings.impl.BooleanSetting;
import wtf.dawn.settings.impl.KeybindSetting;
import wtf.dawn.settings.impl.SliderSetting;


@ModuleInfo(getName = "Auto Clicker", getDescription = "Automatically clicks for you.", getCategory = Category.Ghost)
public class AutoClicker extends Module {

    private final KeybindSetting keybind = new KeybindSetting(0);
    private final BooleanSetting onlyLeft = new BooleanSetting("Only Left", true);
    private final SliderSetting minimumCps = new SliderSetting("Minimum Cps", 12.0, 20.0, 0.0, 0.1);
    private final SliderSetting maximumCps = new SliderSetting("Maximum Cps", 16.0, 20.0, 0.0, 0.1);
    private final BooleanSetting breakBlocks = new BooleanSetting("Break Blocks", true);
    private final SliderSetting breakBlocksDelay = new SliderSetting("Break Blocks Delay", 5, 20.0, 0.0, 0.1);
    private final BooleanSetting inventoryCheck = new BooleanSetting("Inventory Check", true);

    public AutoClicker() {


    }
}