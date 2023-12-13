// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils.shader;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.utils.interfaces.MC;

public class ShaderUtil implements MC
{
    private ShaderLoader backGroundShader;
    private ShaderLoader blurShader;
    private long initTime;
    private long initBlurTime;
    private String name;
    private boolean selected;
    private ScaledResolution sr;
    
    public void createBackgroundShader(final String fragmentSource, final boolean optimize) {
        if (this.backGroundShader == null || !optimize) {
            this.backGroundShader = new ShaderLoader("shaders/shader.vert", fragmentSource);
            this.initTime = System.currentTimeMillis();
        }
    }
    
    public void createBlurShader() {
        if (this.blurShader == null) {
            this.blurShader = new ShaderLoader("shaders/shader.vert", "myshaders/blur.frag");
            this.initBlurTime = System.currentTimeMillis();
        }
    }
    
    public void useBlurSizedShader(final int x1, final int y1, final int x2, final int y2, final float delay) {
        this.sr = new ScaledResolution(ShaderUtil.mc);
        this.blurShader.startShader(this.sr.getScaledWidth(), this.sr.getScaledHeight(), Mouse.getX() * (float)this.sr.getScaledWidth() / ShaderUtil.mc.displayWidth, (float)((float)this.sr.getScaledHeight() - Mouse.getY() * this.sr.getScaledHeight() / (double)ShaderUtil.mc.displayHeight - 1.0), (System.currentTimeMillis() - this.initTime) / delay);
        GL11.glBegin(7);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y2);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x2, (float)y1);
        GL11.glEnd();
        this.blurShader.endShader();
    }
    
    public void createBackgroundShader(final String fragmentSource) {
        if (this.backGroundShader == null) {
            this.backGroundShader = new ShaderLoader("shaders/shader.vert", fragmentSource);
            this.initTime = System.currentTimeMillis();
        }
    }
    
    public void createBackgroundShader(final String fragmentSource, final String name) {
        if (this.backGroundShader == null) {
            this.backGroundShader = new ShaderLoader("shaders/shader.vert", fragmentSource);
            this.initTime = System.currentTimeMillis();
        }
        this.name = name;
    }
    
    public void useBackGroundShader(final float delay) {
        this.sr = new ScaledResolution(ShaderUtil.mc);
        this.backGroundShader.startShader(ShaderUtil.mc.displayWidth, ShaderUtil.mc.displayHeight, (float)Mouse.getX(), (float)Mouse.getY(), (System.currentTimeMillis() - this.initTime) / delay);
        GlStateManager.disableAlpha();
        GL11.glBegin(7);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glVertex2f(0.0f, (float)ShaderUtil.mc.displayHeight);
        GL11.glVertex2f((float)ShaderUtil.mc.displayWidth, (float)ShaderUtil.mc.displayHeight);
        GL11.glVertex2f((float)ShaderUtil.mc.displayWidth, 0.0f);
        GL11.glEnd();
        GlStateManager.enableAlpha();
        this.backGroundShader.endShader();
    }
    
    public void useSizedShader(final int x1, final int y1, final int x2, final int y2, final float delay) {
        this.sr = new ScaledResolution(ShaderUtil.mc);
        this.backGroundShader.startShader(ShaderUtil.mc.displayWidth, ShaderUtil.mc.displayHeight, Mouse.getX() * (float)this.sr.getScaledWidth() / ShaderUtil.mc.displayWidth, (float)((float)this.sr.getScaledHeight() - Mouse.getY() * this.sr.getScaledHeight() / (double)ShaderUtil.mc.displayHeight - 1.0), (System.currentTimeMillis() - this.initTime) / delay);
        GlStateManager.disableAlpha();
        GL11.glBegin(7);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y2);
        GL11.glVertex2f((float)x2, (float)y2);
        GL11.glVertex2f((float)x2, (float)y1);
        GL11.glEnd();
        GlStateManager.enableAlpha();
        this.backGroundShader.endShader();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
