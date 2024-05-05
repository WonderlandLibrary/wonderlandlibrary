package client.module.impl.render;

import client.Client;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.ui.GuiClickMenu;

@ModuleInfo(name = "Click GUI", description = "", category = Category.RENDER)
public class ClickGUI extends Module {
    @Override
    protected void onEnable() {
        Client.INSTANCE.getConfigFile().write();
        toggle();
        mc.displayGuiScreen(new GuiClickMenu(mc.currentScreen));
    }
}
