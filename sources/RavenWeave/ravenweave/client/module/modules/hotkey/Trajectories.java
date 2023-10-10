package ravenweave.client.module.modules.hotkey;

import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class Trajectories extends Module {
    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference;

    public Trajectories() {
        super("Trajectories", ModuleCategory.hotkey);

        this.registerSetting(preferSlot = new TickSetting("Prefer a slot", false));
        this.registerSetting(hotbarSlotPreference = new SliderSetting("Prefer wich slot", 5, 1, 9, 1));
    }

    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame())
            return;

        if (preferSlot.isToggled()) {
            int preferedSlot = (int) hotbarSlotPreference.getInput() - 1;

            if (checkSlot(preferedSlot)) {
                mc.thePlayer.inventory.currentItem = preferedSlot;
                this.disable();
                return;
            }
        }

        for (int slot = 0; slot <= 8; slot++) {
            if (checkSlot(slot)) {
                if (mc.thePlayer.inventory.currentItem != slot) {
                    mc.thePlayer.inventory.currentItem = slot;
                } else {
                    return;
                }
                this.disable();
                return;
            }
        }
        this.disable();
    }

    public static boolean checkSlot(int slot) {
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);

        return itemInSlot != null && (itemInSlot.getItem() instanceof ItemSnowball
                || itemInSlot.getItem() instanceof ItemEgg || itemInSlot.getItem() instanceof ItemFishingRod);
    }
}
