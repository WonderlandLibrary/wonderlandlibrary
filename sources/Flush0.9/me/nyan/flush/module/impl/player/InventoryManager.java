package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.Event;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@SuppressWarnings("rawtypes")
public class InventoryManager extends Module {
    private boolean inventoryOpen;
    private final Timer timer = new Timer();

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "SpoofInv", "OpenInv");
    private final NumberSetting delay = new NumberSetting("Delay", this, 80, 0, 300);

    public InventoryManager() {
        super("InvManager", Category.PLAYER);
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.isOutgoing() && !(mc.currentScreen instanceof GuiContainer) || mc.currentScreen instanceof GuiInventory) {
            if (e.getPacket() instanceof C16PacketClientStatus) {
                C16PacketClientStatus status = e.getPacket();

                if (status.getStatus() == EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                    inventoryOpen = true;
                }
            }

            if (e.getPacket() instanceof C0DPacketCloseWindow) {
                inventoryOpen = false;
            }
        }
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (e.getState() == Event.State.PRE && !(mc.currentScreen instanceof GuiContainer) || mc.currentScreen instanceof GuiInventory) {
            if (mc.currentScreen instanceof GuiInventory || !mode.is("openinv")) {
                if (purgeUnusedArmor() && purgeUnusedTools() && purgeJunk() && manageSword() && manageTools()) {
                    if (hotbarHasSpace()) {
                        manageHotbar();
                    }
                }
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    private boolean manageSword() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();

            if (stack != null) {
                Item item = stack.getItem();
                if (i != 36 && !stack.getDisplayName().toLowerCase().contains("right click") && item instanceof ItemSword &&
                        timer.hasTimeElapsed(delay.getValueInt() + MathUtils.getRandomNumber(0, 10), true)) {
                    moveToHotbarSlot(i, 0);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean manageTools() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();

            if (stack != null) {
                Item item = stack.getItem();
                if (!stack.getDisplayName().toLowerCase().contains("right click") && item instanceof ItemTool &&
                        timer.hasTimeElapsed(delay.getValueInt() + MathUtils.getRandomNumber(0, 10), true)) {
                    moveToHotbarSlot(i, item instanceof ItemPickaxe ? 1 : item instanceof ItemAxe ? 2 : 3);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean purgeUnusedArmor() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();

            if (stack != null) {
                Item item = stack.getItem();

                if (item instanceof ItemArmor) {
                    if (!isBestArmor(stack) && timer.hasTimeElapsed(delay.getValueInt() + MathUtils.getRandomNumber(0, 10), true)) {
                        purge(i);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean purgeUnusedTools() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();

            if (stack != null) {
                Item item = stack.getItem();
                if (item instanceof ItemTool || item instanceof ItemSword) {
                    if (!stack.getDisplayName().toLowerCase().contains("click") && (item instanceof ItemSword ? !isBestSword(stack) : !isBestTool(stack)) &&
                            timer.hasTimeElapsed(delay.getValueInt() + MathUtils.getRandomNumber(0, 10), true)) {
                        purge(i);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean purgeJunk() {
        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                if (isBadItem(stack) && !stack.getDisplayName().toLowerCase().contains("click") && !stack.getDisplayName().toLowerCase().contains("clique")
                        && timer.hasTimeElapsed(delay.getValueInt() + MathUtils.getRandomNumber(0, 10), true)) {
                    purge(i);
                    return false;
                }
            }
        }
        return true;
    }

    private void manageHotbar() {
        for (int i = 36; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getStack() != null) {
                Item item = slot.getStack().getItem();
                if (item == Items.arrow) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot.slotNumber, 0, 1, mc.thePlayer);
                }
            }
        }

        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();
            boolean hotbar = i >= 36;
            if (stack != null) {
                Item item = stack.getItem();
                if (!stack.getDisplayName().toLowerCase().contains("click") && (item instanceof ItemBlock &&
                        (((ItemBlock) item).getBlock().isFullCube()) && !hotbarHasEnoughBlocks()) && !hotbar &&
                        timer.hasTimeElapsed(delay.getValueInt() + MathUtils.getRandomNumber(0, 10), true)) {
                    moveToHotbar(i);
                    return;
                }
            }
        }
    }

    private boolean hotbarHasSpace() {
        for (int i = 36; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getStack() == null)
                return true;
        }
        return false;
    }

    private boolean hotbarNeedsItem(Class<?> type) {
        for (int i = 36; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (type.isInstance(slot.getStack()))
                return false;
        }
        return true;
    }

    private boolean hotbarHasEnoughBlocks() {
        int count = 0;
        for (int i = 36; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getStack() != null && slot.getStack().getItem() instanceof ItemBlock && ((ItemBlock) slot.getStack().getItem()).getBlock().isFullCube())
                count += slot.getStack().stackSize;
        }

        return count >= 64;
    }

    private boolean isBestTool(ItemStack compareStack) {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();

            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemTool) {
                ItemTool item = (ItemTool) stack.getItem();
                ItemTool compare = (ItemTool) compareStack.getItem();
                if (item.getClass() == compare.getClass()) {
                    if (compare.getStrVsBlock(stack, preferredBlock(item.getClass())) <= item.getStrVsBlock(compareStack, preferredBlock(compare.getClass())))
                        return false;
                }
            }
        }

        return true;
    }

    private boolean isBestSword(ItemStack compareStack) {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();
            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemSword) {
                ItemSword item = (ItemSword) stack.getItem();
                ItemSword compare = (ItemSword) compareStack.getItem();
                if (item.getClass() == compare.getClass()) {
                    if (compare.getDamageVsEntity() + getSwordStrength(compareStack) <= item.getDamageVsEntity() + getSwordStrength(stack))
                        return false;
                }
            }
        }

        return true;
    }

    private Block preferredBlock(Class clazz) {
        return clazz == ItemPickaxe.class ? Blocks.cobblestone : clazz == ItemAxe.class ? Blocks.log : Blocks.dirt;
    }

    private boolean isBestArmor(ItemStack compareStack) {
        for (int i = 0; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack stack = slot.getStack();

            if (stack != null && compareStack != stack && stack.getItem() instanceof ItemArmor) {
                ItemArmor item = (ItemArmor) stack.getItem();
                ItemArmor compare = (ItemArmor) compareStack.getItem();
                if (item.armorType == compare.armorType) {
                    if (getProtectionValue(compareStack) <= getProtectionValue(stack))
                        return false;
                }
            }
        }

        return true;
    }

    private void moveToHotbar(int slot) {
        openInvPacket();
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
        closeInvPacket();
    }

    private void moveToHotbarSlot(int slot, int hotbarSlot) {
        openInvPacket();
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarSlot, 2, mc.thePlayer);
        closeInvPacket();
    }

    private void purge(int slot) {
        openInvPacket();
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
        closeInvPacket();
    }

    private void openInvPacket() {
        if (!inventoryOpen && mode.is("spoofinv")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            inventoryOpen = true;
        }
    }

    private void closeInvPacket() {
        if (inventoryOpen && mode.is("spoofinv")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
            inventoryOpen = false;
        }
    }

    private float getSwordStrength(ItemStack stack) {
        return (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F) + (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack));
    }

    private double getProtectionValue(ItemStack stack) {
        return !(stack.getItem() instanceof ItemArmor) ? 0.0D : (double) ((ItemArmor) stack.getItem()).damageReduceAmount + (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075D;
    }

    private boolean isBadItem(ItemStack stack) {
        for (String baditem : PlayerUtils.getBadItems()) {
            if (stack.getItem().getUnlocalizedName().contains(baditem) && !(stack.getItem() instanceof ItemArmor))
                return true;
        }

        if (stack.getItem() instanceof ItemPotion) {
            for (PotionEffect effect : ((ItemPotion) stack.getItem()).getEffects(stack)) {
                if (Potion.potionTypes[effect.getPotionID()].isBadEffect())
                    return true;
            }
        }

        return Block.getBlockFromItem(stack.getItem()) != null && Block.getBlockFromItem(stack.getItem()) instanceof BlockFlower;
    }
}