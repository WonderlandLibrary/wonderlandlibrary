/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceiveMessage;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class AutoAuth
/*    */   extends Feature {
/* 14 */   public static String password = "qwerty123";
/*    */   
/*    */   public AutoAuth() {
/* 17 */     super("AutoAuth", "Автоматически регестрируется и логинится на серверах", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceiveMessage(EventReceiveMessage event) {
/* 22 */     if (event.getMessage().contains("/reg") || event.getMessage().contains("/register") || event.getMessage().contains("Зарегистрируйтесь")) {
/* 23 */       mc.player.sendChatMessage("/reg " + password + " " + password);
/* 24 */       ChatHelper.addChatMessage("Your password: " + ChatFormatting.RED + password);
/* 25 */       NotificationManager.publicity("AutoAuth", "You are successfully registered!", 4, NotificationType.SUCCESS);
/* 26 */     } else if (event.getMessage().contains("Авторизуйтесь") || event.getMessage().contains("/l")) {
/* 27 */       mc.player.sendChatMessage("/login " + password);
/* 28 */       NotificationManager.publicity("AutoAuth", "You are successfully login!", 4, NotificationType.SUCCESS);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AutoAuth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */