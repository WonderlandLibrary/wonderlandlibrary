/*     */ package org.neverhook.client.ui.components.draggable.impl;
/*     */ 
/*     */ import com.mojang.realmsclient.gui.ChatFormatting;
/*     */ import java.awt.Color;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.impl.hud.HUD;
/*     */ import org.neverhook.client.feature.impl.hud.WaterMark;
/*     */ import org.neverhook.client.feature.impl.misc.Disabler;
/*     */ import org.neverhook.client.feature.impl.misc.StreamerMode;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.components.draggable.DraggableModule;
/*     */ import org.neverhook.security.utils.LicenseUtil;
/*     */ 
/*     */ public class WaterMarkComponent
/*     */   extends DraggableModule
/*     */ {
/*     */   public WaterMarkComponent() {
/*  25 */     super("LogoComponent", 4, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  30 */     return 135;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  35 */     return 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(int mouseX, int mouseY) {
/*  40 */     if (!mc.gameSettings.showDebugInfo) {
/*     */       int i;
/*  42 */       String mode = WaterMark.logoMode.getCurrentMode();
/*  43 */       Color color = Color.BLACK;
/*  44 */       switch (WaterMark.logoColor.currentMode) {
/*     */         case "Gradient":
/*  46 */           for (i = this.x; i < this.x + getWidth(); i++) {
/*  47 */             color = new Color(PaletteHelper.fadeColor(WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue(), i));
/*     */           }
/*     */           break;
/*     */         
/*     */         case "Client":
/*  52 */           color = ClientHelper.getClientColor();
/*     */           break;
/*     */         
/*     */         case "Rainbow":
/*  56 */           color = PaletteHelper.rainbow(300, 1.0F, 1.0F);
/*     */           break;
/*     */         
/*     */         case "Default":
/*  60 */           color = WaterMark.logoMode.currentMode.equals("OneTap v2") ? new Color(161, 0, 255) : Color.RED;
/*     */           break;
/*     */       } 
/*  63 */       if (mode.equalsIgnoreCase("Default")) {
/*  64 */         if (!HUD.font.currentMode.equals("Minecraft")) {
/*  65 */           mc.robotoRegularFontRender.drawStringWithOutline("N", getX(), (getY() + 1), color.getRGB());
/*  66 */           mc.robotoRegularFontRender.drawStringWithOutline("ever", (getX() + 7), (getY() + 1), -1);
/*  67 */           mc.robotoRegularFontRender.drawStringWithOutline("H", (getX() + 27), (getY() + 1), color.getRGB());
/*  68 */           mc.robotoRegularFontRender.drawStringWithOutline("ook", (getX() + 34), (getY() + 1), -1);
/*     */         } else {
/*  70 */           mc.fontRendererObj.drawStringWithOutline("N", getX(), (getY() + 1), color.getRGB());
/*  71 */           mc.fontRendererObj.drawStringWithOutline("ever", (getX() + 6), (getY() + 1), -1);
/*  72 */           mc.fontRendererObj.drawStringWithOutline("H", (getX() + 30), (getY() + 1), color.getRGB());
/*  73 */           mc.fontRendererObj.drawStringWithOutline("ook", (getX() + 36), (getY() + 1), -1);
/*     */         } 
/*  75 */       } else if (mode.equalsIgnoreCase("Skeet")) {
/*  76 */         String server; int j; if (mc.isSingleplayer()) {
/*  77 */           server = "localhost";
/*     */         } else {
/*  79 */           server = (mc.getCurrentServerData()).serverIP.toLowerCase();
/*     */         } 
/*  81 */         String text = "never" + ChatFormatting.GREEN + "hook" + ChatFormatting.RESET + " | " + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? (ChatFormatting.LIGHT_PURPLE + "Protected" + ChatFormatting.RESET) : (ChatFormatting.RESET + LicenseUtil.userName)) + " | " + Minecraft.getDebugFPS() + "fps | " + server;
/*  82 */         float width = (mc.fontRenderer.getStringWidth(text) + 6);
/*  83 */         int height = 20;
/*  84 */         int posX = getX();
/*  85 */         int posY = getY();
/*  86 */         RectHelper.drawRect(posX, posY, (posX + width + 2.0F), (posY + height), (new Color(5, 5, 5, 255)).getRGB());
/*  87 */         RectHelper.drawBorderedRect(posX + 0.5F, posY + 0.5F, posX + width + 1.5F, (posY + height) - 0.5F, 0.5F, (new Color(40, 40, 40, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
/*  88 */         RectHelper.drawBorderedRect((posX + 2), (posY + 2), posX + width, (posY + height - 2), 0.5F, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
/*     */         
/*  90 */         switch (WaterMark.logoColor.currentMode) {
/*     */           case "Default":
/*  92 */             RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), posX + 2.5F, posY + 2.5F, width - 3.0F, 1.0F, Color.WHITE);
/*     */             break;
/*     */           
/*     */           case "Custom":
/*  96 */             RectHelper.drawGradientRect(posX + 2.75D, (posY + 3), (getX() + width - 1.0F), (posY + 4), (new Color(WaterMark.customRectTwo.getColorValue())).getRGB(), color.getRGB());
/*     */             break;
/*     */           
/*     */           case "Client":
/* 100 */             RectHelper.drawRect((posX + 3), (posY + 3), (getX() + width) - 0.7D, (posY + 4), color.getRGB());
/*     */ 
/*     */           
/*     */           case "Rainbow":
/* 104 */             for (j = this.x + 3; j < this.x + width; j++) {
/* 105 */               Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 106 */               RectHelper.drawRect(j, (posY + 3), (getX() + width) - 0.7D, (posY + 4), rainbow.getRGB());
/*     */             } 
/*     */           
/*     */           case "Gradient":
/* 110 */             RectHelper.drawGradientRect((this.x + 3), (posY + 3), (getX() + width) - 0.7D, (posY + 4), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */           
/*     */           case "Static":
/* 113 */             RectHelper.drawRect((this.x + 3), (posY + 3), (getX() + width) - 0.7D, (posY + 4), WaterMark.customRect.getColorValue());
/*     */             break;
/*     */         } 
/* 116 */         mc.fontRenderer.drawStringWithShadow(text, (posX + 4), (posY + 7), -1);
/* 117 */       } else if (mode.equalsIgnoreCase("OneTap v2")) {
/* 118 */         String server; if (mc.isSingleplayer()) {
/* 119 */           server = "localhost";
/*     */         } else {
/* 121 */           server = (mc.getCurrentServerData()).serverIP.toLowerCase();
/*     */         } 
/* 123 */         String text = "neverhook.pub | " + NeverHook.instance.name + " | " + server + " | 64 tick | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
/* 124 */         float width = (mc.fontRenderer.getStringWidth(text) + 4);
/*     */         
/* 126 */         if (WaterMark.glowEffect.getBoolValue()) {
/* 127 */           if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
/* 128 */             RectHelper.drawSmoothGradientRect(getX(), (getY() - 3), (getX() + width), (getY() - 0.5F), color.getRGB(), WaterMark.customRectTwo.getColorValue());
/*     */           } else {
/* 130 */             RectHelper.drawSmoothGradientRect(getX(), getY(), (getX() + width), (getY() + 12), color.getRGB(), WaterMark.customRectTwo.getColorValue());
/*     */           } 
/*     */         }
/*     */         
/* 134 */         if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
/* 135 */           int j; switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 138 */               for (j = this.x; j < this.x + width; j++) {
/* 139 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 140 */                 RectHelper.drawSmoothRect(j, (getY() - 3), getX() + width, getY() - 1.5F, rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 144 */               RectHelper.drawSmoothGradientRect(getX(), (getY() - 3), (getX() + width), (getY() - 0.5F), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 147 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 150 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, color.getRGB()); break;
/*     */           } 
/*     */         } else {
/*     */           int j;
/* 154 */           switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 157 */               for (j = this.x; j < this.x + width; j++) {
/* 158 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 159 */                 RectHelper.drawSmoothRect(j, getY(), getX() + width, (getY() + 12), rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 163 */               RectHelper.drawSmoothGradientRect(getX(), getY(), (getX() + width), (getY() + 12), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 166 */               RectHelper.drawSmoothRect(getX(), getY(), getX() + width, (getY() + 12), WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 169 */               RectHelper.drawSmoothRect(getX(), getY(), getX() + width, (getY() + 12), color.getRGB());
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/* 174 */         RectHelper.drawSmoothRect(getX(), (getY() - 1), getX() + width, (getY() + 10), (new Color(47, 47, 47)).getRGB());
/*     */         
/* 176 */         mc.fontRenderer.drawStringWithShadow(text, (getX() + 2), (getY() + 1), -1);
/*     */         
/* 178 */         mc.fontRenderer.drawStringWithShadow(text, (getX() + 2), (getY() + 1), -1);
/* 179 */       } else if (mode.equalsIgnoreCase("OneTap v3")) {
/* 180 */         String server; if (mc.isSingleplayer()) {
/* 181 */           server = "localhost";
/*     */         } else {
/* 183 */           server = (mc.getCurrentServerData()).serverIP.toLowerCase();
/*     */         } 
/* 185 */         String text = "neverhook.pub | " + NeverHook.instance.name + " | " + server + " | Fps " + Minecraft.getDebugFPS() + " | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
/* 186 */         float width = (mc.fontRenderer.getStringWidth(text) + 4);
/*     */         
/* 188 */         if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
/* 189 */           int j; switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 192 */               for (j = this.x; j < this.x + width; j++) {
/* 193 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 194 */                 RectHelper.drawSmoothRect(j, (getY() - 3), getX() + width, getY() - 1.5F, rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 198 */               RectHelper.drawSmoothGradientRect(getX(), (getY() - 3), (getX() + width), (getY() - 0.5F), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 201 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 204 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, color.getRGB()); break;
/*     */           } 
/*     */         } else {
/*     */           int j;
/* 208 */           switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 211 */               for (j = this.x; j < this.x + width; j++) {
/* 212 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 213 */                 RectHelper.drawSmoothRect(j, (getY() + 10), getX() + width, (getY() + 12), rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 217 */               RectHelper.drawSmoothGradientRect(getX(), (getY() + 10), (getX() + width), (getY() + 12), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 220 */               RectHelper.drawSmoothRect(getX(), (getY() + 10), getX() + width, (getY() + 12), WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 223 */               RectHelper.drawSmoothRect(getX(), (getY() + 10), getX() + width, (getY() + 12), color.getRGB());
/*     */               break;
/*     */           } 
/*     */           
/* 227 */           RectHelper.drawSmoothRect(getX(), (getY() - 1), getX() + width, (getY() + 10), (new Color(23, 23, 23, 110)).getRGB());
/*     */           
/* 229 */           mc.fontRenderer.drawStringWithShadow(text, (getX() + 2), (getY() + 1), -1);
/*     */         } 
/* 231 */       } else if (mode.equalsIgnoreCase("NeverLose")) {
/* 232 */         String text = ChatFormatting.BOLD + NeverHook.instance.name + ChatFormatting.RESET + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + LicenseUtil.userName + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + (NeverHook.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : ((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(mc.getConnection())).getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + "ms" + ChatFormatting.GRAY;
/* 233 */         float width = (mc.clickguismall.getStringWidth(text) + 1);
/* 234 */         RectHelper.drawSmoothRect(getX(), (getY() - 2), getX() + width + 3.0F, (getY() + 11), (new Color(0, 0, 28)).getRGB());
/* 235 */         mc.clickguismall.drawStringWithShadow(text, (getX() + 2), (getY() + 2), -1);
/* 236 */         mc.entityRenderer.setupOverlayRendering();
/* 237 */       } else if (mode.equals("NoRender")) {
/*     */         int ping;
/*     */         
/* 240 */         if (mc.isSingleplayer()) {
/* 241 */           ping = 0;
/*     */         } else {
/* 243 */           ping = (int)(mc.getCurrentServerData()).pingToServer;
/*     */         } 
/*     */         
/* 246 */         mc.fontRenderer.drawStringWithShadow("NeverHook §7v" + NeverHook.instance.version + " §7[§f" + Minecraft.getDebugFPS() + " FPS§7] §7[§f" + ping + " PING§7]", getX(), getY(), color.getRGB());
/*     */       } 
/*     */     } 
/* 249 */     super.render(mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw() {
/* 254 */     if (!mc.gameSettings.showDebugInfo) {
/*     */       int i;
/* 256 */       String mode = WaterMark.logoMode.getCurrentMode();
/* 257 */       Color color = Color.BLACK;
/* 258 */       switch (WaterMark.logoColor.currentMode) {
/*     */         case "Gradient":
/* 260 */           for (i = this.x; i < this.x + getWidth(); i++) {
/* 261 */             color = new Color(PaletteHelper.fadeColor(WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue(), i));
/*     */           }
/*     */           break;
/*     */         
/*     */         case "Client":
/* 266 */           color = ClientHelper.getClientColor();
/*     */           break;
/*     */         
/*     */         case "Rainbow":
/* 270 */           color = PaletteHelper.rainbow(300, 1.0F, 1.0F);
/*     */           break;
/*     */         
/*     */         case "Default":
/* 274 */           color = WaterMark.logoMode.currentMode.equals("OneTap v2") ? new Color(161, 0, 255) : Color.RED;
/*     */           break;
/*     */       } 
/* 277 */       if (mode.equalsIgnoreCase("Default")) {
/* 278 */         if (!HUD.font.currentMode.equals("Minecraft")) {
/* 279 */           mc.robotoRegularFontRender.drawStringWithOutline("N", getX(), (getY() + 1), color.getRGB());
/* 280 */           mc.robotoRegularFontRender.drawStringWithOutline("ever", (getX() + 7), (getY() + 1), -1);
/* 281 */           mc.robotoRegularFontRender.drawStringWithOutline("H", (getX() + 27), (getY() + 1), color.getRGB());
/* 282 */           mc.robotoRegularFontRender.drawStringWithOutline("ook", (getX() + 34), (getY() + 1), -1);
/*     */         } else {
/* 284 */           mc.fontRendererObj.drawStringWithOutline("N", getX(), (getY() + 1), color.getRGB());
/* 285 */           mc.fontRendererObj.drawStringWithOutline("ever", (getX() + 6), (getY() + 1), -1);
/* 286 */           mc.fontRendererObj.drawStringWithOutline("H", (getX() + 30), (getY() + 1), color.getRGB());
/* 287 */           mc.fontRendererObj.drawStringWithOutline("ook", (getX() + 36), (getY() + 1), -1);
/*     */         } 
/* 289 */       } else if (mode.equalsIgnoreCase("Skeet")) {
/* 290 */         String server; int j; if (mc.isSingleplayer()) {
/* 291 */           server = "localhost";
/*     */         } else {
/* 293 */           server = (mc.getCurrentServerData()).serverIP.toLowerCase();
/*     */         } 
/* 295 */         String text = "never" + ChatFormatting.GREEN + "hook" + ChatFormatting.RESET + " | " + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? (ChatFormatting.LIGHT_PURPLE + "Protected" + ChatFormatting.RESET) : (ChatFormatting.RESET + LicenseUtil.userName)) + " | " + Minecraft.getDebugFPS() + "fps | " + server;
/* 296 */         float width = (mc.fontRenderer.getStringWidth(text) + 6);
/* 297 */         int height = 20;
/* 298 */         int posX = getX();
/* 299 */         int posY = getY();
/* 300 */         RectHelper.drawRect(posX, posY, (posX + width + 2.0F), (posY + height), (new Color(5, 5, 5, 255)).getRGB());
/* 301 */         RectHelper.drawBorderedRect(posX + 0.5F, posY + 0.5F, posX + width + 1.5F, (posY + height) - 0.5F, 0.5F, (new Color(40, 40, 40, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
/* 302 */         RectHelper.drawBorderedRect((posX + 2), (posY + 2), posX + width, (posY + height - 2), 0.5F, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
/*     */         
/* 304 */         switch (WaterMark.logoColor.currentMode) {
/*     */           case "Default":
/* 306 */             RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), posX + 2.5F, posY + 2.5F, width - 3.0F, 1.0F, Color.WHITE);
/*     */             break;
/*     */           
/*     */           case "Custom":
/* 310 */             RectHelper.drawGradientRect(posX + 2.75D, (posY + 3), (getX() + width - 1.0F), (posY + 4), (new Color(WaterMark.customRectTwo.getColorValue())).getRGB(), color.getRGB());
/*     */             break;
/*     */           
/*     */           case "Client":
/* 314 */             RectHelper.drawRect((posX + 3), (posY + 3), (getX() + width) - 0.7D, (posY + 4), color.getRGB());
/*     */ 
/*     */           
/*     */           case "Rainbow":
/* 318 */             for (j = this.x + 3; j < this.x + width; j++) {
/* 319 */               Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 320 */               RectHelper.drawRect(j, (posY + 3), (getX() + width) - 0.7D, (posY + 4), rainbow.getRGB());
/*     */             } 
/*     */           
/*     */           case "Gradient":
/* 324 */             RectHelper.drawGradientRect((this.x + 3), (posY + 3), (getX() + width) - 0.7D, (posY + 4), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */           
/*     */           case "Static":
/* 327 */             RectHelper.drawRect((this.x + 3), (posY + 3), (getX() + width) - 0.7D, (posY + 4), WaterMark.customRect.getColorValue());
/*     */             break;
/*     */         } 
/* 330 */         mc.fontRenderer.drawStringWithShadow(text, (posX + 4), (posY + 7), -1);
/* 331 */       } else if (mode.equalsIgnoreCase("OneTap v2")) {
/* 332 */         String server; if (mc.isSingleplayer()) {
/* 333 */           server = "localhost";
/*     */         } else {
/* 335 */           server = (mc.getCurrentServerData()).serverIP.toLowerCase();
/*     */         } 
/* 337 */         String text = "neverhook.pub | " + NeverHook.instance.name + " | " + server + " | 64 tick | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
/* 338 */         float width = (mc.fontRenderer.getStringWidth(text) + 4);
/*     */         
/* 340 */         if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
/* 341 */           int j; switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 344 */               for (j = this.x; j < this.x + width; j++) {
/* 345 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 346 */                 RectHelper.drawSmoothRect(j, (getY() - 3), getX() + width, getY() - 1.5F, rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 350 */               RectHelper.drawSmoothGradientRect(getX(), (getY() - 3), (getX() + width), (getY() - 0.5F), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 353 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 356 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, color.getRGB()); break;
/*     */           } 
/*     */         } else {
/*     */           int j;
/* 360 */           switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 363 */               for (j = this.x; j < this.x + width; j++) {
/* 364 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 365 */                 RectHelper.drawSmoothRect(j, getY(), getX() + width, (getY() + 12), rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 369 */               RectHelper.drawSmoothGradientRect(getX(), getY(), (getX() + width), (getY() + 12), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 372 */               RectHelper.drawSmoothRect(getX(), getY(), getX() + width, (getY() + 12), WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 375 */               RectHelper.drawSmoothRect(getX(), getY(), getX() + width, (getY() + 12), color.getRGB());
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/* 380 */         RectHelper.drawSmoothRect(getX(), (getY() - 1), getX() + width, (getY() + 10), (new Color(47, 47, 47)).getRGB());
/* 381 */         mc.fontRenderer.drawStringWithShadow(text, (getX() + 2), (getY() + 1), -1);
/* 382 */       } else if (mode.equalsIgnoreCase("OneTap v3")) {
/* 383 */         String server; if (mc.isSingleplayer()) {
/* 384 */           server = "localhost";
/*     */         } else {
/* 386 */           server = (mc.getCurrentServerData()).serverIP.toLowerCase();
/*     */         } 
/* 388 */         String text = "neverhook.pub | " + NeverHook.instance.name + " | " + server + " | Fps " + Minecraft.getDebugFPS() + " | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
/* 389 */         float width = (mc.fontRenderer.getStringWidth(text) + 4);
/*     */ 
/*     */         
/* 392 */         if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
/* 393 */           int j; switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 396 */               for (j = this.x; j < this.x + width; j++) {
/* 397 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 398 */                 RectHelper.drawSmoothRect(j, (getY() - 3), getX() + width, getY() - 1.5F, rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 402 */               RectHelper.drawSmoothGradientRect(getX(), (getY() - 3), (getX() + width), (getY() - 0.5F), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 405 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 408 */               RectHelper.drawSmoothRect(getX(), (getY() - 3), getX() + width, getY() - 0.5F, color.getRGB()); break;
/*     */           } 
/*     */         } else {
/*     */           int j;
/* 412 */           switch (WaterMark.logoColor.currentMode) {
/*     */             
/*     */             case "Rainbow":
/* 415 */               for (j = this.x; j < this.x + width; j++) {
/* 416 */                 Color rainbow = PaletteHelper.rainbow(j * 15, 0.5F, 1.0F);
/* 417 */                 RectHelper.drawSmoothRect(j, (getY() + 10), getX() + width, (getY() + 12), rainbow.getRGB());
/*     */               } 
/*     */               break;
/*     */             case "Gradient":
/* 421 */               RectHelper.drawSmoothGradientRect(getX(), (getY() + 10), (getX() + width), (getY() + 12), WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
/*     */               break;
/*     */             case "Static":
/* 424 */               RectHelper.drawSmoothRect(getX(), (getY() + 10), getX() + width, (getY() + 12), WaterMark.customRect.getColorValue());
/*     */               break;
/*     */             case "Default":
/* 427 */               RectHelper.drawSmoothRect(getX(), (getY() + 10), getX() + width, (getY() + 12), color.getRGB());
/*     */               break;
/*     */           } 
/*     */ 
/*     */           
/* 432 */           RectHelper.drawSmoothRect(getX(), (getY() - 1), getX() + width, (getY() + 10), (new Color(23, 23, 23, 110)).getRGB());
/*     */           
/* 434 */           mc.fontRenderer.drawStringWithShadow(text, (getX() + 2), (getY() + 1), -1);
/*     */         } 
/* 436 */       } else if (mode.equalsIgnoreCase("NeverLose")) {
/* 437 */         String text = ChatFormatting.BOLD + NeverHook.instance.name + ChatFormatting.RESET + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + LicenseUtil.userName + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + (NeverHook.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : ((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(mc.getConnection())).getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + "ms" + ChatFormatting.GRAY;
/* 438 */         float width = (mc.clickguismall.getStringWidth(text) + 1);
/* 439 */         RectHelper.drawSmoothRect(getX(), (getY() - 2), getX() + width + 3.0F, (getY() + 11), (new Color(0, 0, 28)).getRGB());
/* 440 */         mc.clickguismall.drawStringWithShadow(text, (getX() + 2), (getY() + 2), -1);
/* 441 */         mc.entityRenderer.setupOverlayRendering();
/* 442 */       } else if (mode.equals("NoRender")) {
/*     */         int ping;
/*     */         
/* 445 */         if (mc.isSingleplayer()) {
/* 446 */           ping = 0;
/*     */         } else {
/* 448 */           ping = (int)(mc.getCurrentServerData()).pingToServer;
/*     */         } 
/*     */         
/* 451 */         mc.fontRenderer.drawStringWithShadow("NeverHook §7v" + NeverHook.instance.version + " §7[§f" + Minecraft.getDebugFPS() + " FPS§7] §7[§f" + ping + " PING§7]", getX(), getY(), color.getRGB());
/*     */       } 
/*     */     } 
/* 454 */     super.draw();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\draggable\impl\WaterMarkComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */