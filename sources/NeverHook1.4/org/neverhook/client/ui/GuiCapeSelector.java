/*     */ package org.neverhook.client.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.button.ImageButton;
/*     */ 
/*     */ public class GuiCapeSelector
/*     */   extends GuiScreen {
/*  22 */   protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
/*     */   private int width;
/*     */   private int height;
/*     */   private float spin;
/*     */   
/*     */   public void initGui() {
/*  28 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*  29 */     this.width = sr.getScaledWidth() / 2;
/*  30 */     this.height = sr.getScaledHeight() / 2;
/*  31 */     this.imageButtons.clear();
/*  32 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/close-button.png"), this.width + 106, this.height - 135, 8, 8, "", 19));
/*  33 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/arrow/arrow-right.png"), this.width + 30, this.height + 52, 32, 25, "", 56));
/*  34 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/arrow/arrow-left.png"), this.width - 50, this.height + 52, 32, 25, "", 55));
/*     */     
/*  36 */     super.initGui();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  41 */     drawWorldBackground(0);
/*  42 */     GlStateManager.pushMatrix();
/*  43 */     GlStateManager.disableBlend();
/*  44 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  45 */     RectHelper.drawSkeetRectWithoutBorder((this.width - 70), (this.height - 80), (this.width + 80), (this.height + 20));
/*  46 */     RectHelper.drawSkeetButton((this.width - 70), (this.height - 78), (this.width + 80), (this.height + 80));
/*  47 */     RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), (this.width - 110), (this.height - 140), 230.0F, 1.0F, Color.WHITE);
/*  48 */     this.mc.circleregular.drawStringWithOutline("Cape Changer", (this.width - 100), (this.height - 133), -1);
/*  49 */     drawEntityOnScreen((this.width + 7), (this.height + 38), this.spin, (EntityLivingBase)this.mc.player);
/*  50 */     this.spin = (float)(this.spin + 0.9D);
/*  51 */     for (ImageButton imageButton : this.imageButtons) {
/*  52 */       imageButton.draw(mouseX, mouseY, Color.LIGHT_GRAY);
/*     */     }
/*  54 */     GlStateManager.popMatrix();
/*  55 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  60 */     if (mouseButton == 0) {
/*  61 */       for (ImageButton imageButton : this.imageButtons) {
/*  62 */         imageButton.onClick(mouseX, mouseY);
/*     */       }
/*     */     }
/*  65 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   private void drawEntityOnScreen(float posX, float posY, float mouseX, EntityLivingBase entity) {
/*  69 */     GlStateManager.enableColorMaterial();
/*  70 */     GlStateManager.pushMatrix();
/*  71 */     GlStateManager.translate(posX, posY, 50.0F);
/*  72 */     GlStateManager.scale(-80.0F, 80.0F, 80.0F);
/*  73 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/*  74 */     float f = entity.renderYawOffset;
/*  75 */     float f1 = entity.rotationYaw;
/*  76 */     float f2 = entity.rotationPitchHead;
/*  77 */     float f3 = entity.prevRotationYawHead;
/*  78 */     float f4 = entity.rotationYawHead;
/*  79 */     GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*  80 */     RenderHelper.enableStandardItemLighting();
/*  81 */     entity.renderYawOffset = mouseX;
/*  82 */     entity.rotationYaw = mouseX;
/*  83 */     entity.rotationPitchHead = 0.0F;
/*  84 */     entity.rotationYawHead = entity.rotationYaw;
/*  85 */     entity.prevRotationYawHead = entity.rotationYaw;
/*  86 */     entity.prevRotationPitchHead = 0.0F;
/*  87 */     GlStateManager.translate(0.0F, 0.0F, 0.0F);
/*  88 */     RenderManager rendermanager = Minecraft.getInstance().getRenderManager();
/*  89 */     rendermanager.setPlayerViewY(180.0F);
/*  90 */     rendermanager.setRenderShadow(false);
/*  91 */     rendermanager.doRenderEntity((Entity)entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
/*  92 */     rendermanager.setRenderShadow(true);
/*  93 */     entity.renderYawOffset = f;
/*  94 */     entity.rotationYaw = f1;
/*  95 */     entity.rotationPitchHead = f2;
/*  96 */     entity.prevRotationPitchHead = f2;
/*  97 */     entity.prevRotationYawHead = f3;
/*  98 */     entity.rotationYawHead = f4;
/*  99 */     GlStateManager.popMatrix();
/* 100 */     RenderHelper.disableStandardItemLighting();
/* 101 */     GlStateManager.disableRescaleNormal();
/* 102 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 103 */     GlStateManager.disableTexture2D();
/* 104 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*     */   }
/*     */   
/*     */   public static class Selector {
/*     */     public static String capeName;
/*     */     
/*     */     public static String getCapeName() {
/* 111 */       return capeName;
/*     */     }
/*     */     
/*     */     public static void setCapeName(String capeName) {
/* 115 */       Selector.capeName = capeName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\GuiCapeSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */