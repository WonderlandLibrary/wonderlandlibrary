package org.dreamcore.client.feature.impl.player;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

public class ChestStealer extends Feature {

    private final NumberSetting delay;
    public TimerHelper timer = new TimerHelper();

    public ChestStealer() {
        super("ChestStealer", "Автоматически забирает вещи из сундуков", Type.Player);
        delay = new NumberSetting("Stealer Speed", 10, 0, 100, 1, () -> true);
        addSettings(delay);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setSuffix("" + (int) delay.getNumberValue());

        float delay = this.delay.getNumberValue() * 10;

        if (mc.player.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest) mc.player.openContainer;
            for (int index = 0; index < container.inventorySlots.size(); ++index) {
                if (container.getLowerChestInventory().getStackInSlot(index).getItem() != Item.getItemById(0) && timer.hasReached((delay))) {
                    ChestStealer.mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, mc.player);
                    timer.reset();
                    continue;
                }
                if (!isEmpty(container))
                    continue;
                mc.player.closeScreen();
            }
        }
    }

    public boolean isWhiteItem(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemEnderPearl || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemArrow || itemStack.getItem() instanceof ItemCompass);
    }

    private boolean isEmpty(Container container) {
        for (int index = 0; index < container.inventorySlots.size(); index++) {
            if (isWhiteItem(container.getSlot(index).getStack()))
                return false;
        }
        return true;
    }
}