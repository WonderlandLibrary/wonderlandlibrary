package xyz.cucumber.base.utils.game;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import xyz.cucumber.base.utils.Timer;

public class InventoryUtils
{
    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isInventoryOpen;
    
    public static Timer timer = new Timer();
    
    public static List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane,
			Blocks.ladder, Blocks.web, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water,
			Blocks.flowing_water, Blocks.lava, Blocks.ladder, Blocks.soul_sand, Blocks.ice, Blocks.packed_ice,
			Blocks.sand, Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.ender_chest, Blocks.torch,
			Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate,
			Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
			Blocks.stone_button, Blocks.tnt, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace,
			Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.gold_block,
			Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot);

    public static int getBlockSlot()
    {
        int item = -1;
        int stacksize = 0;

        if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) mc.thePlayer.getHeldItem().getItem()).getBlock()))return mc.thePlayer.inventory.currentItem;
        for (int i = 36; i < 45; ++i)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock()) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize >= stacksize)
            {
                item = i - 36;
                stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }

        return item;
    }
    
    public static ItemStack getBlockSlotInventory()
    {
        ItemStack item = null;
        int stacksize = 0;

        if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) mc.thePlayer.getHeldItem().getItem()).getBlock()))return mc.thePlayer.getHeldItem();
        for (int i = 9; i < 45; ++i)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock()) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize >= stacksize)
            {
                item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }

        return item;
    }
    
    public static int getCobwebSlot()
    {
        int item = -1;
        int stacksize = 0;

        for (int i = 36; i < 45; ++i)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock))
            {
            	ItemBlock block = (ItemBlock) mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
            	if(block.getBlock() == Blocks.web) {
            		item = i - 36;
            		stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            	}
            }
        }

        return item;
    }
    
    public static int getBucketSlot()
    {
        int item = -1;
        int stacksize = 0;

        for (int i = 36; i < 45; ++i)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() == Items.water_bucket))
            {
                item = i - 36;
                stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }

        return item;
    }
    
    public static ItemStack getBucketSlotInventory()
    {
    	ItemStack item = null;
        int stacksize = 0;

        for (int i = 9; i < 45; ++i)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() == Items.water_bucket))
            {
                item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }

        return item;
    }
    
    public static int getProjectileSlot()
    {
        int item = -1;
        int stacksize = 0;

        for (int i = 36; i < 45; ++i)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemSnowball || mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemEgg || mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemFishingRod) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize >= stacksize)
            {
                item = i - 36;
                stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }

        return item;
    }
    
    public static ItemStack getProjectileSlotInventory()
    {
    	ItemStack item = null;
        int stacksize = 0;

        for (int i = 9; i < 45; ++i)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemSnowball || mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemEgg || mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemFishingRod) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize >= stacksize)
            {
                item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }

        return item;
    }

    public static float getProtection(ItemStack stack)
    {
        float prot = 0.0F;

        if (stack.getItem() instanceof ItemArmor)
        {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)(prot + armor.damageReduceAmount + ((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D);
            prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0D);
            prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0D);
            prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0D);
            prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0D);
            prot = (float)(prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) / 100.0D);
        }

        return prot;
    }
    public static boolean isBestArmor(ItemStack stack, int type)
    {
        float prot = getProtection(stack);
        String strType = "";

        if (type == 1)
        {
            strType = "helmet";
        }
        else if (type == 2)
        {
            strType = "chestplate";
        }
        else if (type == 3)
        {
            strType = "leggings";
        }
        else if (type == 4)
        {
            strType = "boots";
        }

        if (!stack.getUnlocalizedName().contains(strType))
        {
            return false;
        }

        for (int i = 5; i < 45; i++)
        {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();

                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static void drop(int slot)
    {
        (Minecraft.getMinecraft()).playerController.windowClick((Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId, slot, 1, 4, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer);
    }
    public static void shiftClick(int slot)
    {
        (Minecraft.getMinecraft()).playerController.windowClick((Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId, slot, 0, 1, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer);
    }

    public static boolean isBadStack(ItemStack is, boolean preferSword, boolean keepTools)
    {
    	for (int type = 1; type < 5; type++) {
    		String strType = "";

            if (type == 1)
            {
                strType = "helmet";
            }
            else if (type == 2)
            {
                strType = "chestplate";
            }
            else if (type == 3)
            {
                strType = "leggings";
            }
            else if (type == 4)
            {
                strType = "boots";
            }
	    	if(is.getItem() instanceof ItemArmor && !isBestArmor(is, type) && is.getUnlocalizedName().contains(strType)) {
	    		return true;
	    	}
	    	if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack() && isBestArmor(mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack(), type) && mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack().getUnlocalizedName().contains(strType) && is.getUnlocalizedName().contains(strType)){
	    		return true;
	    	}
        }
        if ((is.getItem() instanceof ItemSword) && is != bestWeapon() && !preferSword)
        {
            return true;
        }

        if (is.getItem() instanceof ItemSword && is != bestSword() && preferSword)
        {
            return true;
        }

        if (is.getItem() instanceof ItemBow && is != bestBow())
        {
            return true;
        }

        if (keepTools)
        {
            if (is.getItem() instanceof ItemAxe && is != bestAxe() && (preferSword || is != bestWeapon()))
            {
                return true;
            }

            if (is.getItem() instanceof ItemPickaxe && is != bestPick() && (preferSword || is != bestWeapon()))
            {
                return true;
            }

            if (is.getItem() instanceof ItemSpade && is != bestShovel())
            {
                return true;
            }
        }
        else
        {
            if (is.getItem() instanceof ItemAxe && (preferSword || is != bestWeapon()))
            {
                return true;
            }

            if (is.getItem() instanceof ItemPickaxe && (preferSword || is != bestWeapon()))
            {
                return true;
            }

            if (is.getItem() instanceof ItemSpade)
            {
                return true;
            }
        }

        return false;
    }

    public static ItemStack bestWeapon()
    {
        ItemStack bestWeapon = null;
        float itemDamage = -1;

        for (int i = 9; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemSword || is.getItem() instanceof ItemAxe || is.getItem() instanceof ItemPickaxe)
                {
                    float toolDamage = getItemDamage(is);

                    if (toolDamage >= itemDamage)
                    {
                        itemDamage = getItemDamage(is);
                        bestWeapon = is;
                    }
                }
            }
        }

        return bestWeapon;
    }

    public static ItemStack bestSword()
    {
        ItemStack bestSword = null;
        float itemDamage = -1;

        for (int i = 9; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemSword)
                {
                    float swordDamage = getItemDamage(is);

                    if (swordDamage >= itemDamage)
                    {
                        itemDamage = getItemDamage(is);
                        bestSword = is;
                    }
                }
            }
        }

        return bestSword;
    }

    public static ItemStack bestBow()
    {
        ItemStack bestBow = null;
        float itemDamage = -1;

        for (int i = 9; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemBow)
                {
                    float bowDamage = getBowDamage(is);

                    if (bowDamage >= itemDamage)
                    {
                        itemDamage = getBowDamage(is);
                        bestBow = is;
                    }
                }
            }
        }

        return bestBow;
    }

    public static ItemStack bestAxe()
    {
        ItemStack bestTool = null;
        float itemSkill = -1;

        for (int i = 9; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemAxe)
                {
                    float toolSkill = getToolRating(is);

                    if (toolSkill >= itemSkill)
                    {
                        itemSkill = getToolRating(is);
                        bestTool = is;
                    }
                }
            }
        }

        return bestTool;
    }

    public static ItemStack bestPick()
    {
        ItemStack bestTool = null;
        float itemSkill = -1;

        for (int i = 9; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemPickaxe)
                {
                    float toolSkill = getToolRating(is);

                    if (toolSkill >= itemSkill)
                    {
                        itemSkill = getToolRating(is);
                        bestTool = is;
                    }
                }
            }
        }

        return bestTool;
    }

    public static ItemStack bestShovel()
    {
        ItemStack bestTool = null;
        float itemSkill = -1;

        for (int i = 9; i < 45; i++)
        {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
            {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemSpade)
                {
                    float toolSkill = getToolRating(is);

                    if (toolSkill >= itemSkill)
                    {
                        itemSkill = getToolRating(is);
                        bestTool = is;
                    }
                }
            }
        }

        return bestTool;
    }

    public static float getToolRating(ItemStack itemStack)
    {
        float damage = getToolMaterialRating(itemStack, false);
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack) * 2.00F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.10F;
        damage += (itemStack.getMaxDamage() - itemStack.getItemDamage()) * 0.000000000001F;
        return damage;
    }

    public static float getItemDamage(ItemStack itemStack)
    {
        float damage = getToolMaterialRating(itemStack, true);
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.01F;
        damage += (itemStack.getMaxDamage() - itemStack.getItemDamage()) * 0.000000000001F;

        if (itemStack.getItem() instanceof ItemSword)
        {
            damage += 0.2;
        }

        return damage;
    }

    public static float getBowDamage(ItemStack itemStack)
    {
        float damage = 5;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack) * 1.25F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack) * 0.75F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) * 0.50F;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.10F;
        damage += itemStack.getMaxDamage() - itemStack.getItemDamage() * 0.001F;
        return damage;
    }

    public static float getToolMaterialRating(ItemStack itemStack, boolean checkForDamage)
    {
        final Item is = itemStack.getItem();
        float rating = 0;

        if (is instanceof ItemSword)
        {
            switch (((ItemSword) is).getToolMaterialName())
            {
                case "WOOD":
                    rating = 4;
                    break;

                case "GOLD":
                    rating = 4;
                    break;

                case "STONE":
                    rating = 5;
                    break;

                case "IRON":
                    rating = 6;
                    break;

                case "EMERALD":
                    rating = 7;
                    break;
            }
        }
        else if (is instanceof ItemPickaxe)
        {
            switch (((ItemPickaxe) is).getToolMaterialName())
            {
                case "WOOD":
                    rating = 2;
                    break;

                case "GOLD":
                    rating = 2;
                    break;

                case "STONE":
                    rating = 3;
                    break;

                case "IRON":
                    rating = checkForDamage ? 4 : 40;
                    break;

                case "EMERALD":
                    rating = checkForDamage ? 5 : 50;
                    break;
            };
        }
        else if (is instanceof ItemAxe)
        {
            switch (((ItemAxe) is).getToolMaterialName())
            {
                case "WOOD":
                    rating = 3;
                    break;

                case "GOLD":
                    rating = 3;
                    break;

                case "STONE":
                    rating = 4;
                    break;

                case "IRON":
                    rating = 5;
                    break;

                case "EMERALD":
                    rating = 6;
                    break;
            };
        }
        else if (is instanceof ItemSpade)
        {
            switch (((ItemSpade) is).getToolMaterialName())
            {
                case "WOOD":
                    rating = 1;
                    break;

                case "GOLD":
                    rating = 1;
                    break;

                case "STONE":
                    rating = 2;
                    break;

                case "IRON":
                    rating = 3;
                    break;

                case "EMERALD":
                    rating = 4;
                    break;
            }
        }

        return rating;
    }
    public static void openInv(String mode)
    {
        if (mode.equalsIgnoreCase("Spoof"))
        {
            
            if (!InventoryUtils.isInventoryOpen && !(mc.currentScreen instanceof GuiInventory))
            {
            	mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                InventoryUtils.isInventoryOpen = true;
            }
        }
    }
    public static void closeInv(String mode)
    {
        if (mode.equalsIgnoreCase("Spoof"))
        {
            if (InventoryUtils.isInventoryOpen && !(mc.currentScreen instanceof GuiInventory))
            {
            	mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
                for (KeyBinding bind : moveKeys)
                {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
                }
                InventoryUtils.isInventoryOpen = false;
            }
        }
    }

    static KeyBinding[] moveKeys = new KeyBinding[]
    {
        mc.gameSettings.keyBindForward,
        mc.gameSettings.keyBindBack,
        mc.gameSettings.keyBindLeft,
        mc.gameSettings.keyBindRight,
        mc.gameSettings.keyBindJump,
        mc.gameSettings.keyBindSneak
    };
}
