/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*    */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class FastBow extends Feature {
/*    */   private final NumberSetting ticks;
/*    */   
/*    */   public FastBow() {
/* 19 */     super("FastBow", "При зажатии на ПКМ игрок быстро стреляет из лука", Type.Combat);
/* 20 */     this.ticks = new NumberSetting("Bow Ticks", 1.5F, 1.5F, 10.0F, 0.5F, () -> Boolean.valueOf(true));
/* 21 */     addSettings(new Setting[] { (Setting)this.ticks });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate e) {
/* 26 */     if (mc.player.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow && mc.player.isBowing() && mc.player.getItemInUseMaxCount() >= this.ticks.getNumberValue()) {
/* 27 */       mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
/* 28 */       mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
/* 29 */       mc.player.stopActiveHand();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\FastBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */