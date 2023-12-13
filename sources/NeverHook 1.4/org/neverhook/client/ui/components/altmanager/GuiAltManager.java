/*     */ package org.neverhook.client.ui.components.altmanager;
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiMainMenu;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.multiplayer.GuiConnecting;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.ThreadDownloadImageData;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import org.apache.commons.lang3.RandomStringUtils;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.impl.misc.StreamerMode;
/*     */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.button.GuiAltButton;
/*     */ import org.neverhook.client.ui.components.altmanager.alt.Alt;
/*     */ import org.neverhook.client.ui.components.altmanager.alt.AltLoginThread;
/*     */ import org.neverhook.client.ui.components.altmanager.alt.AltManager;
/*     */ import org.neverhook.client.ui.components.altmanager.althening.api.AltService;
/*     */ 
/*     */ public class GuiAltManager extends GuiScreen {
/*  38 */   public static final AltService altService = new AltService();
/*  39 */   public Alt selectedAlt = null;
/*     */   
/*     */   public String status;
/*     */   private GuiAltButton login;
/*     */   private GuiAltButton remove;
/*     */   private GuiAltButton rename;
/*     */   private AltLoginThread loginThread;
/*     */   private float offset;
/*     */   private GuiTextField searchField;
/*     */   private ResourceLocation resourceLocation;
/*     */   
/*     */   public GuiAltManager() {
/*  51 */     this.status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
/*     */   }
/*     */   
/*     */   private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
/*  55 */     TextureManager textureManager = this.mc.getTextureManager();
/*  56 */     textureManager.getTexture(resourceLocationIn);
/*  57 */     ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", new Object[] { StringUtils.stripControlCodes(username) }), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), (IImageBuffer)new ImageBufferDownload());
/*  58 */     textureManager.loadTexture(resourceLocationIn, (ITextureObject)textureObject);
/*     */   }
/*     */   public void actionPerformed(GuiButton button) {
/*     */     String randomName;
/*  62 */     switch (button.id) {
/*     */ 
/*     */       
/*     */       case 1:
/*  66 */         (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
/*     */         break;
/*     */       case 2:
/*  69 */         if (this.loginThread != null) {
/*  70 */           this.loginThread = null;
/*     */         }
/*     */         
/*  73 */         AltManager.registry.remove(this.selectedAlt);
/*  74 */         this.status = "§aRemoved.";
/*     */         
/*  76 */         this.selectedAlt = null;
/*     */         break;
/*     */       case 3:
/*  79 */         this.mc.displayGuiScreen(new GuiAddAlt(this));
/*     */         break;
/*     */       case 4:
/*  82 */         this.mc.displayGuiScreen(new GuiAltLogin(this));
/*     */         break;
/*     */       case 5:
/*  85 */         randomName = "NeverUser" + RandomStringUtils.randomAlphabetic(3) + RandomStringUtils.randomNumeric(2);
/*  86 */         (this.loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
/*  87 */         AltManager.registry.add(new Alt(randomName, ""));
/*     */         break;
/*     */       case 6:
/*  90 */         this.mc.displayGuiScreen(new GuiRenameAlt(this));
/*     */         break;
/*     */       case 7:
/*  93 */         this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 103 */         this.status = "§bRefreshed!";
/*     */         break;
/*     */       case 8931:
/* 106 */         this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer(this));
/*     */         break;
/*     */       case 4545:
/* 109 */         this.mc.displayGuiScreen((GuiScreen)new GuiConnecting(this, this.mc, new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "play.hypixel.net", false)));
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawScreen(int par1, int par2, float par3) {
/* 115 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 116 */     RectHelper.drawBorderedRect(-5.0F, 0.0F, (sr.getScaledWidth() - -6), sr.getScaledHeight(), 0.5F, (new Color(44, 44, 44, 255)).getRGB(), (new Color(33, 33, 33, 255)).getRGB(), true);
/* 117 */     RectHelper.drawBorderedRect(1.0F, 1.4F, (sr.getScaledWidth() - 1), sr.getScaledHeight() - 1.7F, 0.5F, (new Color(17, 17, 17, 255)).getRGB(), (new Color(33, 33, 33, 255)).getRGB(), true);
/*     */     
/* 119 */     if (Mouse.hasWheel()) {
/* 120 */       int wheel = Mouse.getDWheel();
/* 121 */       if (wheel < 0) {
/* 122 */         this.offset += 26.0F;
/* 123 */         if (this.offset < 0.0F) {
/* 124 */           this.offset = 0.0F;
/*     */         }
/* 126 */       } else if (wheel > 0) {
/* 127 */         this.offset -= 26.0F;
/* 128 */         if (this.offset < 0.0F) {
/* 129 */           this.offset = 0.0F;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), 1.0F, 1.0F, sr.getScaledWidth(), 1.0F, Color.white);
/*     */     
/* 136 */     String altName = "Name: " + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? "Protected" : this.mc.session.getUsername());
/* 137 */     this.mc.circleregular.drawStringWithShadow(TextFormatting.GRAY + "~ User Info ~", 16.0D, 75.0D, -1);
/* 138 */     this.mc.circleregular.drawStringWithShadow(altName, 11.0D, 89.0D, 14540253);
/*     */     
/* 140 */     this.mc.circleregular.drawStringWithShadow("Account Status: " + TextFormatting.GREEN + "Working", 11.0D, 100.0D, 14540253);
/* 141 */     RectHelper.drawRect((this.mc.circleregular.getStringWidth("Account Status: Working") + 13), (this.mc.circleregular.getStringHeight("Account Status: Working") + 89), 9.0D, (this.mc.circleregular.getStringHeight("Account Status: Working") + 100), PaletteHelper.getColor(255, 30));
/*     */     
/* 143 */     RectHelper.drawRect((this.mc.circleregular.getStringWidth(altName) + 14), (this.mc.circleregular.getStringHeight(altName) + 78), 9.0D, (this.mc.circleregular.getStringHeight(altName) + 89), PaletteHelper.getColor(255, 30));
/* 144 */     RectHelper.drawBorderedRect(92.0F, 82.0F, 92.0F, 12.0F, 3.0F, -1, -1, false);
/*     */     
/* 146 */     GlStateManager.pushMatrix();
/* 147 */     RenderHelper.drawImage(new ResourceLocation("neverhook/info.png"), 13.0F, 8.0F, 64.0F, 64.0F, Color.white);
/* 148 */     GlStateManager.disableBlend();
/* 149 */     GlStateManager.popMatrix();
/*     */     
/* 151 */     this.mc.circleregular.drawCenteredString("Account Manager", this.width / 2.0F, 10.0F, -1);
/* 152 */     this.mc.circleregular.drawCenteredString((this.loginThread == null) ? this.status : this.loginThread.getStatus(), this.width / 2.0F, 21.0F, -1);
/* 153 */     GlStateManager.pushMatrix();
/* 154 */     RenderHelper.scissorRect(0.0F, 33.0F, this.width, (this.height - 50));
/* 155 */     GL11.glEnable(3089);
/* 156 */     int y = 38;
/* 157 */     int number = 0;
/* 158 */     Iterator<Alt> e = getAlts().iterator();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 163 */       if (!e.hasNext()) {
/* 164 */         GL11.glDisable(3089);
/* 165 */         GL11.glPopMatrix();
/* 166 */         super.drawScreen(par1, par2, par3);
/* 167 */         if (this.selectedAlt == null) {
/* 168 */           this.login.enabled = false;
/* 169 */           this.remove.enabled = false;
/* 170 */           this.rename.enabled = false;
/*     */         } else {
/* 172 */           this.login.enabled = true;
/* 173 */           this.remove.enabled = true;
/* 174 */           this.rename.enabled = true;
/*     */         } 
/*     */         
/* 177 */         if (Keyboard.isKeyDown(200)) {
/* 178 */           this.offset -= 26.0F;
/* 179 */         } else if (Keyboard.isKeyDown(208)) {
/* 180 */           this.offset += 26.0F;
/*     */         } 
/*     */         
/* 183 */         if (this.offset < 0.0F) {
/* 184 */           this.offset = 0.0F;
/*     */         }
/*     */         
/* 187 */         this.searchField.drawTextBox();
/* 188 */         if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
/* 189 */           this.mc.circleregular.drawStringWithShadow("Search Alt", (this.width / 2 + 125), (this.height - 18), PaletteHelper.getColor(180));
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/* 194 */       Alt alt = e.next();
/* 195 */       if (isAltInArea(y)) {
/*     */         String pass;
/* 197 */         number++;
/*     */         
/* 199 */         if (alt.getMask().equals("")) {
/* 200 */           name = alt.getUsername();
/*     */         } else {
/* 202 */           name = alt.getMask();
/*     */         } 
/*     */         
/* 205 */         if (name.equalsIgnoreCase(this.mc.session.getUsername())) {
/* 206 */           name = "§n" + name;
/*     */         }
/*     */         
/* 209 */         String prefix = alt.getStatus().equals(Alt.Status.Banned) ? "§c" : (alt.getStatus().equals(Alt.Status.NotWorking) ? "§m" : "");
/* 210 */         String name = prefix + name + "§r §7| " + alt.getStatus().toFormatted();
/*     */         
/* 212 */         if (alt.getPassword().equals("")) {
/* 213 */           pass = "§cCrack";
/*     */         } else {
/* 215 */           pass = alt.getPassword().replaceAll(".", "*");
/*     */         } 
/*     */         
/* 218 */         if (alt != this.selectedAlt) {
/* 219 */           if (isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
/* 220 */             RectHelper.drawBorderedRect(this.width / 2.0F - 125.0F, y - this.offset - 4.0F, this.width / 1.5F, y - this.offset + 30.0F, 1.0F, -PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50), false);
/* 221 */           } else if (isMouseOverAlt(par1, par2, (y - this.offset))) {
/* 222 */             RectHelper.drawBorderedRect(this.width / 2.0F - 125.0F, y - this.offset - 4.0F, this.width / 1.5F, y - this.offset + 30.0F, 1.0F, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50), false);
/*     */           } 
/*     */         } else {
/* 225 */           if (isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
/* 226 */             RectHelper.drawBorderedRect(this.width / 2.0F - 125.0F, y - this.offset - 4.0F, this.width / 1.5F, y - this.offset + 30.0F, 1.0F, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50), false);
/* 227 */           } else if (isMouseOverAlt(par1, par2, (y - this.offset))) {
/* 228 */             RectHelper.drawBorderedRect(this.width / 2.0F - 125.0F, y - this.offset - 4.0F, this.width / 1.5F, y - this.offset + 30.0F, 1.0F, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50), false);
/*     */           } else {
/* 230 */             RectHelper.drawBorderedRect(this.width / 2.0F - 125.0F, y - this.offset - 4.0F, this.width / 1.5F, y - this.offset + 30.0F, 1.0F, PaletteHelper.getColor(255, 50), PaletteHelper.getColor(40, 50), false);
/*     */           } 
/*     */           
/* 233 */           GlStateManager.pushMatrix();
/* 234 */           boolean hovering = (par1 >= this.width / 1.5F + 5.0F && par1 <= this.width / 1.5D + 26.0D && par2 >= y - this.offset - 4.0F && par2 <= y - this.offset + 20.0F);
/* 235 */           RectHelper.drawBorderedRect(this.width / 1.5F + 5.0F, y - this.offset - 4.0F, this.width / 1.5F + 26.0F, y - this.offset + 20.0F, 1.0F, PaletteHelper.getColor(80, 255), hovering ? PaletteHelper.getColor(30, 222) : PaletteHelper.getColor(20, 255), false);
/* 236 */           RenderHelper.drawImage(new ResourceLocation("neverhook/change.png"), this.width / 1.5F + 8.0F, y - this.offset, 15.0F, 15.0F, hovering ? Color.GRAY : Color.WHITE);
/* 237 */           GlStateManager.popMatrix();
/*     */         } 
/*     */         
/* 240 */         String numberP = "§7" + number + ". §f";
/* 241 */         GlStateManager.pushMatrix();
/* 242 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 243 */         if (this.resourceLocation == null) {
/* 244 */           this.resourceLocation = AbstractClientPlayer.getLocationSkin(name);
/* 245 */           getDownloadImageSkin(this.resourceLocation, name);
/*     */         } else {
/* 247 */           this.mc.getTextureManager().bindTexture(this.resourceLocation);
/* 248 */           GlStateManager.enableTexture2D();
/* 249 */           Gui.drawScaledCustomSizeModalRect(this.width / 2.0F - 161.0F, y - this.offset - 4.0F, 8.0F, 8.0F, 8.0F, 8.0F, 33.0F, 33.0F, 64.0F, 64.0F);
/*     */         } 
/* 251 */         GlStateManager.popMatrix();
/* 252 */         this.mc.fontRenderer.drawCenteredString(numberP + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? "Protected" : name), this.width / 2.0F, y - this.offset + 5.0F, -1);
/* 253 */         this.mc.fontRenderer.drawCenteredString((alt.getStatus().equals(Alt.Status.NotWorking) ? "§m" : "") + pass, this.width / 2.0F, y - this.offset + 17.0F, PaletteHelper.getColor(110));
/* 254 */         y += 40;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void initGui() {
/* 259 */     this.searchField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 + 116, this.height - 22, 72, 16);
/* 260 */     this.buttonList.add(this.login = new GuiAltButton(1, this.width / 2 - 122, this.height - 48, 100, 20, "Login"));
/* 261 */     this.buttonList.add(this.remove = new GuiAltButton(2, this.width / 2 - 40, this.height - 24, 70, 20, "Remove"));
/* 262 */     this.buttonList.add(new GuiAltButton(3, this.width / 2 + 4 + 86, this.height - 48, 100, 20, "Add"));
/* 263 */     this.buttonList.add(new GuiAltButton(4, this.width / 2 - 16, this.height - 48, 100, 20, "Direct Login"));
/* 264 */     this.buttonList.add(new GuiAltButton(5, this.width / 2 - 122, this.height - 24, 78, 20, "Random"));
/* 265 */     this.buttonList.add(this.rename = new GuiAltButton(6, this.width / 2 + 38, this.height - 24, 70, 20, "Edit"));
/* 266 */     this.buttonList.add(new GuiAltButton(7, this.width / 2 - 190, this.height - 24, 60, 20, "Back"));
/* 267 */     this.buttonList.add(new GuiAltButton(8, this.width / 2 - 190, this.height - 48, 60, 20, "Refresh"));
/* 268 */     this.buttonList.add(new GuiAltButton(8931, this.width / 2 + 195, this.height - 48, 100, 20, "MultiPlayer"));
/* 269 */     this.login.enabled = false;
/* 270 */     this.remove.enabled = false;
/* 271 */     this.rename.enabled = false;
/*     */   }
/*     */   
/*     */   protected void keyTyped(char par1, int par2) {
/* 275 */     this.searchField.textboxKeyTyped(par1, par2);
/* 276 */     if ((par1 == '\t' || par1 == '\r') && this.searchField.isFocused()) {
/* 277 */       this.searchField.setFocused(!this.searchField.isFocused());
/*     */     }
/*     */     
/*     */     try {
/* 281 */       super.keyTyped(par1, par2);
/* 282 */     } catch (IOException e) {
/* 283 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isAltInArea(int y) {
/* 288 */     return (y - this.offset <= (this.height - 50));
/*     */   }
/*     */   
/*     */   private boolean isMouseOverAlt(double x, double y, double y1) {
/* 292 */     return (x >= (this.width / 2.0F - 125.0F) && y >= y1 - 4.0D && x <= this.width / 1.5D && y <= y1 + 20.0D && x >= 0.0D && y >= 33.0D && x <= this.width && y <= (this.height - 50));
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int par1, int par2, int par3) {
/* 296 */     this.searchField.mouseClicked(par1, par2, par3);
/* 297 */     if (this.offset < 0.0F) {
/* 298 */       this.offset = 0.0F;
/*     */     }
/*     */     
/* 301 */     double y = (38.0F - this.offset);
/*     */     
/* 303 */     for (Iterator<Alt> e = getAlts().iterator(); e.hasNext(); y += 40.0D) {
/* 304 */       Alt alt = e.next();
/* 305 */       if (isMouseOverAlt(par1, par2, y)) {
/* 306 */         if (alt == this.selectedAlt) {
/* 307 */           actionPerformed((GuiButton)this.login);
/*     */           return;
/*     */         } 
/* 310 */         this.selectedAlt = alt;
/*     */       } 
/*     */       
/* 313 */       boolean hovering = (par1 >= this.width / 1.49F && par1 <= this.width / 1.44D && par2 >= y - this.offset - 4.0D && par2 <= y - this.offset + 35.0D);
/* 314 */       if (hovering && alt == this.selectedAlt) {
/* 315 */         switch (alt.getStatus()) {
/*     */           case Unchecked:
/* 317 */             alt.setStatus(Alt.Status.Working);
/*     */             break;
/*     */           case Working:
/* 320 */             alt.setStatus(Alt.Status.Banned);
/*     */             break;
/*     */           case Banned:
/* 323 */             alt.setStatus(Alt.Status.NotWorking);
/*     */             break;
/*     */           case NotWorking:
/* 326 */             alt.setStatus(Alt.Status.Unchecked);
/*     */             break;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     try {
/* 332 */       super.mouseClicked(par1, par2, par3);
/* 333 */     } catch (IOException iOException) {
/* 334 */       iOException.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<Alt> getAlts() {
/* 339 */     List<Alt> altList = new ArrayList<>();
/* 340 */     Iterator<Alt> iterator = AltManager.registry.iterator();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 345 */       if (!iterator.hasNext()) {
/* 346 */         return altList;
/*     */       }
/*     */       
/* 349 */       Alt alt = iterator.next();
/*     */       
/* 351 */       if (this.searchField.getText().isEmpty() || alt.getMask().toLowerCase().contains(this.searchField.getText().toLowerCase()) || alt.getUsername().toLowerCase().contains(this.searchField.getText().toLowerCase()))
/*     */       {
/* 353 */         altList.add(alt);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\GuiAltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */