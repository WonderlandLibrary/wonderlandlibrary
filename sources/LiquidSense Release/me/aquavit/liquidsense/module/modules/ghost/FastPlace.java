package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.IntegerValue;

@ModuleInfo(name = "FastPlace", description = "Allows you to place blocks faster.", category = ModuleCategory.GHOST)
public class FastPlace extends Module {
    public static IntegerValue speedValue = new IntegerValue("Speed", 0, 0, 4);
}
