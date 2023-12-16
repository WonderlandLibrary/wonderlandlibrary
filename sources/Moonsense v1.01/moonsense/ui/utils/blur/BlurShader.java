// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils.blur;

import net.minecraft.client.shader.ShaderUniform;
import java.util.Iterator;
import net.minecraft.client.shader.Shader;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;

public class BlurShader
{
    public static final BlurShader INSTANCE;
    private final Minecraft mc;
    private final ResourceLocation SHADER;
    private Map resourceManager;
    private long start;
    
    static {
        INSTANCE = new BlurShader();
    }
    
    public BlurShader() {
        this.mc = Minecraft.getMinecraft();
        this.SHADER = new ResourceLocation("streamlined", "shaders/post/fade_in_blur.json");
    }
    
    public void onGuiOpen(final float BLUR_RADIUS) {
        if (this.resourceManager == null) {
            this.resourceManager = Minecraft.getMinecraft().getResourceManager().getDomainResourceManagers();
        }
        if (!this.resourceManager.containsKey("streamlined")) {
            this.resourceManager.put("streamlined", new BlurResourceManager(BLUR_RADIUS));
        }
        this.resourceManager.put("menustreamlinedblur", new BlurResourceManager(BLUR_RADIUS));
        if (this.mc.theWorld != null && !this.mc.entityRenderer.isShaderActive()) {
            this.mc.entityRenderer.func_175069_a(this.SHADER);
            this.start = System.currentTimeMillis();
        }
    }
    
    public void onGuiClose() {
        if (this.mc.theWorld != null && this.mc.entityRenderer.isShaderActive()) {
            this.mc.entityRenderer.stopUseShader();
        }
    }
    
    public void onRenderTick() {
        if (this.mc.currentScreen != null && this.mc.entityRenderer.isShaderActive()) {
            for (final Object s : this.mc.entityRenderer.getShaderGroup().getListShaders()) {
                final ShaderUniform su = ((Shader)s).getShaderManager().getShaderUniform("Progress");
                if (su != null) {
                    su.set(this.getProgress());
                }
            }
        }
    }
    
    private float getProgress() {
        return Math.min((System.currentTimeMillis() - this.start) / 100.0f, 1.0f);
    }
}
