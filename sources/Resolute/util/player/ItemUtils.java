// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.player;

import java.util.Iterator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;

public class ItemUtils
{
    protected static Minecraft mc;
    
    public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = ItemUtils.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBad(final ItemStack item) {
        return !(item.getItem() instanceof ItemArmor) && !(item.getItem() instanceof ItemTool) && !(item.getItem() instanceof ItemBlock) && !(item.getItem() instanceof ItemSword) && !(item.getItem() instanceof ItemEnderPearl) && !(item.getItem() instanceof ItemFood) && (!(item.getItem() instanceof ItemPotion) || isBadPotion(item)) && !item.getDisplayName().toLowerCase().contains(EnumChatFormatting.GRAY + "(right click)");
    }
    
    public static boolean isTrash(final ItemStack item) {
        return item.getItem().getUnlocalizedName().contains("tnt") || item.getDisplayName().contains("frog") || item.getItem().getUnlocalizedName().contains("stick") || item.getItem().getUnlocalizedName().contains("string") || item.getItem().getUnlocalizedName().contains("flint") || item.getItem().getUnlocalizedName().contains("feather") || item.getItem().getUnlocalizedName().contains("bucket") || item.getItem().getUnlocalizedName().contains("snow") || item.getItem().getUnlocalizedName().contains("enchant") || item.getItem().getUnlocalizedName().contains("exp") || item.getItem().getUnlocalizedName().contains("shears") || item.getItem().getUnlocalizedName().contains("arrow") || item.getItem().getUnlocalizedName().contains("anvil") || item.getItem().getUnlocalizedName().contains("torch") || item.getItem().getUnlocalizedName().contains("seeds") || item.getItem().getUnlocalizedName().contains("leather") || item.getItem().getUnlocalizedName().contains("boat") || item.getItem().getUnlocalizedName().contains("fishing") || item.getItem().getUnlocalizedName().contains("wheat") || item.getItem().getUnlocalizedName().contains("flower") || item.getItem().getUnlocalizedName().contains("record") || item.getItem().getUnlocalizedName().contains("note") || item.getItem().getUnlocalizedName().contains("sugar") || item.getItem().getUnlocalizedName().contains("wire") || item.getItem().getUnlocalizedName().contains("trip") || item.getItem().getUnlocalizedName().contains("slime") || item.getItem().getUnlocalizedName().contains("web") || item.getItem() instanceof ItemGlassBottle || item.getItem().getUnlocalizedName().contains("piston") || (item.getItem().getUnlocalizedName().contains("potion") && isBadPotion(item)) || item.getItem() instanceof ItemEgg || item.getItem() instanceof ItemSnow || (item.getItem().getUnlocalizedName().contains("bow") && !item.getDisplayName().contains("Kit")) || item.getItem().getUnlocalizedName().contains("Raw");
    }
    
    public static int getSwordSlot() {
        if (ItemUtils.mc.thePlayer == null) {
            return -1;
        }
        int bestSword = -1;
        float bestDamage = 1.0f;
        for (int i = 9; i < 45; ++i) {
            if (ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null && item.getItem() instanceof ItemSword) {
                    final ItemSword is = (ItemSword)item.getItem();
                    float damage = is.getDamageVsEntity();
                    damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, item) * 1.26f + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, item) * 0.01f;
                    if (damage > bestDamage) {
                        bestDamage = damage;
                        bestSword = i;
                    }
                }
            }
        }
        return bestSword;
    }
    
    public static int getEmptyHotbarSlot() {
        for (int k = 0; k < 9; ++k) {
            if (ItemUtils.mc.thePlayer.inventory.mainInventory[k] == null) {
                return k;
            }
        }
        return -1;
    }
    
    public static int getPickaxeSlot() {
        if (ItemUtils.mc.thePlayer == null) {
            return -1;
        }
        int bestSword = -1;
        float bestDamage = 1.0f;
        for (int i = 9; i < 45; ++i) {
            if (ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null && item.getItem() instanceof ItemPickaxe) {
                    final ItemPickaxe is = (ItemPickaxe)item.getItem();
                    final float damage = is.getStrVsBlock(item, Block.getBlockById(4));
                    if (damage > bestDamage) {
                        bestDamage = damage;
                        bestSword = i;
                    }
                }
            }
        }
        return bestSword;
    }
    
    public static int getAxeSlot() {
        if (ItemUtils.mc.thePlayer == null) {
            return -1;
        }
        int bestSword = -1;
        float bestDamage = 1.0f;
        for (int i = 9; i < 45; ++i) {
            if (ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null && item.getItem() instanceof ItemAxe) {
                    final ItemAxe is = (ItemAxe)item.getItem();
                    final float damage = is.getStrVsBlock(item, Block.getBlockById(17));
                    if (damage > bestDamage) {
                        bestDamage = damage;
                        bestSword = i;
                    }
                }
            }
        }
        return bestSword;
    }
    
    public static int getShovelSlot() {
        if (ItemUtils.mc.thePlayer == null) {
            return -1;
        }
        int bestSword = -1;
        float bestDamage = 1.0f;
        for (int i = 9; i < 45; ++i) {
            if (ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null && item.getItem() instanceof ItemTool) {
                    final ItemTool is = (ItemTool)item.getItem();
                    if (isShovel(is)) {
                        final float damage = is.getStrVsBlock(item, Block.getBlockById(3));
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestSword = i;
                        }
                    }
                }
            }
        }
        return bestSword;
    }
    
    public static boolean isShovel(final Item is) {
        return Item.getItemById(256) == is || Item.getItemById(269) == is || Item.getItemById(273) == is || Item.getItemById(277) == is || Item.getItemById(284) == is;
    }
    
    public static void shiftClick(final int k) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, k, 0, 1, Minecraft.getMinecraft().thePlayer);
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
    
    public static void swap(final int slot, final int hotbarNum) {
        ItemUtils.mc.playerController.windowClick(ItemUtils.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, ItemUtils.mc.thePlayer);
    }
    
    public static void drop(final int slot) {
        ItemUtils.mc.playerController.windowClick(ItemUtils.mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, ItemUtils.mc.thePlayer);
        ItemUtils.mc.playerController.windowClick(ItemUtils.mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, ItemUtils.mc.thePlayer);
    }
    
    public static float getSwordDamage(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        float damage = ((ItemSword)itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        return damage;
    }
    
    static {
        ItemUtils.mc = Minecraft.getMinecraft();
    }
}
