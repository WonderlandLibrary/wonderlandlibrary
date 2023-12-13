/*     */ package org.neverhook.client.ui.newclickgui;
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.ScreenHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ import org.neverhook.client.ui.newclickgui.settings.BooleanSettingComponent;
/*     */ import org.neverhook.client.ui.newclickgui.settings.Component;
/*     */ import org.neverhook.client.ui.newclickgui.settings.KeybindButton;
/*     */ import org.neverhook.client.ui.newclickgui.settings.ListSettingComponent;
/*     */ import org.neverhook.client.ui.newclickgui.settings.NumberSettingComponent;
/*     */ 
/*     */ public class FeaturePanel implements Helper {
/*  27 */   public ArrayList<Component> components = new ArrayList<>();
/*  28 */   public ArrayList<KeybindButton> keybindButtons = new ArrayList<>(); public Feature feature; public int x; public int y;
/*     */   public int width;
/*     */   public int height;
/*  31 */   public Theme theme = new Theme(); public int scrollY;
/*     */   public int scrollOffset;
/*     */   public int yOffset;
/*     */   public boolean usingSettings = false;
/*     */   public ScreenHelper screenHelper;
/*     */   
/*     */   public FeaturePanel(Feature feature) {
/*  38 */     this.feature = feature;
/*  39 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*     */     
/*  41 */     this.keybindButtons.add(new KeybindButton(feature));
/*     */     
/*  43 */     for (Setting setting : feature.getSettings()) {
/*  44 */       if (setting instanceof BooleanSetting) {
/*  45 */         this.components.add(new BooleanSettingComponent(this, (BooleanSetting)setting)); continue;
/*  46 */       }  if (setting instanceof NumberSetting) {
/*  47 */         this.components.add(new NumberSettingComponent(this, (NumberSetting)setting)); continue;
/*  48 */       }  if (setting instanceof ListSetting) {
/*  49 */         this.components.add(new ListSettingComponent(this, (ListSetting)setting)); continue;
/*  50 */       }  if (setting instanceof ColorSetting) {
/*  51 */         this.components.add(new ColorPickerComponent(this, (ColorSetting)setting)); continue;
/*  52 */       }  if (setting instanceof org.neverhook.client.settings.impl.StringSetting);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY) {
/*  59 */     ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());
/*     */     
/*  61 */     RectHelper.drawRect(this.x - 0.5D, this.y, (this.x + this.width) + 0.5D, (this.y + this.height), (new Color(24, 24, 24, 230)).getRGB());
/*     */     
/*  63 */     if (isHovering(mouseX, mouseY) && !NeverHook.instance.newClickGui.usingSetting) {
/*  64 */       RectHelper.drawRect(this.x, this.y, (this.x + this.width), (this.y + this.height), (new Color(20, 20, 20)).getRGB());
/*     */     }
/*     */     
/*  67 */     if (!ClickGuiScreen.search.getText().isEmpty() && !this.feature.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     int color = 0;
/*  72 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/*  73 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/*  74 */     double speed = ClickGui.speed.getNumberValue();
/*  75 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/*  77 */         color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.y * 6L / 60L * 2L)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Fade":
/*  80 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + this.y + ((float)(this.height * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Color Two":
/*  83 */         color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + this.y + ((float)(this.height * 6L) / 60.0F * 2.0F)) % 2.0D - 1.0D));
/*     */         break;
/*     */       case "Astolfo":
/*  86 */         color = PaletteHelper.astolfo(true, this.y).getRGB();
/*     */         break;
/*     */       case "Static":
/*  89 */         color = onecolor.getRGB();
/*     */         break;
/*     */       case "Rainbow":
/*  92 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F).getRGB();
/*     */         break;
/*     */     } 
/*     */     
/*  96 */     boolean hovered = isHovering(mouseX, mouseY);
/*     */     
/*  98 */     if (hovered && this.feature.getDesc() != null && !this.usingSettings && !NeverHook.instance.newClickGui.usingSetting) {
/*  99 */       RectHelper.drawSmoothRect((this.x + this.width + 14), this.y + this.height / 1.5F + 3.5F, (this.x + this.width + 19 + mc.fontRendererObj.getStringWidth(this.feature.getDesc())), (this.y + 1), (new Color(30, 30, 30, 255)).getRGB());
/*     */       
/* 101 */       mc.fontRendererObj.drawStringWithOutline(this.feature.getDesc(), (this.x + this.width + 17), this.y + this.height / 1.35F - 7.5F, -1);
/*     */     } 
/*     */     
/* 104 */     if (this.components.size() > 0) {
/* 105 */       mc.fontRenderer.drawStringWithShadow(this.usingSettings ? "<" : ">", (this.x + this.width - 10), (this.y + this.height / 2.0F - mc.fontRenderer.getFontHeight() / 2.0F - 1.0F), Color.GRAY.getRGB());
/*     */     }
/*     */     
/* 108 */     mc.circleregular.drawStringWithShadow(this.feature.getLabel(), (this.x + 7), (this.y + this.height / 2.0F - mc.circleregular.getFontHeight() / 2.0F + 1.0F), this.feature.getState() ? color : Color.LIGHT_GRAY.getRGB());
/*     */     
/* 110 */     int yPlus = 0;
/* 111 */     if (this.usingSettings) {
/* 112 */       for (KeybindButton keybindButton : this.keybindButtons) {
/* 113 */         keybindButton.setPosition(scaledResolution.getScaledWidth() / 2 - 140, 90 + yPlus - this.scrollY, 276, 15);
/* 114 */         yPlus += 20;
/*     */       } 
/*     */       
/* 117 */       for (Component component : this.components) {
/* 118 */         if (component.setting.isVisible()) {
/* 119 */           if (component.setting instanceof ColorSetting) {
/* 120 */             component.setInformations(scaledResolution.getScaledWidth() / 2.0F + 55.0F, (90 + yPlus - this.scrollY), 276.0F, 15.0F);
/* 121 */             yPlus += 80;
/*     */           } 
/* 123 */           if (component.setting instanceof NumberSetting) {
/* 124 */             component.setInformations(scaledResolution.getScaledWidth() / 2.0F + 50.0F, (90 + yPlus - this.scrollY), 97.0F, 15.0F);
/* 125 */           } else if (component.setting instanceof BooleanSetting) {
/* 126 */             component.setInformations(scaledResolution.getScaledWidth() / 2.0F - 140.0F, (90 + yPlus - this.scrollY), 276.0F, 15.0F);
/* 127 */           } else if (!(component.setting instanceof org.neverhook.client.settings.impl.StringSetting)) {
/*     */             
/* 129 */             if (component.setting instanceof ListSetting) {
/*     */               
/* 131 */               ArrayList<String> modesArray = new ArrayList<>(((ListSetting)component.setting).getModes());
/* 132 */               String max = Collections.<String>max(modesArray, Comparator.comparing(String::length));
/*     */               
/* 134 */               int widthCombo = mc.fontRenderer.getStringWidth(max + "") + mc.fontRenderer.getStringWidth("V");
/*     */               
/* 136 */               modesArray.clear();
/*     */               
/* 138 */               component.setInformations(scaledResolution.getScaledWidth() / 2.0F + 148.0F, (90 + yPlus - this.scrollY), widthCombo, 15.0F);
/*     */               
/* 140 */               if (component.extended)
/* 141 */                 for (String sld : ((ListSetting)component.setting).getModes()) {
/* 142 */                   if (!((ListSetting)component.setting).getCurrentMode().equals(sld)) {
/* 143 */                     yPlus += 20;
/*     */                   }
/*     */                 }  
/*     */             } 
/*     */           } 
/* 148 */           yPlus += 20;
/*     */         } 
/*     */       } 
/*     */       
/* 152 */       int mouseWheel = Mouse.getDWheel();
/*     */       
/* 154 */       if (mouseWheel > 0 && 
/* 155 */         this.scrollOffset > 0) {
/* 156 */         this.scrollOffset = (int)(this.scrollOffset - ClickGui.scrollSpeed.getNumberValue());
/*     */       }
/*     */       
/* 159 */       if (mouseWheel < 0 && 
/* 160 */         this.scrollOffset < yPlus - scaledResolution.getScaledHeight() / 2 - 40 && yPlus + 25 > scaledResolution.getScaledHeight() - 150) {
/* 161 */         this.scrollOffset = (int)(this.scrollOffset + ClickGui.scrollSpeed.getNumberValue());
/*     */       }
/*     */ 
/*     */       
/* 165 */       this.screenHelper.interpolate(this.scrollOffset, 0.0F, 1.0D);
/* 166 */       this.scrollY = (int)this.screenHelper.getX();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int state) {
/* 171 */     for (Component component : this.components) {
/* 172 */       component.mouseReleased(mouseX, mouseY, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 177 */     if (NeverHook.instance.newClickGui.usingSetting) {
/* 178 */       if (this.usingSettings) {
/* 179 */         for (KeybindButton keybindButton : this.keybindButtons) {
/* 180 */           keybindButton.mouseClicked(mouseX, mouseY, mouseButton);
/*     */         }
/*     */         
/* 183 */         for (Component component : this.components) {
/* 184 */           component.mouseClicked(mouseX, mouseY, mouseButton);
/*     */         }
/*     */       } 
/*     */     } else {
/* 188 */       if (isHovering(mouseX, mouseY) && mouseButton == 0) {
/* 189 */         this.feature.state();
/*     */       }
/*     */       
/* 192 */       if (isHovering(mouseX, mouseY) && mouseButton == 1 && !NeverHook.instance.newClickGui.usingSetting) {
/* 193 */         NeverHook.instance.newClickGui.usingSetting = true;
/* 194 */         this.usingSettings = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void keyTyped(char chars, int keyCode) throws IOException {
/* 200 */     if (NeverHook.instance.newClickGui.usingSetting && 
/* 201 */       this.usingSettings) {
/* 202 */       for (Component component : this.components) {
/* 203 */         component.keyTyped(chars, keyCode);
/*     */       }
/*     */     }
/*     */     
/* 207 */     if (NeverHook.instance.newClickGui.usingSetting) {
/* 208 */       if (keyCode == 1) {
/* 209 */         NeverHook.instance.newClickGui.usingSetting = false;
/* 210 */         this.usingSettings = false;
/*     */       } else {
/* 212 */         for (KeybindButton keybindButton : this.keybindButtons) {
/* 213 */           keybindButton.keyTyped(keyCode);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isHovering(int mouseX, int mouseY) {
/* 220 */     return (mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height);
/*     */   }
/*     */   
/*     */   public void setPosition(int x, int y, int width, int height) {
/* 224 */     this.x = x;
/* 225 */     this.y = y;
/* 226 */     this.width = width;
/* 227 */     this.height = height;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\FeaturePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */