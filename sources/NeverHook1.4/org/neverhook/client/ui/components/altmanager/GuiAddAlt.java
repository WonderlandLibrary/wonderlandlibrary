/*     */ package org.neverhook.client.ui.components.altmanager;
/*     */ 
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*     */ import com.mojang.realmsclient.gui.ChatFormatting;
/*     */ import java.awt.Color;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.impl.misc.StreamerMode;
/*     */ import org.neverhook.client.helpers.render.RenderHelper;
/*     */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*     */ import org.neverhook.client.ui.button.GuiAltButton;
/*     */ import org.neverhook.client.ui.components.altmanager.alt.Alt;
/*     */ import org.neverhook.client.ui.components.altmanager.alt.AltManager;
/*     */ 
/*     */ public class GuiAddAlt
/*     */   extends GuiScreen {
/*     */   private final GuiAltManager manager;
/*     */   private PasswordField password;
/*     */   private String status;
/*     */   private GuiTextField username;
/*     */   
/*     */   GuiAddAlt(GuiAltManager manager) {
/*  35 */     this.status = TextFormatting.GRAY + "Idle...";
/*  36 */     this.manager = manager;
/*     */   }
/*     */   
/*     */   private static void setStatus(GuiAddAlt guiAddAlt, String status) {
/*  40 */     guiAddAlt.status = status;
/*     */   } protected void actionPerformed(GuiButton button) {
/*     */     AddAltThread login;
/*     */     String data;
/*  44 */     switch (button.id) {
/*     */       case 0:
/*  46 */         login = new AddAltThread(this.username.getText(), this.password.getText());
/*  47 */         login.start();
/*     */         break;
/*     */       case 1:
/*  50 */         this.mc.displayGuiScreen(this.manager);
/*     */         break;
/*     */       
/*     */       case 2:
/*     */         try {
/*  55 */           data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
/*  56 */         } catch (Exception var4) {
/*     */           return;
/*     */         } 
/*     */         
/*  60 */         if (data.contains(":")) {
/*  61 */           String[] credentials = data.split(":");
/*  62 */           this.username.setText(credentials[0]);
/*  63 */           this.password.setText(credentials[1]);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawScreen(int i, int j, float f) {
/*  70 */     RectHelper.drawBorderedRect(0.0F, 0.0F, this.width, this.height, 0.5F, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
/*  71 */     RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), 1.0F, 1.0F, 958.0F, 1.0F, Color.white);
/*  72 */     this.username.drawTextBox();
/*  73 */     this.password.drawTextBox();
/*  74 */     this.mc.circleregular.drawCenteredString("Add Account", this.width / 2.0F, 15.0F, -1);
/*  75 */     if (this.username.getText().isEmpty() && !this.username.isFocused()) {
/*  76 */       this.mc.circleregular.drawStringWithShadow("Username / E-Mail", (this.width / 2 - 96), 66.0D, -7829368);
/*     */     }
/*     */     
/*  79 */     if (this.password.getText().isEmpty() && !this.password.isFocused()) {
/*  80 */       this.mc.circleregular.drawStringWithShadow("Password", (this.width / 2 - 96), 106.0D, -7829368);
/*     */     }
/*     */     
/*  83 */     this.mc.circleregular.drawCenteredString(this.status, this.width / 2.0F, 30.0F, -1);
/*  84 */     super.drawScreen(i, j, f);
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  88 */     Keyboard.enableRepeatEvents(true);
/*  89 */     this.buttonList.clear();
/*  90 */     this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
/*  91 */     this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
/*  92 */     this.buttonList.add(new GuiAltButton(2, this.width / 2 - 100, this.height / 4 + 92 - 12, "Import User:Pass"));
/*  93 */     this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
/*  94 */     this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char par1, int par2) {
/*  98 */     this.username.textboxKeyTyped(par1, par2);
/*  99 */     this.password.textboxKeyTyped(par1, par2);
/* 100 */     if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
/* 101 */       this.username.setFocused(!this.username.isFocused());
/* 102 */       this.password.setFocused(!this.password.isFocused());
/*     */     } 
/*     */     
/* 105 */     if (par1 == '\r') {
/* 106 */       actionPerformed(this.buttonList.get(0));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int par1, int par2, int par3) {
/*     */     try {
/* 112 */       super.mouseClicked(par1, par2, par3);
/* 113 */     } catch (IOException var5) {
/* 114 */       var5.printStackTrace();
/*     */     } 
/*     */     
/* 117 */     this.username.mouseClicked(par1, par2, par3);
/* 118 */     this.password.mouseClicked(par1, par2, par3);
/*     */   }
/*     */   
/*     */   private class AddAltThread extends Thread {
/*     */     private final String password;
/*     */     private final String username;
/*     */     
/*     */     AddAltThread(String username, String password) {
/* 126 */       this.username = username;
/* 127 */       this.password = password;
/* 128 */       GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GRAY + "Idle...");
/*     */     }
/*     */     
/*     */     private void checkAndAddAlt(String username, String password) {
/*     */       try {
/* 133 */         YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
/* 134 */         YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
/* 135 */         auth.setUsername(username);
/* 136 */         auth.setPassword(password);
/*     */         
/*     */         try {
/* 139 */           auth.logIn();
/* 140 */           AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 147 */           GuiAddAlt.setStatus(GuiAddAlt.this, "§AAdded alt - " + ChatFormatting.RED + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? "Protected" : this.username) + ChatFormatting.GREEN + " §a" + ChatFormatting.BOLD + "(license)");
/* 148 */         } catch (AuthenticationException var7) {
/* 149 */           GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Connect failed!");
/* 150 */           var7.printStackTrace();
/*     */         } 
/* 152 */       } catch (Throwable e) {
/* 153 */         GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Error");
/* 154 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void run() {
/* 159 */       if (this.password.equals("")) {
/* 160 */         AltManager.registry.add(new Alt(this.username, ""));
/* 161 */         GuiAddAlt.setStatus(GuiAddAlt.this, "§AAdded alt - " + ChatFormatting.RED + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? "Protected" : this.username) + ChatFormatting.GREEN + " §c" + ChatFormatting.BOLD + "(non license)");
/*     */       } else {
/* 163 */         GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.AQUA + "Trying connect...");
/* 164 */         checkAndAddAlt(this.username, this.password);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\GuiAddAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */