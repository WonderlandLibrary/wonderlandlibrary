/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.network.play.server.SPacketPlayerPosLook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ public class NoRotate
/*    */   extends Feature {
/*    */   public NoRotate() {
/* 12 */     super("NoServerRotation", "Убирает ротацию со стороны сервера", Type.Player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 17 */     if (event.getPacket() instanceof SPacketPlayerPosLook) {
/* 18 */       SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
/* 19 */       packet.yaw = mc.player.rotationYaw;
/* 20 */       packet.pitch = mc.player.rotationPitch;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\NoRotate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */