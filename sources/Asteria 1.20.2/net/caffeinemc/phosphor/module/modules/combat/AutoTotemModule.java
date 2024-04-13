package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.event.orbit.EventPriority;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static net.caffeinemc.phosphor.api.util.InvUtils.selectItemFromHotbar;

public class AutoTotemModule extends Module {
    public final NumberSetting delay = new NumberSetting("Delay", this, 0, 0, 5, 1);

    public AutoTotemModule() {
        super("AutoTotem", "Automatically swaps totem", Category.COMBAT);
    }

    private boolean swapped;
    private int clock;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTick(PlayerTickEvent event) {
        if (mc.currentScreen != null)
            return;

        PlayerInventory inventory = mc.player.getInventory();
        ItemStack offhandStack = inventory.getStack(40);

        if (offhandStack.getItem() == Items.TOTEM_OF_UNDYING) {
            swapped = false;
            clock = delay.getIValue();
            return;
        }

        if (clock > 0) {
            clock--;
            return;
        }

        if (swapped)
            return;

        if (mc.currentScreen != null)
            return;

        if (!selectItemFromHotbar(Items.TOTEM_OF_UNDYING)) {
            swapped = false;
            return;
        }

        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        swapped = true;
    }
}
