package club.dortware.client.module.impl.hidden;

import club.dortware.client.gui.click.DortGUI;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.module.Module;
import org.lwjgl.input.Keyboard;

@ModuleData(name = "ClickGUI", description = "Click UI", category = ModuleCategory.HIDE_ME, defaultKeyBind = Keyboard.KEY_RSHIFT)
public class ClickGUI extends Module {

    @Override
    public void setup() {}

    @Override
    public void onEnable() {
        mc.displayGuiScreen(DortGUI.instance());
        toggle();
        super.onEnable();
    }
}
