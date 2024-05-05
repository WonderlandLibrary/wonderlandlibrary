package ru.smertnix.celestial.utils.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.ResourceLocation;

public class TextureEngine
{
    private int x;
    private int y;
    private int width;
    private int height;
    private ResourceLocation texture;
    private final ScaleUtils scale;
    
    public TextureEngine(final String path, final ScaleUtils scale, final int width, final int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;
        try {
            this.texture = new ResourceLocation(path);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public TextureEngine(final ResourceLocation loc, final ScaleUtils scale, final int width, final int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;
        try {
            this.texture = loc;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void bind(final int x, final int y) {
        this.x = x;
        this.y = y;
        bind(this.getX() + 5.0f, (float)this.getY(), (float)(this.width / this.scale.getScale()), (float)(this.height / this.scale.getScale()), 1.0f, this.texture);
    }
    
    public void bind(final int x, final int y, final float red, final float green, final float blue) {
        this.x = x;
        this.y = y;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(red, green, blue, 1.0f);
        GlStateManager.enableTexture2D();
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        a(this.getX() + 5.0f, (float)this.getY(), 0.0f, 0.0f, (float)(this.width / this.scale.getScale()), (float)(this.height / this.scale.getScale()), (float)(this.width / this.scale.getScale()), (float)(this.height / this.scale.getScale()));
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }
    
    public static void bind(final float f, final float f2, final float f3, final float f4, final float f5, final ResourceLocation rs) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, f5);
        Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
        a(f, f2, 0.0f, 0.0f, f3, f4, f3, f4);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }
    
    public static void a(final float f, final float f2, final float f3, final float f4, final float f5, final float f6, final float f7, final float f8) {
        final float f9 = 1.0f / f7;
        final float f10 = 1.0f / f8;
        final Tessellator bly2 = Tessellator.getInstance();
        final BufferBuilder ali2 = bly2.getBuffer();
        ali2.begin(7, DefaultVertexFormats.POSITION_TEX);
        ali2.pos((double)f, (double)(f2 + f6), 0.0).tex((double)(f3 * f9), (double)((f4 + f6) * f10)).endVertex();
        ali2.pos((double)(f + f5), (double)(f2 + f6), 0.0).tex((double)((f3 + f5) * f9), (double)((f4 + f6) * f10)).endVertex();
        ali2.pos((double)(f + f5), (double)f2, 0.0).tex((double)((f3 + f5) * f9), (double)(f4 * f10)).endVertex();
        ali2.pos((double)f, (double)f2, 0.0).tex((double)(f3 * f9), (double)(f4 * f10)).endVertex();
        bly2.draw();
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
}
