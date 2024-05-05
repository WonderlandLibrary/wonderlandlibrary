// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.misc;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.Item;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.client.entity.EntityPlayerSP;
import Lavish.Client;
import java.util.Iterator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class PlayerUtil
{
    static Timer timer;
    
    static {
        PlayerUtil.timer = new Timer();
    }
    
    public static boolean isValid(final EntityLivingBase entity, final double range, final boolean invisible, final boolean teams, final boolean dead, final boolean players, final boolean animals, final boolean monsters) {
        if (entity == Minecraft.getMinecraft().thePlayer) {
            return false;
        }
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (invisible && entity.isInvisible()) {
            return false;
        }
        if (dead && (entity.isDead || entity.getHealth() <= 0.0f)) {
            return false;
        }
        if (teams && entity != null && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (isOnSameTeam(player)) {
                return false;
            }
        }
        return entity != null && entity != Minecraft.getMinecraft().thePlayer && ((entity instanceof EntityPlayer && players) || (entity instanceof EntityAnimal && animals) || (entity instanceof EntityMob && monsters) || (entity instanceof EntityVillager && animals)) && entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= range && !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc]");
    }
    
    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                    final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static int getHealthColor(final EntityLivingBase player) {
        final float f = player.getHealth();
        final float f2 = player.getMaxHealth();
        final float f3 = Math.max(0.0f, Math.min(f, f2) / f2);
        return Color.HSBtoRGB(f3 / 3.0f, 1.0f, 0.75f) | 0xFF000000;
    }
    
    public static void swapBackToItem() {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, 9, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 2, Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 9, 2, Minecraft.getMinecraft().thePlayer);
    }
    
    public static void swapToItem() {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 9, 2, Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, 9, Minecraft.getMinecraft().thePlayer.inventory.currentItem, 2, Minecraft.getMinecraft().thePlayer);
    }
    
    public static boolean isOnSameTeam(final EntityPlayer entity) {
        return entity.getTeam() != null && Minecraft.getMinecraft().thePlayer.getTeam() != null && entity.getDisplayName().getFormattedText().charAt(1) == Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().charAt(1);
    }
    
    public static boolean isHoldingSword() {
        return Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
    }
    
    public static boolean isBad(final ItemStack item) {
        return !(item.getItem() instanceof ItemArmor) && !(item.getItem() instanceof ItemTool) && !(item.getItem() instanceof ItemBlock) && !(item.getItem() instanceof ItemSword) && !(item.getItem() instanceof ItemEnderPearl) && !(item.getItem() instanceof ItemFood) && (!(item.getItem() instanceof ItemPotion) || isBadPotion(item)) && !item.getDisplayName().toLowerCase().contains(EnumChatFormatting.GRAY + "(right click)");
    }
    
    public static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    boolean validEntity(final EntityLivingBase entity, final double range) {
        final boolean players = (boolean)Client.instance.setmgr.getSettingByName("Players").getValBoolean();
        final boolean animals = (boolean)Client.instance.setmgr.getSettingByName("Animals").getValBoolean();
        if (Minecraft.getMinecraft().thePlayer.isEntityAlive() && !(entity instanceof EntityPlayerSP) && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= range) {
            if (entity instanceof EntityPlayer) {
                if (players) {
                    final EntityPlayer player = (EntityPlayer)entity;
                    return (player.isEntityAlive() || player.getHealth() != 0.0) && (!isTeam(Minecraft.getMinecraft().thePlayer, player) || !(boolean)Client.instance.setmgr.getSettingByName("Teams").getValBoolean()) && (!player.isInvisible() || (boolean)Client.instance.setmgr.getSettingByName("Invisibles").getValBoolean());
                }
            }
            else if (!entity.isEntityAlive()) {
                return false;
            }
            if (animals && (entity instanceof EntityMob || entity instanceof EntityIronGolem || entity instanceof EntityAnimal || entity instanceof EntityVillager)) {
                return !entity.getName().equals("Villager") || !(entity instanceof EntityVillager);
            }
        }
        return false;
    }
    
    public static boolean isTeam(final EntityPlayer e, final EntityPlayer e2) {
        if (e2.getTeam() != null && e.getTeam() != null) {
            final Character target = e2.getDisplayName().getFormattedText().charAt(1);
            final Character player = e.getDisplayName().getFormattedText().charAt(1);
            return target.equals(player);
        }
        return true;
    }
    
    public static boolean isBestArmor(final ItemStack stack, final int type) {
        final float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        }
        else if (type == 2) {
            strType = "chestplate";
        }
        else if (type == 3) {
            strType = "leggings";
        }
        else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static float getProtection(final ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor)stack.getItem();
            prot += (float)(armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) / 100.0);
        }
        return prot;
    }
    
    public static boolean isDuplicate(final ItemStack stack, final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is != stack && slot != i && is.getUnlocalizedName().equalsIgnoreCase(stack.getUnlocalizedName()) && !(is.getItem() instanceof ItemPotion) && !(is.getItem() instanceof ItemBlock)) {
                    if (is.getItem() instanceof ItemSword) {
                        if (getDamage(is) == getDamage(stack)) {
                            return true;
                        }
                    }
                    else if (is.getItem() instanceof ItemTool && getToolEffect(is) == getToolEffect(stack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static float getDamage(final ItemStack stack) {
        float damage = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemTool) {
            final ItemTool tool = (ItemTool)item;
            damage += tool.getMaxDamage();
        }
        if (item instanceof ItemSword) {
            final ItemSword sword = (ItemSword)item;
            damage += sword.getMaxDamage();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }
    
    private static float getToolEffect(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        final String name = item.getUnlocalizedName();
        final ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }
}
