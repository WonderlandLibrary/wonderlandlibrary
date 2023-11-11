package me.nyan.flush.utils.player;

import me.nyan.flush.Flush;
import me.nyan.flush.module.impl.combat.AntiBot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.Arrays;
import java.util.List;

public class PlayerUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static Flush flush = Flush.getInstance();

    public static boolean isOnHypixel() {
        return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.contains("hypixel");
    }

    public static boolean isBlockUnder() {
        for (int y = (int) (mc.thePlayer.posY - 1); y > 0; y--) {
            if (!(new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ).getBlock() instanceof BlockAir)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isHoldingSword() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    public static boolean isBlocking() {
        return isHoldingSword() && mc.thePlayer.isBlocking();
    }

    public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            if (mc.thePlayer.inventoryContainer.getSlot(index).getStack() == null)
                return false;
        }

        return true;
    }

    public static boolean isHoldingWeapon() {
        return mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword ||
                mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe);
    }

    public static List<String> getBadItems() {
        return Arrays.asList("stick", "egg", "string", "cake", "mushroom", "flint", "dyePowder", "feather", "chest", "snowball",
                             "enchant", "exp", "shears", "anvil", "torch", "seeds", "leather", "reeds", "skull", "record", "piston", "snow",
                             "button", "door", "fence", "sapling", "banner", "bucket", "ladder", "fishing");
    }


    public static boolean isValid(EntityLivingBase entity, boolean players, boolean creatures, boolean villagers, boolean invisibles,
                                  boolean ignoreTeam) {
        if (entity == null || entity == mc.thePlayer ||
                entity.isDead || entity instanceof EntityArmorStand ||
                entity.getHealth() <= 0 ||
                flush.getFriendManager().isFriend(entity) ||
                entity.getName().equalsIgnoreCase("[NPC]") ||
                entity.getName().isEmpty() ||
                flush.getModuleManager().getModule(AntiBot.class).isBot(entity)) {
            return false;
        }

        return (!entity.isInvisible() || invisibles) &&
                (!(entity instanceof IAnimals) || creatures) &&
                (!(entity instanceof EntityVillager) || villagers) &&
                (!(entity instanceof EntityOtherPlayerMP) || players &&
                        (!ignoreTeam || !mc.thePlayer.isOnSameTeam(entity)));
    }
}