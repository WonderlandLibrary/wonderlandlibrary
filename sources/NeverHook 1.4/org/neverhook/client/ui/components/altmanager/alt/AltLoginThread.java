/*    */ package org.neverhook.client.ui.components.altmanager.alt;
/*    */ 
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import java.net.Proxy;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.Session;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.feature.impl.misc.StreamerMode;
/*    */ import org.neverhook.client.ui.components.altmanager.GuiAltManager;
/*    */ import org.neverhook.client.ui.components.altmanager.althening.api.AltService;
/*    */ 
/*    */ public class AltLoginThread
/*    */   extends Thread
/*    */ {
/*    */   private final Alt alt;
/* 20 */   private final Minecraft mc = Minecraft.getInstance();
/*    */   private String status;
/*    */   
/*    */   public AltLoginThread(Alt alt) {
/* 24 */     this.alt = alt;
/* 25 */     this.status = "§7Waiting...";
/*    */   }
/*    */   
/*    */   private Session createSession(String username, String password) {
/*    */     try {
/* 30 */       GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
/*    */       
/* 32 */       YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
/* 33 */       YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
/* 34 */       auth.setUsername(username);
/* 35 */       auth.setPassword(password);
/*    */       try {
/* 37 */         auth.logIn();
/* 38 */         return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
/* 39 */       } catch (AuthenticationException e) {
/* 40 */         return null;
/*    */       } 
/* 42 */     } catch (Exception e) {
/* 43 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getStatus() {
/* 48 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(String status) {
/* 52 */     this.status = status;
/*    */   }
/*    */   
/*    */   public void run() {
/* 56 */     if (this.alt.getPassword().equals("")) {
/* 57 */       this.mc.session = new Session(this.alt.getUsername(), "", "", "mojang");
/* 58 */       this.status = "§aLogged in - " + ChatFormatting.RED + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? "Protected" : this.alt.getUsername()) + ChatFormatting.GREEN + " §c" + ChatFormatting.BOLD + "(non license)";
/*    */     } else {
/*    */       
/* 61 */       this.status = "§bLogging in...";
/* 62 */       Session auth = createSession(this.alt.getUsername(), this.alt.getPassword());
/* 63 */       if (auth == null) {
/* 64 */         this.status = "§cConnect failed!";
/* 65 */         if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
/* 66 */           this.alt.setStatus(Alt.Status.NotWorking);
/*    */         }
/*    */       } else {
/* 69 */         AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
/* 70 */         this.status = "§aLogged in - " + ChatFormatting.RED + ((NeverHook.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue()) ? "Protected" : auth.getUsername()) + "§a" + ChatFormatting.BOLD + " (license)";
/*    */         
/* 72 */         this.alt.setMask(auth.getUsername());
/* 73 */         this.mc.session = auth;
/* 74 */         if (this.alt.getStatus().equals(Alt.Status.Unchecked))
/* 75 */           this.alt.setStatus(Alt.Status.Working); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\alt\AltLoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */