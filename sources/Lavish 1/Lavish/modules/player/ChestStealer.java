// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemAxe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemTool;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.client.gui.inventory.GuiChest;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class ChestStealer extends Module
{
    public boolean priority;
    public boolean autoClose;
    public int randomization;
    public Timer startDelay;
    public Timer closeDelay;
    Timer timer;
    public boolean wasNotChest;
    private static final Timer inventoryTimer;
    
    static {
        inventoryTimer = new Timer();
    }
    
    public ChestStealer() {
        super("ChestStealer", 0, true, Category.Player, "Automatically steals items out of a chest");
        this.priority = true;
        this.autoClose = true;
        this.randomization = 1;
        this.startDelay = new Timer();
        this.closeDelay = new Timer();
        this.timer = new Timer();
        Client.instance.setmgr.rSetting(new Setting("Close", this, true));
        Client.instance.setmgr.rSetting(new Setting("ChestStealer Speed", this, 20.0, 1.0, 500.0, true));
    }
    
    @Override
    public void onUpdate() {
        if (this.wasNotChest && ChestStealer.mc.currentScreen instanceof GuiChest) {
            this.startDelay.reset();
        }
        this.wasNotChest = !(ChestStealer.mc.currentScreen instanceof GuiChest);
        if (ChestStealer.mc.currentScreen instanceof GuiChest && this.startDelay.getTime() > 250L && this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("ChestStealer Speed").getValDouble(), true)) {
            final GuiChest chestGui = (GuiChest)ChestStealer.mc.currentScreen;
            final Container container = chestGui.inventorySlots;
            final String name = chestGui.lowerChestInventory.getDisplayName().getUnformattedText();
            if (!name.equals("Large Chest") && !name.equals("Chest") && !name.equals("LOW")) {
                return;
            }
            boolean shouldClose = true;
            for (final Slot slot : container.inventorySlots) {
                final int id = slot.slotNumber;
                final ItemStack item = slot.getStack();
                if (id <= 26 && item != null && this.shouldSteal(item)) {
                    shouldClose = false;
                    for (final Slot playerSlot : ChestStealer.mc.thePlayer.inventoryContainer.inventorySlots) {
                        final int playerSlotId = playerSlot.slotNumber;
                        final boolean empty = !playerSlot.getHasStack();
                        if (empty) {
                            ChestStealer.mc.playerController.windowClick(container.windowId, id, 0, 1, ChestStealer.mc.thePlayer);
                            this.closeDelay.reset();
                            return;
                        }
                    }
                }
            }
            if (this.closeDelay.getTime() > 150L && shouldClose && this.autoClose) {
                ChestStealer.mc.thePlayer.closeScreen();
                this.startDelay.reset();
            }
        }
    }
    
    public static boolean invCooldownElapsed(final long time) {
        return ChestStealer.inventoryTimer.hasTimeElapsed((double)time, true);
    }
    
    @Override
    public void onEnable() {
        ChestStealer.inventoryTimer.reset();
    }
    
    public boolean isBestArmor(final ItemStack compareStack) {
        for (int i = 0; i < 45; ++i) {
            final Slot slot = ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i);
            final ItemStack stack = slot.getStack();
            final boolean hotbar = i >= 36;
            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemArmor) {
                final ItemArmor item = (ItemArmor)stack.getItem();
                final ItemArmor compare = (ItemArmor)compareStack.getItem();
                if (item.armorType == compare.armorType && AutoArmor.getProtection(compareStack) <= AutoArmor.getProtection(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static float getSwordStrength(final ItemStack stack) {
        return ((stack.getItem() instanceof ItemSword) ? (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f) : 0.0f) + ((stack.getItem() instanceof ItemSword) ? (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1.0f) : 0.0f);
    }
    
    public boolean isBestSword(final ItemStack compareStack) {
        for (int i = 9; i < 45; ++i) {
            final Slot slot = ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i);
            final ItemStack stack = slot.getStack();
            final boolean hotbar = i >= 36;
            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemSword) {
                final ItemSword item = (ItemSword)stack.getItem();
                final ItemSword compare = (ItemSword)compareStack.getItem();
                if (item.getClass() == compare.getClass() && compare.attackDamage + getSwordStrength(compareStack) <= item.attackDamage + getSwordStrength(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isBestTool(final ItemStack compareStack) {
        for (int i = 9; i < 45; ++i) {
            final Slot slot = ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i);
            final ItemStack stack = slot.getStack();
            final boolean hotbar = i >= 36;
            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemTool) {
                final ItemTool item = (ItemTool)stack.getItem();
                final ItemTool compare = (ItemTool)compareStack.getItem();
                if (item.getClass() == compare.getClass() && compare.getStrVsBlock(stack, this.preferredBlock(item.getClass())) <= item.getStrVsBlock(compareStack, this.preferredBlock(compare.getClass()))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Block preferredBlock(final Class clazz) {
        return (clazz == ItemPickaxe.class) ? Blocks.cobblestone : ((clazz == ItemAxe.class) ? Blocks.log : Blocks.dirt);
    }
    
    public boolean shouldSteal(final ItemStack itemStack) {
        final Item item = itemStack.getItem();
        if (!this.priority) {
            return true;
        }
        if (item instanceof ItemSword) {
            return this.isBestSword(itemStack);
        }
        if (item instanceof ItemTool) {
            return this.isBestTool(itemStack);
        }
        if (item instanceof ItemFood) {
            return true;
        }
        if (item instanceof ItemBlock) {
            return true;
        }
        if (item instanceof ItemEnderPearl) {
            return true;
        }
        if (item instanceof ItemArmor) {
            return this.isBestArmor(itemStack);
        }
        return item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemMonsterPlacer || item instanceof ItemBow || item instanceof ItemPotion || item instanceof ItemFishingRod || item == Items.arrow;
    }
}
