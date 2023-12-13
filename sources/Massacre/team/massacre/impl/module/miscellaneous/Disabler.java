package team.massacre.impl.module.miscellaneous;

import java.util.LinkedList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Timer;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventMove;
import team.massacre.impl.event.EventPacket;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.Logger;
import team.massacre.utils.PacketUtil;
import team.massacre.utils.TimerUtil;

public class Disabler extends Module {
   TimerUtil timer = new TimerUtil();
   private int aac5Status = 0;
   private double aac5LastPosX = 0.0D;
   private int aac5Same = 0;
   private C03PacketPlayer.C06PacketPlayerPosLook aac5QueuedPacket = null;
   private int aac5SameReach = 5;
   private boolean aac5FlyClip = false;
   private boolean aac5FlyStart = false;
   private boolean aac5nextFlag = false;
   private double aac5LastFlag = 0.0D;
   private LinkedList<Packet> packetQueue = new LinkedList();
   private EnumProperty<Disabler.Mode> mode;

   public Disabler() {
      super("Disabler", 0, Category.MISCELLANEOUS);
      this.mode = new EnumProperty("Mode", Disabler.Mode.Verus_Combat);
      this.addValues(new Property[]{this.mode});
   }

   @Handler
   public void bbb(EventMove event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         switch((Disabler.Mode)this.mode.getValue()) {
         case AACv5:
         default:
         }
      }
   }

   @Handler
   public void a(EventUpdate event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         this.setSuffix(this.mode.getValue() != Disabler.Mode.Verus_Combat ? ((Disabler.Mode)this.mode.getValue()).name() : "Verus Combat");
         switch((Disabler.Mode)this.mode.getValue()) {
         case AACv5:
            if (this.mc.thePlayer.onGround) {
               Logger.print("JUMP INTO AIR AND TOGGLE THIS MODULE");
               this.setState(false);
            } else {
               this.mc.gameSettings.keyBindForward.pressed = this.aac5Status != 1;
               this.mc.thePlayer.motionX = 0.0D;
               this.mc.thePlayer.motionZ = 0.0D;
               this.mc.thePlayer.motionY = 0.0D;
               this.mc.thePlayer.rotationYaw = this.mc.thePlayer.rotationYaw;
               this.mc.thePlayer.rotationPitch = this.mc.thePlayer.rotationPitch;
               if (this.aac5Status == 1) {
                  if (this.aac5QueuedPacket != null) {
                     PacketUtil.sendPacketNoEvent(this.aac5QueuedPacket);
                     double dist = 0.13D;
                     double yaw = Math.toRadians((double)this.mc.thePlayer.rotationYaw);
                     double x = -Math.sin(yaw) * dist;
                     double z = Math.cos(yaw) * dist;
                     this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z);
                     PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
                  }

                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, Double.MAX_VALUE, this.mc.thePlayer.posZ, true));
                  this.aac5QueuedPacket = new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false);
               }
            }
            break;
         case Verus_Combat:
            if (this.mc.thePlayer.ticksExisted % 180 == 0) {
               while(this.packetQueue.size() > 25) {
                  PacketUtil.sendPacketNoEvent((Packet)this.packetQueue.poll());
               }
            }
            break;
         case Verus_Movemento:
            if (this.mc.thePlayer.ticksExisted % 180 == 0) {
               while(this.packetQueue.size() > 22) {
                  PacketUtil.sendPacketNoEvent((Packet)this.packetQueue.poll());
               }
            }
         }

      }
   }

   @Handler
   public void b(EventPacket event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         double x;
         double y;
         S08PacketPlayerPosLook packet;
         switch((Disabler.Mode)this.mode.getValue()) {
         case AACv5:
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
               packet = (S08PacketPlayerPosLook)event.getPacket();
               if (this.aac5Status == 0) {
                  this.mc.thePlayer.setPosition(packet.getX(), packet.getY(), packet.getZ());
                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, packet.getYaw(), packet.getPitch(), false));
                  if (this.mc.thePlayer.posX == this.aac5LastPosX) {
                     ++this.aac5Same;
                     if (this.aac5Same >= 5) {
                        this.aac5Status = 1;
                        this.mc.timer.timerSpeed = 0.65F;
                        this.aac5Same = 0;
                        return;
                     }
                  }

                  x = 0.12D;
                  y = Math.toRadians((double)this.mc.thePlayer.rotationYaw);
                  this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + -Math.sin(y) * x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + Math.cos(y) * x);
                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
                  this.aac5LastPosX = this.mc.thePlayer.posX;
                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, Double.MAX_VALUE, this.mc.thePlayer.posZ, true));
               } else if (this.mc.timer.timerSpeed <= 1.65F) {
                  ++this.aac5Same;
                  if (this.aac5Same >= this.aac5SameReach) {
                     this.aac5Same = 0;
                     this.aac5SameReach = (int)((float)this.aac5SameReach + 13.0F);
                     Timer var10000 = this.mc.timer;
                     var10000.timerSpeed += 0.4F;
                  }
               }
            }
            break;
         case Verus_Combat:
            if (this.mc.thePlayer != null && this.mc.thePlayer.ticksExisted < 8) {
               this.packetQueue.clear();
            }

            if (event.getPacket() instanceof C00PacketKeepAlive) {
               this.packetQueue.add(event.getPacket());
               event.setCancelled(true);
            }
            break;
         case Verus_Movemento:
            if (this.mc.thePlayer != null && this.mc.thePlayer.ticksExisted == 0) {
               this.packetQueue.clear();
            }

            if (event.getPacket() instanceof C03PacketPlayer) {
               double yPos = (double)Math.round(this.mc.thePlayer.posY / 0.015625D) * 0.015625D;
               this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, yPos, this.mc.thePlayer.posZ);
               if (this.mc.thePlayer.ticksExisted % 45 == 0) {
                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, 1.0E159D, this.mc.thePlayer.posZ, false));
                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
               }
            } else if (event.getPacket() instanceof S08PacketPlayerPosLook) {
               packet = (S08PacketPlayerPosLook)event.getPacket();
               x = packet.getX() - this.mc.thePlayer.posX;
               y = packet.getY() - this.mc.thePlayer.posY;
               double z = packet.getZ() - this.mc.thePlayer.posZ;
               double diff = Math.sqrt(x * x + y * y + z * z);
               if (diff <= 8.0D) {
                  event.cancelEvent();
                  PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), true));
               }
            } else if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
               C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction)event.getPacket();

               for(int i = 0; i < 4; ++i) {
                  this.packetQueue.add(packet);
               }

               event.cancelEvent();
            }
         }

      }
   }

   public void onEnable() {
      if (this.mode.getValue() == Disabler.Mode.AACv5) {
         double x = this.mc.thePlayer.posX;
         double y = this.mc.thePlayer.posY;
         double z = this.mc.thePlayer.posZ;
         PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, Double.MAX_VALUE, z, true));
         this.aac5LastPosX = 0.0D;
         this.aac5QueuedPacket = null;
         this.aac5Same = 0;
         this.aac5SameReach = 5;
         this.aac5Status = 0;
      }

      this.packetQueue.clear();
      super.onEnable();
   }

   public static enum Mode {
      AACv5,
      Verus_Combat,
      Verus_Movemento;
   }
}
