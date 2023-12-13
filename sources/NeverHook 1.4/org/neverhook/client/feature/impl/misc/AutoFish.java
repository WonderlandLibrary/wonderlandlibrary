/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*    */ import net.minecraft.network.play.server.SPacketSoundEffect;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.SoundCategory;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class AutoFish
/*    */   extends Feature {
/*    */   public AutoFish() {
/* 17 */     super("Auto Fish", "Автоматически ловит рыбу за вас", Type.Misc);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 22 */     if (event.getPacket() instanceof SPacketSoundEffect) {
/* 23 */       SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
/* 24 */       if (packet.getCategory() == SoundCategory.NEUTRAL && packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH && (
/* 25 */         mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemFishingRod || mc.player.getHeldItemOffhand().getItem() instanceof net.minecraft.item.ItemFishingRod)) {
/* 26 */         mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
/* 27 */         mc.player.swingArm(EnumHand.MAIN_HAND);
/* 28 */         mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
/* 29 */         mc.player.swingArm(EnumHand.MAIN_HAND);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\AutoFish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */