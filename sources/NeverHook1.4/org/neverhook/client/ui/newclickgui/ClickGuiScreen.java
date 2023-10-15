/*     */ package org.neverhook.client.ui.newclickgui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.Scrollbar;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.ScreenHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.button.ImageButton;
/*     */ import org.neverhook.client.ui.clickgui.GuiSearcher;
/*     */ import org.neverhook.client.ui.newclickgui.settings.Component;
/*     */ import org.neverhook.client.ui.newclickgui.settings.KeybindButton;
/*     */ 
/*     */ public class ClickGuiScreen
/*     */   extends GuiScreen
/*     */ {
/*     */   public static GuiSearcher search;
/*  30 */   public ArrayList<Panel> panels = new ArrayList<>();
/*  31 */   public Theme hud = new Theme();
/*     */   public boolean exit = false;
/*     */   public boolean usingSetting = false;
/*  34 */   public int x = 20;
/*  35 */   public int y = 80;
/*     */   public ScreenHelper screenHelper;
/*     */   public ScreenHelper screenHelperSetting;
/*  38 */   public Scrollbar scrollbar = new Scrollbar();
/*  39 */   protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
/*     */   
/*     */   public ClickGuiScreen() {
/*  42 */     for (Type modCategory : Type.values()) {
/*  43 */       this.panels.add(new Panel(modCategory, this.x, this.y));
/*  44 */       this.y += this.height + 30;
/*     */     } 
/*  46 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*  47 */     this.screenHelperSetting = new ScreenHelper(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  53 */     this.mc.entityRenderer.theShaderGroup = null;
/*  54 */     super.onGuiClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  60 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*     */     
/*  62 */     if (!this.exit)
/*     */     {
/*     */       
/*  65 */       if (!this.usingSetting) {
/*  66 */         this.screenHelper.interpolate(sr.getScaledWidth(), sr.getScaledHeight(), 6.0D);
/*     */       }
/*     */     }
/*     */     
/*  70 */     Color color = Color.WHITE;
/*  71 */     Color onecolor = new Color(ClickGui.color.getColorValue());
/*  72 */     Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
/*  73 */     double speed = ClickGui.speed.getNumberValue();
/*  74 */     switch (ClickGui.clickGuiColor.currentMode) {
/*     */       case "Client":
/*  76 */         color = ClientHelper.getClientColor();
/*     */         break;
/*     */       case "Fade":
/*  79 */         color = new Color(ClickGui.color.getColorValue());
/*     */         break;
/*     */       case "Astolfo":
/*  82 */         color = PaletteHelper.astolfo(true, this.width);
/*     */         break;
/*     */       case "Color Two":
/*  85 */         color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float)Math.abs((System.currentTimeMillis() / speed / speed + (this.height * 6L / 60L * 2L)) % 2.0D - 1.0D)));
/*     */         break;
/*     */       case "Rainbow":
/*  88 */         color = PaletteHelper.rainbow(300, 1.0F, 1.0F);
/*     */         break;
/*     */       case "Static":
/*  91 */         color = onecolor;
/*     */         break;
/*     */     } 
/*  94 */     Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
/*  95 */     if (ClickGui.background.getBoolValue()) {
/*  96 */       drawDefaultBackground();
/*  97 */       drawGradientRect(0.0F, 0.0F, this.width, this.height, color1.getRGB(), color1.brighter().getRGB());
/*     */     } 
/*     */     
/* 100 */     GL11.glPushMatrix();
/* 101 */     GL11.glTranslatef(sr.getScaledWidth() / 2.0F, sr.getScaledHeight() / 2.0F, 0.0F);
/* 102 */     GL11.glScaled((this.screenHelper.getX() / sr.getScaledWidth()), (this.screenHelper.getY() / sr.getScaledHeight()), 1.0D);
/* 103 */     GL11.glTranslatef(-sr.getScaledWidth() / 2.0F, -sr.getScaledHeight() / 2.0F, 0.0F);
/*     */     
/* 105 */     for (Panel panel : this.panels) {
/* 106 */       if (!this.usingSetting) {
/* 107 */         updateMouseWheel();
/*     */       }
/* 109 */       panel.drawScreen(mouseX, mouseY);
/*     */     } 
/*     */     
/* 112 */     search.drawTextBox();
/*     */     
/* 114 */     RectHelper.drawGradientRect((this.width - this.mc.fontRenderer.getStringWidth("Search Feature...") - 90), (sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 92), (sr.getScaledWidth() - 10), (sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 90), color.getRGB(), color.darker().getRGB());
/*     */     
/* 116 */     if (search.getText().isEmpty() && !search.isFocused()) {
/* 117 */       this.mc.fontRenderer.drawStringWithShadow("Search Feature...", (this.width - this.mc.fontRenderer.getStringWidth("Search Feature...") - 50), (sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 84), -1);
/*     */     }
/*     */     
/* 120 */     for (ImageButton imageButton : this.imageButtons) {
/* 121 */       imageButton.draw(mouseX, mouseY, Color.WHITE);
/* 122 */       if (Mouse.isButtonDown(0)) {
/* 123 */         imageButton.onClick(mouseX, mouseY);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 128 */     GL11.glPopMatrix();
/* 129 */     if (this.usingSetting) {
/* 130 */       for (Panel panel : this.panels) {
/*     */         
/* 132 */         for (FeaturePanel featurePanel : panel.featurePanel) {
/* 133 */           if (featurePanel.usingSettings) {
/* 134 */             this.screenHelperSetting.interpolate(sr.getScaledWidth(), sr.getScaledHeight(), 8.0D);
/*     */             
/* 136 */             GlStateManager.pushMatrix();
/* 137 */             GlStateManager.translate(sr.getScaledWidth() / 2.0F, sr.getScaledHeight() / 2.0F, 0.0F);
/* 138 */             GlStateManager.scale(this.screenHelperSetting.getX() / sr.getScaledWidth(), this.screenHelperSetting.getY() / sr.getScaledHeight(), 1.0F);
/* 139 */             GlStateManager.translate(-sr.getScaledWidth() / 2.0F, -sr.getScaledHeight() / 2.0F, 0.0F);
/*     */             
/* 141 */             RectHelper.drawRoundedRect((sr.getScaledWidth() / 2.0F - 151.0F), 49.0D, 302.0D, (sr.getScaledHeight() - 98), 4.0F, new Color(48, 48, 48, 255));
/* 142 */             RectHelper.drawRoundedRect((sr.getScaledWidth() / 2.0F - 150.0F), 50.0D, 300.0D, (sr.getScaledHeight() - 100), 4.0F, new Color(17, 17, 17));
/*     */             
/* 144 */             RectHelper.drawRoundedRect((sr.getScaledWidth() / 2 - 130), 74.0D, 262.0D, 1.0D, 0.0F, new Color(49, 48, 48));
/*     */             
/* 146 */             Helper.mc.circleregular.drawStringWithOutline(featurePanel.feature.getLabel() + " Settings", (sr.getScaledWidth() / 2.0F - 37.0F), 56.0D, Color.gray.getRGB());
/*     */             
/* 148 */             GlStateManager.pushMatrix();
/* 149 */             GlStateManager.enable(3089);
/* 150 */             RenderHelper.scissorRect(sr.getScaledWidth() / 2.0F - 150.0F, 76.0F, sr.getScaledWidth() / 2.0F + 155.0F, (sr.getScaledHeight() - 47));
/*     */             
/* 152 */             if (featurePanel.yOffset + 25 > sr.getScaledHeight() - 150) {
/* 153 */               this.scrollbar.setInformation(sr.getScaledWidth() / 2.0F - 147.0F, 76.0F, (sr.getScaledHeight() - 53 - 125), featurePanel.scrollY, featurePanel.yOffset - sr.getScaledHeight() / 2.0F - 125.0F, 0.0F);
/* 154 */               this.scrollbar.drawScrollBar();
/*     */             } 
/*     */             
/* 157 */             for (Component component : featurePanel.components) {
/* 158 */               if (component.setting.isVisible()) {
/* 159 */                 component.drawScreen(mouseX, mouseY);
/*     */               }
/*     */             } 
/*     */             
/* 163 */             for (KeybindButton keybindButton : featurePanel.keybindButtons) {
/* 164 */               keybindButton.drawScreen();
/*     */             }
/*     */             
/* 167 */             GlStateManager.disable(3089);
/* 168 */             GlStateManager.popMatrix();
/*     */             
/* 170 */             GlStateManager.popMatrix();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 175 */       this.screenHelperSetting = new ScreenHelper(0.0F, 0.0F);
/*     */     } 
/* 177 */     if (this.exit) {
/* 178 */       this.screenHelper.interpolate(0.0F, 0.0F, 2.0D);
/* 179 */       if (this.screenHelper.getY() < 300.0F) {
/* 180 */         this.exit = false;
/* 181 */         this.mc.displayGuiScreen(null);
/* 182 */         if (this.mc.currentScreen == null) {
/* 183 */           this.mc.setIngameFocus();
/*     */         }
/*     */       } 
/*     */     } 
/* 187 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void updateMouseWheel() {
/* 191 */     int scrollWheel = Mouse.getDWheel();
/* 192 */     for (Panel panel : this.panels) {
/* 193 */       if (scrollWheel > 0) {
/* 194 */         panel.setY(panel.getY() + 15);
/*     */       }
/* 196 */       if (scrollWheel < 0) {
/* 197 */         panel.setY(panel.getY() - 15);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 205 */     search.setFocused(false);
/* 206 */     search.setText("");
/* 207 */     search.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     
/* 209 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*     */     
/* 211 */     for (Panel panel : this.panels) {
/* 212 */       if (panel.isWithinHeader(mouseX, mouseY) && mouseButton == 1 && !this.usingSetting) {
/* 213 */         panel.setOpen(!panel.isOpen());
/*     */       }
/* 215 */       panel.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     } 
/*     */     
/* 218 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 223 */     for (Panel panel : this.panels) {
/* 224 */       panel.mouseReleased(mouseX, mouseY, state);
/*     */     }
/* 226 */     super.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 231 */     for (Panel panel : this.panels) {
/* 232 */       for (FeaturePanel featurePanel : panel.featurePanel) {
/* 233 */         if (this.usingSetting && featurePanel.usingSettings) {
/* 234 */           featurePanel.keyTyped(typedChar, keyCode);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 240 */     search.textboxKeyTyped(typedChar, keyCode);
/*     */     
/* 242 */     if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
/* 243 */       search.setFocused(!search.isFocused());
/*     */     }
/*     */ 
/*     */     
/* 247 */     if (keyCode == 1 && !this.usingSetting) {
/* 248 */       this.exit = true;
/*     */     }
/*     */     
/* 251 */     if (this.exit) {
/*     */       return;
/*     */     }
/* 254 */     super.keyTyped(typedChar, keyCode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initGui() {
/* 260 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*     */     
/* 262 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 263 */     this.imageButtons.clear();
/* 264 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/brush.png"), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("Welcome") - 70, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 18));
/* 265 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/config.png"), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("Welcome") - 19, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 22));
/* 266 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/capeicon.png"), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("Welcome") - 115, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 23));
/* 267 */     search = new GuiSearcher(1337, this.mc.fontRendererObj, this.width - this.mc.fontRenderer.getStringWidth("Search Feature...") - 90, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 90, 150, 18);
/*     */     
/* 269 */     if (ClickGui.blur.getBoolValue()) {
/* 270 */       this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
/*     */     }
/*     */     
/* 273 */     super.initGui();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\newclickgui\ClickGuiScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */