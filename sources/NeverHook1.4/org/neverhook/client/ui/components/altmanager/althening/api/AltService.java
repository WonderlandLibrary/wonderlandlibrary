/*    */ package org.neverhook.client.ui.components.altmanager.althening.api;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AltService
/*    */ {
/* 13 */   private final AltHelper userAuthentication = new AltHelper("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
/* 14 */   private final AltHelper minecraftSession = new AltHelper("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService");
/*    */   private EnumAltService currentService;
/*    */   
/*    */   public void switchService(EnumAltService v1) throws NoSuchFieldException, IllegalAccessException {
/* 18 */     if (this.currentService == v1) {
/*    */       return;
/*    */     }
/* 21 */     reflectionFields(v1.hostname);
/* 22 */     this.currentService = v1;
/*    */   }
/*    */   
/*    */   private void reflectionFields(String v666) throws NoSuchFieldException, IllegalAccessException {
/* 26 */     HashMap<String, URL> v2 = new HashMap<>();
/* 27 */     String v3 = v666.contains("thealtening") ? "http" : "https";
/* 28 */     v2.put("ROUTE_AUTHENTICATE", constantURL(v3 + "://authserver." + v666 + ".com/authenticate"));
/* 29 */     v2.put("ROUTE_INVALIDATE", constantURL(v3 + "://authserver" + v666 + "com/invalidate"));
/* 30 */     v2.put("ROUTE_REFRESH", constantURL(v3 + "://authserver." + v666 + ".com/refresh"));
/* 31 */     v2.put("ROUTE_VALIDATE", constantURL(v3 + "://authserver." + v666 + ".com/validate"));
/* 32 */     v2.put("ROUTE_SIGNOUT", constantURL(v3 + "://authserver." + v666 + ".com/signout"));
/* 33 */     v2.forEach((a2, v1) -> {
/*    */           try {
/*    */             this.userAuthentication.setStaticField(a2, v1);
/* 36 */           } catch (Exception v4) {
/*    */             v4.printStackTrace();
/*    */           } 
/*    */         });
/*    */     
/* 41 */     this.userAuthentication.setStaticField("BASE_URL", v3 + "://authserver." + v666 + ".com/");
/* 42 */     this.minecraftSession.setStaticField("BASE_URL", v3 + "://sessionserver." + v666 + ".com/session/minecraft/");
/* 43 */     this.minecraftSession.setStaticField("JOIN_URL", constantURL(v3 + "://sessionserver." + v666 + ".com/session/minecraft/join"));
/* 44 */     this.minecraftSession.setStaticField("CHECK_URL", constantURL(v3 + "://sessionserver." + v666 + ".com/session/minecraft/hasJoined"));
/* 45 */     this.minecraftSession.setStaticField("WHITELISTED_DOMAINS", new String[] { ".minecraft.net", ".mojang.com", ".thealtening.com" });
/*    */   }
/*    */   
/*    */   private URL constantURL(String url) {
/*    */     try {
/* 50 */       return new URL(url);
/* 51 */     } catch (MalformedURLException v2) {
/* 52 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum EnumAltService {
/* 57 */     MOJANG("MOJANG", 0, "mojang"),
/* 58 */     THEALTENING("THEALTENING", 1, "thealtening");
/*    */     
/*    */     String hostname;
/*    */     
/*    */     EnumAltService(String s, int n, String a2) {
/* 63 */       this.hostname = a2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\components\altmanager\althening\api\AltService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */