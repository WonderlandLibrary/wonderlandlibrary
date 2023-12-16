// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.colorsaturation;

import moonsense.enums.ModuleCategory;
import moonsense.event.SubscribeEvent;
import net.minecraft.client.renderer.EntityRenderer;
import moonsense.event.impl.SCUpdateEvent;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import java.lang.reflect.Field;
import java.util.Map;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class ColorSaturationModule extends SCModule
{
    public static ColorSaturationModule INSTANCE;
    public final Setting saturation;
    private final Map resourceManager;
    private Field cachedFastRender;
    
    public ColorSaturationModule() {
        super("Color Saturation", "Saturate the colors of your Minecraft world.");
        ColorSaturationModule.INSTANCE = this;
        this.resourceManager = ((SimpleReloadableResourceManager)this.mc.getResourceManager()).getDomainResourceManagers();
        this.saturation = new Setting(this, "Saturation").setDefault(1.0f).setRange(0.0f, 3.0f, 0.01f).onValueChanged(setting -> {
            this.resourceManager.put("streamlinedclient", new ColorSaturationResourceManager());
            this.mc.entityRenderer.func_175069_a(new ResourceLocation("streamlinedclient", "saturation"));
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
        final EntityRenderer entityRenderer = this.mc.entityRenderer;
        this.resourceManager.put("streamlinedclient", new ColorSaturationResourceManager());
        if (entityRenderer == null || entityRenderer.isShaderActive()) {
            return;
        }
        entityRenderer.func_175069_a(new ResourceLocation("streamlinedclient", "saturation"));
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
}
