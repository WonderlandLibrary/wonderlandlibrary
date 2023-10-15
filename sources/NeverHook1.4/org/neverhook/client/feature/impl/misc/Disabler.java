/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketEntityAction;
/*    */ import org.apache.commons.lang3.RandomUtils;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Disabler
/*    */   extends Feature
/*    */ {
/* 25 */   public long randomLong = 0L;
/*    */   
/* 27 */   public BooleanSetting MatrixDestruction = new BooleanSetting("MatrixDestruction", false, () -> Boolean.valueOf(true));
/* 28 */   public BooleanSetting MatrixFlagClear = new BooleanSetting("MatrixFlagClear", false, () -> Boolean.valueOf(true));
/*    */   
/* 30 */   public ListSetting mode = new ListSetting("Disabler Mode", "Matrix", () -> Boolean.valueOf(true), new String[] { "Matrix" });
/*    */   
/*    */   public Disabler() {
/* 33 */     super("Disabler", "Ослабляет воздействие античитов на вас", Type.Misc);
/* 34 */     addSettings(new Setting[] { (Setting)this.mode, (Setting)this.MatrixDestruction, (Setting)this.MatrixFlagClear });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 39 */     setSuffix(this.mode.currentMode);
/* 40 */     if (this.mode.currentMode.equals("Matrix") && 
/* 41 */       this.MatrixDestruction.getBoolValue() && 
/* 42 */       mc.player.ticksExisted % 6 == 0) {
/* 43 */       mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 51 */     if (this.mode.currentMode.equals("Matrix") && 
/* 52 */       this.MatrixFlagClear.getBoolValue()) {
/* 53 */       this.randomLong = RandomUtils.nextLong(0L, 10L);
/* 54 */       if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketKeepAlive)
/*    */         try {
/* 56 */           Thread.sleep(50L * this.randomLong);
/* 57 */         } catch (InterruptedException e) {
/* 58 */           e.printStackTrace();
/*    */         }  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\Disabler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */