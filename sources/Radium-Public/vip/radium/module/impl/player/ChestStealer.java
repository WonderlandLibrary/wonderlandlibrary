package vip.radium.module.impl.player;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.InventoryUtils;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(label = "Chest Stealer", category = ModuleCategory.PLAYER)
public final class ChestStealer extends Module {

    private final List<Integer> lootedChests = new ArrayList<>();
    private final DoubleProperty clickDelayProperty = new DoubleProperty("Click Delay", 150, 10,
            500, 10, Representation.MILLISECONDS);
    private final DoubleProperty closeDelayProperty = new DoubleProperty("Close Delay", 150, 10,
            500, 10, Representation.MILLISECONDS);
    private final Property<Boolean> auraProperty = new Property<>("Aura", false);
    private final Property<Boolean> nameCheckProperty = new Property<>("Name Check", true);
    private final Property<Boolean> archeryProperty = new Property<>("Archery", false);

    private final TimerUtil timer = new TimerUtil();

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        if (event.getPacket() instanceof S2DPacketOpenWindow) {
            timer.reset();
        }
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = e -> {
        if (e.isPre()) {
            if (Wrapper.getCurrentScreen() instanceof GuiChest) {
                GuiChest chest = (GuiChest) Wrapper.getCurrentScreen();
                IInventory lowerChestInv = chest.getLowerChestInventory();
                if (lowerChestInv.getDisplayName().getUnformattedText().contains("Chest") || !nameCheckProperty.getValue()) {
                    if (isInventoryFull() || InventoryUtils.isInventoryEmpty(lowerChestInv, archeryProperty.getValue())) {
                        if (timer.hasElapsed(closeDelayProperty.getValue().longValue()))
                            Wrapper.getPlayer().closeScreen();
                        return;
                    }

                    for (int i = 0; i < lowerChestInv.getSizeInventory(); i++) {
                        if (timer.hasElapsed(clickDelayProperty.getValue().longValue())) {
                            if (InventoryUtils.isValid(lowerChestInv.getStackInSlot(i), archeryProperty.getValue())) {
                                InventoryUtils.windowClick(
                                        chest.inventorySlots.windowId, i, 0,
                                        InventoryUtils.ClickType.SHIFT_CLICK);
                                timer.reset();
                                return;
                            }
                        }
                    }
                }
            }
        }
    };

    private boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (!Wrapper.getPlayer().inventoryContainer.getSlot(i).getHasStack())
                return false;
        }
        return true;
    }

}
