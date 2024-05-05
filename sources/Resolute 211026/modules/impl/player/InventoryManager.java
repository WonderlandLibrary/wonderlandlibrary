// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemBlock;
import java.util.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemBow;
import vip.Resolute.util.player.PlayerUtil;
import net.minecraft.item.ItemSword;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.impl.EventWindowClick;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.network.play.client.C16PacketClientStatus;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import net.minecraft.client.gui.inventory.GuiInventory;
import vip.Resolute.settings.Setting;
import java.util.ArrayList;
import java.util.List;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class InventoryManager extends Module
{
    public NumberSetting clickDelayProp;
    public BooleanSetting combatCheck;
    public BooleanSetting autoArmor;
    public BooleanSetting sortHotbar;
    public BooleanSetting sortTools;
    public BooleanSetting spoof;
    public BooleanSetting onOpen;
    private int bestSwordSlot;
    private int bestBowSlot;
    private boolean openInventory;
    private final int[] bestArmorPieces;
    private final int[] bestToolSlots;
    private final TimerUtil interactionsTimer;
    private final List<Integer> trash;
    private final List<Integer> duplicateSwords;
    private final List<Integer> gappleStackSlots;
    
    public InventoryManager() {
        super("InvManager", 22, "Sorts out your inventory", Category.PLAYER);
        this.clickDelayProp = new NumberSetting("Click Delay", 150.0, 0.0, 1000.0, 10.0);
        this.combatCheck = new BooleanSetting("While Fighting", false);
        this.autoArmor = new BooleanSetting("Auto Armor", true);
        this.sortHotbar = new BooleanSetting("Sort Hotbar", true);
        this.sortTools = new BooleanSetting("Sort Tools", true);
        this.spoof = new BooleanSetting("Spoof", true);
        this.onOpen = new BooleanSetting("Open Inv", false);
        this.bestArmorPieces = new int[4];
        this.bestToolSlots = new int[3];
        this.interactionsTimer = new TimerUtil();
        this.trash = new ArrayList<Integer>();
        this.duplicateSwords = new ArrayList<Integer>();
        this.gappleStackSlots = new ArrayList<Integer>();
        this.addSettings(this.clickDelayProp, this.combatCheck, this.autoArmor, this.sortHotbar, this.sortTools, this.spoof, this.onOpen);
    }
    
    @Override
    public void onEnable() {
        this.openInventory = (InventoryManager.mc.currentScreen instanceof GuiInventory);
        this.interactionsTimer.reset();
    }
    
    @Override
    public void onDisable() {
        this.close();
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("");
        try {
            if (e instanceof EventPacket && this.openInventory) {
                if (((EventPacket)e).getPacket() instanceof C16PacketClientStatus) {
                    final C16PacketClientStatus status = ((EventPacket)e).getPacket();
                    if (status.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT && !(InventoryManager.mc.currentScreen instanceof GuiChest)) {
                        e.setCancelled(true);
                    }
                }
                if (((EventPacket)e).getPacket() instanceof C0DPacketCloseWindow) {
                    e.setCancelled(true);
                }
                if (((EventPacket)e).getPacket() instanceof S2DPacketOpenWindow) {
                    this.close();
                }
                if (((EventPacket)e).getPacket() instanceof S2EPacketCloseWindow) {
                    e.setCancelled(true);
                }
            }
        }
        catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        if (e instanceof EventWindowClick) {
            this.interactionsTimer.reset();
        }
        if (e instanceof EventMotion && e.isPre()) {
            final GuiScreen currentScreen = InventoryManager.mc.currentScreen;
            if ((currentScreen == null && !this.onOpen.isEnabled()) || currentScreen instanceof GuiInventory) {
                if (!this.interactionsTimer.hasElapsed((long)this.clickDelayProp.getValue())) {
                    return;
                }
                this.reset();
                boolean foundSword = false;
                final boolean foundBow = false;
                boolean foundGapple = false;
                for (int slot = 5; slot < 45; ++slot) {
                    final ItemStack stack = InventoryManager.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                    if (stack != null) {
                        if (stack.getItem() instanceof ItemSword && PlayerUtil.isBestSword(stack)) {
                            if (foundSword) {
                                this.duplicateSwords.add(slot);
                            }
                            else if (slot != this.bestSwordSlot) {
                                foundSword = true;
                                this.bestSwordSlot = slot;
                            }
                        }
                        else if (stack.getItem() instanceof ItemBow && PlayerUtil.isBestBow(stack)) {
                            if (slot != this.bestBowSlot) {
                                this.bestBowSlot = slot;
                            }
                        }
                        else if (stack.getItem() instanceof ItemTool && PlayerUtil.isBestTool(stack)) {
                            final int toolType = PlayerUtil.getToolType(stack);
                            if (toolType != -1 && slot != this.bestToolSlots[toolType]) {
                                this.bestToolSlots[toolType] = slot;
                            }
                        }
                        else if (stack.getItem() instanceof ItemArmor && PlayerUtil.isBestArmor(stack) && this.autoArmor.isEnabled()) {
                            final ItemArmor armor = (ItemArmor)stack.getItem();
                            final int bestSlot = this.bestArmorPieces[armor.armorType];
                            if (bestSlot == -1 || slot != bestSlot) {
                                this.bestArmorPieces[armor.armorType] = slot;
                            }
                        }
                        else if (stack.getItem() instanceof ItemAppleGold) {
                            if (!foundGapple) {
                                if (stack.stackSize == 64) {
                                    foundGapple = true;
                                    this.gappleStackSlots.add(slot);
                                }
                            }
                            else {
                                this.trash.add(slot);
                            }
                        }
                        else if (!this.trash.contains(slot) && !this.isValidStack(stack)) {
                            this.trash.add(slot);
                        }
                    }
                }
                for (int i = 0; i < this.bestArmorPieces.length; ++i) {
                    final int piece = this.bestArmorPieces[i];
                    if (piece != -1) {
                        final int armorPieceSlot = i + 5;
                        final ItemStack stack2 = InventoryManager.mc.thePlayer.inventoryContainer.getSlot(armorPieceSlot).getStack();
                        if (stack2 == null) {
                            this.open();
                            PlayerUtil.windowClick(piece, 0, PlayerUtil.ClickType.SHIFT_CLICK);
                            this.close();
                            return;
                        }
                    }
                }
                int currentSlot = 36;
                if (!this.combatCheck.isEnabled() && InventoryManager.mc.objectMouseOver != null && InventoryManager.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && InventoryManager.mc.objectMouseOver.entityHit.hurtResistantTime >= 10) {
                    this.interactionsTimer.reset();
                    return;
                }
                if (this.purgeList(this.duplicateSwords)) {
                    return;
                }
                if (this.sortHotbar.isEnabled() && this.bestSwordSlot != -1) {
                    if (this.bestSwordSlot != currentSlot) {
                        this.putItemInSlot(ItemType.SWORD, currentSlot);
                        return;
                    }
                    ++currentSlot;
                }
                if (this.purgeList(this.trash)) {
                    return;
                }
                if (this.sortHotbar.isEnabled()) {
                    if (this.bestBowSlot != -1) {
                        if (this.bestBowSlot != currentSlot) {
                            this.putItemInSlot(ItemType.BOW, currentSlot);
                            return;
                        }
                        ++currentSlot;
                    }
                    if (!this.gappleStackSlots.isEmpty()) {
                        this.open();
                        this.gappleStackSlots.sort((s1, s2) -> InventoryManager.mc.thePlayer.inventoryContainer.getSlot(s2).getStack().stackSize - InventoryManager.mc.thePlayer.inventoryContainer.getSlot(s2).getStack().stackSize);
                        final int bestSlot2 = this.gappleStackSlots.get(0);
                        if (bestSlot2 != currentSlot) {
                            PlayerUtil.windowClick(bestSlot2, currentSlot - 36, PlayerUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                            return;
                        }
                        ++currentSlot;
                    }
                }
                if (this.sortTools.isEnabled()) {
                    final int[] toolSlots = { currentSlot, currentSlot + 1, currentSlot + 2 };
                    for (final int bestSlot3 : this.bestToolSlots) {
                        if (bestSlot3 != -1) {
                            final int type = PlayerUtil.getToolType(InventoryManager.mc.thePlayer.inventoryContainer.getSlot(bestSlot3).getStack());
                            if (type != -1 && bestSlot3 != toolSlots[type]) {
                                this.putToolsInSlot(type, toolSlots);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void reset() {
        this.trash.clear();
        this.bestBowSlot = -1;
        this.bestSwordSlot = -1;
        this.duplicateSwords.clear();
        this.gappleStackSlots.clear();
        Arrays.fill(this.bestArmorPieces, -1);
        Arrays.fill(this.bestToolSlots, -1);
    }
    
    private void open() {
        if (!this.openInventory) {
            this.interactionsTimer.reset();
            if (this.spoof.isEnabled()) {
                PlayerUtil.openInventory();
            }
            this.openInventory = true;
        }
    }
    
    private void close() {
        if (this.openInventory) {
            if (this.spoof.isEnabled()) {
                PlayerUtil.closeInventory();
            }
            this.openInventory = false;
        }
    }
    
    private boolean isValidStack(final ItemStack stack) {
        return (stack.getItem() instanceof ItemBlock && PlayerUtil.isGoodBlockStack(stack)) || stack.getItem().getUnlocalizedName().equals("item.arrow") || stack.getItem() instanceof ItemEnderPearl || (stack.getItem() instanceof ItemPotion && PlayerUtil.isBuffPotion(stack)) || (stack.getItem() instanceof ItemFood && PlayerUtil.isGoodFood(stack));
    }
    
    private boolean purgeList(final List<Integer> listOfSlots) {
        if (!listOfSlots.isEmpty()) {
            this.open();
            final int slot = listOfSlots.remove(0);
            PlayerUtil.windowClick(slot, 1, PlayerUtil.ClickType.DROP_ITEM);
            if (listOfSlots.isEmpty()) {
                this.close();
            }
            return true;
        }
        return false;
    }
    
    private void putItemInSlot(final ItemType type, final int slot) {
        this.open();
        switch (type) {
            case SWORD: {
                PlayerUtil.windowClick(this.bestSwordSlot, slot - 36, PlayerUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                break;
            }
            case BOW: {
                PlayerUtil.windowClick(this.bestBowSlot, slot - 36, PlayerUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                break;
            }
        }
        this.close();
    }
    
    private void putToolsInSlot(final int tool, final int[] toolSlots) {
        this.open();
        final int toolSlot = toolSlots[tool];
        PlayerUtil.windowClick(this.bestToolSlots[tool], toolSlot - 36, PlayerUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.bestToolSlots[tool] = toolSlot;
        this.close();
    }
    
    private enum ItemType
    {
        SWORD, 
        BOW;
    }
}
