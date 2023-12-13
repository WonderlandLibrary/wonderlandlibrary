package io.github.nevalackin.client.module.misc.inventory;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.player.WindowClickEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.util.player.InventoryUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

public final class ChestStealer extends Module {

//    private final BooleanProperty auraProperty = new BooleanProperty("Aura", true);
    private final BooleanProperty autoDelayProperty = new BooleanProperty("Auto Delay", true);
    private final DoubleProperty delayProperty = new DoubleProperty("Delay", 100,
                                                                    () -> !this.autoDelayProperty.getValue(),
                                                                    0, 500, 50);

    private long lastClickTime;
    private int lastColumn, lastRow;

    private long nextDelay = 100L;

    public ChestStealer() {
        super("Stealer", Category.MISC, Category.SubCategory.MISC_INVENTORY);

        this.register(this.autoDelayProperty, this.delayProperty);
    }

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        // Open delay (wait before you steal)
        if (packet instanceof S2DPacketOpenWindow) { // When you open a window
            final S2DPacketOpenWindow openWindow = (S2DPacketOpenWindow) packet;

            if (openWindow.getGuiId().equals("minecraft:container")) // Check it's a chest
                this.reset();
        }
    };

    private void reset() {
        // Reset last click & do open delay
        this.lastClickTime = System.currentTimeMillis();
        this.nextDelay = 100L;
        // Reset cursor pos
        this.lastColumn = 0;
        this.lastRow = 0;
    }

    @EventLink
    private final Listener<WindowClickEvent> onWindowClick = event -> {
        this.lastClickTime = System.currentTimeMillis();
    };

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdate = event -> {
        if (event.isPre()) {
            final long timeSinceLastClick = System.currentTimeMillis() - this.lastClickTime;
            if (timeSinceLastClick < this.nextDelay) return;

            final GuiScreen current = this.mc.currentScreen;

            if (current instanceof GuiChest) {
                final GuiChest guiChest = (GuiChest) current;

                final IInventory lowerChestInventory = guiChest.getLowerChestInventory();

                final String chestName = lowerChestInventory.getDisplayName().getUnformattedText();

                if (!chestName.equals(I18n.format("container.chest")) && !chestName.equals(I18n.format("container.chestDouble")))
                    return;
                if (!InventoryUtil.hasFreeSlots(this.mc.thePlayer)) {
                    // Close delay
                    if (timeSinceLastClick > 100L)
                        this.mc.thePlayer.closeScreen();
                    return;
                }

                final int nSlots = lowerChestInventory.getSizeInventory();
                for (int i = 0; i < nSlots; i++) {
                    final ItemStack stack = lowerChestInventory.getStackInSlot(i);

                    if (InventoryUtil.isValidStack(this.mc.thePlayer, stack)) {
                        final int column = i % 9;
                        final int row = i % (nSlots / 9);

                        final int columnDif = this.lastColumn - column;
                        final int rowDif = this.lastRow - row;

                        this.nextDelay = this.autoDelayProperty.getValue() ?
                            (long) Math.ceil(50.0 * Math.max(1.0, Math.sqrt(columnDif * columnDif + rowDif * rowDif))) :
                            this.delayProperty.getValue().longValue();

                        if (timeSinceLastClick < this.nextDelay) return;

                        InventoryUtil.windowClick(this.mc, this.mc.thePlayer.openContainer.windowId, i, 0, InventoryUtil.ClickType.SHIFT_CLICK);

                        this.lastColumn = column;
                        this.lastRow = row;
                        return;
                    }
                }

                // Close delay
                if (timeSinceLastClick > 100L)
                    this.mc.thePlayer.closeScreen();
            }
        }
    };

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {

    }
}
