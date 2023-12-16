// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import moonsense.event.impl.SCDamageEntityEvent;
import moonsense.event.impl.SCUpdateEvent;
import moonsense.event.SubscribeEvent;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import moonsense.event.impl.SCRenderEvent;
import java.awt.Color;
import moonsense.settings.Setting;
import net.minecraft.util.ResourceLocation;
import moonsense.features.SCModule;

public class HitTimerModule extends SCModule
{
    private static final ResourceLocation vignette;
    private long hitTime;
    private boolean displayingVignette;
    private final Setting vignetteColor;
    
    static {
        vignette = new ResourceLocation("textures/misc/vignette.png");
    }
    
    public HitTimerModule() {
        super("Hit Timer", "Overlays a colored vignette on your screen when you take damage to better time your hits.");
        this.hitTime = -1L;
        this.displayingVignette = false;
        this.vignetteColor = new Setting(this, "Vignette Color").setDefault(Color.red.getRGB(), 0);
    }
    
    @SubscribeEvent
    public void onRenderOverlay(final SCRenderEvent event) {
        if (this.displayingVignette) {
            final Tessellator var4 = Tessellator.getInstance();
            final WorldRenderer var5 = var4.getWorldRenderer();
            this.mc.getTextureManager().bindTexture(HitTimerModule.vignette);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(0, 769);
            var5.startDrawingQuads();
            var5.color(this.vignetteColor.getColor());
            final ScaledResolution sr = new ScaledResolution(this.mc);
            var5.addVertexWithUV(0.0, sr.getScaledHeight_double(), 0.0, 0.0, 1.0);
            var5.addVertexWithUV(sr.getScaledWidth_double(), sr.getScaledHeight_double(), 0.0, 1.0, 1.0);
            var5.addVertexWithUV(sr.getScaledWidth_double(), 0.0, 0.0, 1.0, 0.0);
            var5.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
            var4.draw();
            GlStateManager.disableBlend();
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final SCUpdateEvent event) {
        if (System.currentTimeMillis() - this.hitTime > 500L) {
            this.displayingVignette = false;
        }
    }
    
    @SubscribeEvent
    public void onEntityDamage(final SCDamageEntityEvent event) {
        if (event.entity == this.mc.thePlayer) {
            this.hitTime = System.currentTimeMillis();
            this.displayingVignette = true;
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
}
