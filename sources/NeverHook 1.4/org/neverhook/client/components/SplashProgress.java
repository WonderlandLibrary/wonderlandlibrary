/*    */ package org.neverhook.client.components;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*    */ import net.minecraft.client.shader.Framebuffer;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ 
/*    */ public class SplashProgress
/*    */   implements Helper {
/* 17 */   public static ResourceLocation resourceLocation = new ResourceLocation("neverhook/launch.png");
/*    */   public static int Progress;
/* 19 */   public static int maxProgress = 7;
/*    */   public static FontRenderer fontRenderer;
/*    */   
/*    */   public static void update() {
/* 23 */     drawSplash(mc.getTextureManager());
/*    */   }
/*    */   
/*    */   public static void setProgress(int progress) {
/* 27 */     Progress = progress;
/* 28 */     update();
/*    */   }
/*    */   
/*    */   public static void drawSplash(TextureManager textureManager) {
/* 32 */     fontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), false);
/* 33 */     if (mc.gameSettings.language != null) {
/* 34 */       fontRenderer.setUnicodeFlag(mc.isUnicode());
/* 35 */       fontRenderer.setBidiFlag(mc.mcLanguageManager.isCurrentLanguageBidirectional());
/*    */     } 
/* 37 */     mc.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)fontRenderer);
/* 38 */     int scaleFactor = sr.getScaleFactor();
/* 39 */     Framebuffer framebuffer = new Framebuffer(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor, true);
/* 40 */     framebuffer.bindFramebuffer(false);
/* 41 */     GlStateManager.matrixMode(5889);
/* 42 */     GlStateManager.loadIdentity();
/* 43 */     GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
/* 44 */     GlStateManager.matrixMode(5888);
/* 45 */     GlStateManager.loadIdentity();
/* 46 */     GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/* 47 */     GlStateManager.disableLighting();
/* 48 */     GlStateManager.disableFog();
/* 49 */     GlStateManager.disableDepth();
/* 50 */     GlStateManager.enableTexture2D();
/* 51 */     textureManager.bindTexture(resourceLocation);
/* 52 */     GlStateManager.resetColor();
/* 53 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 54 */     Gui.drawScaledCustomSizeModalRect(0.0F, 0.0F, 0.0F, 0.0F, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
/* 55 */     drawProgress();
/* 56 */     framebuffer.unbindFramebuffer();
/* 57 */     framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
/* 58 */     GlStateManager.enableAlpha();
/* 59 */     GlStateManager.alphaFunc(516, 0.1F);
/* 60 */     mc.updateDisplay();
/*    */   }
/*    */   
/*    */   private static void drawProgress() {
/* 64 */     if (mc.gameSettings == null) {
/*    */       return;
/*    */     }
/* 67 */     float calc = Progress / 7.0F * sr.getScaledWidth() * 0.595F;
/* 68 */     float color = PaletteHelper.getHealthColor(Progress, maxProgress);
/* 69 */     GlStateManager.resetColor();
/* 70 */     GlStateManager.TextureState.textureName = -1;
/* 71 */     RectHelper.drawSmoothRect(84.0F, (sr.getScaledHeight() - 110), (sr.getScaledWidth() - 85), (sr.getScaledHeight() - 95), (new Color(0, 0, 0)).getRGB());
/* 72 */     RectHelper.drawSmoothRect(86.0F, (sr.getScaledHeight() - 108), (sr.getScaledWidth() - 87), (sr.getScaledHeight() - 97), (new Color(50, 50, 50)).getRGB());
/* 73 */     RectHelper.drawRect(86.0D, (sr.getScaledHeight() - 108), (86.0F + calc), (sr.getScaledHeight() - 97), (int)color);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\components\SplashProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */