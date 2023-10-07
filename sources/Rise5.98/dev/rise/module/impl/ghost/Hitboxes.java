package dev.rise.module.impl.ghost;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;

@ModuleInfo(name = "Hitboxes", description = "Expands entity hitboxes", category = Category.LEGIT)
public class Hitboxes extends Module {

    public NumberSetting expand = new NumberSetting("Expand", this, 1, 0, 5, 0.05);
    public BooleanSetting invis = new BooleanSetting("Expand Invis", this, false);

}
