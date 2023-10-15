/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.feature.impl.movement.Flight;
/*    */ import org.neverhook.client.helpers.misc.ChatHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class AntiVoid extends Feature {
/*    */   private final NumberSetting fallDist;
/* 20 */   public float fall = 0.0F; private final ListSetting antiVoidMode;
/*    */   public boolean toggleFeature = false;
/*    */   
/*    */   public AntiVoid() {
/* 24 */     super("AntiVoid", "Не дает вам упасть в бездну", Type.Player);
/* 25 */     this.antiVoidMode = new ListSetting("AntiVoid Mode", "Packet", () -> Boolean.valueOf(true), new String[] { "Packet", "Spoof", "High-Motion", "Invalid Position", "Invalid Pitch", "Flag" });
/* 26 */     this.fallDist = new NumberSetting("Fall Distance", 5.0F, 1.0F, 10.0F, 1.0F, () -> Boolean.valueOf(true));
/* 27 */     addSettings(new Setting[] { (Setting)this.antiVoidMode, (Setting)this.fallDist });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventPreMotion event) {
/* 32 */     String mode = this.antiVoidMode.getOptions();
/* 33 */     setSuffix(mode);
/* 34 */     if (NeverHook.instance.featureManager.getFeatureByClass(Flight.class).getState()) {
/*    */       return;
/*    */     }
/*    */     
/* 38 */     if (!mc.player.onGround && !mc.player.isCollidedVertically && 
/* 39 */       mc.player.fallDistance > this.fallDist.getNumberValue() && 
/* 40 */       mc.world.getBlockState(new BlockPos(0.0D, -this.fall, 0.0D)).getBlock() == Blocks.AIR)
/* 41 */       if (mode.equalsIgnoreCase("High-Motion")) {
/* 42 */         mc.player.motionY += 3.0D;
/* 43 */       } else if (mode.equals("Packet")) {
/* 44 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 12.0D, mc.player.posZ, true));
/* 45 */       } else if (mode.equals("Spoof")) {
/* 46 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer(true));
/* 47 */       } else if (mode.equals("Flag")) {
/* 48 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX + 1.0D, mc.player.posY + 1.0D, mc.player.posZ + 1.0D, true));
/* 49 */       } else if (mode.equals("Invalid Pitch")) {
/* 50 */         event.setOnGround(true);
/* 51 */         event.setPitch(-91.0F);
/* 52 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
/* 53 */       } else if (mode.equals("Invalid Position")) {
/* 54 */         if (mc.player.onGround) {
/* 55 */           this.toggleFeature = true;
/*    */         }
/*    */         
/* 58 */         if (!this.toggleFeature && NoFall.noFallMode.currentMode.equals("Matrix")) {
/* 59 */           ChatHelper.addChatMessage("Переключи мод NoFall на другой!");
/*    */         }
/*    */         
/* 62 */         if (!this.toggleFeature && !NoFall.noFallMode.currentMode.equals("Matrix")) {
/* 63 */           mc.player.setPosition(mc.player.posX + 1.0D, mc.player.posY + 1.0D, mc.player.posZ + 1.0D);
/*    */         } else {
/* 65 */           setState(false);
/*    */         } 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\AntiVoid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */