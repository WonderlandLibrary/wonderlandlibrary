package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.BlockUtils;
import cn.hanabi.utils.ReflectionUtils;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.WorldSettings.GameType;

public class AutoMLG extends Mod {
   public Value delay = new Value("Auto MLG", "Delay", 100.0, 1.0, 1000.0, 50.0);
   boolean tick;

   public AutoMLG() {
      super("Auto MLG", Category.GHOST);
   }

   @EventTarget
   public void onMove(EventMove e) {
      if (this.tick) {
      }

   }

   @EventTarget
   public void onPre(EventPreMotion e) {
      if (mc.thePlayer.fallDistance > 4.0F && this.getSlotWaterBucket() != -1 && this.isMLGNeeded()) {
         mc.thePlayer.rotationPitch = 90.0F;
         this.swapToWaterBucket(this.getSlotWaterBucket());
      }

      if (mc.thePlayer.fallDistance > 4.0F && this.isMLGNeeded() && !mc.thePlayer.isOnLadder() && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
         BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - BlockUtils.getDistanceToFall() - 1.0, mc.thePlayer.posZ);
         this.placeWater(pos, EnumFacing.UP);
         if (mc.thePlayer.getHeldItem().getItem() == Items.bucket) {
            Thread thr = new Thread(() -> {
               try {
                  Thread.sleep(((Double)this.delay.getValue()).longValue());
               } catch (Exception var2) {
               }

               ReflectionUtils.rightClickMouse();
            });
            thr.start();
         }

         mc.thePlayer.fallDistance = 0.0F;
      }

   }

   private void swapToWaterBucket(int blockSlot) {
      mc.thePlayer.inventory.currentItem = blockSlot;
      mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(blockSlot));
   }

   private int getSlotWaterBucket() {
      for(int i = 0; i < 8; ++i) {
         if (mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].getItem().getUnlocalizedName().contains("bucketWater")) {
            return i;
         }
      }

      return -1;
   }

   private void placeWater(BlockPos pos, EnumFacing facing) {
      ItemStack heldItem = mc.thePlayer.inventory.getCurrentItem();
      mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), pos, facing, new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5));
      if (heldItem != null) {
         mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, heldItem);
         mc.entityRenderer.itemRenderer.resetEquippedProgress2();
      }

   }

   private boolean isMLGNeeded() {
      if (mc.playerController.getCurrentGameType() != GameType.CREATIVE && mc.playerController.getCurrentGameType() != GameType.SPECTATOR && !mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.allowFlying) {
         double y = Minecraft.getMinecraft().thePlayer.posY;

         while(true) {
            if (y > 0.0) {
               Block block = BlockUtils.getBlock(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, y, Minecraft.getMinecraft().thePlayer.posZ));
               if (block.getMaterial() == Material.water) {
                  return false;
               }

               if (block.getMaterial() != Material.air) {
                  return true;
               }

               if (!(y < 0.0)) {
                  --y;
                  continue;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
