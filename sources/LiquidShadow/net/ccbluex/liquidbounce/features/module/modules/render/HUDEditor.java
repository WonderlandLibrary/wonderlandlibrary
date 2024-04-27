package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;

@ModuleInfo(name = "HUDEditor",description = "Edit the in-game hud.",category = ModuleCategory.RENDER,array = false,canEnable = false)
public class HUDEditor extends Module {
    @Override
    public void onEnable() {
        mc.displayGuiScreen(new GuiHudDesigner());
    }
}
