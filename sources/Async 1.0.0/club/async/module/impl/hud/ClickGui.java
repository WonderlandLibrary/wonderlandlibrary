package club.async.module.impl.hud;

import club.async.Async;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;

@ModuleInfo(name = "ClickGui", description = "UI for managing Modules and settings", category = Category.HUD)
public class ClickGui extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, new String[]{"Dropdown", "Flat"}, "Dropdown");
    public BooleanSetting gradient = new BooleanSetting("Gradient", this, true, () -> mode.is("dropdown"));
    public BooleanSetting blur = new BooleanSetting("Blur", this, true, () -> mode.is("dropdown"));

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.currentScreen == null) {
            switch (mode.getCurrMode()) {
                case "Dropdown":
                    mc.displayGuiScreen(Async.INSTANCE.getDropDown());
                    break;
                case "Flat":
                    mc.displayGuiScreen(Async.INSTANCE.getFlat());
                    break;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.displayGuiScreen(null);
    }
}
