/*     */ package org.neverhook.client.ui.newclickgui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ 
/*     */ public class Panel
/*     */   implements Helper {
/*     */   public Type type;
/*     */   public int x;
/*     */   public int y;
/*     */   public int width;
/*  23 */   public ArrayList<FeaturePanel> featurePanel = new ArrayList<>(); public int height; public int dragX; public int dragY; public boolean drag;
/*  24 */   public Theme theme = new Theme();
/*     */   private boolean open;
/*     */   
/*     */   public Panel(Type type, int x, int y) {
/*  28 */     this.type = type;
/*  29 */     this.x = x;
/*  30 */     this.y = y;
/*  31 */     this.width = 105;
/*  32 */     this.height = 21;
/*  33 */     this.open = false;
/*     */     
/*  35 */     for (Feature feature : NeverHook.instance.featureManager.getFeaturesForCategory(type)) {
/*  36 */       this.featurePanel.add(new FeaturePanel(feature));
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY) {
/*  41 */     this.featurePanel.sort(new SorterHelper());
/*  42 */     int color = 0;
/*  43 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/*  44 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/*  45 */     double speed = ClickGui.speed.getNumberValue();
/*  46 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/*  48 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.y * 6L / 60L * 2L)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Fade":
/*  51 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(this.y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Color Two":
/*  54 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + ((float)(this.y * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Astolfo":
/*  57 */         color = PaletteHelper.astolfo(true, this.y).getRGB();
/*     */         break;
/*     */       case "Static":
/*  60 */         color = onecolor.getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  63 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/*  67 */     RectHelper.drawRoundedRect(this.x - 0.5D, this.y, this.width - 1.5D, this.height, 4.0F, new Color(20, 20, 20));
/*  68 */     RenderHelper.drawImage(new ResourceLocation("neverhook/clickguiicons/" + this.type.getName() + ".png"), (this.x + 4), (this.y + 4), 14.0F, 14.0F, new Color(color));
/*     */     
/*  70 */     mc.circleregular.drawStringWithOutline(this.type.getName(), (this.x + 22), (this.y + this.height / 2.0F - 3.5F), Color.GRAY.getRGB());
/*     */     
/*  72 */     int yOffset = 0;
/*  73 */     if (this.open) {
/*  74 */       for (FeaturePanel featurePanel : this.featurePanel) {
/*  75 */         featurePanel.setPosition(this.x, this.y + this.height + yOffset, this.width, 15);
/*  76 */         featurePanel.drawScreen(mouseX, mouseY);
/*  77 */         yOffset += 15;
/*     */       } 
/*     */     }
/*     */     
/*  81 */     if (this.drag) {
/*  82 */       this.x = this.dragX + mouseX;
/*  83 */       this.y = this.dragY + mouseY;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int state) {
/*  88 */     this.drag = false;
/*     */     
/*  90 */     if (this.open) {
/*  91 */       for (FeaturePanel featurePanel : this.featurePanel) {
/*  92 */         featurePanel.mouseReleased(mouseX, mouseY, state);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  99 */     if (this.open) {
/* 100 */       for (FeaturePanel featurePanel : this.featurePanel) {
/* 101 */         featurePanel.mouseClicked(mouseX, mouseY, mouseButton);
/*     */       }
/*     */     }
/*     */     
/* 105 */     if (NeverHook.instance.newClickGui.usingSetting) {
/*     */       return;
/*     */     }
/* 108 */     if (isHovering(mouseX, mouseY) && mouseButton == 0) {
/* 109 */       this.dragX = this.x - mouseX;
/* 110 */       this.dragY = this.y - mouseY;
/* 111 */       this.drag = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void keyTyped(char chars, int keyCode) throws IOException {
/* 116 */     if (this.open) {
/* 117 */       for (FeaturePanel featurePanel : this.featurePanel) {
/* 118 */         featurePanel.keyTyped(chars, keyCode);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 124 */     return this.open;
/*     */   }
/*     */   
/*     */   public void setOpen(boolean open) {
/* 128 */     this.open = open;
/*     */   }
/*     */   
/*     */   public boolean isHovering(int mouseX, int mouseY) {
/* 132 */     return (mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height);
/*     */   }
/*     */   
/*     */   public boolean isWithinHeader(int x, int y) {
/* 136 */     return (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height);
/*     */   }
/*     */   
/*     */   public int getX() {
/* 140 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 144 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 148 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 152 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 156 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 160 */     this.width = width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 164 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 168 */     this.height = height;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\Panel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */