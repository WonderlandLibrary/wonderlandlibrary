// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.motionblur;

import moonsense.enums.ModuleCategory;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.input.Keyboard;
import moonsense.config.ModuleConfig;
import moonsense.event.impl.SCKeyEvent;
import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCUpdateEvent;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import java.lang.reflect.Field;
import java.util.Map;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class MotionBlurModule extends SCModule
{
    public static MotionBlurModule INSTANCE;
    public final Setting blurAmount;
    private final Map resourceManager;
    private Field cachedFastRender;
    
    public MotionBlurModule() {
        super("Motion Blur", "Blurs the screen when you move.");
        MotionBlurModule.INSTANCE = this;
        this.resourceManager = ((SimpleReloadableResourceManager)this.mc.getResourceManager()).getDomainResourceManagers();
        this.blurAmount = new Setting(this, "Blur Amount").setDefault(2.0f).setRange(0.0f, 4.0f, 0.01f).onValueChanged(setting -> {
            this.resourceManager.put("streamlinedclient", new MotionBlurResourceManager());
            this.mc.entityRenderer.func_175069_a(new ResourceLocation("streamlinedclient", "motionblur"));
            this.mc.entityRenderer.getShaderGroup().createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            return;
        });
        try {
            this.cachedFastRender = GameSettings.class.getDeclaredField("ofFastRender");
        }
        catch (Exception ex) {}
    }
    
    @SubscribeEvent
    public void onUpdate(final SCUpdateEvent event) {
        if (!this.resourceManager.containsKey("streamlinedclient")) {
            this.resourceManager.put("streamlinedclient", new MotionBlurResourceManager());
        }
    }
    
    @SubscribeEvent
    public void onKey(final SCKeyEvent event) {
        final EntityRenderer entityRenderer = this.mc.entityRenderer;
        if (ModuleConfig.INSTANCE.isEnabled(this) && this.mc.thePlayer != null && !GameSettings.isKeyDown(this.mc.gameSettings.keyBindTogglePerspective) && !Keyboard.isKeyDown(1) && this.mc.gameSettings.thirdPersonView != 1 && this.mc.gameSettings.thirdPersonView != 2) {
            if (entityRenderer == null || entityRenderer.isShaderActive()) {
                return;
            }
            if (this.blurAmount.getFloat() != 0.0f) {
                entityRenderer.func_175069_a(new ResourceLocation("streamlinedclient", "motionblur"));
            }
        }
    }
    
    public boolean isFastRenderEnabled() {
        try {
            return this.cachedFastRender.getBoolean(this.mc.gameSettings);
        }
        catch (Exception exception) {
            return false;
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
