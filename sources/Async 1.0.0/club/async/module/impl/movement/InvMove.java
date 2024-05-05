package club.async.module.impl.movement;

import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.NumberSetting;

@ModuleInfo(name = "InvMove", description = "Move in inventory", category = Category.MOVEMENT)
public class InvMove extends Module {

    public BooleanSetting clickGuiOnly = new BooleanSetting("ClickGui only", this, false);

}
