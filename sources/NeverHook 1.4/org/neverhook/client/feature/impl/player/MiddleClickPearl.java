/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*    */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.input.EventMouse;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class MiddleClickPearl extends Feature {
/*    */   public MiddleClickPearl() {
/* 16 */     super("MiddleClickPearl", "Автоматически кидает эндер-перл при нажатии на колесо мыши", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onMouseEvent(EventMouse event) {
/* 21 */     if (event.getKey() == 2) {
/* 22 */       for (int i = 0; i < 9; i++) {
/* 23 */         ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
/* 24 */         if (itemStack.getItem() == Items.ENDER_PEARL) {
/* 25 */           mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(i));
/* 26 */           mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
/* 27 */           mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem));
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 35 */     mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem));
/* 36 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\MiddleClickPearl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */