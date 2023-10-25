package wtf.evolution.module.impl.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRenderHand;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "SwingAnimation", type = Category.Render)
public class SwingAnimation extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Bonk", "Bonk", "Knife", "Block").call(this);
    public SliderSetting mult = new SliderSetting("Hand Speed", 6, 1, 20, 1).call(this);

    @EventTarget
    public void onRender(EventRenderHand e) {
        if (mc.player.isSwingInProgress && !mc.player.getHeldItemMainhand().isEmpty() && e.e == EnumHandSide.RIGHT) {
            switch (mode.get()) {
                case "Bonk":
                    GlStateManager.translate(0, 0.5f, 0);
                    GlStateManager.rotate(45, 0, 0, 1);
                    break;
                case "Knife":
                    GlStateManager.translate(0, 0.5f, 0);
                    GlStateManager.rotate(90, 0, 0, 1);
                case "Block":
                    GlStateManager.translate(0, 0.5f, 0);
                    GlStateManager.rotate((this.mc.gameSettings.mainHand == EnumHandSide.RIGHT) ? 60.0F : -75.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate((this.mc.gameSettings.mainHand ==   EnumHandSide.RIGHT) ? 110 : -20.0F, -0.2F, 1.0F, 0.0F);
            }
        }
    }

}
