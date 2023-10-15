/*    */ package org.neverhook.client.ui.components.altmanager;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.ui.button.GuiAltButton;
/*    */ 
/*    */ public class GuiRenameAlt
/*    */   extends GuiScreen {
/*    */   private final GuiAltManager manager;
/*    */   private GuiTextField nameField;
/*    */   private PasswordField pwField;
/*    */   private String status;
/*    */   
/*    */   public GuiRenameAlt(GuiAltManager manager) {
/* 23 */     this.status = TextFormatting.GRAY + "Waiting...";
/* 24 */     this.manager = manager;
/*    */   }
/*    */   
/*    */   public void actionPerformed(GuiButton button) {
/* 28 */     switch (button.id) {
/*    */       case 0:
/* 30 */         this.manager.selectedAlt.setMask(this.nameField.getText());
/* 31 */         this.manager.selectedAlt.setPassword(this.pwField.getText());
/* 32 */         this.status = "Edited!";
/*    */         break;
/*    */       case 1:
/* 35 */         this.mc.displayGuiScreen(this.manager);
/*    */         break;
/*    */     } 
/*    */   }
/*    */   public void drawScreen(int par1, int par2, float par3) {
/* 40 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 41 */     RectHelper.drawBorderedRect(0.0F, 0.0F, this.width, this.height, 0.5F, (new Color(17, 17, 17, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
/* 42 */     RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), 1.0F, 1.0F, sr.getScaledWidth(), 1.0F, Color.white);
/* 43 */     this.mc.fontRendererObj.drawStringWithShadow("Edit Alt", this.width / 2.0F, 10.0F, -1);
/* 44 */     this.mc.fontRendererObj.drawStringWithShadow(this.status, this.width / 2.0F, 20.0F, -1);
/* 45 */     this.nameField.drawTextBox();
/* 46 */     this.pwField.drawTextBox();
/* 47 */     if (this.nameField.getText().isEmpty() && !this.nameField.isFocused()) {
/* 48 */       drawString(this.mc.fontRendererObj, "Name", this.width / 2 - 96, 66, -7829368);
/*    */     }
/*    */     
/* 51 */     if (this.pwField.getText().isEmpty() && !this.pwField.isFocused()) {
/* 52 */       drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
/*    */     }
/*    */     
/* 55 */     super.drawScreen(par1, par2, par3);
/*    */   }
/*    */   
/*    */   public void initGui() {
/* 59 */     this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Edit"));
/* 60 */     this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Cancel"));
/* 61 */     this.nameField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
/* 62 */     this.pwField = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
/*    */   }
/*    */   
/*    */   protected void keyTyped(char par1, int par2) {
/* 66 */     this.nameField.textboxKeyTyped(par1, par2);
/* 67 */     this.pwField.textboxKeyTyped(par1, par2);
/* 68 */     if (par1 == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
/* 69 */       this.nameField.setFocused(!this.nameField.isFocused());
/* 70 */       this.pwField.setFocused(!this.pwField.isFocused());
/*    */     } 
/*    */     
/* 73 */     if (par1 == '\r') {
/* 74 */       actionPerformed(this.buttonList.get(0));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void mouseClicked(int par1, int par2, int par3) {
/*    */     try {
/* 80 */       super.mouseClicked(par1, par2, par3);
/* 81 */     } catch (IOException e) {
/* 82 */       e.printStackTrace();
/*    */     } 
/* 84 */     this.nameField.mouseClicked(par1, par2, par3);
/* 85 */     this.pwField.mouseClicked(par1, par2, par3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\GuiRenameAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */