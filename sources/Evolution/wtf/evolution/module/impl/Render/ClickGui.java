package wtf.evolution.module.impl.Render;

import org.lwjgl.input.Keyboard;
import wtf.evolution.Main;
import wtf.evolution.clickgui.ClickScreen;
import wtf.evolution.helpers.file.ClickGuiSave;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;
@ModuleInfo(name = "ClickGui", type = Category.Render)
public class ClickGui extends Module {

    public ColorSetting color = new ColorSetting("Click Gui Color", -1);

    public ClickGui() {
        bind = Keyboard.KEY_RSHIFT;
        addSettings(color);
    }

@Override
public void onEnable() {
    super.onEnable();
    mc.displayGuiScreen(Main.s);
    ClickGuiSave.save();
    toggle();
}

public static int getColor() {
        return ((ClickGui) Main.m.getModule(ClickGui.class)).color.get();
}

}
//govnocode

