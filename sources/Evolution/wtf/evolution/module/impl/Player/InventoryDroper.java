package wtf.evolution.module.impl.Player;

import net.minecraft.inventory.ClickType;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "InventoryDroper", type = Category.Player)
public class InventoryDroper extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
        for (int o = 0; o < 46; ++o) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, o, 1, ClickType.THROW, mc.player);
        }
        toggle();
    }
}
