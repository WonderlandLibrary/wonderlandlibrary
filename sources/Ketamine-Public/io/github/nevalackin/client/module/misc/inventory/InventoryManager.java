package io.github.nevalackin.client.module.misc.inventory;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.packet.SendPacketEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.player.WindowClickEvent;
import io.github.nevalackin.client.module.combat.rage.Aura;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.player.InventoryUtil;
import io.github.nevalackin.client.util.player.WindowClickRequest;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class InventoryManager extends Module {

    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.WHILE_OPEN);
    private final DoubleProperty delayProperty = new DoubleProperty("Delay", 150, 0, 500, 50);
    private final BooleanProperty dropItemsProperty = new BooleanProperty("Drop Items", true);
    private final BooleanProperty sortItemsProperty = new BooleanProperty("Sort Items", true);
    private final BooleanProperty autoArmorProperty = new BooleanProperty("Auto Armor", true);
    private final BooleanProperty ignoreItemsWithCustomName = new BooleanProperty("Ignore Custom Name", true);

    private final int[] bestArmorPieces = new int[4];
    private final List<Integer> trash = new ArrayList<>();
    private final int[] bestToolSlots = new int[3];
    private final List<Integer> gappleStackSlots = new ArrayList<>();
    private int bestSwordSlot;
    private int bestBowSlot;

    private final List<WindowClickRequest> clickRequests = new ArrayList<>();

    private boolean serverOpen;
    private boolean clientOpen;

    private int ticksSinceLastClick;

    private boolean nextTickCloseInventory;

    private Aura aura;

    public InventoryManager() {
        super("Inventory Manager", Category.MISC, Category.SubCategory.MISC_INVENTORY);

        this.register(this.modeProperty, this.delayProperty, this.dropItemsProperty, this.sortItemsProperty, this.autoArmorProperty, this.ignoreItemsWithCustomName);
    }

    private boolean isSpoof() {
        return this.modeProperty.getValue() == Mode.SPOOF;
    }

    @EventLink
    private final Listener<SendPacketEvent> onSendPacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C16PacketClientStatus) {
            final C16PacketClientStatus clientStatus = (C16PacketClientStatus) packet;

            if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                this.clientOpen = true;
                this.serverOpen = true;
            }
        } else if (packet instanceof C0DPacketCloseWindow) {
            final C0DPacketCloseWindow packetCloseWindow = (C0DPacketCloseWindow) packet;

            if (packetCloseWindow.getWindowId() == this.mc.thePlayer.inventoryContainer.windowId) {
                this.clientOpen = false;
                this.serverOpen = false;
            }
        }
    };

    @EventLink
    private final Listener<WindowClickEvent> onWindowClick = event ->
        this.ticksSinceLastClick = 0;

    private boolean dropItem(final List<Integer> listOfSlots) {
        if (this.dropItemsProperty.getValue()) {
            if (!listOfSlots.isEmpty()) {
                int slot = listOfSlots.remove(0);
                InventoryUtil.windowClick(this.mc, slot, 1, InventoryUtil.ClickType.DROP_ITEM);
                return true;
            }
        }
        return false;
    }

    public List<WindowClickRequest> getClickRequests() {
        return clickRequests;
    }

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S2DPacketOpenWindow) {
            this.clientOpen = false;
            this.serverOpen = false;
        }
    };

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdate = event -> {
        if (event.isPre()) {
            this.ticksSinceLastClick++;

            if (this.ticksSinceLastClick < Math.floor(this.delayProperty.getValue() / 50)) return;

            if (this.aura.getTarget() != null || this.aura.canAutoBlock()) {
                if (this.nextTickCloseInventory) {
                    this.nextTickCloseInventory = false;
                }

                this.close();
                return;
            }

            if (this.clientOpen || (this.mc.currentScreen == null && this.modeProperty.getValue() != Mode.WHILE_OPEN)) {
                this.clear();

                for (int slot = InventoryUtil.INCLUDE_ARMOR_BEGIN; slot < InventoryUtil.END; slot++) {
                    final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();

                    if (stack != null) {
                        if (this.ignoreItemsWithCustomName.getValue() &&
                            stack.hasDisplayName())
                            continue;

                        if (stack.getItem() instanceof ItemSword && InventoryUtil.isBestSword(this.mc.thePlayer, stack)) {
                            this.bestSwordSlot = slot;
                        } else if (stack.getItem() instanceof ItemTool && InventoryUtil.isBestTool(this.mc.thePlayer, stack)) {
                            final int toolType = InventoryUtil.getToolType(stack);
                            if (toolType != -1 && slot != this.bestToolSlots[toolType])
                                this.bestToolSlots[toolType] = slot;
                        } else if (stack.getItem() instanceof ItemArmor && InventoryUtil.isBestArmor(this.mc.thePlayer, stack)) {
                            final ItemArmor armor = (ItemArmor) stack.getItem();

                            final int pieceSlot = this.bestArmorPieces[armor.armorType];

                            if (pieceSlot == -1 || slot != pieceSlot)
                                this.bestArmorPieces[armor.armorType] = slot;
                        } else if (stack.getItem() instanceof ItemBow && InventoryUtil.isBestBow(this.mc.thePlayer, stack)) {
                            if (slot != this.bestBowSlot)
                                this.bestBowSlot = slot;
                        } else if (stack.getItem() instanceof ItemAppleGold) {
                            this.gappleStackSlots.add(slot);
                        } else if (!this.trash.contains(slot) && !isValidStack(stack)) {
                            this.trash.add(slot);
                        }
                    }
                }

                final boolean busy = (!this.trash.isEmpty() && this.dropItemsProperty.getValue()) || this.equipArmor(false) || this.sortItems(false) || !this.clickRequests.isEmpty();

                if (!busy) {
                    if (this.nextTickCloseInventory) {
                        this.close();
                        this.nextTickCloseInventory = false;
                    } else {
                        this.nextTickCloseInventory = true;
                    }
                    return;
                } else {
                    boolean waitUntilNextTick = !this.serverOpen;

                    this.open();

                    if (this.nextTickCloseInventory)
                        this.nextTickCloseInventory = false;

                    if (waitUntilNextTick) return;
                }

                if (!this.clickRequests.isEmpty()) {
                    final WindowClickRequest request = this.clickRequests.remove(0);
                    request.performRequest();
                    request.onCompleted();
                    return;
                }

                if (this.equipArmor(true)) return;
                if (this.dropItem(this.trash)) return;
                this.sortItems(true);
            }
        }
    };

    private boolean sortItems(final boolean moveItems) {
        if (this.sortItemsProperty.getValue()) {
            if (this.bestSwordSlot != -1) {
                if (this.bestSwordSlot != 36) {
                    if (moveItems) {
                        this.putItemInSlot(36, this.bestSwordSlot);
                        this.bestSwordSlot = 36;
                    }

                    return true;
                }
            }

            if (this.bestBowSlot != -1) {
                if (this.bestBowSlot != 38) {
                    if (moveItems) {
                        this.putItemInSlot(38, this.bestBowSlot);
                        this.bestBowSlot = 38;
                    }
                    return true;
                }
            }

            if (!this.gappleStackSlots.isEmpty()) {
                this.gappleStackSlots.sort(Comparator.comparingInt(slot -> this.mc.thePlayer.inventoryContainer.getSlot(slot).getStack().stackSize));

                final int bestGappleSlot = this.gappleStackSlots.get(0);

                if (bestGappleSlot != 37) {
                    if (moveItems) {
                        this.putItemInSlot(37, bestGappleSlot);
                        this.gappleStackSlots.set(0, 37);
                    }
                    return true;
                }
            }

            final int[] toolSlots = {39, 40, 41};

            for (final int toolSlot : this.bestToolSlots) {
                if (toolSlot != -1) {
                    final int type = InventoryUtil.getToolType(this.mc.thePlayer.inventoryContainer.getSlot(toolSlot).getStack());

                    if (type != -1) {
                        if (toolSlot != toolSlots[type]) {
                            if (moveItems) {
                                this.putToolsInSlot(type, toolSlots);
                            }
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean equipArmor(boolean moveItems) {
        if (this.autoArmorProperty.getValue()) {
            for (int i = 0; i < this.bestArmorPieces.length; i++) {
                final int piece = this.bestArmorPieces[i];

                if (piece != -1) {
                    int armorPieceSlot = i + 5;
                    final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(armorPieceSlot).getStack();
                    if (stack != null)
                        continue;

                    if (moveItems)
                        InventoryUtil.windowClick(this.mc, piece, 0, InventoryUtil.ClickType.SHIFT_CLICK);

                    return true;
                }
            }
        }

        return false;
    }

    private void putItemInSlot(final int slot, final int slotIn) {
        InventoryUtil.windowClick(this.mc, slotIn,
                                  slot - 36,
                                  InventoryUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
    }

    private void putToolsInSlot(final int tool, final int[] toolSlots) {
        final int toolSlot = toolSlots[tool];

        InventoryUtil.windowClick(this.mc, this.bestToolSlots[tool],
                                  toolSlot - 36,
                                  InventoryUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.bestToolSlots[tool] = toolSlot;
    }

    private static boolean isValidStack(final ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock && InventoryUtil.isStackValidToPlace(stack)) {
            return true;
        } else if (stack.getItem() instanceof ItemPotion && InventoryUtil.isBuffPotion(stack)) {
            return true;
        } else if (stack.getItem() instanceof ItemFood && InventoryUtil.isGoodFood(stack)) {
            return true;
        } else {
            return InventoryUtil.isGoodItem(stack.getItem());
        }
    }

    @Override
    public void onEnable() {
        if (this.aura == null) {
            this.aura = KetamineClient.getInstance().getModuleManager().getModule(Aura.class);
        }

        this.ticksSinceLastClick = 0;

        this.clientOpen = this.mc.currentScreen instanceof GuiInventory;
        this.serverOpen = this.clientOpen;
    }

    @Override
    public void onDisable() {
        this.close();
        this.clear();
        this.clickRequests.clear();
    }

    private void open() {
        if (!this.clientOpen && !this.serverOpen) {
            this.mc.thePlayer.sendQueue.sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            this.serverOpen = true;
        }
    }

    private void close() {
        if (!this.clientOpen && this.serverOpen) {
            this.mc.thePlayer.sendQueue.sendPacket(new C0DPacketCloseWindow(this.mc.thePlayer.inventoryContainer.windowId));
            this.serverOpen = false;
        }
    }

    private void clear() {
        this.trash.clear();
        this.bestBowSlot = -1;
        this.bestSwordSlot = -1;
        this.gappleStackSlots.clear();
        Arrays.fill(this.bestArmorPieces, -1);
        Arrays.fill(this.bestToolSlots, -1);
    }

    private enum Mode {
        WHILE_OPEN("In Inventory"),
        SPOOF("Spoof");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}