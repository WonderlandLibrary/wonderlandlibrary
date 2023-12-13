package cn.hanabi.modules.modules.player;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPacket;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.InvUtils;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

@ObfuscationClass
public class AutoTools extends Mod {
   public Value sword = new Value("AutoTools", "Sword", false);
   public Value mouseCheck = new Value("AutoTools", "Check Mouse", false);

   public AutoTools() {
      super("AutoTools", Category.PLAYER);
   }

   public Entity getItems(double range) {
      Entity tempEntity = null;
      double dist = range;
      Iterator var6 = mc.theWorld.loadedEntityList.iterator();

      while(var6.hasNext()) {
         Entity i = (Entity)var6.next();
         if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && i instanceof EntityItem) {
            double curDist = (double)mc.thePlayer.getDistanceToEntity(i);
            if (curDist <= dist) {
               dist = curDist;
               tempEntity = i;
            }
         }
      }

      return tempEntity;
   }

   @EventTarget
   public void onAttack(EventPacket e) {
      if (e.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)e.getPacket()).getAction().equals(Action.ATTACK)) {
         boolean checks = !mc.thePlayer.isEating();
         if (checks && (Boolean)this.sword.getValue()) {
            this.bestSword();
         }
      }

   }

   @EventTarget
   public void onClickBlock(EventPacket e) {
      if (e.getPacket() instanceof C07PacketPlayerDigging) {
         C07PacketPlayerDigging packetPlayerDigging = (C07PacketPlayerDigging)e.getPacket();
         if (packetPlayerDigging.getStatus() == net.minecraft.network.play.client.C07PacketPlayerDigging.Action.START_DESTROY_BLOCK && (mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK || !(Boolean)this.mouseCheck.getValue()) && !mc.thePlayer.capabilities.isCreativeMode) {
            BlockPos blockPosHit = (Boolean)this.mouseCheck.getValue() ? mc.objectMouseOver.getBlockPos() : packetPlayerDigging.getPosition();
            if (blockPosHit != null || !(Boolean)this.mouseCheck.getValue()) {
               mc.thePlayer.inventory.currentItem = this.getBestTool(blockPosHit);
               mc.playerController.updateController();
            }
         }
      }

   }

   public void bestSword() {
      int bestSlot = 0;
      double f = -1.0;

      for(int i1 = 36; i1 < 45; ++i1) {
         if (mc.thePlayer.inventoryContainer.inventorySlots.toArray()[i1] != null) {
            ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
            if (curSlot != null && curSlot.getItem() instanceof ItemSword) {
               double dmg = ((AttributeModifier)curSlot.getAttributeModifiers().get("generic.attackDamage").toArray()[0]).getAmount() + (double)InvUtils.getEnchantment(curSlot, Enchantment.sharpness) * 1.25 + (double)InvUtils.getEnchantment(curSlot, Enchantment.fireAspect);
               if (dmg > f) {
                  bestSlot = i1 - 36;
                  f = dmg;
               }
            }
         }
      }

      if (f > -1.0) {
         mc.thePlayer.inventory.currentItem = bestSlot;
         mc.playerController.updateController();
      }

   }

   private int getBestTool(BlockPos pos) {
      Block block = mc.theWorld.getBlockState(pos).getBlock();
      int slot = 0;
      float dmg = 0.1F;

      for(int index = 36; index < 45; ++index) {
         ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
         if (itemStack != null && block != null && itemStack.getItem().getStrVsBlock(itemStack, block) > dmg) {
            slot = index - 36;
            dmg = itemStack.getItem().getStrVsBlock(itemStack, block);
         }
      }

      if (dmg > 0.1F) {
         return slot;
      } else {
         return mc.thePlayer.inventory.currentItem;
      }
   }
}
