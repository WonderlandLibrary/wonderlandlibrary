package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.client.InventoryUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "AutoSoup", description = "Makes you automatically eat soup whenever your health is low.", category = ModuleCategory.BLATANT)
public class AutoSoup extends Module {

    private final FloatValue healthValue = new FloatValue("Health", 15.0f, 0.0f, 20.0f);
    private final IntegerValue delayValue = new IntegerValue("Delay", 150, 0, 500);
    private final BoolValue openInventoryValue = new BoolValue("OpenInv", false);
    private final BoolValue simulateInventory = new BoolValue("SimulateInventory", true);
    private final MSTimer timer = new MSTimer();

    @Override
    public String getTag() {
        return String.valueOf((healthValue.get()));
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!timer.hasTimePassed((long)delayValue.get())) return;
        int soupInHotbar = InventoryUtils.findItem(36, 45, Items.mushroom_stew);

        if (mc.thePlayer.getHealth() <= healthValue.get() && soupInHotbar != -1) {
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(soupInHotbar - 36));
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer
                    .getSlot(soupInHotbar).getStack()));
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM,
                    BlockPos.ORIGIN, EnumFacing.DOWN));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            timer.reset();
            return;
        }

        int soupInInventory = InventoryUtils.findItem(9, 36, Items.mushroom_stew);
        if (soupInInventory != -1 && InventoryUtils.hasSpaceHotbar()) {
            if (openInventoryValue.get() && !(mc.currentScreen instanceof GuiInventory))
                return;

            boolean openInventory = !(mc.currentScreen instanceof GuiInventory) && simulateInventory.get();

            if (openInventory)
                mc.getNetHandler().addToSendQueue(
                        new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));

            mc.playerController.windowClick(0, soupInInventory, 0, 1, mc.thePlayer);

            if (openInventory)
                mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());

            timer.reset();
        }
    }

}
