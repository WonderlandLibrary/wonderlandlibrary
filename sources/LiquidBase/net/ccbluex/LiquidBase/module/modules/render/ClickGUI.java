package net.ccbluex.LiquidBase.module.modules.render;

import net.ccbluex.LiquidBase.LiquidBase;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleCategory;
import net.ccbluex.LiquidBase.module.ModuleInfo;

/**
 * Copyright © 2015 - 2017 | CCBlueX | All rights reserved.
 * <p>
 * LiquidBase - By CCBlueX(Marco)
 */
@ModuleInfo(moduleName = "ClickGUI", moduleDescription = "", moduleCateogry = ModuleCategory.RENDER, canEnable = false)
public class ClickGUI extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(LiquidBase.CLIENT_INSTANCE.clickGUI);
    }
}