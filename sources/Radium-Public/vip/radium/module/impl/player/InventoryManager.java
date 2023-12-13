package vip.radium.module.impl.player;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.event.impl.packet.PacketSendEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.player.WindowClickEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.HypixelGameUtils;
import vip.radium.utils.InventoryUtils;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(label = "Inventory Manager", category = ModuleCategory.PLAYER)
public final class InventoryManager extends Module {

    private final MultiSelectEnumProperty<Modules> modulesProperty = new MultiSelectEnumProperty<>("Modules", Modules.AUTO_ARMOR, Modules.CLEANER);
    private final DoubleProperty clickDelayProperty = new DoubleProperty("Click Delay", 150,
            10, 300, 10, Representation.MILLISECONDS);
    private final Property<Boolean> archeryProperty = new Property<>("Archery", false);
    private final Property<Boolean> sortHotbarProperty = new Property<>("Sort Hotbar", true);
    private final Property<Boolean> sortToolsProperty = new Property<>("Sort Tools", false, sortHotbarProperty::getValue);
    private final Property<Boolean> spoofInventoryProperty = new Property<>("Spoof", true);
    private final TimerUtil interactionsTimer = new TimerUtil();
    private final int[] bestArmorPieces = new int[4];
    private final List<Integer> trash = new ArrayList<>();
    private final int[] bestToolSlots = new int[3];
    private final List<Integer> gappleStackSlots = new ArrayList<>();
    private int bestSwordSlot;
    private int bestBowSlot;
    private boolean openInventory;

    @EventLink
    public final Listener<WindowClickEvent> onWindowClickEvent = event ->
            interactionsTimer.reset();

    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent = event -> {
        if (openInventory) {
            if (event.getPacket() instanceof C16PacketClientStatus) {
                C16PacketClientStatus packet = (C16PacketClientStatus) event.getPacket();
                if (packet.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                    event.setCancelled();
            } else if (event.getPacket() instanceof C0DPacketCloseWindow) {
                event.setCancelled();
            }
        }
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        if (openInventory) {
            if (event.getPacket() instanceof S2DPacketOpenWindow) {
                close();
            } else if (event.getPacket() instanceof S2EPacketCloseWindow) {
                event.setCancelled();
            }
        }
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            final GuiScreen currentScreen = Wrapper.getCurrentScreen();
            if (currentScreen == null || currentScreen instanceof GuiInventory) {
                final long clickDelay = this.clickDelayProperty.getValue().longValue();

                if (!this.interactionsTimer.hasElapsed(clickDelay))
                    return;

                final boolean autoArmor = this.modulesProperty.isSelected(Modules.AUTO_ARMOR);
                final boolean cleaner = this.modulesProperty.isSelected(Modules.CLEANER);
                final boolean sorter = this.sortHotbarProperty.getValue();

                this.clear();

                boolean foundSword = false;

                for (int slot = InventoryUtils.INCLUDE_ARMOR_BEGIN; slot < InventoryUtils.END; slot++) {
                    final ItemStack stack = Wrapper.getStackInSlot(slot);

                    if (stack != null) {
                        if (stack.getItem() instanceof ItemSword && InventoryUtils.isBestSword(stack)) {
                            if (foundSword) {
                                this.trash.add(slot);
                            } else if (slot != bestSwordSlot) {
                                foundSword = true;
                                this.bestSwordSlot = slot;
                            }
                        } else if (stack.getItem() instanceof ItemTool && InventoryUtils.isBestTool(stack)) {
                            final int toolType = InventoryUtils.getToolType(stack);
                            if (toolType != -1 && slot != this.bestToolSlots[toolType])
                                this.bestToolSlots[toolType] = slot;
                        } else if (stack.getItem() instanceof ItemArmor && InventoryUtils.isBestArmor(stack)) {
                            final ItemArmor armor = (ItemArmor) stack.getItem();

                            final int pieceSlot = this.bestArmorPieces[armor.armorType];

                            if (pieceSlot == -1 || slot != pieceSlot)
                                this.bestArmorPieces[armor.armorType] = slot;
                        } else if (stack.getItem() instanceof ItemBow && this.archeryProperty.getValue() && InventoryUtils.isBestBow(stack)) {
                            if (slot != bestBowSlot)
                                this.bestBowSlot = slot;
                        } else if (stack.getItem() instanceof ItemAppleGold)
                            this.gappleStackSlots.add(slot);
                        else if (!this.trash.contains(slot) && !isValidStack(stack, this.archeryProperty.getValue()))
                            this.trash.add(slot);
                    }
                }

                if (autoArmor && this.equipArmor())
                    return;

                if (cleaner && this.dropItem(this.trash))
                    return;

                if (sorter) {
                    int currentSlot = 36;

                    if (this.bestSwordSlot != -1) {
                        if (this.bestSwordSlot != currentSlot) {
                            this.putItemInSlot(currentSlot, this.bestSwordSlot);
                            this.bestSwordSlot = currentSlot;
                            return;
                        }
                        currentSlot++;
                    }


                    if (this.bestBowSlot != -1) {
                        if (this.bestBowSlot != currentSlot) {
                            this.putItemInSlot(currentSlot, this.bestSwordSlot);
                            this.bestBowSlot = currentSlot;
                            return;
                        }
                        currentSlot++;
                    }

                    if (!this.gappleStackSlots.isEmpty()) {
                        this.gappleStackSlots.sort((slot1, slot2) -> Wrapper.getStackInSlot(slot2).stackSize - Wrapper.getStackInSlot(slot1).stackSize);
                        int bestGappleSlot = this.gappleStackSlots.get(0);
                        if (bestGappleSlot != currentSlot) {
                            this.putItemInSlot(currentSlot, bestGappleSlot);
                            this.gappleStackSlots.set(0, currentSlot);
                            return;
                        }
                        currentSlot++;
                    }


                    if (this.sortToolsProperty.getValue()) {
                        final int[] toolSlots = {currentSlot, currentSlot + 1, currentSlot + 2};

                        for (int toolSlot : this.bestToolSlots) {
                            if (toolSlot != -1) {
                                int type = InventoryUtils.getToolType(Wrapper.getStackInSlot(toolSlot));
                                if (type != -1) {
                                    if (toolSlot != toolSlots[type]) {
                                        putToolsInSlot(type, toolSlots);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    private static boolean isValidStack(final ItemStack stack, final boolean archery) {
        if (stack.hasDisplayName() && HypixelGameUtils.getGameMode() != HypixelGameUtils.GameMode.BLITZ_SG)
            return true;
        else if (stack.getItem() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack))
            return true;
        else if (archery && stack.getItem().getUnlocalizedName().equals("item.arrow"))
            return true;
        else if (stack.getItem() instanceof ItemEnderPearl)
            return true;
        else if (stack.getItem() instanceof ItemPotion && InventoryUtils.isBuffPotion(stack))
            return true;
        else return stack.getItem() instanceof ItemFood && InventoryUtils.isGoodFood(stack);
    }

    private boolean equipArmor() {
        for (int i = 0; i < bestArmorPieces.length; i++) {
            final int piece = bestArmorPieces[i];

            if (piece != -1) {
                int armorPieceSlot = i + 5;
                ItemStack stack = Wrapper.getStackInSlot(armorPieceSlot);
                if (stack != null)
                    continue;

                open();
                InventoryUtils.equipArmor(piece);
                close();
                return true;
            }
        }

        return false;
    }

    private boolean dropItem(final List<Integer> listOfSlots) {
        if (!listOfSlots.isEmpty()) {
            open();
            int slot = listOfSlots.remove(0);
            InventoryUtils.windowClick(slot, 1, InventoryUtils.ClickType.DROP_ITEM);
            if (listOfSlots.isEmpty())
                close();
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        this.openInventory = Wrapper.getCurrentScreen() instanceof GuiContainer;
        this.interactionsTimer.reset();
    }

    @Override
    public void onDisable() {
        this.close();
    }

    private void clear() {
        this.trash.clear();
        this.bestBowSlot = -1;
        this.bestSwordSlot = -1;
        this.gappleStackSlots.clear();
        Arrays.fill(this.bestArmorPieces, -1);
        Arrays.fill(this.bestToolSlots, -1);
    }

    private void putItemInSlot(final int slot, final int slotIn) {
        open();
        InventoryUtils.windowClick(Wrapper.getPlayer().inventoryContainer.windowId,
                slotIn,
                slot - 36,
                InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        close();
    }

    private void putToolsInSlot(final int tool, final int[] toolSlots) {
        open();
        int toolSlot = toolSlots[tool];
        InventoryUtils.windowClick(Wrapper.getPlayer().inventoryContainer.windowId,
                this.bestToolSlots[tool],
                toolSlot - 36,
                InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.bestToolSlots[tool] = toolSlot;
        close();
    }

    private void open() {
        if (!this.openInventory) {
            this.interactionsTimer.reset();
            if (this.spoofInventoryProperty.getValue())
                InventoryUtils.openInventory();
            this.openInventory = true;
        }
    }

    private void close() {
        if (this.openInventory) {
            if (this.spoofInventoryProperty.getValue())
                InventoryUtils.closeInventory();
            this.openInventory = false;
        }
    }

    private enum Modules {
        AUTO_ARMOR,
        CLEANER,
    }
}
