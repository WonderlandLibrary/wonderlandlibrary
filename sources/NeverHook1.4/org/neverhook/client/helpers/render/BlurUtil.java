/*    */ package org.neverhook.client.helpers.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.shader.Framebuffer;
/*    */ import net.minecraft.client.shader.Shader;
/*    */ import net.minecraft.client.shader.ShaderGroup;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class BlurUtil {
/* 12 */   protected static Minecraft mc = Minecraft.getInstance();
/* 13 */   private final ResourceLocation resourceLocation = new ResourceLocation("neverhook/shaders/fragment/blur.json");
/*    */   
/*    */   private ShaderGroup shaderGroup;
/*    */   private Framebuffer framebuffer;
/*    */   private int lastFactor;
/*    */   private int lastWidth;
/*    */   private int lastHeight;
/*    */   
/*    */   public void init() {
/*    */     try {
/* 23 */       this.shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), this.resourceLocation);
/* 24 */       this.shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
/* 25 */       this.framebuffer = this.shaderGroup.mainFramebuffer;
/*    */     }
/* 27 */     catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public void blur(float xBlur, float yBlur, float widthBlur, float heightBlur, int strength) {
/* 32 */     ScaledResolution scaledResolution = new ScaledResolution(mc);
/*    */     
/* 34 */     int scaleFactor = scaledResolution.getScaleFactor();
/* 35 */     int width = scaledResolution.getScaledWidth();
/* 36 */     int height = scaledResolution.getScaledHeight();
/*    */     
/* 38 */     if (sizeHasChanged(scaleFactor, width, height) || this.framebuffer == null || this.shaderGroup == null) {
/* 39 */       init();
/*    */     }
/*    */     
/* 42 */     this.lastFactor = scaleFactor;
/* 43 */     this.lastWidth = width;
/* 44 */     this.lastHeight = height;
/*    */     
/* 46 */     GL11.glPushMatrix();
/*    */     
/* 48 */     GL11.glEnable(3089);
/*    */     
/* 50 */     RenderHelper.scissorRect(xBlur, yBlur, widthBlur, heightBlur);
/*    */     
/* 52 */     this.framebuffer.bindFramebuffer(true);
/* 53 */     this.shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);
/*    */     
/* 55 */     for (int i = 0; i < 3; i++) {
/* 56 */       ((Shader)this.shaderGroup.getShaders().get(i)).getShaderManager().getShaderUniform("Radius").set(strength);
/*    */     }
/*    */     
/* 59 */     mc.getFramebuffer().bindFramebuffer(false);
/*    */     
/* 61 */     GL11.glDisable(3089);
/* 62 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */   private boolean sizeHasChanged(int scaleFactor, int width, int height) {
/* 66 */     return (this.lastFactor != scaleFactor || this.lastWidth != width || this.lastHeight != height);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\render\BlurUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */