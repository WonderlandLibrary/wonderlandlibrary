package club.async.module.impl.hud;

import club.async.Async;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.NumberSetting;

@ModuleInfo(name = "Color", description = "Color picker for the Client color", category = Category.HUD)
public class Color extends Module {

    public NumberSetting red = new NumberSetting("Red", this, 0,255,61, 1);
    public NumberSetting green = new NumberSetting("Green", this, 0,255,165, 1);
    public NumberSetting blue = new NumberSetting("Blue", this, 0,255,235, 1);

    @Override
    public void onEnable() {
        super.onEnable();
        this.toggle();
    }

    public static Color getInstance() {
        return Async.INSTANCE.getModuleManager().moduleBy(Color.class);
    }

}
