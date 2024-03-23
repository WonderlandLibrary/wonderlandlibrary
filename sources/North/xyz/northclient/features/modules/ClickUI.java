package xyz.northclient.features.modules;

import org.lwjgl.input.Keyboard;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.DropdownClickGUI;
import xyz.northclient.features.ModuleInfo;

@ModuleInfo(name = "ClickUI",description = "Shows all modules",category = Category.INTERFACE,keyCode = Keyboard.KEY_RSHIFT)
public class ClickUI extends AbstractModule {

    @Override
    public void onEnable() {
        super.onEnable();

        mc.displayGuiScreen(new DropdownClickGUI());
        toggle();
    }
}
