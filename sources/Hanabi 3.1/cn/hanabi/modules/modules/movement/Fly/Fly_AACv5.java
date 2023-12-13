package cn.hanabi.modules.modules.movement.Fly;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPacket;
import cn.hanabi.utils.MoveUtils;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

@ObfuscationClass
public class Fly_AACv5 {
   Minecraft mc = Minecraft.getMinecraft();
   private boolean blockC03 = false;
   private final ArrayList cacheList = new ArrayList();

   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (this.blockC03 && packet instanceof C03PacketPlayer) {
         this.cacheList.add((C03PacketPlayer)packet);
         event.setCancelled(true);
         if (this.cacheList.size() > 7) {
            this.sendC03();
         }
      }

   }

   public void onEnable() {
      this.blockC03 = true;
   }

   public void onDisable() {
      this.sendC03();
      this.blockC03 = false;
   }

   public void onUpdate() {
      double vanillaSpeed = (Double)Fly.timer.getValue();
      this.mc.thePlayer.capabilities.isFlying = false;
      this.mc.thePlayer.motionY = 0.0;
      this.mc.thePlayer.motionX = 0.0;
      this.mc.thePlayer.motionZ = 0.0;
      EntityPlayerSP var10000;
      if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
         var10000 = this.mc.thePlayer;
         var10000.motionY += vanillaSpeed;
      }

      if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
         var10000 = this.mc.thePlayer;
         var10000.motionY -= vanillaSpeed;
      }

      MoveUtils.strafe(vanillaSpeed);
   }

   private void sendC03() {
      this.blockC03 = false;
      Iterator var1 = this.cacheList.iterator();

      while(var1.hasNext()) {
         C03PacketPlayer packet = (C03PacketPlayer)var1.next();
         this.mc.getNetHandler().addToSendQueue(packet);
         if (packet.isMoving()) {
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(packet.getPositionX(), 1.0E159, packet.getPositionZ(), true));
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ(), true));
         }
      }

      this.cacheList.clear();
      this.blockC03 = true;
   }
}
