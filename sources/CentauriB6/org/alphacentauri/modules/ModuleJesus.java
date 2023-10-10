package org.alphacentauri.modules;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventBlockBB;
import org.alphacentauri.management.events.EventInput;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventPacketSend;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.RotationUtils;

public class ModuleJesus extends Module implements EventListener {
   private Property mode = new Property(this, "Mode", ModuleJesus.Mode.NCP);
   private Property AAC1Speed = new Property(this, "AAC1Speed", Float.valueOf(0.13F));
   private boolean jesusDown = false;
   private boolean lastTickLiquid = false;

   public ModuleJesus() {
      super("Jesus", "Walk over water", new String[]{"jesus"}, Module.Category.Movement, 9291994);
   }

   public void setBypass(AntiCheat ac) {
      if(ac == AntiCheat.AAC) {
         this.mode.value = ModuleJesus.Mode.AAC1;
      } else if(ac == AntiCheat.NCP) {
         this.mode.value = ModuleJesus.Mode.NCP;
      } else if(ac == AntiCheat.Reflex) {
         this.mode.value = ModuleJesus.Mode.NCP;
      } else if(ac == AntiCheat.Spartan) {
         this.mode.value = ModuleJesus.Mode.Spartan;
      } else if(ac == AntiCheat.Vanilla) {
         this.mode.value = ModuleJesus.Mode.Vanilla;
      }

      super.setBypass(ac);
   }

   public void onEvent(Event event) {
      if(this.mode.value == ModuleJesus.Mode.NCP) {
         if(event instanceof EventBlockBB) {
            if(((EventBlockBB)event).getBlock().getMaterial().isLiquid()) {
               BlockPos pos = ((EventBlockBB)event).getPos();
               Block block = ((EventBlockBB)event).getBlock();
               EntityPlayerSP player = AC.getMC().getPlayer();
               if(player == null || player.isSneaking() || player.isInLiquid()) {
                  return;
               }

               if(pos == null || block == null) {
                  return;
               }

               ((EventBlockBB)event).setBb(new AxisAlignedBB((double)pos.getX() + block.minX, (double)pos.getY() + block.minY, (double)pos.getZ() + block.minZ, (double)pos.getX() + block.maxX, (double)pos.getY() + block.maxY - (player.fallDistance >= 4.0F?0.42D:0.01D), (double)pos.getZ() + block.maxZ));
            }
         } else if(event instanceof EventTick) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(BlockUtils.getBlock(-0.001F).getBlock().getMaterial().isLiquid()) {
               if(!player.isCollidedVertically && AC.getMC().gameSettings.keyBindJump.isKeyDown()) {
                  player.motionY = 0.059D;
               }

               if(player.isCollidedVertically && RotationUtils.isSet()) {
                  player.motionY = 0.02D;
               }
            }
         } else if(event instanceof EventPacketSend) {
            if(!RotationUtils.isSet() && ((EventPacketSend)event).getPacket() instanceof C03PacketPlayer && BlockUtils.getBlock(-0.001F).getBlock().getMaterial().isLiquid() && AC.getMC().getPlayer().isCollidedVertically) {
               C03PacketPlayer c03 = (C03PacketPlayer)((EventPacketSend)event).getPacket();
               if(!c03.isMoving()) {
                  ((EventPacketSend)event).cancel();
                  EntityPlayerSP p = AC.getMC().getPlayer();
                  Packet toSend;
                  if(c03.getRotating()) {
                     toSend = new C06PacketPlayerPosLook(p.posX, p.posY, p.posZ, c03.yaw, c03.pitch, false);
                  } else {
                     toSend = new C04PacketPlayerPosition(p.posX, p.posY, p.posZ, false);
                  }

                  AC.getMC().getPlayer().sendQueue.addToSendQueue(toSend);
                  return;
               }

               c03.y += this.jesusDown?0.01D:0.0D;
               this.jesusDown = !this.jesusDown;
               c03.onGround = false;
            }
         } else if(event instanceof EventInput && BlockUtils.getBlock(-0.001F).getBlock().getMaterial().isLiquid()) {
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.isCollidedVertically) {
               ((EventInput)event).setJump(false);
            }
         }
      } else if(this.mode.value == ModuleJesus.Mode.Vanilla) {
         if(event instanceof EventBlockBB && ((EventBlockBB)event).getBlock().getMaterial().isLiquid()) {
            BlockPos pos = ((EventBlockBB)event).getPos();
            Block block = ((EventBlockBB)event).getBlock();
            ((EventBlockBB)event).setBb(new AxisAlignedBB((double)pos.getX() + block.minX, (double)pos.getY() + block.minY, (double)pos.getZ() + block.minZ, (double)pos.getX() + block.maxX, (double)pos.getY() + block.maxY, (double)pos.getZ() + block.maxZ));
         }
      } else if(this.mode.value == ModuleJesus.Mode.AAC1) {
         if(event instanceof EventTick) {
            if(AC.getMC().getPlayer().isInLiquid() && this.lastTickLiquid && !AC.getMC().gameSettings.keyBindSneak.isKeyDown() && !AC.getMC().gameSettings.keyBindJump.isKeyDown()) {
               MoveUtils.setSpeed((double)((Float)this.AAC1Speed.value).floatValue());
               AC.getMC().getPlayer().motionY = 0.2D;
            } else if(this.lastTickLiquid && !AC.getMC().gameSettings.keyBindSneak.isKeyDown() && !AC.getMC().gameSettings.keyBindJump.isKeyDown()) {
               AC.getMC().getPlayer().motionY = -0.2D;
            }
         }
      } else if(this.mode.value == ModuleJesus.Mode.Spartan) {
         if(event instanceof EventTick) {
            if(BlockUtils.getBlock(-0.1F).getBlock().getMaterial().isLiquid()) {
               MoveUtils.setSpeed(0.10000000149011612D);
            }
         } else if(event instanceof EventBlockBB && ((EventBlockBB)event).getBlock().getMaterial().isLiquid()) {
            BlockPos pos = ((EventBlockBB)event).getPos();
            Block block = ((EventBlockBB)event).getBlock();
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.isSneaking() || player.isInLiquid()) {
               return;
            }

            ((EventBlockBB)event).setBb(new AxisAlignedBB((double)pos.getX() + block.minX, (double)pos.getY() + block.minY, (double)pos.getZ() + block.minZ, (double)pos.getX() + block.maxX, (double)pos.getY() + block.maxY - (player.fallDistance >= 4.0F?0.42D:0.005D), (double)pos.getZ() + block.maxZ));
         }
      } else if(this.mode.value == ModuleJesus.Mode.AAC2 && event instanceof EventTick) {
         if(AC.getMC().getPlayer().isInLiquid() && this.lastTickLiquid && !AC.getMC().gameSettings.keyBindSneak.pressed) {
            MoveUtils.setSpeed(0.12999999523162842D);
            AC.getMC().getPlayer().motionY = 0.4D;
         } else if(!AC.getMC().getPlayer().isInLiquid() && this.lastTickLiquid) {
            MoveUtils.setSpeed(0.12999999523162842D);
         }
      }

      if(event instanceof EventTick) {
         this.lastTickLiquid = AC.getMC().getPlayer().isInLiquid();
      }

   }

   public static enum Mode {
      Vanilla,
      NCP,
      AAC1,
      AAC2,
      Spartan;
   }
}
