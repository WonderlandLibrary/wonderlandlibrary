/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.init.MobEffects;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayerDigging;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventBlockInteract;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class SpeedMine extends Feature {
/* 18 */   public NumberSetting damageValue = new NumberSetting("Damage Value", 0.8F, 0.7F, 4.0F, 0.1F, () -> Boolean.valueOf(this.mode.currentMode.equals("Damage"))); public ListSetting mode;
/*    */   
/*    */   public SpeedMine() {
/* 21 */     super("SpeedMine", "Позволяет быстро ломать блоки", Type.Player);
/* 22 */     this.mode = new ListSetting("SpeedMine Mode", "Packet", () -> Boolean.valueOf(true), new String[] { "Packet", "Damage", "Potion" });
/* 23 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.damageValue });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 28 */     setSuffix(this.mode.currentMode);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onBlockInteract(EventBlockInteract event) {
/* 33 */     switch (this.mode.currentMode) {
/*    */       case "Potion":
/* 35 */         mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 817, 1));
/*    */         break;
/*    */       case "Damage":
/* 38 */         if (mc.playerController.curBlockDamageMP >= 0.7D) {
/* 39 */           mc.playerController.curBlockDamageMP = this.damageValue.getNumberValue();
/*    */         }
/*    */         break;
/*    */       case "Packet":
/* 43 */         mc.player.swingArm(EnumHand.MAIN_HAND);
/* 44 */         mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
/* 45 */         mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
/* 46 */         event.setCancelled(true);
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 53 */     mc.player.removePotionEffect(MobEffects.HASTE);
/* 54 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\SpeedMine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */