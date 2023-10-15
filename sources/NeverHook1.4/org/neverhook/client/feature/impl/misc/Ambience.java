/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Ambience
/*    */   extends Feature {
/*    */   private final NumberSetting time;
/*    */   private final ListSetting modeAmbri;
/* 16 */   private long spin = 0L;
/*    */   
/*    */   public Ambience() {
/* 19 */     super("Ambience", "Позволяет менять время суток", Type.Misc);
/* 20 */     this.modeAmbri = new ListSetting("Ambience Mode", "Night", () -> Boolean.valueOf(true), new String[] { "Day", "Night", "Morning", "Sunset", "Spin" });
/* 21 */     this.time = new NumberSetting("TimeSpin Speed", 2.0F, 1.0F, 10.0F, 1.0F, () -> Boolean.valueOf(true));
/* 22 */     addSettings(new Setting[] { (Setting)this.modeAmbri, (Setting)this.time });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 27 */     if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate) {
/* 28 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 34 */     String mode = this.modeAmbri.getOptions();
/* 35 */     setSuffix(mode);
/* 36 */     if (mode.equalsIgnoreCase("Spin")) {
/* 37 */       mc.world.setWorldTime(this.spin);
/* 38 */       this.spin = (long)((float)this.spin + this.time.getNumberValue() * 100.0F);
/* 39 */     } else if (mode.equalsIgnoreCase("Day")) {
/* 40 */       mc.world.setWorldTime(5000L);
/* 41 */     } else if (mode.equalsIgnoreCase("Night")) {
/* 42 */       mc.world.setWorldTime(17000L);
/* 43 */     } else if (mode.equalsIgnoreCase("Morning")) {
/* 44 */       mc.world.setWorldTime(0L);
/* 45 */     } else if (mode.equalsIgnoreCase("Sunset")) {
/* 46 */       mc.world.setWorldTime(13000L);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\Ambience.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */