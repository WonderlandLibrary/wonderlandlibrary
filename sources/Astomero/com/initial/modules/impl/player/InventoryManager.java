package com.initial.modules.impl.player;

import com.initial.settings.impl.*;
import com.initial.settings.*;
import com.initial.utils.player.*;
import com.initial.modules.*;
import net.minecraft.network.play.client.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import net.minecraft.client.gui.inventory.*;
import java.util.concurrent.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.enchantment.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

public class InventoryManager extends Module
{
    public DoubleSet delaySet;
    public DoubleSet randomizationSet;
    public BooleanSet cleanBad;
    public ModuleCategory limitsCategory;
    public DoubleSet blocksLimit;
    public ModuleCategory slotsCategory;
    public DoubleSet swordSlot;
    public DoubleSet axeSlot;
    public DoubleSet pickaxeSlot;
    public DoubleSet gappleSlot;
    public DoubleSet blockSlot;
    public DoubleSet potionSlot;
    public ModuleCategory checksCategory;
    public BooleanSet onlyInventory;
    public BooleanSet editCheck;
    public BooleanSet noInventoryCheck;
    public Setting[] settingList;
    private TimerHelper delayTimer;
    
    public void addSettingsToCats() {
        this.limitsCategory.addCatSettings(this.blocksLimit);
        this.slotsCategory.addCatSettings(this.swordSlot, this.axeSlot, this.pickaxeSlot, this.gappleSlot, this.blockSlot, this.potionSlot);
        this.checksCategory.addCatSettings(this.onlyInventory, this.editCheck, this.noInventoryCheck);
    }
    
    public InventoryManager() {
        super("InventoryManager", 0, Category.PLAYER);
        this.delaySet = new DoubleSet("Delay", 200.0, 20.0, 1000.0, 5.0, " ms");
        this.randomizationSet = new DoubleSet("Randomization", 20.0, 0.0, 400.0, 5.0, " ms");
        this.cleanBad = new BooleanSet("Clean", true);
        this.limitsCategory = new ModuleCategory("Limits...");
        this.blocksLimit = new DoubleSet("Blocks", 64.0, 0.0, 320.0, 16.0);
        this.slotsCategory = new ModuleCategory("Slots...");
        this.swordSlot = new DoubleSet("Sword Slot", 1.0, 1.0, 9.0, 1.0);
        this.axeSlot = new DoubleSet("Axe Slot", 2.0, 1.0, 9.0, 1.0);
        this.pickaxeSlot = new DoubleSet("Pickaxe Slot", 3.0, 1.0, 9.0, 1.0);
        this.gappleSlot = new DoubleSet("Gapple Slot", 4.0, 1.0, 9.0, 1.0);
        this.blockSlot = new DoubleSet("Block Slot", 8.0, 1.0, 9.0, 1.0);
        this.potionSlot = new DoubleSet("Potion Slot", 7.0, 1.0, 9.0, 1.0);
        this.checksCategory = new ModuleCategory("Checks...");
        this.onlyInventory = new BooleanSet("Only Inventory", false);
        this.editCheck = new BooleanSet("Edit Check", true);
        this.noInventoryCheck = new BooleanSet("No ChestGui", true);
        this.settingList = new Setting[] { this.delaySet, this.randomizationSet, this.cleanBad, this.limitsCategory, this.slotsCategory, this.checksCategory };
        this.delayTimer = new TimerHelper();
        this.addSettings(this.settingList);
        this.addSettingsToCats();
    }
    
    @EventTarget
    public void onPacket(final EventSendPacket e) {
        if (e.getPacket() instanceof C0DPacketCloseWindow || e.getPacket() instanceof C07PacketPlayerDigging || e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            this.delayTimer.resetWithOffset(150L);
        }
    }
    
    @EventTarget
    public void onMotion(final EventMotion e) {
        this.setDisplayName("Inventory Manager");
        final int gapple = this.getGapple();
        final int potion = this.getPotion();
        final int bestSword = this.getBestSword();
        final int bestPick = this.getBestPickaxe();
        final int bestAxe = this.getBestAxe();
        final int bestShovel = this.getBestShovel();
        final int blocks = this.getBlocks();
        if (this.onlyInventory.isEnabled() && !(this.mc.currentScreen instanceof GuiContainer)) {
            return;
        }
        if (this.editCheck.isEnabled() && !InventoryManager.localPlayer.isAllowEdit()) {
            return;
        }
        if (this.noInventoryCheck.isEnabled() && this.mc.currentScreen instanceof GuiChest) {
            return;
        }
        final long finalDelay = (long)Math.max(0.0, this.delaySet.getValue() + ThreadLocalRandom.current().nextDouble(0.0, this.randomizationSet.getValue() + 1.0));
        if (this.delayTimer.timeElapsed(finalDelay)) {
            for (int k = 0; k < Math.min(InventoryManager.localPlayer.inventory.mainInventory.length, 879123742); ++k) {
                final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
                if (is != null && !(is.getItem() instanceof ItemArmor)) {
                    final int gappleSlot = (int)(this.gappleSlot.getValue() - 1.0);
                    boolean gappleBoolean = true;
                    if (InventoryManager.localPlayer.inventory.getStackInSlot(gappleSlot) != null) {
                        gappleBoolean = (InventoryManager.localPlayer.inventory.getStackInSlot(gappleSlot).getItem() != Items.golden_apple);
                    }
                    if (gapple != -1 && gapple != gappleSlot && gappleBoolean) {
                        for (int i = 0; i < this.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                            final Slot s = this.mc.thePlayer.inventoryContainer.inventorySlots.get(i);
                            if (s.getHasStack() && s.getStack() == this.mc.thePlayer.inventory.mainInventory[gapple]) {
                                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, s.slotNumber, gappleSlot, 2, this.mc.thePlayer);
                                this.delayTimer.reset();
                                return;
                            }
                        }
                    }
                    final int potionSlot = (int)(this.potionSlot.getValue() - 1.0);
                    boolean potionBoolean = true;
                    if (InventoryManager.localPlayer.inventory.getStackInSlot(potionSlot) != null) {
                        potionBoolean = !(InventoryManager.localPlayer.inventory.getStackInSlot(potionSlot).getItem() instanceof ItemPotion);
                    }
                    if (potion != -1 && potion != potionSlot && potionBoolean) {
                        for (int j = 0; j < this.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++j) {
                            final Slot s2 = this.mc.thePlayer.inventoryContainer.inventorySlots.get(j);
                            if (s2.getHasStack() && s2.getStack() == this.mc.thePlayer.inventory.mainInventory[potion]) {
                                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, s2.slotNumber, potionSlot, 2, this.mc.thePlayer);
                                this.delayTimer.reset();
                                return;
                            }
                        }
                    }
                    final int blockSlot = (int)(this.blockSlot.getValue() - 1.0);
                    boolean blockBoolean = true;
                    if (InventoryManager.localPlayer.inventory.getStackInSlot(blockSlot) != null) {
                        blockBoolean = !(InventoryManager.localPlayer.inventory.getStackInSlot(blockSlot).getItem() instanceof ItemBlock);
                    }
                    if (blocks != -1 && blocks != blockSlot && blockBoolean) {
                        for (int l = 0; l < this.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++l) {
                            final Slot s3 = this.mc.thePlayer.inventoryContainer.inventorySlots.get(l);
                            if (s3.getHasStack() && s3.getStack() == this.mc.thePlayer.inventory.mainInventory[blocks]) {
                                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, s3.slotNumber, blockSlot, 2, this.mc.thePlayer);
                                this.delayTimer.reset();
                                return;
                            }
                        }
                    }
                    final int swordSlot = (int)(this.swordSlot.getValue() - 1.0);
                    if (bestSword != -1 && bestSword != swordSlot) {
                        for (int m = 0; m < this.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++m) {
                            final Slot s4 = this.mc.thePlayer.inventoryContainer.inventorySlots.get(m);
                            if (s4.getHasStack() && s4.getStack() == this.mc.thePlayer.inventory.mainInventory[bestSword]) {
                                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, s4.slotNumber, swordSlot, 2, this.mc.thePlayer);
                                this.delayTimer.reset();
                                return;
                            }
                        }
                    }
                    final int axeSlot = (int)(this.axeSlot.getValue() - 1.0);
                    if (bestAxe != -1 && bestAxe != axeSlot) {
                        for (int i2 = 0; i2 < this.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i2) {
                            final Slot s5 = this.mc.thePlayer.inventoryContainer.inventorySlots.get(i2);
                            if (s5.getHasStack() && s5.getStack() == this.mc.thePlayer.inventory.mainInventory[bestAxe]) {
                                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, s5.slotNumber, axeSlot, 2, this.mc.thePlayer);
                                this.delayTimer.reset();
                                return;
                            }
                        }
                    }
                    final int pickaxeSlot = (int)(this.pickaxeSlot.getValue() - 1.0);
                    if (bestPick != -1 && bestPick != pickaxeSlot) {
                        for (int i3 = 0; i3 < this.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i3) {
                            final Slot s6 = this.mc.thePlayer.inventoryContainer.inventorySlots.get(i3);
                            if (s6.getHasStack() && s6.getStack() == this.mc.thePlayer.inventory.mainInventory[bestPick]) {
                                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, s6.slotNumber, pickaxeSlot, 2, this.mc.thePlayer);
                                this.delayTimer.reset();
                                return;
                            }
                        }
                    }
                    if (this.cleanBad.isEnabled() && this.isBad(is.getItem())) {
                        this.drop(k, is);
                        this.delayTimer.reset();
                        return;
                    }
                    final boolean clean = this.cleanBad.isEnabled();
                    if (clean) {
                        if (is.getItem() instanceof ItemSword && bestSword != -1 && bestSword != k) {
                            this.drop(k, is);
                            this.delayTimer.reset();
                            return;
                        }
                        if (is.getItem() instanceof ItemPickaxe && bestPick != -1 && bestPick != k) {
                            this.drop(k, is);
                            this.delayTimer.reset();
                            return;
                        }
                        if (is.getItem() instanceof ItemAxe && bestAxe != -1 && bestAxe != k) {
                            this.drop(k, is);
                            this.delayTimer.reset();
                            return;
                        }
                        if (this.isShovel(is.getItem()) && bestShovel != -1 && bestShovel != k) {
                            this.drop(k, is);
                            this.delayTimer.reset();
                            return;
                        }
                    }
                    this.delayTimer.reset();
                }
            }
            this.delayTimer.reset();
        }
    }
    
    private int getGapple() {
        int gapple = -1;
        for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() == Items.golden_apple) {
                gapple = k;
            }
        }
        return gapple;
    }
    
    private int getPotion() {
        int potion = -1;
        for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemPotion) {
                potion = k;
            }
        }
        return potion;
    }
    
    private int getBlocks() {
        int bestBlocks = -1;
        for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemBlock) {
                bestBlocks = k;
            }
        }
        return bestBlocks;
    }
    
    private int getBestSword() {
        int bestSword = -1;
        float bestDamage = 1.0f;
        for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemSword) {
                final ItemSword itemSword = (ItemSword)is.getItem();
                float damage = itemSword.getDamageVsEntity();
                damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, is);
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestSword = k;
                }
            }
        }
        return bestSword;
    }
    
    private int getBestPickaxe() {
        int bestPick = -1;
        float bestDamage = 1.0f;
        for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemPickaxe) {
                final ItemPickaxe itemSword = (ItemPickaxe)is.getItem();
                final float damage = itemSword.getStrVsBlock(is, Block.getBlockById(4));
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestPick = k;
                }
            }
        }
        return bestPick;
    }
    
    private int getBestAxe() {
        int bestPick = -1;
        float bestDamage = 1.0f;
        for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
            if (is != null && is.getItem() instanceof ItemAxe) {
                final ItemAxe itemSword = (ItemAxe)is.getItem();
                final float damage = itemSword.getStrVsBlock(is, Block.getBlockById(17));
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestPick = k;
                }
            }
        }
        return bestPick;
    }
    
    private int getBestShovel() {
        int bestPick = -1;
        float bestDamage = 1.0f;
        for (int k = 0; k < this.mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack is = this.mc.thePlayer.inventory.mainInventory[k];
            if (is != null && this.isShovel(is.getItem())) {
                final ItemTool itemSword = (ItemTool)is.getItem();
                final float damage = itemSword.getStrVsBlock(is, Block.getBlockById(3));
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestPick = k;
                }
            }
        }
        return bestPick;
    }
    
    private boolean isShovel(final Item is) {
        return Item.getItemById(256) == is || Item.getItemById(269) == is || Item.getItemById(273) == is || Item.getItemById(277) == is || Item.getItemById(284) == is;
    }
    
    public void drop(final int index, final ItemStack item) {
        boolean hotbar = false;
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemK = this.mc.thePlayer.inventory.getStackInSlot(k);
            if (itemK != null && itemK == item) {
                hotbar = true;
            }
        }
        if (hotbar) {
            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, index + 36, 1, 4, this.mc.thePlayer);
        }
        else {
            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, index, 1, 4, this.mc.thePlayer);
        }
    }
    
    public void shiftClick(final int slot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, this.mc.thePlayer);
    }
    
    public void click(final int slot) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, this.mc.thePlayer);
    }
    
    public void swap(final int slot, final int hotbarSlot) {
        if (hotbarSlot == slot - 36) {
            return;
        }
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarSlot, 2, this.mc.thePlayer);
    }
    
    private boolean isBad(final Item i) {
        return i.getUnlocalizedName().contains("tnt") || i.getUnlocalizedName().contains("stick") || i.getUnlocalizedName().contains("string") || i.getUnlocalizedName().contains("flint") || i.getUnlocalizedName().contains("bucket") || i.getUnlocalizedName().contains("feather") || i.getUnlocalizedName().contains("snow") || i.getUnlocalizedName().contains("piston") || i instanceof ItemGlassBottle || i.getUnlocalizedName().contains("web") || i.getUnlocalizedName().contains("slime") || i.getUnlocalizedName().contains("trip") || i.getUnlocalizedName().contains("wire") || i.getUnlocalizedName().contains("sugar") || i.getUnlocalizedName().contains("note") || i.getUnlocalizedName().contains("record") || i.getUnlocalizedName().contains("flower") || i.getUnlocalizedName().contains("wheat") || i.getUnlocalizedName().contains("fishing") || i.getUnlocalizedName().contains("boat") || i.getUnlocalizedName().contains("leather") || i.getUnlocalizedName().contains("seeds") || i.getUnlocalizedName().contains("skull") || i.getUnlocalizedName().contains("torch") || i.getUnlocalizedName().contains("anvil") || i.getUnlocalizedName().contains("enchant") || i.getUnlocalizedName().contains("exp") || i.getUnlocalizedName().contains("shears");
    }
}
