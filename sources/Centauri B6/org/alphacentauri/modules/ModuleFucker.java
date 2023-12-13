package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.events.EventWorldChanged;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.BlockUtils;
import org.alphacentauri.management.util.RaycastUtils;

public class ModuleFucker extends Module implements EventListener {
   private Property range = new Property(this, "StartRange", Float.valueOf(4.0F));
   private Property stoprange = new Property(this, "StopRange", Float.valueOf(6.0F));
   private Property blockID = new Property(this, "BlockID", Integer.valueOf(26));
   private Property face = new Property(this, "Rotate", Boolean.valueOf(true));
   private Property instabreak = new Property(this, "InstantBreak", Boolean.valueOf(true));
   private Property raycast = new Property(this, "Raycast", Boolean.valueOf(false));
   private Property swing = new Property(this, "Swing", Boolean.valueOf(true));
   private Property breakSpeed = new Property(this, "BreakSpeed", Float.valueOf(1.0F));
   private static Block currentBlock;
   private float currentDamage;
   private EnumFacing side = EnumFacing.UP;
   private byte blockHitDelay = 0;
   private BlockPos pos;

   public ModuleFucker() {
      super("Fucker", "Fucks blocks", new String[]{"fucker", "bedfucker", "blockfucker"}, Module.Category.World, 6457893);
   }

   private BlockPos find() {
      LinkedList<BlockPos> queue = new LinkedList();
      HashSet<BlockPos> alreadyProcessed = new HashSet();
      queue.add(new BlockPos(AC.getMC().getPlayer()));

      while(!queue.isEmpty()) {
         BlockPos currentPos = (BlockPos)queue.poll();
         if(!alreadyProcessed.contains(currentPos)) {
            alreadyProcessed.add(currentPos);
            if(BlockUtils.getPlayerBlockDistanceSq(currentPos) <= (double)(((Float)this.range.value).floatValue() * ((Float)this.range.value).floatValue())) {
               int currentID = Block.getIdFromBlock(AC.getMC().getWorld().getBlockState(currentPos).getBlock());
               if(currentID != 0 && currentID == ((Integer)this.blockID.value).intValue()) {
                  return currentPos;
               }

               if(this.getBypass() != AntiCheat.NCP || !AC.getMC().getWorld().getBlockState(currentPos).getBlock().getMaterial().blocksMovement()) {
                  queue.add(currentPos.north());
                  queue.add(currentPos.south());
                  queue.add(currentPos.west());
                  queue.add(currentPos.east());
                  queue.add(currentPos.down());
                  queue.add(currentPos.up());
               }
            }
         }
      }

      return null;
   }

   public ArrayList autocomplete(Command cmd) {
      ArrayList<String> ret = new ArrayList();
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         String[] opts = new String[]{"core", "bed"};

         for(String opt : opts) {
            if(opt.toLowerCase().startsWith(args[0].toLowerCase())) {
               ret.add(opt);
            }
         }
      }

      ret.addAll(super.autocomplete(cmd));
      return ret;
   }

   public boolean onCommand(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 1) {
         if(args[0].toLowerCase().startsWith("core")) {
            this.blockID.value = Integer.valueOf(138);
            AC.addChat(this.getName(), "Now fucking Cores");
            return true;
         }

         if(args[0].toLowerCase().startsWith("bed")) {
            this.blockID.value = Integer.valueOf(26);
            AC.addChat(this.getName(), "Now fucking beds");
            return true;
         }
      }

      return super.onCommand(cmd);
   }

   public void onEvent(Event event) {
      if(!(event instanceof EventTick)) {
         if(event instanceof EventWorldChanged) {
            Block currentBlock = null;
            float currentDamage = 0.0F;
            EnumFacing side = EnumFacing.UP;
            byte blockHitDelay = 0;
            Object var6 = null;
         }
      } else {
         EntityPlayerSP player = AC.getMC().getPlayer();
         WorldClient world = AC.getMC().getWorld();
         BlockPos newPos = this.find();
         if(this.pos == null || newPos != null && !this.pos.equals(newPos)) {
            this.currentDamage = 0.0F;
         }

         if(this.pos != null && BlockUtils.getPlayerBlockDistanceSq(this.pos) > (double)(((Float)this.stoprange.value).floatValue() * ((Float)this.stoprange.value).floatValue())) {
            this.pos = null;
         }

         if(newPos != null || this.pos != null && world.isAirBlock(this.pos)) {
            this.pos = newPos;
         }

         if(this.pos == null) {
            return;
         }

         if(((Boolean)this.raycast.value).booleanValue() && !RaycastUtils.canSeeBlock(this.pos)) {
            return;
         }

         currentBlock = world.getBlockState(this.pos).getBlock();
         if(this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return;
         }

         if(((Boolean)this.face.value).booleanValue()) {
            BlockUtils.faceBlockPacket(this.pos);
         }

         if(this.currentDamage == 0.0F) {
            player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, this.pos, this.side));
            if(player.capabilities.isCreativeMode || currentBlock.getPlayerRelativeBlockHardness(player, world, this.pos) >= 1.0F) {
               this.currentDamage = 0.0F;
               player.swingItem();
               AC.getMC().playerController.onPlayerDestroyBlock(this.pos, this.side);
               return;
            }
         }

         if(((Boolean)this.swing.value).booleanValue()) {
            player.sendQueue.addToSendQueue(new C0APacketAnimation());
         }

         this.currentDamage += currentBlock.getPlayerRelativeBlockHardness(player, world, this.pos) * ((Float)this.breakSpeed.value).floatValue();
         world.sendBlockBreakProgress(player.getEntityId(), this.pos, (int)(this.currentDamage * 10.0F) - 1);
         if(this.currentDamage >= 1.0F) {
            player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.pos, this.side));
            AC.getMC().playerController.onPlayerDestroyBlock(this.pos, this.side);
            this.blockHitDelay = 4;
            this.currentDamage = 0.0F;
         } else if(((Boolean)this.instabreak.value).booleanValue()) {
            player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.pos, this.side));
         }
      }

   }
}
