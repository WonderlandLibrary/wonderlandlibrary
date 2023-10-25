package wtf.evolution.module.impl.Render;

import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ListSetting;
@ModuleInfo(name = "NoRender", type = Category.Render)
public class NoRender extends Module {

    public ListSetting listSetting = new ListSetting("Modes", "Fire", "Block").call(this);

}
