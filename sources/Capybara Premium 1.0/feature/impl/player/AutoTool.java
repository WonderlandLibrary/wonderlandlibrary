package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventBlockInteract;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoTool extends Feature {
    private BooleanSetting swapBack = new BooleanSetting("Swap Back", true, () -> true);
    private BooleanSetting saveItem = new BooleanSetting("Save Item", true, () -> true);
    public BooleanSetting silentSwitch = new BooleanSetting("Silent Switch", true, () -> true);

    public int itemIndex;
    private boolean swap;
    private long swapDelay;
    private ItemStack swapedItem = null;
    private List<Integer> lastItem = new ArrayList<>();

    public AutoTool() {
        super("AutoTool", "Автоматически берет лучший инструмент в руки при ломании блока", FeatureCategory.Player);
        addSettings(swapBack, saveItem, silentSwitch);
    }


    @EventTarget
    public void onUpdate(EventUpdate update) {

        Block hoverBlock = null;
        if (mc.objectMouseOver.getBlockPos() == null) return;
        hoverBlock = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
        List<Integer> bestItem = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (hoverBlock == null) break;
            if (!(mc.player.inventory.getStackInSlot(i).getMaxDamage() - mc.player.inventory.getStackInSlot(i).getItemDamage() > 1) && saveItem.getBoolValue())
                continue;
            if(mc.player.getDigSpeed(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()), mc.player.inventory.getStackInSlot(i)) > 1)
                bestItem.add(i);

        }

        bestItem.sort(Comparator.comparingDouble(x -> -mc.player.getDigSpeed(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()), mc.player.inventory.getStackInSlot(x))));


        if (!bestItem.isEmpty() && mc.gameSettings.keyBindAttack.pressed) {
            if (mc.player.inventory.getCurrentItem() != swapedItem) {
                lastItem.add(mc.player.inventory.currentItem);
                if (silentSwitch.getBoolValue())
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(bestItem.get(0)));
                else
                    mc.player.inventory.currentItem = bestItem.get(0);

                itemIndex = bestItem.get(0);
                swap = true;
            }
            swapDelay = System.currentTimeMillis();
        } else if (swap && !lastItem.isEmpty() && System.currentTimeMillis() >= swapDelay + 300 && swapBack.getBoolValue()) {

            if (silentSwitch.getBoolValue())
                mc.player.connection.sendPacket(new CPacketHeldItemChange(lastItem.get(0)));
            else
                mc.player.inventory.currentItem = lastItem.get(0);

            itemIndex = lastItem.get(0);
            lastItem.clear();
            swap = false;
        }
    }
}