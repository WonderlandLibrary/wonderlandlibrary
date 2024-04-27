package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.ListValue;

@ModuleInfo(name = "Cape", description = "Cape", category = ModuleCategory.CLIENT)
public class Cape extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[] {"Funny","LiquidBance","Envy","ETB","2011","2012","2013","2015","2016","Mojang","Classic","Mojira","Cobalt","Christmas","SnowMan","Turtle"}, "LiquidBance");
}
