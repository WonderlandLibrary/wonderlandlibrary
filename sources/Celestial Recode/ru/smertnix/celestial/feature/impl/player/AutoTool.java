package ru.smertnix.celestial.feature.impl.player;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventBlockInteract;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoTool extends Feature {
    public BooleanSetting silentSwitch = new BooleanSetting("Silent", true, () -> true);

    public int itemIndex;
    private boolean swap;
    private long swapDelay;
    private ItemStack swapedItem = null;
    private List<Integer> lastItem = new ArrayList<>();

    public AutoTool() {
        super("Auto Tool", "Автоматически берет нужный предмет", FeatureCategory.Util);
        addSettings(silentSwitch);
    }


    @EventTarget
    public void onUpdate(EventUpdate update) {

        Block hoverBlock = null;
        if (mc.objectMouseOver.getBlockPos() == null) return;
        hoverBlock = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
        List<Integer> bestItem = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (hoverBlock == null) break;
            if (!(mc.player.inventory.getStackInSlot(i).getMaxDamage() - mc.player.inventory.getStackInSlot(i).getItemDamage() > 1))
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
        }
    }
}