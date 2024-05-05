package client.module.impl.player;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.event.impl.other.Render3DEvent;
import client.event.impl.other.WorldLoadEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.liquidbounce.ItemUtils;
import client.util.liquidbounce.MSTimer;
import client.util.liquidbounce.RandomUtils;
import client.util.liquidbounce.TimeUtils;
import client.value.impl.BooleanValue;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ModuleInfo(name = "Chest Stealer", description = "Steals items from chests for you", category = Category.PLAYER)
public final class ChestStealer extends Module {

    private final int maxDelay = 100, minDelay = 75;
    private final BooleanValue delayOnFirst = new BooleanValue("DelayOnFirst", this, false), takeRandomized = new BooleanValue("TakeRandomized", this, false), onlyItems = new BooleanValue("OnlyItems", this, false), noCompass = new BooleanValue("NoCompass", this, false), autoClose = new BooleanValue("AutoClose", this, true);
    private final int autoCloseMaxDelay = 0, autoCloseMinDelay = 0;
    private final BooleanValue closeOnFull = new BooleanValue("CloseOnFull", this, true), chestTitle = new BooleanValue("ChestTitle", this, false);

    private final MSTimer delayTimer = new MSTimer();
    private int nextDelay = TimeUtils.randomDelay(minDelay, maxDelay);
    private final MSTimer autoCloseTimer = new MSTimer();
    private int nextCloseDelay = TimeUtils.randomDelay(autoCloseMinDelay, autoCloseMaxDelay);
    private int contentReceived;

    @EventLink
    public final Listener<Render3DEvent> onRender3D = event -> {
        final GuiScreen screen = mc.currentScreen;
        if (screen == null) return;
        if (!(screen instanceof GuiChest)) {
            if (delayOnFirst.getValue()) delayTimer.reset();
            autoCloseTimer.reset();
            return;
        }
        if (!delayTimer.hasTimePassed(nextDelay)) {
            autoCloseTimer.reset();
            return;
        }
        if (noCompass.getValue() && Objects.equals(mc.thePlayer.inventory.getCurrentItem() != null ? mc.thePlayer.inventory.getCurrentItem().getItem() != null ? mc.thePlayer.inventory.getCurrentItem().getItem().getUnlocalizedName() : null : null, "item.compass")) return;
        if (chestTitle.getValue() && (((GuiChest) screen).getLowerChestInventory() == null || !((GuiChest) screen).getLowerChestInventory().getName().contains(new ItemStack(Item.itemRegistry.getObject(new ResourceLocation("minecraft:chest"))).getDisplayName()))) return;
        if (!isEmpty((GuiChest) screen) && (!closeOnFull.getValue() || !getFullInventory())) {
            autoCloseTimer.reset();
            if (takeRandomized.getValue()) {
                List<Slot> items;
                do {
                    items = new ArrayList<>();
                    for (int slotIndex = 0; slotIndex < ((GuiChest) screen).getInventoryRows() * 9; slotIndex++) {
                        final Slot slot = ((GuiChest) screen).inventorySlots.getSlot(slotIndex);
                        final ItemStack stack = slot.getStack();
                        final InventoryCleaner inventoryCleaner = Client.INSTANCE.getModuleManager().get(InventoryCleaner.class);
                        if (stack != null && (!onlyItems.getValue() || !(stack.getItem() instanceof ItemBlock)) && (!inventoryCleaner.isEnabled() || inventoryCleaner.isUseful(stack, -1))) items.add(slot);
                    }
                    move((GuiChest) screen, items.get(RandomUtils.nextInt(0, items.size())));
                } while (delayTimer.hasTimePassed(nextDelay) && !items.isEmpty());
                return;
            }
            for (int slotIndex = 0; slotIndex < ((GuiChest) screen).getInventoryRows() * 9; slotIndex++) {
                final Slot slot = ((GuiChest) screen).inventorySlots.getSlot(slotIndex);
                final ItemStack stack = slot.getStack();
                if (delayTimer.hasTimePassed(nextDelay) && shouldTake(stack)) move((GuiChest) screen, slot);
            }
        } else if (autoClose.getValue() && ((GuiChest) screen).inventorySlots.windowId == contentReceived && autoCloseTimer.hasTimePassed(nextCloseDelay)) {
            mc.thePlayer.closeScreen();
            nextCloseDelay = TimeUtils.randomDelay(autoCloseMinDelay, autoCloseMaxDelay);
        }
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S30PacketWindowItems) contentReceived = ((S30PacketWindowItems) packet).func_148911_c();
    };

    private boolean shouldTake(final ItemStack stack) {
        final InventoryCleaner inventoryCleaner = Client.INSTANCE.getModuleManager().get(InventoryCleaner.class);
        return stack != null && !ItemUtils.isStackEmpty(stack) && (!onlyItems.getValue() || !(stack.getItem() instanceof ItemBlock)) && (!inventoryCleaner.isEnabled() || inventoryCleaner.isUseful(stack, -1));
    }

    private void move(final GuiChest screen, final Slot slot) {
        mc.playerController.windowClick(screen.inventorySlots.windowId, slot.slotNumber, 0, 1, mc.thePlayer);
        delayTimer.reset();
        nextDelay = TimeUtils.randomDelay(minDelay, maxDelay);
    }

    private boolean isEmpty(final GuiChest chest) {
        for (int i = 0; i < chest.getInventoryRows() * 9; i++) if (shouldTake(chest.inventorySlots.getSlot(i).getStack())) return false;
        return true;
    }

    private boolean getFullInventory() {
        return Arrays.stream(mc.thePlayer.inventory.mainInventory).noneMatch(ItemUtils::isStackEmpty);
    }

    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> toggle();
}
