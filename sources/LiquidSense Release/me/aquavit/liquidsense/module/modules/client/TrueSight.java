package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;

@ModuleInfo(name = "TrueSight", description = "Allows you to see invisible entities and barriers.", category = ModuleCategory.CLIENT)
public class TrueSight extends Module {
    public static BoolValue barriersValue = new BoolValue("Barriers", true);
    public static BoolValue entitiesValue = new BoolValue("Entities", true);
}
