/*     */ package org.neverhook.client.ui.components.altmanager;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.button.GuiAltButton;
/*     */ import org.neverhook.client.ui.components.altmanager.alt.Alt;
/*     */ import org.neverhook.client.ui.components.altmanager.alt.AltLoginThread;
/*     */ 
/*     */ public final class GuiAltLogin extends GuiScreen {
/*     */   private final GuiScreen previousScreen;
/*     */   private PasswordField password;
/*     */   private AltLoginThread thread;
/*     */   private GuiTextField username;
/*     */   
/*     */   public GuiAltLogin(GuiScreen previousScreen) {
/*  26 */     this.previousScreen = previousScreen;
/*     */   }
/*     */   protected void actionPerformed(GuiButton button) {
/*     */     try {
/*     */       String data;
/*  31 */       switch (button.id) {
/*     */         case 0:
/*  33 */           (this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()))).start();
/*     */           break;
/*     */         case 1:
/*  36 */           this.mc.displayGuiScreen(this.previousScreen);
/*     */           break;
/*     */         case 2:
/*  39 */           data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
/*  40 */           if (data.contains(":")) {
/*  41 */             String[] credentials = data.split(":");
/*  42 */             this.username.setText(credentials[0]);
/*  43 */             this.password.setText(credentials[1]);
/*     */           } 
/*     */           break;
/*     */       } 
/*  47 */     } catch (Throwable e) {
/*  48 */       throw new RuntimeException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawScreen(int x, int y, float z) {
/*  53 */     RectHelper.drawBorderedRect(0.0F, 0.0F, this.width, this.height, 0.5F, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
/*  54 */     RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), 1.0F, 1.0F, 958.0F, 1.0F, Color.white);
/*  55 */     this.username.drawTextBox();
/*  56 */     this.password.drawTextBox();
/*  57 */     this.mc.circleregular.drawStringWithShadow("Alt Login", (this.width / 2.0F), 20.0D, -1);
/*  58 */     this.mc.circleregular.drawStringWithShadow((this.thread == null) ? (TextFormatting.GRAY + "Alts...") : this.thread.getStatus(), (this.width / 2.0F), 29.0D, -1);
/*  59 */     if (this.username.getText().isEmpty() && !this.username.isFocused()) {
/*  60 */       this.mc.circleregular.drawStringWithShadow("Username / E-Mail", (this.width / 2 - 96), 66.0D, -7829368);
/*     */     }
/*     */     
/*  63 */     if (this.password.getText().isEmpty() && !this.password.isFocused()) {
/*  64 */       this.mc.circleregular.drawStringWithShadow("Password", (this.width / 2 - 96), 106.0D, -7829368);
/*     */     }
/*     */     
/*  67 */     super.drawScreen(x, y, z);
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  71 */     int height1 = this.height / 4 + 24;
/*  72 */     this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, height1 + 72 + 12, "Login"));
/*  73 */     this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, height1 + 72 + 12 + 24, "Back"));
/*  74 */     this.buttonList.add(new GuiAltButton(2, this.width / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
/*  75 */     this.username = new GuiTextField(height1, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
/*  76 */     this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
/*  77 */     this.username.setFocused(true);
/*  78 */     Keyboard.enableRepeatEvents(true);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char character, int key) {
/*     */     try {
/*  83 */       super.keyTyped(character, key);
/*  84 */     } catch (IOException e) {
/*  85 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  88 */     if (character == '\t') {
/*  89 */       if (!this.username.isFocused() && !this.password.isFocused()) {
/*  90 */         this.username.setFocused(true);
/*     */       } else {
/*  92 */         this.username.setFocused(this.password.isFocused());
/*  93 */         this.password.setFocused(!this.username.isFocused());
/*     */       } 
/*     */     }
/*     */     
/*  97 */     if (character == '\r') {
/*  98 */       actionPerformed(this.buttonList.get(0));
/*     */     }
/*     */     
/* 101 */     this.username.textboxKeyTyped(character, key);
/* 102 */     this.password.textboxKeyTyped(character, key);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int x, int y, int button) {
/*     */     try {
/* 107 */       super.mouseClicked(x, y, button);
/* 108 */     } catch (IOException e) {
/* 109 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 112 */     this.username.mouseClicked(x, y, button);
/* 113 */     this.password.mouseClicked(x, y, button);
/*     */   }
/*     */   
/*     */   public void onGuiClosed() {
/* 117 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/* 121 */     this.username.updateCursorCounter();
/* 122 */     this.password.updateCursorCounter();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\GuiAltLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */