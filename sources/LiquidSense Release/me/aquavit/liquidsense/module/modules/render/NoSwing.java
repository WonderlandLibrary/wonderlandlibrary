package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;

@ModuleInfo(name = "NoSwing", description = "Disabled swing effect when hitting an entity/mining a block.", category = ModuleCategory.RENDER)
public class NoSwing extends Module {
    public static BoolValue serverSideValue = new BoolValue("ServerSide", true);
}
