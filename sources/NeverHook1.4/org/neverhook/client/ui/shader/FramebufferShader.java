/*    */ package org.neverhook.client.ui.shader;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.RenderHelper;
/*    */ import net.minecraft.client.shader.Framebuffer;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.lwjgl.opengl.GL20;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FramebufferShader
/*    */   extends Shader
/*    */ {
/*    */   private static Framebuffer framebuffer;
/*    */   protected float red;
/*    */   protected float green;
/*    */   protected float blue;
/* 20 */   protected float alpha = 1.0F;
/* 21 */   protected float radius = 2.0F;
/* 22 */   protected float quality = 1.0F;
/*    */   
/*    */   private boolean entityShadows;
/*    */   
/*    */   public FramebufferShader(String fragmentShader) {
/* 27 */     super(fragmentShader);
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderShader(float partialTicks) {
/* 32 */     GlStateManager.enableAlpha();
/*    */     
/* 34 */     GlStateManager.pushMatrix();
/* 35 */     GlStateManager.pushAttrib();
/*    */     
/* 37 */     framebuffer = setupFrameBuffer(framebuffer);
/* 38 */     framebuffer.framebufferClear();
/* 39 */     framebuffer.bindFramebuffer(true);
/* 40 */     this.entityShadows = mc.gameSettings.entityShadows;
/* 41 */     mc.gameSettings.entityShadows = false;
/* 42 */     mc.entityRenderer.setupCameraTransform(partialTicks, 0);
/*    */   }
/*    */   
/*    */   public void stopRenderShader(Color color, float radius, float quality) {
/* 46 */     mc.gameSettings.entityShadows = this.entityShadows;
/* 47 */     GL11.glEnable(3042);
/* 48 */     GL11.glBlendFunc(770, 771);
/* 49 */     mc.getFramebuffer().bindFramebuffer(true);
/*    */     
/* 51 */     this.red = color.getRed() / 255.0F;
/* 52 */     this.green = color.getGreen() / 255.0F;
/* 53 */     this.blue = color.getBlue() / 255.0F;
/* 54 */     this.alpha = color.getAlpha() / 255.0F;
/* 55 */     this.radius = radius;
/* 56 */     this.quality = quality;
/*    */     
/* 58 */     mc.entityRenderer.disableLightmap();
/* 59 */     RenderHelper.disableStandardItemLighting();
/*    */     
/* 61 */     startShader();
/* 62 */     mc.entityRenderer.setupOverlayRendering();
/* 63 */     drawFramebuffer(framebuffer);
/* 64 */     stopShader();
/*    */     
/* 66 */     mc.entityRenderer.disableLightmap();
/*    */     
/* 68 */     GlStateManager.popMatrix();
/* 69 */     GlStateManager.popAttrib();
/*    */   }
/*    */   
/*    */   public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
/* 73 */     if (frameBuffer != null) {
/* 74 */       frameBuffer.deleteFramebuffer();
/*    */     }
/* 76 */     frameBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
/*    */     
/* 78 */     return frameBuffer;
/*    */   }
/*    */   
/*    */   public void drawFramebuffer(Framebuffer framebuffer) {
/* 82 */     ScaledResolution scaledResolution = new ScaledResolution(mc);
/* 83 */     GL11.glBindTexture(3553, framebuffer.framebufferTexture);
/* 84 */     GL11.glBegin(7);
/* 85 */     GL11.glTexCoord2d(0.0D, 1.0D);
/* 86 */     GL11.glVertex2d(0.0D, 0.0D);
/* 87 */     GL11.glTexCoord2d(0.0D, 0.0D);
/* 88 */     GL11.glVertex2d(0.0D, scaledResolution.getScaledHeight());
/* 89 */     GL11.glTexCoord2d(1.0D, 0.0D);
/* 90 */     GL11.glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
/* 91 */     GL11.glTexCoord2d(1.0D, 1.0D);
/* 92 */     GL11.glVertex2d(scaledResolution.getScaledWidth(), 0.0D);
/* 93 */     GL11.glEnd();
/* 94 */     GL20.glUseProgram(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\shader\FramebufferShader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */