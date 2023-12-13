package team.massacre.utils;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

public final class PlayerUtils {
   public static Minecraft mc = Minecraft.getMinecraft();

   private PlayerUtils() {
   }

   public static boolean onHypixel() {
      ServerData serverData = mc.getCurrentServerData();
      if (serverData == null) {
         return false;
      } else {
         return serverData.serverIP.endsWith("hypixel.net") || serverData.serverIP.endsWith("hypixel.net:25565") || serverData.serverIP.equals("104.17.71.15") || serverData.serverIP.equals("104.17.71.15:25565");
      }
   }

   public static void openInventory() {
      PacketUtil.sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
   }

   public static void closeInventory() {
      PacketUtil.sendPacketNoEvent(new C0DPacketCloseWindow(getLocalPlayer().inventoryContainer.windowId));
   }

   public static boolean isValid(EntityLivingBase entity, boolean players, boolean monsters, boolean animals, boolean teams, boolean invisibles, boolean passives, double range) {
      if (entity == null) {
         return false;
      } else if (entity instanceof EntityPlayer && !players) {
         return false;
      } else if (entity instanceof EntityMob && !monsters) {
         return false;
      } else if (entity instanceof EntityVillager || entity instanceof EntityGolem && !passives) {
         return false;
      } else if (entity instanceof EntityAnimal && !animals) {
         return false;
      } else if (entity.isDead) {
         return false;
      } else if (entity.isInvisible() && !invisibles) {
         return false;
      } else if (mc.thePlayer.isOnSameTeam(entity) && teams) {
         return false;
      } else if (entity instanceof EntityBat) {
         return false;
      } else {
         return !(entity instanceof EntityArmorStand) && entity != mc.thePlayer && !((double)mc.thePlayer.getDistanceToEntity(entity) > range);
      }
   }

   public static int findInHotBar(Predicate<ItemStack> cond) {
      for(int i = 36; i < 45; ++i) {
         ItemStack stack = getStackInSlot(i);
         if (cond.test(stack)) {
            return i - 36;
         }
      }

      return -1;
   }

   public static ItemStack getStackInSlot(int index) {
      return getLocalPlayer().inventoryContainer.getSlot(index).getStack();
   }

   public static double getTotalArmorProtection(EntityPlayer player) {
      double totalArmor = 0.0D;

      for(int i = 0; i < 4; ++i) {
         ItemStack armorStack = player.getCurrentArmor(i);
         if (armorStack != null && armorStack.getItem() instanceof ItemArmor) {
            totalArmor += getDamageReduction(armorStack);
         }
      }

      return totalArmor;
   }

   public static double getDamageReduction(ItemStack stack) {
      double reduction = 0.0D;
      ItemArmor armor = (ItemArmor)stack.getItem();
      reduction += (double)armor.damageReduceAmount;
      if (stack.isItemEnchanted()) {
         reduction += (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.25D;
      }

      return reduction;
   }

   public static EntityPlayerSP getLocalPlayer() {
      return Minecraft.getMinecraft().thePlayer;
   }
}
