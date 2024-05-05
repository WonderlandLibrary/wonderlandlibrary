package club.async.module.impl.player;

import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.PlayerUtil;
import club.async.util.TimeUtil;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "ChestStealer", description = "Steal Chests", category = Category.PLAYER)
public class ChestStealer extends Module {

    public NumberSetting delay = new NumberSetting("Delay", this, 0, 1000, 100, 50);
    public NumberSetting firstDelay = new NumberSetting("FirstDelay", this, 0, 1000, 100, 50);
    public BooleanSetting autoClose = new BooleanSetting("AutoClose", this, true);
    public BooleanSetting name = new BooleanSetting("Name", this, true);

    public TimeUtil timer = new TimeUtil();
    public TimeUtil fistTimer = new TimeUtil();

    @Handler
    public void update(EventUpdate event) {
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            if (chest.getLowerChestInventory().getName().toLowerCase().contains("chest")) {
                if (!PlayerUtil.isChestEmpty()) {
                    for (ItemStack itemStack : chest.getInventory()) {
                        int slot = chest.getInventory().indexOf(itemStack);
                        ItemStack stack = chest.getInventory().get(slot);
                        if (chest.getLowerChestInventory().getStackInSlot(slot) != null) {
                            if (timer.hasTimePassed(delay.getLong()) && fistTimer.hasTimePassed(firstDelay.getLong())) {
                                mc.playerController.windowClick(chest.windowId, slot, 0, 1, mc.thePlayer);
                                timer.reset();
                            }
                        }
                    }
                } else {
                    if (autoClose.get()) {
                        mc.thePlayer.closeScreen();
                    }
                }
            }
        } else {
            fistTimer.reset();
        }
    }

    @Override
    public void onEnable() {
        fistTimer.reset();
        timer.reset();
        super.onEnable();
    }
}
