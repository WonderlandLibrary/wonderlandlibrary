
package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SoupItem;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoSoup extends Module {
    private final NumberValue delay = new NumberValue("Delay", 0, 0, 20, NumberValue.NUMBER_TYPE.INT);
    private final NumberValue health = new NumberValue("Health", 10, 0, 20, NumberValue.NUMBER_TYPE.INT);

    public AutoSoup() {
        super("AutoSoup", Category.Item, "Automatically drinks soup");
     registerValue(delay);
     registerValue(health);
    }

    int oldSlot = -1, delayT = 0;
    boolean click = false;

    @Override
    public void onEnable() {
        click = false;
    }

    public void no() {
        if (oldSlot != -1) {
            mc.player.inventory.currentItem = oldSlot;
            mc.gameSettings.keyBindUseItem.pressed = false;
            oldSlot = -1;
        }
    }

    @Override
    public void onUpdateEvent(UpdateEvent e) {
        if (e.isPost()) return;
        PlayerInventory inventory = mc.player.inventory;
        int nextTotemSlot = searchForTotems(inventory);
        if (nextTotemSlot == -1) {
            no();
            return;
        }
        if (delayT > 0) {
            delayT--;
            no();
            return;
        }
        if (mc.player.getHealth() > health.getValue().intValue()) {
            no();
            return;
        }
        if (mc.player.inventory.currentItem != nextTotemSlot) {
            oldSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = nextTotemSlot;
        }
        click = true;
        delayT = delay.getValue().intValue();
    }

    @Override
    public void onClickEvent(ClickEvent event) {
        if (click) {
            click = false;
            mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
        }
    }

    private int searchForTotems(PlayerInventory inventory) {
        for (int slot = 0; slot < 9; slot++) {
            if (!isNeed(inventory.getStackInSlot(slot)))
                continue;
            return slot;
        }
        return -1;
    }

    private boolean isNeed(ItemStack stack) {
        return stack.getItem() instanceof SoupItem;
    }

}
