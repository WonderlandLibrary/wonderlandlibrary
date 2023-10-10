package org.alphacentauri.management.util;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventSetback;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;

public class RotationUtils implements EventListener {
   private static float yaw;
   private static float pitch;
   private static float oldYaw;
   private static float oldPitch;
   private static boolean lookChanged = false;
   private static int notSet = 0;
   private static Module setBy;

   public static boolean isSet() {
      return setBy != null;
   }

   public static void set(Module setter, float _yaw, float _pitch) {
      if(!Double.isNaN((double)_yaw) && !Double.isNaN((double)_pitch)) {
         yaw = _yaw;

         for(pitch = _pitch; pitch < -90.0F; pitch += 180.0F) {
            ;
         }

         while(pitch > 90.0F) {
            pitch -= 180.0F;
         }

         lookChanged = true;
         notSet = 0;
         setBy = setter;
      }
   }

   public static void reset() {
      lookChanged = false;
      setBy = null;
   }

   public static float getYaw() {
      return yaw;
   }

   public static float getLastYaw() {
      return oldYaw;
   }

   public static float getPitch() {
      return pitch;
   }

   public static Module getSetBy() {
      return setBy;
   }

   public void onEvent(Event event) {
      if(event instanceof EventPacketSend) {
         Packet packet = ((EventPacketSend)event).getPacket();
         if(packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer)packet;
            if(needsRotation()) {
               if(!c03.getRotating()) {
                  ((EventPacketSend)event).cancel();
                  AC.getMC().getPlayer().sendQueue.addToSendQueue((Packet)(c03.isMoving()?new C06PacketPlayerPosLook(c03.x, c03.y, c03.z, -999.0F, -999.0F, c03.onGround):new C05PacketPlayerLook(-999.0F, -999.0F, c03.onGround)));
               } else {
                  c03.pitch = pitch;
                  c03.yaw = yaw;
                  oldPitch = pitch;
                  oldYaw = yaw;
               }
            } else if(c03.getRotating() && lookChanged) {
               ((EventPacketSend)event).cancel();
               AC.getMC().getPlayer().sendQueue.addToSendQueue((Packet)(c03.isMoving()?new C04PacketPlayerPosition(c03.x, c03.y, c03.z, c03.onGround):new C03PacketPlayer(c03.onGround)));
            }
         }
      } else if(event instanceof EventTick) {
         ++notSet;
         if(notSet > 2) {
            reset();
            notSet = 3;
         }
      } else if(event instanceof EventSetback) {
         yaw = oldYaw = ((EventSetback)event).getSetbackYaw();
         pitch = oldPitch = ((EventSetback)event).getSetbackPitch();
      }

   }

   public static float getLastPitch() {
      return oldPitch;
   }

   private static boolean needsRotation() {
      return lookChanged && (yaw != oldYaw || pitch != oldPitch);
   }
}
