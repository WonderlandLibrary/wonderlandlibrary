package org.alphacentauri.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.management.util.RotationUtils;
import org.alphacentauri.modules.ModuleBowAimBot;
import org.alphacentauri.modules.ModuleKillAura;

public class ModuleScaffold extends Module implements EventListener {
   private Property aacExpand = new Property(this, "AACExpand", Boolean.valueOf(false));
   private boolean cooldown;
   private int expandCooldown;

   public ModuleScaffold() {
      super("Scaffold", "Like a magic carpet", new String[]{"scaffold", "scaffoldwalk"}, Module.Category.World, 963268);
   }

   private boolean IsValidBlock(BlockPos pos) {
      Block block = AC.getMC().theWorld.getBlockState(pos).getBlock();
      return block instanceof BlockLiquid?false:block.getMaterial() != Material.air;
   }

   private BlockPos getPlayerPos() {
      double x = AC.getMC().getPlayer().posX;
      double z = AC.getMC().getPlayer().posZ;
      int y = (int)AC.getMC().getPlayer().posY;
      boolean xN = x < 0.0D;
      boolean zN = z < 0.0D;
      int blockX = xN?(int)x:(int)x;
      int blockZ = zN?(int)z:(int)z;
      return new BlockPos(blockX, y, blockZ);
   }

   public void PlaceBlock(BlockPos pos, EnumFacing face) {
      if(!this.cooldown || this.getBypass() != AntiCheat.AAC) {
         this.cooldown = true;
         if(face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
         } else if(face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
         } else if(face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
         } else if(face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
         } else if(face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
         }

         float[] rotations = BlockUtils.getRotations(pos, face);
         RotationUtils.set(this, rotations[0], rotations[1]);
         if(AC.getMC().getPlayer().getHeldItem() != null && AC.getMC().getPlayer().getHeldItem().getItem() instanceof ItemBlock) {
            AC.getMC().getPlayer().swingItem();
            AC.getMC().playerController.onPlayerRightClick(AC.getMC().getPlayer(), AC.getMC().getWorld(), AC.getMC().getPlayer().getHeldItem(), pos, face, new Vec3(0.5D, 0.5D, 0.5D));
         }

      }
   }

   public void onEvent(Event event) {
      if(((Boolean)this.aacExpand.value).booleanValue()) {
         if(event instanceof EventTick) {
            if(this.expandCooldown-- > 0) {
               return;
            }

            this.expandCooldown = 0;
            EntityPlayerSP player = AC.getMC().getPlayer();
            if(player.getHeldItem() == null) {
               return;
            }

            if(!(player.getHeldItem().getItem() instanceof ItemBlock)) {
               return;
            }

            WorldClient world = AC.getMC().getWorld();
            EnumFacing faceing = player.getHorizontalFacing();
            BlockPos playerBlock = new BlockPos(AC.getMC().getRenderViewEntity().posX, AC.getMC().getRenderViewEntity().getEntityBoundingBox().minY, AC.getMC().getRenderViewEntity().posZ);

            for(int i = 0; i < 5; ++i) {
               BlockPos offset = playerBlock.add(0, -1, 0).offset(faceing, i);
               if(world.isAirBlock(offset)) {
                  this.cooldown = false;
                  this.PlaceBlock(offset, faceing);
                  this.expandCooldown = 2;
                  break;
               }
            }
         }
      } else if(event instanceof EventTick) {
         if(this.cooldown) {
            this.cooldown = false;
         }

         if(AC.getMC().thePlayer == null || AC.getMC().theWorld == null) {
            return;
         }

         if(RotationUtils.isSet() && (RotationUtils.getSetBy() instanceof ModuleKillAura || RotationUtils.getSetBy() instanceof ModuleBowAimBot)) {
            return;
         }

         if(AC.getMC().thePlayer.movementInput.sneak) {
            return;
         }

         if(this.getBypass() == AntiCheat.AAC) {
            RotationUtils.set(this, AC.getMC().thePlayer.rotationYaw > 0.0F?AC.getMC().thePlayer.rotationYaw - 180.0F:AC.getMC().thePlayer.rotationYaw + 180.0F, 80.0F);
         }

         BlockPos playerBlock = new BlockPos(AC.getMC().getRenderViewEntity().posX, AC.getMC().getRenderViewEntity().getEntityBoundingBox().minY, AC.getMC().getRenderViewEntity().posZ);
         if(AC.getMC().theWorld.isAirBlock(playerBlock.add(0, -1, 0))) {
            if(this.IsValidBlock(playerBlock.add(0, -2, 0))) {
               this.PlaceBlock(playerBlock.add(0, -1, 0), EnumFacing.UP);
            } else if(this.IsValidBlock(playerBlock.add(-1, -1, 0))) {
               this.PlaceBlock(playerBlock.add(0, -1, 0), EnumFacing.EAST);
            } else if(this.IsValidBlock(playerBlock.add(1, -1, 0))) {
               this.PlaceBlock(playerBlock.add(0, -1, 0), EnumFacing.WEST);
            } else if(this.IsValidBlock(playerBlock.add(0, -1, -1))) {
               this.PlaceBlock(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
            } else if(this.IsValidBlock(playerBlock.add(0, -1, 1))) {
               this.PlaceBlock(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
            } else if(this.IsValidBlock(playerBlock.add(1, -1, 1))) {
               if(this.IsValidBlock(playerBlock.add(0, -1, 1))) {
                  this.PlaceBlock(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
               }

               this.PlaceBlock(playerBlock.add(1, -1, 1), EnumFacing.EAST);
            } else if(this.IsValidBlock(playerBlock.add(-1, -1, 1))) {
               if(this.IsValidBlock(playerBlock.add(-1, -1, 0))) {
                  this.PlaceBlock(playerBlock.add(-1, -1, 0), EnumFacing.WEST);
               }

               this.PlaceBlock(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
            } else if(this.IsValidBlock(playerBlock.add(-1, -1, -1))) {
               if(this.IsValidBlock(playerBlock.add(0, -1, -1))) {
                  this.PlaceBlock(playerBlock.add(0, -1, -1), EnumFacing.SOUTH);
               }

               this.PlaceBlock(playerBlock.add(-1, -1, -1), EnumFacing.WEST);
            } else if(this.IsValidBlock(playerBlock.add(1, -1, -1))) {
               if(this.IsValidBlock(playerBlock.add(1, -1, 0))) {
                  this.PlaceBlock(playerBlock.add(1, -1, 0), EnumFacing.EAST);
               }

               this.PlaceBlock(playerBlock.add(1, -1, -1), EnumFacing.NORTH);
            }
         }
      }

   }
}
