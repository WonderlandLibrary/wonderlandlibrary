/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.utils.Timer;

public class ChestStealer
extends Module {
    private Container inventory;
    private IInventory iinventory;
    private Integer windowId;
    private Integer ticks = 0;
    private Timer timer = new Timer();
    public DoubleSetting speed = new DoubleSetting("Speed", 0.0, 500.0, 0.0);
    public BooleanSetting slientSetting = new BooleanSetting("Slient", true);
    public BooleanSetting chestCheck = new BooleanSetting("ChestCheck", true);

    public ChestStealer() {
        super("ChestStealer", "Grab all items from chest", 0, Category.PLAYER);
    }

    @Override
    public String getSuffix() {
        if (this.slientSetting.getValue().booleanValue()) {
            return "Slient";
        }
        return "OpenInv";
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @Override
    public void onEvent(Event e2) {
        try {
            Object packet;
            Object p2;
            if (e2 instanceof PacketReceiveEvent && this.slientSetting.getValue().booleanValue()) {
                p2 = ((PacketReceiveEvent)e2).getPacket();
                if (((PacketReceiveEvent)e2).getPacket() instanceof S2DPacketOpenWindow && (((S2DPacketOpenWindow)(packet = (S2DPacketOpenWindow)((PacketReceiveEvent)e2).getPacket())).getWindowTitle().getUnformattedTextForChat().equalsIgnoreCase("chest") || !this.chestCheck.getValue().booleanValue())) {
                    ((PacketReceiveEvent)e2).setCanceled(true);
                    this.iinventory = new InventoryBasic(((S2DPacketOpenWindow)packet).getWindowTitle(), ((S2DPacketOpenWindow)packet).getSlotCount());
                    this.inventory = new ContainerChest(this.mc.thePlayer.inventory, this.iinventory, this.mc.thePlayer);
                    this.windowId = ((S2DPacketOpenWindow)packet).getWindowId();
                }
                if (p2 instanceof S30PacketWindowItems) {
                    packet = (S30PacketWindowItems)((PacketReceiveEvent)e2).getPacket();
                    if (this.windowId == null) {
                        return;
                    }
                    if (((S30PacketWindowItems)packet).getWindowID() != this.windowId.intValue()) {
                        return;
                    }
                    this.inventory.putStacksInSlots(((S30PacketWindowItems)packet).getItemStacks());
                    ((PacketReceiveEvent)e2).setCanceled(true);
                }
            }
            if (e2 instanceof WorldTickEvent) {
                p2 = this.ticks;
                this.ticks = this.ticks + 1;
                packet = this.ticks;
                if (this.ticks % 1 == 0 && this.timer.hasTimeElapsed((int)this.speed.getValue().doubleValue(), true)) {
                    for (int i2 = 0; i2 <= 10; ++i2) {
                        boolean isChest = false;
                        if (!this.slientSetting.getValue().booleanValue()) {
                            this.inventory = this.mc.thePlayer.openContainer;
                            isChest = this.chestCheck.getValue().booleanValue() ? this.mc.currentScreen instanceof GuiChest && ((GuiChest)this.mc.currentScreen).lowerChestInventory.getDisplayName().getUnformattedText().equalsIgnoreCase("chest") : this.mc.currentScreen instanceof GuiChest;
                        } else {
                            boolean bl = isChest = this.windowId != null;
                        }
                        if (!isChest) continue;
                        int emptySlots = 0;
                        for (int slot = 10; slot < 45; ++slot) {
                            if (this.mc.thePlayer.inventoryContainer.getSlot(slot).getHasStack()) continue;
                            ++emptySlots;
                        }
                        if (!this.empty(this.inventory) && emptySlots != 0) {
                            int index = this.next(this.inventory);
                            if (this.slientSetting.getValue().booleanValue()) {
                                short short1 = this.inventory.getNextTransactionID(this.mc.thePlayer.inventory);
                                this.mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(this.windowId, index, 0, 1, this.inventory.getSlot(index).getStack(), short1));
                                this.inventory.slotClick(index, 0, 1, this.mc.thePlayer);
                            } else {
                                this.mc.playerController.windowClick(this.mc.thePlayer.openContainer.windowId, index, 0, 1, this.mc.thePlayer);
                            }
                            if (!this.slientSetting.getValue().booleanValue()) continue;
                            this.inventory.putStackInSlot(index, null);
                            continue;
                        }
                        if (this.slientSetting.getValue().booleanValue()) {
                            this.mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow(this.inventory.windowId));
                            this.windowId = null;
                            continue;
                        }
                        this.mc.thePlayer.closeScreen();
                    }
                }
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private boolean empty(Container c2) {
        int slot = c2.inventorySlots.size() == 90 ? 54 : 27;
        for (int i2 = 0; i2 < slot; ++i2) {
            if (!c2.getSlot(i2).getHasStack()) continue;
            return false;
        }
        return true;
    }

    private int next(Container c2) {
        int slot = c2.inventorySlots.size() == 90 ? 54 : 27;
        for (int i2 = 0; i2 < slot; ++i2) {
            if (c2.getInventory().get(i2) == null) continue;
            return i2;
        }
        return -1;
    }
}

