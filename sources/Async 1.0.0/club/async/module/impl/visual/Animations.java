package club.async.module.impl.visual;

import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import org.lwjgl.system.CallbackI;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "Animations", description = "Blockanimation", category = Category.VISUAL)
public class Animations extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, new String[] {"Default", "Old", "Exhibition"});
    public NumberSetting animSpeed = new NumberSetting("AnimationSpeed", this, 0.4, 3, 1, 0.1);
    public BooleanSetting equipProgress = new BooleanSetting("EquipProgress", this, true);
    public NumberSetting equipProgressMultiplier = new NumberSetting("Progress Multiplier", this, 0, 2, 1, 0.05, () -> equipProgress.get());
    public NumberSetting x = new NumberSetting("X", this, -1, 1, 0, 0.1);
    public NumberSetting y = new NumberSetting("Y", this, -1, 1, 0, 0.1);
    public NumberSetting animX = new NumberSetting("AnimX", this, -1, 1, 0, 0.1);
    public NumberSetting animY = new NumberSetting("AnimY", this, -1, 1, 0, 0.1);
    public NumberSetting itemSize = new NumberSetting("ItemSize", this, 0.1, 1.5, 1, 0.1);

    @Handler
    public void update(EventUpdate eventUpdate) {
        setExtraTag(mode.getCurrMode());
    }

}
