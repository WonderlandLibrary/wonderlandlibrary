package wtf.diablo.utils.player;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;

public class InventoryUtil {
    public static void switchToSlot(int slot) {
        (Minecraft.getMinecraft()).thePlayer.inventory.currentItem = slot - 1;
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot - 1));
    }

    public static void shiftClick(int slot, int windowId) {
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot, 0, 1, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void shiftClick(int slot) {
        shiftClick(slot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static void drop(int slot, int windowId) {
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot, 1, 4, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void drop(int slot) {
        shiftClick(slot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static void click(int slot, int windowId) {
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot, 0, 0, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void click(int slot) {
        shiftClick(slot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static void swap(int slot1, int hotbarSlot, int windowId) {
        if (hotbarSlot == slot1 - 36)
            return;
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot1, hotbarSlot, 2, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void swap(int slot1, int hotbarSlot) {
        swap(slot1, hotbarSlot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static float getDamage(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSword) {
            double damage = 4.0 + ((ItemSword) itemStack.getItem()).getDamageVsEntity();
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25;
            return (float) damage;
        }
        if (itemStack.getItem() instanceof ItemTool) {
            double damage = 1.0 + ((ItemTool) itemStack.getItem()).getToolMaterial().getDamageVsEntity();
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
            return (float) damage;
        }
        return 1.0f;
    }

    public static boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof net.minecraft.item.ItemPickaxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemPickaxe)
                    return false;
            }
        }
        return true;
    }

    public static boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof net.minecraft.item.ItemSpade))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemSpade)
                    return false;
            }
        }
        return true;
    }

    public static boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof net.minecraft.item.ItemAxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemAxe && !isBestWeapon(stack))
                    return false;
            }
        }
        return true;
    }

    public static float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool))
            return 0.0F;
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool) item;
        float value;
        if (item instanceof net.minecraft.item.ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold"))
                value -= 5.0F;
        } else if (item instanceof net.minecraft.item.ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold"))
                value -= 5.0F;
        } else if (item instanceof net.minecraft.item.ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold"))
                value -= 5.0F;
        } else {
            return 1.0F;
        }
        value = (float) (value + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D);
        value = (float) (value + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0D);
        return value;
    }

    public static boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion) stack.getItem();
            if (potion.getEffects(stack) == null)
                return true;
            for (PotionEffect o : potion.getEffects(stack)) {
                if (o.getPotionID() == Potion.poison.getId() || o.getPotionID() == Potion.harm.getId() || o.getPotionID() == Potion.moveSlowdown.getId() || o.getPotionID() == Potion.weakness.getId())
                    return true;
            }
        }
        return false;
    }

    public static boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && is.getItem() instanceof ItemSword)
                    return false;
            }
        }
        return stack.getItem() instanceof ItemSword;
    }

    public static Slot getBestSwordSlot() {
        Slot bestSword = null;
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Slot slot = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i);
                if (slot.getStack().getItem() instanceof ItemSword &&
                        isBestWeapon(slot.getStack()))
                    bestSword = slot;
            }
        }
        return bestSword;
    }

    public static Slot getBestPickaxeSlot() {
        Slot bestPickaxe = null;
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Slot slot = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i);
                if (slot.getStack().getItem() instanceof net.minecraft.item.ItemPickaxe &&
                        isBestPickaxe(slot.getStack()))
                    bestPickaxe = slot;
            }
        }
        return bestPickaxe;
    }

    public static Slot getBestAxeSlot() {
        Slot bestAxe = null;
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Slot slot = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i);
                if (slot.getStack().getItem() instanceof net.minecraft.item.ItemAxe &&
                        isBestAxe(slot.getStack()))
                    bestAxe = slot;
            }
        }
        return bestAxe;
    }

    public static boolean isInventoryFull() {
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = (Minecraft.getMinecraft()).thePlayer.inventory.mainInventory).length, b = 0; b < i; ) {
            ItemStack stack = arrayOfItemStack[b];
            if (stack == null || stack.getItem() == null)
                return false;
            b++;
        }
        return true;
    }

    public static boolean isBestArmor(ItemStack stack) {
        Minecraft mc = Minecraft.getMinecraft();
        for (int type = 1; type < 5; type++) {
            if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                ItemStack item = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (!getBestArmor(item, type)) {
                    return false;
                }
            } else for (int i = 9; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (getBestArmor(is, type) && getProtection(is) > 0.0F) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean getBestArmor(ItemStack stack, int type) {
        float prot = getProtection(stack);
        String strType = "";
        switch (type) {
            case 1:
                strType = "helmet";
                break;
            case 2:
                strType = "chestplate";
                break;
            case 3:
                strType = "leggings";
                break;
            case 4:
                strType = "boots";
                break;
        }

        if (!stack.getUnlocalizedName().contains(strType)) return false;
        for (int i = 5; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) return false;
            }
        }
        return true;
    }

    public static boolean isBestHelmet(ItemStack stack) {
        float prot = getProtection(stack);
        String strType = "helmet";

        if (!stack.getUnlocalizedName().contains(strType)) return false;
        for (int i = 5; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) return false;
            }
        }
        return true;
    }

    public static boolean isBestChestplate(ItemStack stack) {
        float prot = getProtection(stack);
        String strType = "chestplate";

        if (!stack.getUnlocalizedName().contains(strType)) return false;
        for (int i = 5; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) return false;
            }
        }
        return true;
    }

    public static boolean isBestLeggings(ItemStack stack) {
        float prot = getProtection(stack);
        String strType = "leggings";

        if (!stack.getUnlocalizedName().contains(strType)) return false;
        for (int i = 5; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) return false;
            }
        }
        return true;
    }

    public static boolean isBestBoots(ItemStack stack) {
        float prot = getProtection(stack);
        String strType = "boots";

        if (!stack.getUnlocalizedName().contains(strType)) return false;
        for (int i = 5; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) return false;
            }
        }
        return true;
    }

    public static String getArmorType(ItemStack stack){
        String type = "";
        if(stack.getUnlocalizedName().contains("helmet")){
            type = "helmet";
        }
        if(stack.getUnlocalizedName().contains("chestplate")){
            type = "chestplate";
        }
        if(stack.getUnlocalizedName().contains("leggings")){
            type = "leggings";
        }
        if(stack.getUnlocalizedName().contains("boots")){
            type = "boots";
        }
        return type;
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0F;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            prot = (float) (prot + armor.damageReduceAmount + ((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0D);
        }
        return prot;
    }
    public static boolean itemWhitelisted(final ItemStack itemStack) {
        final ArrayList<Item> whitelistedItems = new ArrayList<Item>() {{
            add(Items.ender_pearl);
            add(Items.iron_ingot);
            add(Items.gold_ingot);
            add(Items.redstone);
            add(Items.diamond);
            add(Items.emerald);
            add(Items.quartz);
            add(Items.bow);
            add(Items.arrow);
            add(Items.fishing_rod);
        }};

        final Item item = itemStack.getItem();
        final String itemName = itemStack.getDisplayName();

        if (itemName.contains("Right Click") || itemName.contains("Click to Use") || itemName.contains("Players Finder"))
            return true;

        final ArrayList<Integer> whitelistedPotions = new ArrayList<Integer>() {{
            add(6);
            add(1);
            add(5);
            add(8);
            add(14);
            add(12);
            add(10);
            add(16);
        }};

        if (item instanceof ItemPotion) {
            final int potionID = getPotionId(itemStack);
            return whitelistedPotions.contains(potionID);
        }

        return (item instanceof ItemBlock
                && !(((ItemBlock) item).getBlock() instanceof BlockTNT)
                && !(((ItemBlock) item).getBlock() instanceof BlockSlime)
                && !(((ItemBlock) item).getBlock() instanceof BlockFalling))
                || item instanceof ItemAnvilBlock
                || item instanceof ItemSword
                || item instanceof ItemArmor
                || item instanceof ItemTool
                || item instanceof ItemFood
                || whitelistedItems.contains(item)
                && !item.equals(Items.spider_eye);
    }

    private static int getPotionId(final ItemStack potion) {
        final Item item = potion.getItem();
        try { if (item instanceof ItemPotion) {
            final ItemPotion p = (ItemPotion) item;
            return p.getEffects(potion.getMetadata()).get(0).getPotionID();
        }} catch (final NullPointerException ignored) {}
        return 0;
    }
    public static float getMineSpeed(final ItemStack itemStack) {
        final Item item = itemStack.getItem();
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);

        //Percentage of Efficiency per Level
        switch (level) {
            case 1:
                level = 30;
                break;
            case 2:
                level = 69;
                break;
            case 3:
                level = 120;
                break;
            case 4:
                level = 186;
                break;
            case 5:
                level = 271;
                break;

            default:
                level = 0;
                break;
        }

        if (item instanceof ItemPickaxe) {
            return ((ItemPickaxe) item).getToolMaterial().getEfficiencyOnProperMaterial() + level;
        } else if (item instanceof ItemSpade) {
            return ((ItemSpade) item).getToolMaterial().getEfficiencyOnProperMaterial() + level;
        } else if (item instanceof ItemAxe) {
            return ((ItemAxe) item).getToolMaterial().getEfficiencyOnProperMaterial() + level;
        }

        return 0;
    }

}
