/*    */ package org.neverhook.client.ui.button;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.audio.ISound;
/*    */ import net.minecraft.client.audio.PositionedSoundRecord;
/*    */ import net.minecraft.client.audio.SoundHandler;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ 
/*    */ public class GuiAuthButton extends GuiButton {
/* 13 */   private final int fade = 20;
/*    */   
/*    */   public GuiAuthButton(int buttonId, int x, int y, String buttonText) {
/* 16 */     this(buttonId, x, y, 200, 20, buttonText);
/*    */   }
/*    */   
/*    */   public GuiAuthButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
/* 20 */     super(buttonId, x, y, widthIn, heightIn, buttonText);
/*    */   }
/*    */   
/*    */   protected int getHoverState(boolean mouseOver) {
/* 24 */     int i = 1;
/*    */     
/* 26 */     if (!this.enabled) {
/* 27 */       i = 0;
/* 28 */     } else if (mouseOver) {
/* 29 */       i = 2;
/*    */     } 
/*    */     
/* 32 */     return i;
/*    */   }
/*    */   
/*    */   public void drawButton(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_) {
/* 36 */     if (this.visible) {
/* 37 */       FontRenderer fontrenderer = p_191745_1_.fontRendererObj;
/* 38 */       p_191745_1_.getTextureManager().bindTexture(BUTTON_TEXTURES);
/* 39 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 40 */       this.hovered = (p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height);
/* 41 */       int i = getHoverState(this.hovered);
/* 42 */       GlStateManager.enableBlend();
/* 43 */       GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 44 */       GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
/* 45 */       drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
/* 46 */       drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
/* 47 */       mouseDragged(p_191745_1_, p_191745_2_, p_191745_3_);
/* 48 */       int j = 14737632;
/*    */       
/* 50 */       if (!this.enabled) {
/* 51 */         j = 10526880;
/* 52 */       } else if (this.hovered) {
/* 53 */         j = 16777120;
/*    */       } 
/*    */       
/* 56 */       drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 77 */     return (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMouseOver() {
/* 84 */     return this.hovered;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawButtonForegroundLayer(int mouseX, int mouseY) {}
/*    */   
/*    */   public void playPressSound(SoundHandler soundHandlerIn) {
/* 91 */     soundHandlerIn.playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*    */   }
/*    */   
/*    */   public int getButtonWidth() {
/* 95 */     return this.width;
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 99 */     this.width = width;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\button\GuiAuthButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */