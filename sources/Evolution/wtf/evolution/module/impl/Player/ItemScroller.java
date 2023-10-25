package wtf.evolution.module.impl.Player;

import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "ItemScroller", type = Category.Player)
public class ItemScroller extends Module {
    public SliderSetting delay = new SliderSetting("Delay", 10F, 0F, 500F, 1F).call(this);


}
