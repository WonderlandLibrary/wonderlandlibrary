package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name = "Cape", description = "Cape", category = ModuleCategory.RENDER)
public class Cape extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[] {"Funny","LiquidBance","Envy","ETB","2011","2012","2013","2015","2016","Mojang","Classic","Mojira","Cobalt","Christmas","SnowMan","Turtle"}, "LiquidBance");
}
