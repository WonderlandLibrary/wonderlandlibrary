package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputOverridden;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventLeftClick;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.RaycastUtils;
import org.alphacentauri.management.util.RenderUtils;

public class ModuleTP extends Module implements EventListener {
   private Property mode = new Property(this, "Mode", ModuleTP.Mode.NCP1);
   private double tX;
   private double tY;
   private double tZ;
   private int counter;
   private boolean aac2b1;
   private int aac2i1;
   private double aac2x;
   private double aac2y;
   private double aac2z;

   public ModuleTP() {
      super("Teleport", "Enderman Style", new String[]{"tp", "teleport"}, Module.Category.Exploits, 1258295);
   }

   public void onEvent(Event event) {
      if(this.mode.value == ModuleTP.Mode.NCP1) {
         if(event instanceof EventPacketSend) {
            if(((EventPacketSend)event).getPacket() instanceof C03PacketPlayer) {
               C03PacketPlayer c03 = (C03PacketPlayer)((EventPacketSend)event).getPacket();
               if(!c03.isMoving() && !c03.getRotating()) {
                  if(this.counter < 180) {
                     ++this.counter;
                  }

                  ((EventPacketSend)event).cancel();
               } else {
                  EntityPlayerSP player = AC.getMC().getPlayer();
                  if(c03.x == player.posX && player.posX == player.lastTickPosX && c03.y == player.posY && player.posY == player.lastTickPosY && c03.z == player.posZ && player.posZ == player.lastTickPosZ && !c03.getRotating()) {
                     ((EventPacketSend)event).cancel();
                  } else {
                     this.counter = 0;
                  }
               }
            }
         } else if(event instanceof EventRender3D) {
            if(this.counter == 0) {
               return;
            }

            EntityPlayerSP player = AC.getMC().getPlayer();
            Vec3 look = player.getLook(((EventRender3D)event).getPartialTicks()).multiply((double)this.counter / 3.333D);
            MovingObjectPosition rayCast = RaycastUtils.rayCast(player, player.posX + look.xCoord, player.posY + (double)player.getEyeHeight() + look.yCoord, player.posZ + look.zCoord);
            if(rayCast != null && rayCast.getBlockPos() != null) {
               BlockPos pos = rayCast.getBlockPos();
               RenderUtils.blockESP(pos, -72, true);
            }
         } else if(event instanceof EventLeftClick) {
            this.tp1(this.counter);
         }
      } else if(this.mode.value == ModuleTP.Mode.AAC1) {
         if(event instanceof EventLeftClick) {
            MovingObjectPosition ray = RaycastUtils.normalRaycast(999.0F);
            if(ray != null && ray.getBlockPos() != null) {
               BlockPos pos = ray.getBlockPos();
               this.tX = (double)pos.getX() + 0.5D;
               this.tY = (double)pos.getY();
               this.tZ = (double)pos.getZ() + 0.5D;
               AC.addChat(this.getName(), "TP-Location: " + this.tX + " " + this.tY + " " + this.tZ);
            }
         } else if(event instanceof EventTick) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.isSneaking() && player.onGround) {
               double dX = player.posX - this.tX;
               double dZ = player.posZ - this.tZ;
               double dist = Math.sqrt(dX * dX + dZ * dZ);
               if(dist > 5.0D) {
                  int runs = (int)(dist / 5.0D);
                  double cX = player.posX;
                  double cZ = player.posZ;

                  for(int i = 0; i < runs; ++i) {
                     cX -= dX / (double)runs;
                     cZ -= dZ / (double)runs;
                     player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(cX, player.posY, cZ, false));
                  }
               }

               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(this.tX, player.posY, this.tZ, false));
               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(this.tX, player.posY + 2.0D, this.tZ, false));
            }
         }
      } else if(this.mode.value == ModuleTP.Mode.AAC2) {
         if(event instanceof EventLeftClick) {
            MovingObjectPosition ray = RaycastUtils.normalRaycast(999.0F);
            if(ray != null && ray.getBlockPos() != null) {
               BlockPos pos = ray.getBlockPos();
               this.aac2x = (double)pos.getX() + 0.5D;
               this.aac2y = (double)(pos.getY() + 1);
               this.aac2z = (double)pos.getZ() + 0.5D;
               AC.addChat(this.getName(), "TP-Location: " + this.aac2x + " " + this.aac2y + " " + this.aac2z);
            }
         } else if(event instanceof EventTick) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(AC.getMC().getPlayer().getDistanceSq(this.aac2x, this.aac2y, this.aac2z) < 3.0D) {
               return;
            }

            if(AC.getMC().getPlayer().isSneaking()) {
               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY, player.posZ, true));
               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(this.aac2x, this.aac2y, this.aac2z, true));
               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY, player.posZ, true));
               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 5.0D, player.posZ, true));
               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(this.aac2x, this.aac2y, this.aac2z, true));
               player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY, player.posZ, true));
               MoveUtils.toFwd(0.04D);
            }
         } else if(event instanceof EventRender3D) {
            RenderUtils.blockESP(new BlockPos(this.aac2x, this.aac2y - 1.0D, this.aac2z), -1207912264, true);
         }
      } else if(this.mode.value == ModuleTP.Mode.DEBUG1 && event instanceof EventLeftClick) {
         MovingObjectPosition ray = RaycastUtils.normalRaycast(999.0F);
         if(ray != null && ray.getBlockPos() != null) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            BlockPos pos = ray.getBlockPos();
            double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY();
            double z = (double)pos.getZ() + 0.5D;
            player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(x, y, z, true));
            player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(x + 1.0D, y + 5.0D, z + 3.0D, true));
            player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY, player.posZ, true));
            player.sendQueue.addToSendQueue(new C04PacketPlayerPosition(player.posX, player.posY + 5.0D, player.posZ, true));
            MoveUtils.toFwd(0.4D);
         }
      }

   }

   public void tp1(int ticks) {
      EntityPlayerSP player = AC.getMC().getPlayer();
      MovementInput org = player.movementInput;
      player.movementInput = new MovementInputOverridden(1.0F, 0.0F, true, false);

      for(int i = 0; i < ticks; ++i) {
         player.onUpdate();
      }

      player.movementInput = org;
   }

   public static enum Mode {
      NCP1,
      AAC1,
      AAC2,
      DEBUG1;
   }
}
