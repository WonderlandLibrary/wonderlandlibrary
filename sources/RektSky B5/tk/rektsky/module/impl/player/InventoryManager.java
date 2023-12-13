/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.IntSetting;
import tk.rektsky.utils.inventory.InventoryUtils;

public class InventoryManager
extends Module {
    public BooleanSetting openInventory = new BooleanSetting("OpenInv", false);
    public BooleanSetting keepSword = new BooleanSetting("KeepSword", true);
    public BooleanSetting keepArmor = new BooleanSetting("KeepArmor", true);
    public BooleanSetting keepBow = new BooleanSetting("KeepBow", true);
    public BooleanSetting keepGApple = new BooleanSetting("KeepGApple", true);
    public BooleanSetting keepPotions = new BooleanSetting("KeepPotions", true);
    public BooleanSetting keepBlocks = new BooleanSetting("KeepBlocks", true);
    public BooleanSetting keepAxe = new BooleanSetting("KeepAxe", true);
    public BooleanSetting keepPickaxe = new BooleanSetting("KeepPickaxe", true);
    public BooleanSetting keepShovel = new BooleanSetting("KeepShovel", true);
    public BooleanSetting keepFood = new BooleanSetting("KeepFood", true);
    public BooleanSetting keepBoat = new BooleanSetting("KeepBoat", true);
    public BooleanSetting keepCraftingTable = new BooleanSetting("KeepCraftingTable", true);
    public IntSetting delay = new IntSetting("Delay", 1, 20, 1);
    private int delayTimer = 0;

    public InventoryManager() {
        super("InvManager", "Keking rektsky since 2021 (Means we don't have inv manager in older versions)", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        this.delayTimer = 0;
    }

    @Override
    public void onDisable() {
    }

    public int getTrashSlot() {
        int i2;
        List<Slot> inventory = this.mc.thePlayer.inventoryContainer.inventorySlots;
        for (Slot slot : inventory) {
            ItemStack itemStack = slot.getStack();
            if (!this.isUseless(itemStack)) continue;
            return slot.slotNumber;
        }
        if (this.keepSword.getValue().booleanValue() && (i2 = this.compareSwords(inventory)) != -1) {
            return i2;
        }
        int toolResult = this.compareTools(inventory);
        if (toolResult != -1) {
            return toolResult;
        }
        return -1;
    }

    public int[] getMoves(List<Slot> inventory) {
        for (Slot slot : inventory) {
            if (slot.getStack() == null) continue;
            if (slot.getStack().getItem() instanceof ItemSword && this.keepSword.getValue().booleanValue() && slot.slotNumber != 36) {
                return new int[]{slot.slotNumber, 0};
            }
            if (slot.getStack().getItem() instanceof ItemPickaxe && this.keepPickaxe.getValue().booleanValue() && slot.slotNumber != 37) {
                return new int[]{slot.slotNumber, 1};
            }
            if (slot.getStack().getItem() instanceof ItemAxe && this.keepAxe.getValue().booleanValue() && slot.slotNumber != 38) {
                return new int[]{slot.slotNumber, 2};
            }
            if (!(slot.getStack().getItem() instanceof ItemTool) || !this.keepShovel.getValue().booleanValue() || !slot.getStack().getItem().getUnlocalizedName().contains("shovel") || slot.slotNumber == 39) continue;
            return new int[]{slot.slotNumber, 3};
        }
        return new int[0];
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        if (this.openInventory.getValue().booleanValue() && !(this.mc.currentScreen instanceof GuiContainer)) {
            return;
        }
        --this.delayTimer;
        if (this.delayTimer <= 0) {
            int windowId = 0;
            int slot = this.getTrashSlot();
            if (slot == -1) {
                int[] moves = this.getMoves(this.mc.thePlayer.inventoryContainer.inventorySlots);
                if (moves.length == 0) {
                    this.delayTimer = 0;
                    return;
                }
                this.delayTimer = this.delay.getValue();
                int to = moves[1];
                int from = moves[0];
                this.mc.playerController.windowClick(windowId, from, to, 2, this.mc.thePlayer);
                return;
            }
            this.mc.playerController.windowClick(windowId, slot, 1, 4, this.mc.thePlayer);
            this.delayTimer = this.delay.getValue();
        }
    }

    public int compareTools(List<Slot> inventory) {
        int shovelLowestLevel = -1;
        int pickaxeLowestLevel = -1;
        int axeLowestLevel = -1;
        int shovelSlot = -1;
        int pickaxeSlot = -1;
        int axeSlot = -1;
        int shovels = 0;
        int pickaxes = 0;
        int axes = 0;
        for (Slot slot : inventory) {
            int level;
            if (slot.getStack() == null) continue;
            if (this.keepShovel.getValue().booleanValue() && slot.getStack().getItem() instanceof ItemTool && slot.getStack().getItem().getUnlocalizedName().contains("shovel")) {
                level = 0;
                if (slot.getStack().getItem().getUnlocalizedName().contains("Wood")) {
                    level = 0;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Gold")) {
                    level = 1;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Stone")) {
                    level = 2;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Iron")) {
                    level = 3;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Diamond")) {
                    level = 4;
                }
                ++shovels;
                if (level < shovelLowestLevel || shovelLowestLevel == -1) {
                    shovelLowestLevel = level;
                    shovelSlot = slot.slotNumber;
                }
            }
            if (this.keepPickaxe.getValue().booleanValue() && slot.getStack().getItem() instanceof ItemPickaxe) {
                level = 0;
                if (slot.getStack().getItem().getUnlocalizedName().contains("Wood")) {
                    level = 0;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Gold")) {
                    level = 1;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Stone")) {
                    level = 2;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Iron")) {
                    level = 3;
                } else if (slot.getStack().getItem().getUnlocalizedName().contains("Diamond")) {
                    level = 4;
                }
                ++pickaxes;
                if (level < pickaxeLowestLevel || pickaxeLowestLevel == -1) {
                    pickaxeLowestLevel = level;
                    pickaxeSlot = slot.slotNumber;
                }
            }
            if (!this.keepAxe.getValue().booleanValue() || !(slot.getStack().getItem() instanceof ItemAxe)) continue;
            level = 0;
            if (slot.getStack().getItem().getUnlocalizedName().contains("Wood")) {
                level = 0;
            } else if (slot.getStack().getItem().getUnlocalizedName().contains("Gold")) {
                level = 1;
            } else if (slot.getStack().getItem().getUnlocalizedName().contains("Stone")) {
                level = 2;
            } else if (slot.getStack().getItem().getUnlocalizedName().contains("Iron")) {
                level = 3;
            } else if (slot.getStack().getItem().getUnlocalizedName().contains("Diamond")) {
                level = 4;
            }
            ++axes;
            if (level >= axeLowestLevel && axeLowestLevel != -1) continue;
            axeLowestLevel = level;
            axeSlot = slot.slotNumber;
        }
        if (shovelSlot != -1 && shovels > 1) {
            return shovelSlot;
        }
        if (pickaxeSlot != -1 && pickaxes > 1) {
            return pickaxeSlot;
        }
        if (axeSlot != -1 && axes > 1) {
            return axeSlot;
        }
        return -1;
    }

    public int compareSwords(List<Slot> inventory) {
        double lowest = -1.0;
        int slot = 0;
        int compared = 0;
        for (Slot i2 : inventory) {
            ItemStack itemStack = i2.getStack();
            if (itemStack == null || !(itemStack.getItem() instanceof ItemSword)) continue;
            double damage = InventoryUtils.getAttackDamage(itemStack);
            ++compared;
            if (!(damage < lowest) && lowest != -1.0) continue;
            lowest = damage;
            slot = i2.slotNumber;
        }
        if (compared >= 2) {
            return slot;
        }
        return -1;
    }

    public boolean isUseless(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (InventoryUtils.isBlock(itemStack)) {
            return this.keepBlocks.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemPotion) {
            return this.keepPotions.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemArmor) {
            return this.keepArmor.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemPickaxe) {
            return this.keepPickaxe.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemSword) {
            return this.keepSword.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemAxe) {
            return this.keepAxe.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemAppleGold) {
            return this.keepGApple.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemBow) {
            return this.keepBow.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemFood) {
            return this.keepFood.getValue() == false;
        }
        if (itemStack.getItem() == Items.paper) {
            return false;
        }
        if (itemStack.getItem() == Items.slime_ball) {
            return false;
        }
        if (itemStack.getItem().equals(Items.arrow)) {
            return this.keepBow.getValue() == false;
        }
        if (itemStack.getItem().equals(Items.boat)) {
            return this.keepBoat.getValue() == false;
        }
        if (itemStack.getItem() instanceof ItemBlock && ((ItemBlock)itemStack.getItem()).getBlock().equals(Blocks.crafting_table)) {
            return this.keepBoat.getValue() == false;
        }
        return true;
    }

    @Subscribe
    public void onPacket(PacketSentEvent event) {
    }
}

