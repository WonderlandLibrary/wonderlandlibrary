package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.MoveUtils;
import org.alphacentauri.management.util.RotationUtils;

public class ModuleTower extends Module implements EventListener {
   private int cooldown;
   private boolean dont = false;

   public ModuleTower() {
      super("Tower", "Stack up fast", new String[]{"tower"}, Module.Category.World, 5468010);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(this.dont) {
            return;
         }

         if(AC.getMC().gameSettings.keyBindSneak.isKeyDown()) {
            return;
         }

         EntityPlayerSP player = AC.getMC().getPlayer();
         if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock && AC.getMC().gameSettings.keyBindJump.isKeyDown()) {
            if(this.getBypass() == AntiCheat.AAC) {
               BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
               RotationUtils.set(this, RotationUtils.isSet() && RotationUtils.getSetBy() != this?RotationUtils.getYaw():player.rotationYaw, 90.0F);
               if(AC.getMC().getWorld().isAirBlock(playerPos.down()) && !AC.getMC().getWorld().isAirBlock(playerPos.down(2))) {
                  if(AC.getMC().playerController.onPlayerRightClick(player, AC.getMC().getWorld(), player.getHeldItem(), playerPos.down(2), EnumFacing.UP, new Vec3(0.5D, 1.0D, 0.5D))) {
                     player.sendQueue.addToSendQueue(new C0APacketAnimation());
                     player.motionY = -1.0D;
                  } else if(player.onGround) {
                     player.jump();
                     player.motionX = player.motionZ = 0.0D;
                     this.dont = true;
                     this.dont = false;
                  }
               } else if(player.onGround) {
                  player.jump();
                  player.motionX = player.motionZ = 0.0D;
                  this.dont = true;
                  player.onUpdate();
                  this.dont = false;
               }
            } else {
               BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
               if(AC.getMC().getWorld().isAirBlock(playerPos.down()) && !AC.getMC().getWorld().isAirBlock(playerPos.down(2))) {
                  AC.getMC().playerController.onPlayerRightClick(player, AC.getMC().getWorld(), player.getHeldItem(), playerPos.down(2), EnumFacing.UP, new Vec3(0.5D, 1.0D, 0.5D));
                  player.sendQueue.addToSendQueue(new C0APacketAnimation());
               }

               if(player.onGround && this.cooldown <= 0) {
                  player.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(player.posX, player.posY + 0.42D, player.posZ, player.rotationYaw, Math.max(player.rotationPitch, 30.0F), false));
                  player.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(player.posX, player.posY + 0.75D, player.posZ, player.rotationYaw, 90.0F, false));
                  MoveUtils.tpRel(0.0D, 1.0D, 0.0D);
                  player.motionY = 0.0D;
                  if(this.getBypass() != AntiCheat.Vanilla) {
                     this.cooldown = 2;
                  }
               } else {
                  player.motionY = -1.0D;
                  player.motionX = 0.0D;
                  player.motionZ = 0.0D;
                  if(this.cooldown > 0) {
                     --this.cooldown;
                  }
               }
            }
         }
      }

   }
}
