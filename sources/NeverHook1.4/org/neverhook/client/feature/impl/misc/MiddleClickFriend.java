/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.input.EventMouse;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.friend.Friend;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.ui.notification.NotificationManager;
/*    */ import org.neverhook.client.ui.notification.NotificationType;
/*    */ 
/*    */ public class MiddleClickFriend
/*    */   extends Feature
/*    */ {
/*    */   public MiddleClickFriend() {
/* 18 */     super("MiddleClickFriend", "Добавляет игрока в френд лист при нажатии на кнопку мыши", Type.Misc);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onMouseEvent(EventMouse event) {
/* 23 */     if (event.getKey() == 2 && mc.pointedEntity instanceof net.minecraft.entity.EntityLivingBase)
/* 24 */       if (NeverHook.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(mc.pointedEntity.getName()))) {
/* 25 */         NeverHook.instance.friendManager.getFriends().remove(NeverHook.instance.friendManager.getFriend(mc.pointedEntity.getName()));
/* 26 */         ChatHelper.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "' as Friend!");
/* 27 */         NotificationManager.publicity("MCF", "Removed '" + mc.pointedEntity.getName() + "' as Friend!", 4, NotificationType.INFO);
/*    */       } else {
/* 29 */         NeverHook.instance.friendManager.addFriend(new Friend(mc.pointedEntity.getName()));
/* 30 */         ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + mc.pointedEntity.getName() + " as Friend!");
/* 31 */         NotificationManager.publicity("MCF", "Added " + mc.pointedEntity.getName() + " as Friend!", 4, NotificationType.SUCCESS);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\MiddleClickFriend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */