package wtf.evolution.module.impl.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRenderHand;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "ViewModel", type = Category.Render)
public class ViewModel extends Module {

    public SliderSetting rightX = new SliderSetting("Right X", 0, -180, 180, 1);
    public SliderSetting rightY = new SliderSetting("Right Y", 0, -180, 180, 1);
    public SliderSetting rightZ = new SliderSetting("Right Z", 0, -180, 180, 1);
    public SliderSetting leftX = new SliderSetting("Left X", 0, -180, 180, 1);
    public SliderSetting leftY = new SliderSetting("Left Y", 0, -180, 180, 1);
    public SliderSetting leftZ = new SliderSetting("Left Z", 0, -180, 180, 1);

    public ViewModel() {
        addSettings(rightX, rightY, rightZ, leftX, leftY, leftZ);
    }

    @EventTarget
    public void onRender(EventRenderHand e) {
        if (e.e == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightX.get() / 180, rightY.get() / 180, rightZ.get() / 180);
        }
        if (e.e == EnumHandSide.LEFT) {
            GlStateManager.translate(leftX.get() / 180, leftY.get() / 180, leftZ.get() / 180);
        }
    }

}
