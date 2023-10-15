/*     */ package org.neverhook.client.ui.clickgui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*     */ import org.neverhook.client.helpers.misc.ClientHelper;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.ScreenHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.button.ImageButton;
/*     */ import org.neverhook.client.ui.clickgui.component.Component;
/*     */ import org.neverhook.client.ui.clickgui.component.ExpandableComponent;
/*     */ 
/*     */ public class ClickGuiScreen
/*     */   extends GuiScreen
/*     */ {
/*     */   public static boolean escapeKeyInUse;
/*     */   public static GuiSearcher search;
/*  29 */   public List<Panel> components = new ArrayList<>();
/*     */   public ScreenHelper screenHelper;
/*     */   public boolean exit = false;
/*     */   public Type type;
/*  33 */   protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
/*     */   private Component selectedPanel;
/*     */   
/*     */   public ClickGuiScreen() {
/*  37 */     int height = 20;
/*  38 */     int x = 20;
/*  39 */     int y = 80;
/*  40 */     for (Type type : Type.values()) {
/*  41 */       this.type = type;
/*  42 */       this.components.add(new Panel(type, x, y));
/*  43 */       this.selectedPanel = (Component)new Panel(type, x, y);
/*  44 */       y += height + 6;
/*     */     } 
/*  46 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  51 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/*  52 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*  53 */     this.imageButtons.clear();
/*  54 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/brush.png"), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("Welcome") - 70, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 18));
/*  55 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/config.png"), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("Welcome") - 19, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 22));
/*  56 */     this.imageButtons.add(new ImageButton(new ResourceLocation("neverhook/capeicon.png"), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("Welcome") - 115, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 23));
/*  57 */     search = new GuiSearcher(1337, this.mc.fontRendererObj, this.width - this.mc.fontRenderer.getStringWidth("Search Feature...") - 90, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 90, 150, 18);
/*     */     
/*  59 */     if (ClickGui.blur.getBoolValue()) {
/*  60 */       this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
/*     */     }
/*     */     
/*  63 */     super.initGui();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  69 */     ScaledResolution sr = new ScaledResolution(this.mc);
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
/*     */       case "Category":
/*  91 */         color = new Color(this.type.getColor());
/*     */         break;
/*     */       case "Static":
/*  94 */         color = onecolor;
/*     */         break;
/*     */     } 
/*  97 */     Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
/*  98 */     if (ClickGui.background.getBoolValue()) {
/*  99 */       drawDefaultBackground();
/* 100 */       drawGradientRect(0.0F, 0.0F, this.width, this.height, color1.getRGB(), color1.brighter().getRGB());
/*     */     } 
/*     */     
/* 103 */     if (!this.exit)
/*     */     {
/*     */       
/* 106 */       this.screenHelper.interpolate(this.width, this.height, 6.0D * Minecraft.frameTime / 6.0D);
/*     */     }
/*     */     
/* 109 */     GlStateManager.pushMatrix();
/* 110 */     GL11.glTranslatef((sr.getScaledWidth() / 2), (sr.getScaledHeight() / 2), 0.0F);
/* 111 */     GL11.glScaled((this.screenHelper.getX() / sr.getScaledWidth()), (this.screenHelper.getY() / sr.getScaledHeight()), 1.0D);
/* 112 */     GL11.glTranslatef(-sr.getScaledWidth() / 2.0F, -sr.getScaledHeight() / 2.0F, 0.0F);
/*     */     
/* 114 */     for (Panel panel : this.components) {
/* 115 */       panel.drawComponent(sr, mouseX, mouseY);
/*     */     }
/*     */     
/* 118 */     search.drawTextBox();
/* 119 */     RectHelper.drawGradientRect((this.width - this.mc.fontRenderer.getStringWidth("Search Feature...") - 90), (sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 92), (sr.getScaledWidth() - 10), (sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 90), color.getRGB(), color.darker().getRGB());
/* 120 */     if (search.getText().isEmpty() && !search.isFocused()) {
/* 121 */       this.mc.fontRenderer.drawStringWithShadow("Search Feature...", (this.width - this.mc.fontRenderer.getStringWidth("Search Feature...") - 50), (sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 84), -1);
/*     */     }
/*     */     
/* 124 */     for (ImageButton imageButton : this.imageButtons) {
/* 125 */       imageButton.draw(mouseX, mouseY, Color.WHITE);
/* 126 */       if (Mouse.isButtonDown(0)) {
/* 127 */         imageButton.onClick(mouseX, mouseY);
/*     */       }
/*     */     } 
/* 130 */     GlStateManager.popMatrix();
/*     */     
/* 132 */     updateMouseWheel();
/*     */     
/* 134 */     if (this.exit) {
/* 135 */       this.screenHelper.interpolate(0.0F, 0.0F, 2.0D);
/* 136 */       if (this.screenHelper.getY() < 200.0F) {
/* 137 */         this.exit = false;
/* 138 */         this.mc.displayGuiScreen(null);
/* 139 */         if (this.mc.currentScreen == null) {
/* 140 */           this.mc.setIngameFocus();
/*     */         }
/*     */       } 
/*     */     } 
/* 144 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void updateMouseWheel() {
/* 148 */     int scrollWheel = Mouse.getDWheel();
/* 149 */     for (Component panel : this.components) {
/* 150 */       if (scrollWheel > 0) {
/* 151 */         panel.setY(panel.getY() + 15);
/*     */       }
/* 153 */       if (scrollWheel < 0) {
/* 154 */         panel.setY(panel.getY() - 15);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 161 */     if (keyCode == 1) {
/* 162 */       this.exit = true;
/*     */     }
/* 164 */     if (this.exit) {
/*     */       return;
/*     */     }
/* 167 */     this.selectedPanel.onKeyPress(keyCode);
/*     */     
/* 169 */     if (!escapeKeyInUse) {
/* 170 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/* 172 */     search.textboxKeyTyped(typedChar, keyCode);
/*     */     
/* 174 */     if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
/* 175 */       search.setFocused(!search.isFocused());
/*     */     }
/*     */     
/* 178 */     escapeKeyInUse = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 183 */     search.setFocused(false);
/* 184 */     search.setText("");
/* 185 */     search.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     
/* 187 */     for (Component component : this.components) {
/* 188 */       int x = component.getX();
/* 189 */       int y = component.getY();
/* 190 */       int cHeight = component.getHeight();
/* 191 */       if (component instanceof ExpandableComponent) {
/* 192 */         ExpandableComponent expandableComponent = (ExpandableComponent)component;
/* 193 */         if (expandableComponent.isExpanded())
/* 194 */           cHeight = expandableComponent.getHeightWithExpand(); 
/*     */       } 
/* 196 */       if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
/* 197 */         this.selectedPanel = component;
/* 198 */         component.onMouseClick(mouseX, mouseY, mouseButton);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 206 */     this.selectedPanel.onMouseRelease(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 211 */     this.screenHelper = new ScreenHelper(0.0F, 0.0F);
/* 212 */     this.mc.entityRenderer.theShaderGroup = null;
/* 213 */     super.onGuiClosed();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\clickgui\ClickGuiScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */