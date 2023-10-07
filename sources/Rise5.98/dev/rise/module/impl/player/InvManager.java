/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.player;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.DamageSource;

import java.util.*;

// This is an old version of InvManager, Manager is the new InvManager.

@ModuleInfo(name = "InvManager", description = "Manages your inventory for you", category = Category.PLAYER)
public final class InvManager extends Module {

    private static int bestSwordSlot, bestPickaxeSlot, bestBowSlot, bestBlockSlot, bestGapSlot;
    private static int[] bestArmorDamageReducment, bestArmorSlot;

    //Lists
    private final List<Integer> allSwords = new ArrayList<>();
    private final List<Integer> allBows = new ArrayList<>();
    private final List<Integer> allPickaxes = new ArrayList<>();
    private final List<Integer>[] allArmors = new List[4];
    private final List<Integer> allBlocks = new ArrayList<>();
    private final List<Integer> trash = new ArrayList<>();

    //Timers
    private final TimeUtil delayTimer = new TimeUtil();
    private final TimeUtil startDelayTimer = new TimeUtil();

    //Settings
    public NumberSetting minDelay = new NumberSetting("Min Delay", this, 10, 0, 100, 5);
    public NumberSetting maxDelay = new NumberSetting("Max Delay", this, 20, 0, 100, 5);
    public NumberSetting startDelay = new NumberSetting("Start Delay", this, 20, 0, 100, 1);
    public NumberSetting maxBlocks = new NumberSetting("Maximum Block Stacks", this, 2, 0, 8, 1);
    public BooleanSetting sort = new BooleanSetting("Sort", this, true);
    public BooleanSetting autoArmor = new BooleanSetting("Auto Armor", this, true);
    public BooleanSetting random = new BooleanSetting("Random", this, true);
    public BooleanSetting openInv = new BooleanSetting("Open Inv", this, true);
    public BooleanSetting keepArmor = new BooleanSetting("Keep Armor", this, false);
    public BooleanSetting onlyStill = new BooleanSetting("Only When Standing Still", this, false);

    private boolean invOpen;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        closeInvServerSide();

        if (Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Scaffold")).isEnabled() || mc.currentScreen instanceof GuiChest)
            return;

        if (onlyStill.isEnabled() && (mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()))
            return;

        searchForItems();
        searchForBestArmor();
        searchForTrash();

        //Drop Trash
        if (!keepArmor.isEnabled()) {
            for (final int slot : trash) {
                if (hasNoDelay())
                    return;

                try {
                    openInvServerSide();
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot < 9 ? slot + 36 : slot, 1, 4, mc.thePlayer); //windowID - slot to Drop - Mouse Button 4 for Item Drop - Player

                } catch (final IndexOutOfBoundsException p) {
                }

                delayTimer.reset();
            }
        }

        //AutoArmor
        if (autoArmor.isEnabled()) {

            for (int i = 0; i < 4; i++) {
                if (bestArmorSlot[i] != -1) {
                    final int bestSlot = bestArmorSlot[i];
                    final ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(i);

                    if (hasNoDelay()) return;

                    //Drop old Armor
                    if (oldArmor != null && oldArmor.getItem() != null && !keepArmor.isEnabled()) {
                        openInvServerSide();
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8 - i, 0, 4, mc.thePlayer);
                        delayTimer.reset();
                    }

                    final int slot = bestSlot < 9 ? bestSlot + 36 : bestSlot;

                    if (hasNoDelay()) return;

                    //Equip new armor
                    openInvServerSide();
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
                    delayTimer.reset();
                }
            }
        }

        //Sort Inventory
        if (sort.isEnabled()) {
            final int switchSlotSword = 0;
            final int switchSlotBow = 1;
            final int switchSlotPickaxe = 2;
            final int switchSlotBlock = 8;
            final int switchGap = 7;

            //Sword Switch
            if (bestSwordSlot != -1 && bestSwordSlot != switchSlotSword) {
                final int slot = bestSwordSlot < 9 ? bestSwordSlot + 36 : bestSwordSlot;
                if (hasNoDelay() && slot != switchSlotSword)
                    return;
                openInvServerSide();
                try {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, switchSlotSword, 2, mc.thePlayer); //windowID - current Slot - destination Slot - Player
                } catch (final Exception ignored) {
                }
                delayTimer.reset();
            }

            //Bow Switch
            if (bestBowSlot != -1 && bestBowSlot != switchSlotBow) {
                final int slot = bestBowSlot < 9 ? bestBowSlot + 36 : bestBowSlot;
                if (hasNoDelay() && slot != switchSlotBow)
                    return;
                openInvServerSide();
                try {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, switchSlotBow, 2, mc.thePlayer); //windowID - current Slot - destination Slot - Player
                } catch (final Exception ignored) {
                }
                delayTimer.reset();
            }

            //Pickaxe Switch
            if (bestPickaxeSlot != -1 && bestPickaxeSlot != switchSlotPickaxe) {
                final int slot = bestPickaxeSlot < 9 ? bestPickaxeSlot + 36 : bestPickaxeSlot;
                if (hasNoDelay() && slot != switchSlotPickaxe)
                    return;
                openInvServerSide();
                try {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, switchSlotPickaxe, 2, mc.thePlayer); //windowID - current Slot - destination Slot - Player
                } catch (final Exception ignored) {
                }
                delayTimer.reset();
            }

            //Block Switch
            if (bestBlockSlot != -1 && bestBlockSlot != switchSlotBlock) {
                final int slot = bestBlockSlot < 9 ? bestBlockSlot + 36 : bestBlockSlot;
                if (mc.thePlayer.inventory.getStackInSlot(switchSlotBlock) != null && mc.thePlayer.inventory.getStackInSlot(switchSlotBlock).getItem() instanceof ItemBlock)
                    return;
                if (hasNoDelay() && slot != switchSlotBlock)
                    return;
                openInvServerSide();
                try {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, switchSlotBlock, 2, mc.thePlayer); //windowID - current Slot - destination Slot - Player
                } catch (final Exception ignored) {
                }
                delayTimer.reset();
            }

            //Gap Switch
            if (bestGapSlot != -1 && bestGapSlot != switchGap) {
                final int slot = bestGapSlot < 9 ? bestGapSlot + 36 : bestGapSlot;
                if (hasNoDelay() && slot != switchGap)
                    return;
                openInvServerSide();
                try {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, switchGap, 2, mc.thePlayer); //windowID - current Slot - destination Slot - Player
                } catch (final Exception ignored) {
                }
                delayTimer.reset();
            }
        }
    }

    private void openInvServerSide() {
        if (!invOpen && !openInv.isEnabled()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
            invOpen = true;
        }
    }

    private void closeInvServerSide() {
        if (invOpen && !openInv.isEnabled()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
            invOpen = false;
        }
    }

    private boolean hasNoDelay() {
        return !startDelayTimer.hasReached((long) ((long) startDelay.getValue() * 10 + Math.random() * 2)) || !delayTimer.hasReached((long) (Math.random() * (maxDelay.getValue() * 10 - minDelay.getValue() * 10 + 1) + minDelay.getValue() * 10)) || (!(mc.currentScreen instanceof GuiInventory)) && openInv.isEnabled();
    }

    private void searchForTrash() {
        //Clear the Trash List
        trash.clear();

        //Filter the best Armor
        for (int i = 0; i < allArmors.length; i++) {
            final List<Integer> armorItem = allArmors[i];
            if (armorItem != null) {
                final int finalI = i;
                armorItem.stream().filter(slot -> slot != bestArmorSlot[finalI]).forEach(trash::add);
            }
        }

        //Filter the best Items
        allBows.stream().filter(slot -> slot != bestBowSlot).forEach(trash::add);
        allSwords.stream().filter(slot -> slot != bestSwordSlot).forEach(trash::add);
        allPickaxes.stream().filter(slot -> slot != bestPickaxeSlot).forEach(trash::add);

        // Throw blocks after the limit is reached.
        int blockStacks = allBlocks.size();

        for (final int slot : allBlocks) {
            if (blockStacks <= Math.round(maxBlocks.getValue() + 1)) break;

            trash.add(slot);
            blockStacks -= 1;
        }

        //Random Option
        if (random.isEnabled()) Collections.shuffle(trash);
        else Collections.sort(trash);
    }

    private void searchForBestArmor() {
        //Reset all Stats and Slots
        bestArmorDamageReducment = new int[4];
        bestArmorSlot = new int[4];

        //Fill Arrays
        Arrays.fill(bestArmorDamageReducment, -1);
        Arrays.fill(bestArmorSlot, -1);

        //Fill Array with current Armor
        for (int i = 0; i < bestArmorSlot.length; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
            allArmors[i] = new ArrayList<>();

            if (itemStack != null && itemStack.getItem() != null) {
                if (itemStack.getItem() instanceof ItemArmor) {
                    final ItemArmor armor = (ItemArmor) itemStack.getItem();
                    final int currentProtection = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic); //Get the Reduce amount of the ArmorType and Protection Level
                    bestArmorDamageReducment[i] = currentProtection;
                }
            }
        }

        //Search for better Armor in Inventory
        for (int i = 0; i < 9 * 4; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) continue;

            if (itemStack.getItem() instanceof ItemArmor) {
                final ItemArmor armor = (ItemArmor) itemStack.getItem();

                final int armorType = 3 - armor.armorType; //Slots reversed for some reason
                allArmors[armorType].add(i);
                final int slotProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic); //Get the Reduce amount of the ArmorType and Protection Level

                if (bestArmorDamageReducment[armorType] < slotProtectionLevel) {
                    bestArmorDamageReducment[armorType] = slotProtectionLevel;
                    bestArmorSlot[armorType] = i;
                }

            }
        }
    }


    private void searchForItems() {
        //Reset all Slots
        bestSwordSlot = -1;
        bestBowSlot = -1;
        bestPickaxeSlot = -1;
        bestBlockSlot = -1;
        bestGapSlot = -1;

        allSwords.clear();
        allBows.clear();
        allPickaxes.clear();
        allBlocks.clear();

        //Other Stats
        float bestSwordDamage = -1, bestSwordDurability = -1, bestPickaxeEfficiency = -1, bestPickaxeDurability = -1, bestBowDurability = -1;
        int bestBowDamage = -1, bestBlockSize = -1;
        int gapStackSize = -1;

        for (int i = 0; i < 9 * 4; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) continue;

            //Search for best Sword
            if (itemStack.getItem() instanceof ItemSword) {
                final ItemSword sword = (ItemSword) itemStack.getItem();
                final int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                final float damageLevel = (float) (sword.getDamageVsEntity() + level * 1.25); //Enchantment Multiplier

                allSwords.add(i);

                if (bestSwordDamage < damageLevel) {
                    bestSwordDamage = damageLevel;
                    bestSwordDurability = sword.getDamageVsEntity();
                    bestSwordSlot = i;
                }

                if ((damageLevel == bestSwordDamage) && (sword.getDamageVsEntity() < bestSwordDurability)) {
                    bestSwordDurability = sword.getDamageVsEntity();
                    bestSwordSlot = i;
                }
            }

            //Search for best Pickaxe
            if (itemStack.getItem() instanceof ItemPickaxe) {
                final ItemPickaxe pickaxe = (ItemPickaxe) itemStack.getItem();
                allPickaxes.add(i);

                final float efficiencyLevel = getPickaxeEfficiency(pickaxe);

                if (bestPickaxeEfficiency < efficiencyLevel) {
                    bestPickaxeEfficiency = efficiencyLevel;
                    bestPickaxeDurability = pickaxe.getMaxDamage();
                    bestPickaxeSlot = i;
                }

                if ((efficiencyLevel == bestPickaxeEfficiency) && (pickaxe.getMaxDamage() < bestPickaxeDurability)) {
                    bestPickaxeDurability = pickaxe.getMaxDamage();
                    bestPickaxeSlot = i;
                }
            }

            //Search for best Bow
            if (itemStack.getItem() instanceof ItemBow) {
                final ItemBow bow = (ItemBow) itemStack.getItem();
                final int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
                allBows.add(i);

                if (bestBowDamage < level || (bestBowDamage == -1 && level == 0)) {
                    bestBowDamage = level;
                    bestBowDurability = bow.getMaxDamage();
                    bestBowSlot = i;
                }

                if ((level == bestBowDamage) && bow.getMaxDamage() < bestBowDurability) {
                    bestBowDurability = bow.getMaxDamage();
                    bestBowSlot = i;
                }
            }

            //Search for Blocks
            if (itemStack.getItem() instanceof ItemBlock) {
                final ItemBlock block = (ItemBlock) itemStack.getItem();

                if (block.getBlock() == Blocks.web || block.getBlock() == Blocks.bed || block.getBlock() == Blocks.noteblock || block.getBlock() == Blocks.cactus || block.getBlock() == Blocks.cake || block.getBlock() == Blocks.anvil || block.getBlock() == Blocks.skull || block.getBlock() instanceof BlockDoor || block.getBlock() instanceof BlockFlower || block.getBlock() instanceof BlockCarpet)
                    continue;

                allBlocks.add(i);

                if (bestBlockSize < itemStack.stackSize) {
                    bestBlockSize = itemStack.stackSize;
                    bestBlockSlot = i;
                }
            }

            // Search for Gaps.
            if (itemStack.getItem() instanceof ItemAppleGold) {
                if (gapStackSize < itemStack.stackSize) {
                    gapStackSize = itemStack.stackSize;
                    bestGapSlot = i;
                }
            }
        }
    }

    private float getPickaxeEfficiency(final ItemPickaxe pickaxe) {
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, new ItemStack(pickaxe));

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

        return pickaxe.getToolMaterial().getEfficiencyOnProperMaterial() + level;
    }

}
