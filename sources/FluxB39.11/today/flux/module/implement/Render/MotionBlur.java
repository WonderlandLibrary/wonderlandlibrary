package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import today.flux.event.TickEvent;
import today.flux.gui.resources.MotionBlurResourceManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.FloatValue;
import today.flux.utility.ChatUtils;

import java.util.Map;

public class MotionBlur extends Module {
    float lastValue;
    public static FloatValue amount = new FloatValue("MotionBlur", "Amount", 1f, 0f, 10f, 0.1f);
    private Map domainResourceManagers;

    public MotionBlur() {
        super("MotionBlur", Category.Render, false);
    }

    @Override
    public void onDisable() {
        mc.entityRenderer.stopUseShader();
        super.onDisable();
    }

    @EventTarget
    public void onClientTick(TickEvent event) {
        try {
            float curValue = amount.getValue();

            if (!mc.entityRenderer.isShaderActive() && mc.theWorld != null) {
                mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
            }

            if (domainResourceManagers == null) {
                domainResourceManagers = ((SimpleReloadableResourceManager) mc.mcResourceManager).getDomainResourceManagers();
            }

            if (!domainResourceManagers.containsKey("motionblur")) {
                domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
            }

            if (curValue != lastValue) {
                ChatUtils.debug("Motion Blur Updated!");
                domainResourceManagers.remove("motionblur");
                domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
                mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
            }

            lastValue = curValue;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
