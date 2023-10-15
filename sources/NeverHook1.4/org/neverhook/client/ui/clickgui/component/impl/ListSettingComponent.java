/*     */ package org.neverhook.client.ui.clickgui.component.impl;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.ui.clickgui.component.Component;
/*     */ import org.neverhook.client.ui.clickgui.component.ExpandableComponent;
/*     */ import org.neverhook.client.ui.clickgui.component.PropertyComponent;
/*     */ 
/*     */ public class ListSettingComponent
/*     */   extends ExpandableComponent
/*     */   implements PropertyComponent
/*     */ {
/*     */   private final ListSetting listSetting;
/*     */   
/*     */   public ListSettingComponent(Component parent, ListSetting listSetting, int x, int y, int width, int height) {
/*  24 */     super(parent, listSetting.getName(), x, y, width, height);
/*  25 */     this.listSetting = listSetting;
/*     */   }
/*     */ 
/*     */   
/*     */   public Setting getSetting() {
/*  30 */     return (Setting)this.listSetting;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
/*  35 */     super.drawComponent(scaledResolution, mouseX, mouseY);
/*  36 */     int x = getX();
/*  37 */     int y = getY();
/*  38 */     int width = getWidth();
/*  39 */     int height = getHeight();
/*  40 */     String selectedText = this.listSetting.getCurrentMode();
/*  41 */     int dropDownBoxY = y + 10;
/*  42 */     int textColor = 16777215;
/*  43 */     Gui.drawRect(x, y, (x + width), (y + height), (new Color(15, 15, 15)).getRGB());
/*  44 */     mc.smallfontRenderer.drawStringWithShadow(getName(), (x + 2), (y + 3), textColor);
/*  45 */     Gui.drawRect((x + 2), dropDownBoxY, (x + getWidth() - 2), (int)(dropDownBoxY + 10.5D), (new Color(30, 30, 30)).getRGB());
/*  46 */     RectHelper.drawRect(x + 2.5D, dropDownBoxY + 0.5D, (x + getWidth()) - 2.5D, (dropDownBoxY + 10), -12828863);
/*  47 */     mc.circleregularSmall.drawStringWithShadow(selectedText, (x + 3.5F), (dropDownBoxY + 2.5F), -1);
/*  48 */     if (isExpanded()) {
/*  49 */       Gui.drawRect((x + 1), (y + height), (x + width - 1), (y + getHeightWithExpand()), (new Color(30, 30, 30)).getRGB());
/*  50 */       handleRender(x, y + getHeight() + 2, width, textColor);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int mouseX, int mouseY, int button) {
/*  56 */     super.onMouseClick(mouseX, mouseY, button);
/*  57 */     if (isExpanded()) {
/*  58 */       handleClick(mouseX, mouseY, getX(), getY() + getHeight() + 2, getWidth());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleRender(int x, int y, int width, int textColor) {
/*  63 */     int color = 0;
/*  64 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/*  65 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/*  66 */     double speed = ClickGui.speed.getNumberValue();
/*  67 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/*  69 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (y * 6L / 60L * 2L)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Fade":
/*  72 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Color Two":
/*  75 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Astolfo":
/*  78 */         color = PaletteHelper.astolfo(true, y).getRGB();
/*     */         break;
/*     */       case "Static":
/*  81 */         color = onecolor.getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  84 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/*  88 */     for (String e : this.listSetting.getModes()) {
/*  89 */       if (this.listSetting.currentMode.equals(e)) {
/*  90 */         GlStateManager.pushMatrix();
/*  91 */         GlStateManager.disableBlend();
/*  92 */         RectHelper.drawGradientRect((x + 1), (y - 2), (x + width - 1), (y + 15 - 6), color, (new Color(color)).darker().getRGB());
/*  93 */         GlStateManager.popMatrix();
/*     */       } 
/*  95 */       mc.fontRenderer.drawStringWithShadow(e, (x + 1 + 4), (y + 1), textColor);
/*  96 */       y += 12;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
/* 101 */     for (String e : this.listSetting.getModes()) {
/* 102 */       if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + 15 - 3) {
/* 103 */         this.listSetting.setListMode(e);
/*     */       }
/*     */       
/* 106 */       y += 12;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeightWithExpand() {
/* 112 */     return getHeight() + (this.listSetting.getModes().toArray()).length * 12;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPress(int mouseX, int mouseY, int button) {}
/*     */ 
/*     */   
/*     */   public boolean canExpand() {
/* 121 */     return ((this.listSetting.getModes().toArray()).length > 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\component\impl\ListSettingComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */