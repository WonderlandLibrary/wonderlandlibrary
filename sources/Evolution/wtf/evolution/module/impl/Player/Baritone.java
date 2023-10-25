package wtf.evolution.module.impl.Player;

import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;

@ModuleInfo(name = "Baritone", type = Category.Player)
public class Baritone extends Module {
    public BooleanSetting antirg = new BooleanSetting("AntiRG", true).call(this);
}
