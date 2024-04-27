package net.ccbluex.liquidbounce.features.module.modules.render;

import java.lang.reflect.Field;
import java.util.Map;
import me.utils.motionblur.MotionBlurResourceManager;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

@ModuleInfo(name="MotionBlur", description="Render view.", category=ModuleCategory.RENDER)
public class MotionBlur
extends Module {
    public static IntegerValue MOTION_BLUR_AMOUNT = new IntegerValue("BlurAmount", 2, 1, 10);
    int lastValue = 0;
    private Map<String, MotionBlurResourceManager> domainResourceManagers;

    @Override
    public void onDisable() {
        mc.getEntityRenderer().stopUseShader();
    }

    @Override
    public void onEnable() {
        if (this.domainResourceManagers == null) {
            try {
                Field[] fields;
                for (Field field : fields = SimpleReloadableResourceManager.class.getDeclaredFields()) {
                    if (field.getType() != Map.class) continue;
                    field.setAccessible(true);
                    this.domainResourceManagers = (Map)field.get(mc2.getResourceManager());
                    break;
                }
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        if (!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
        if (this.isFastRenderEnabled()) {
            ClientUtils.disableFastRender();
        }
        this.lastValue = (Integer)MOTION_BLUR_AMOUNT.get();
        this.applyShader();
    }

    public boolean isFastRenderEnabled() {
        try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean(MotionBlur.mc2.gameSettings);
        }
        catch (Exception exception) {
            return false;
        }
    }

    public void applyShader() {
        mc.getEntityRenderer().loadShader2(new ResourceLocation("motionblur", "motionblur"));
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (!(MotionBlur.mc2.entityRenderer.isShaderActive() && this.lastValue == (Integer)MOTION_BLUR_AMOUNT.get() || MotionBlur.mc2.world == null || this.isFastRenderEnabled())) {
            this.lastValue = (Integer)MOTION_BLUR_AMOUNT.get();
            this.applyShader();
        }
        if (this.domainResourceManagers == null) {
            try {
                Field[] fields;
                for (Field field : fields = SimpleReloadableResourceManager.class.getDeclaredFields()) {
                    if (field.getType() != Map.class) continue;
                    field.setAccessible(true);
                    this.domainResourceManagers = (Map)field.get(mc2.getResourceManager());
                    break;
                }
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        if (!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
        if (this.isFastRenderEnabled()) {
            ClientUtils.disableFastRender();
        }
    }
}
