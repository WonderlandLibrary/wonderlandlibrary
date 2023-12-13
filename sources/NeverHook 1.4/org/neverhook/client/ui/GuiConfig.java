/*     */ package org.neverhook.client.ui;
/*     */ 
/*     */ import com.mojang.realmsclient.gui.ChatFormatting;
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.helpers.input.MouseHelper;
/*     */ import org.neverhook.client.helpers.misc.ChatHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.ScreenHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.settings.config.Config;
/*     */ import org.neverhook.client.settings.config.ConfigManager;
/*     */ import org.neverhook.client.ui.button.ConfigGuiButton;
/*     */ import org.neverhook.client.ui.button.ImageButton;
/*     */ import org.neverhook.client.ui.font.MCFontRenderer;
/*     */ import org.neverhook.client.ui.notification.NotificationManager;
/*     */ import org.neverhook.client.ui.notification.NotificationType;
/*     */ 
/*     */ public class GuiConfig
/*     */   extends GuiScreen
/*     */ {
/*     */   public static GuiTextField search;
/*  34 */   public static Config selectedConfig = null;
/*     */   public ScreenHelper screenHelper;
/*  36 */   protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
/*     */   
/*     */   private int width;
/*     */   
/*     */   public GuiConfig() {
/*  41 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*     */   }
/*     */   private int height; private float scrollOffset;
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  46 */     if (button.id == 1) {
/*  47 */       NeverHook.instance.configManager.saveConfig(search.getText());
/*  48 */       ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + search.getText() + "\"");
/*  49 */       NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "created config: " + ChatFormatting.RED + "\"" + search.getText() + "\"", 4, NotificationType.SUCCESS);
/*  50 */       ConfigManager.getLoadedConfigs().clear();
/*  51 */       NeverHook.instance.configManager.load();
/*  52 */       search.setFocused(false);
/*  53 */       search.setText("");
/*     */     } 
/*  55 */     if (selectedConfig != null) {
/*  56 */       if (button.id == 2) {
/*  57 */         if (NeverHook.instance.configManager.loadConfig(selectedConfig.getName())) {
/*  58 */           ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
/*  59 */           NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
/*     */         } else {
/*  61 */           ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
/*  62 */           NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
/*     */         } 
/*  64 */       } else if (button.id == 3) {
/*  65 */         if (NeverHook.instance.configManager.saveConfig(selectedConfig.getName())) {
/*  66 */           ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
/*  67 */           NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
/*  68 */           ConfigManager.getLoadedConfigs().clear();
/*  69 */           NeverHook.instance.configManager.load();
/*     */         } else {
/*  71 */           ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + search.getText() + "\"");
/*  72 */           NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + search.getText() + "\"", 4, NotificationType.ERROR);
/*     */         } 
/*  74 */       } else if (button.id == 4) {
/*  75 */         if (NeverHook.instance.configManager.deleteConfig(selectedConfig.getName())) {
/*  76 */           ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
/*  77 */           NotificationManager.publicity("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
/*     */         } else {
/*  79 */           ChatHelper.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"");
/*  80 */           NotificationManager.publicity("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
/*     */         } 
/*     */       } 
/*     */     }
/*  84 */     super.actionPerformed(button);
/*     */   }
/*     */   
/*     */   private boolean isHoveredConfig(int x, int y, int width, int height, int mouseX, int mouseY) {
/*  88 */     return MouseHelper.isHovered(x, y, (x + width), (y + height), mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  93 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*  94 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*  95 */     this.width = sr.getScaledWidth() / 2;
/*  96 */     this.height = sr.getScaledHeight() / 2;
/*  97 */     search = new GuiTextField(228, this.mc.fontRendererObj, this.width - 100, this.height + 62, 85, 13);
/*  98 */     this.buttonList.add(new ConfigGuiButton(1, this.width - 105, this.height + 102, "Create"));
/*  99 */     this.buttonList.add(new ConfigGuiButton(2, this.width - 40, this.height - 48, "Load"));
/* 100 */     this.buttonList.add(new ConfigGuiButton(3, this.width - 40, this.height - 65, "Save"));
/* 101 */     this.buttonList.add(new ConfigGuiButton(4, this.width - 40, this.height - 82, "Delete"));
/* 102 */     this.imageButtons.clear();
/* 103 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/close-button.png"), this.width + 106, this.height - 135, 8, 8, "", 19));
/* 104 */     super.initGui();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 109 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 110 */     drawWorldBackground(0);
/* 111 */     this.screenHelper.interpolate(sr.getScaledWidth(), sr.getScaledHeight(), 6.0D);
/* 112 */     GL11.glPushMatrix();
/* 113 */     for (Config config : NeverHook.instance.configManager.getContents()) {
/* 114 */       if (config != null && 
/* 115 */         Mouse.hasWheel() && 
/* 116 */         isHoveredConfig(this.width - 100, this.height - 122, 151, this.height + 59, mouseX, mouseY)) {
/* 117 */         int wheel = Mouse.getDWheel();
/* 118 */         if (wheel < 0) {
/* 119 */           this.scrollOffset += 13.0F;
/* 120 */           if (this.scrollOffset < 0.0F)
/* 121 */             this.scrollOffset = 0.0F;  continue;
/*     */         } 
/* 123 */         if (wheel > 0) {
/* 124 */           this.scrollOffset -= 13.0F;
/* 125 */           if (this.scrollOffset < 0.0F) {
/* 126 */             this.scrollOffset = 0.0F;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 133 */     GlStateManager.pushMatrix();
/* 134 */     RectHelper.drawSkeetRectWithoutBorder((this.width - 70), (this.height - 80), (this.width + 80), (this.height + 20));
/* 135 */     RectHelper.drawSkeetButton((this.width - 70), (this.height - 80), (this.width + 20), (this.height + 90));
/* 136 */     RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), (this.width - 110), (this.height - 140), 230.0F, 1.0F, Color.WHITE);
/* 137 */     this.mc.circleregular.drawStringWithOutline("Config System", (this.width - 100), (this.height - 135), -1);
/* 138 */     search.drawTextBox();
/* 139 */     if (search.getText().isEmpty() && !search.isFocused()) {
/* 140 */       MCFontRenderer.drawStringWithOutline(this.mc.fontRendererObj, "...", (this.width - 97), (this.height + 65), PaletteHelper.getColor(200));
/*     */     }
/* 142 */     for (ImageButton imageButton : this.imageButtons) {
/* 143 */       imageButton.draw(mouseX, mouseY, Color.WHITE);
/* 144 */       if (Mouse.isButtonDown(0)) {
/* 145 */         imageButton.onClick(mouseX, mouseY);
/*     */       }
/*     */     } 
/* 148 */     int yDist = 0;
/*     */     
/* 150 */     GL11.glEnable(3089);
/* 151 */     RenderHelper.scissorRect(0.0F, (this.height - 119), this.width, (this.height + 60));
/* 152 */     for (Config config : NeverHook.instance.configManager.getContents()) {
/* 153 */       if (config != null) {
/* 154 */         int color; if (isHoveredConfig(this.width - 96, (int)((this.height - 117 + yDist) - this.scrollOffset), this.mc.fontRendererObj.getStringWidth(config.getName()) + 5, 14, mouseX, mouseY)) {
/* 155 */           color = -1;
/* 156 */           if (Mouse.isButtonDown(0)) {
/* 157 */             selectedConfig = new Config(config.getName());
/*     */           }
/*     */         } else {
/* 160 */           color = PaletteHelper.getColor(200);
/*     */         } 
/* 162 */         if (selectedConfig != null && config.getName().equals(selectedConfig.getName())) {
/* 163 */           RectHelper.drawBorder((this.width - 98), (this.height - 119 + yDist) - this.scrollOffset, (this.width + this.mc.fontRendererObj.getStringWidth(config.getName()) - 90), (this.height - 107 + yDist) - this.scrollOffset, 0.65F, (new Color(255, 255, 255, 75)).getRGB(), (new Color(0, 0, 0, 255)).getRGB(), true);
/*     */         }
/* 165 */         this.mc.fontRendererObj.drawStringWithOutline(config.getName(), (this.width - 95), (this.height - 117 + yDist) - this.scrollOffset, color);
/* 166 */         yDist += 15;
/*     */       } 
/*     */     } 
/* 169 */     GL11.glDisable(3089);
/* 170 */     GlStateManager.popMatrix();
/* 171 */     GL11.glPopMatrix();
/* 172 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 177 */     super.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 182 */     search.mouseClicked(mouseX, mouseY, mouseButton);
/* 183 */     if (this.scrollOffset < 0.0F) {
/* 184 */       this.scrollOffset = 0.0F;
/*     */     }
/* 186 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 191 */     for (Config config : NeverHook.instance.configManager.getContents()) {
/* 192 */       if (config != null) {
/* 193 */         if (keyCode == 200) {
/* 194 */           this.scrollOffset += 13.0F;
/* 195 */         } else if (keyCode == 208) {
/* 196 */           this.scrollOffset -= 13.0F;
/*     */         } 
/* 198 */         if (this.scrollOffset < 0.0F) {
/* 199 */           this.scrollOffset = 0.0F;
/*     */         }
/*     */       } 
/*     */     } 
/* 203 */     search.textboxKeyTyped(typedChar, keyCode);
/* 204 */     search.setText(search.getText().replace(" ", ""));
/* 205 */     if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
/* 206 */       search.setFocused(!search.isFocused());
/*     */     }
/*     */     try {
/* 209 */       super.keyTyped(typedChar, keyCode);
/* 210 */     } catch (IOException e) {
/* 211 */       e.printStackTrace();
/*     */     } 
/* 213 */     super.keyTyped(typedChar, keyCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 218 */     selectedConfig = null;
/* 219 */     super.onGuiClosed();
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 223 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 227 */     this.width = width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 231 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 235 */     this.height = height;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\GuiConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */