package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.ChatUtils;
import today.flux.utility.InvUtils;
import today.flux.utility.TimeHelper;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;

public class AutoHead extends Module {
    public FloatValue slot = new FloatValue("AutoHead", "Slot", 7.0f, 1.0f, 9.0f, 1.0f);
    public FloatValue delay = new FloatValue("AutoHead", "Delay", 0.0f, 0.0f, 2.0f, 0.05f);
    public FloatValue health = new FloatValue("AutoHead", "Health", 14.0f, 1.0f, 20.0f, 0.5f);
    public ModeValue healMode = new ModeValue("AutoHead", "HealMode", "Absorption", "Regen", "Absorption");

    TimeHelper timer = new TimeHelper();

    public AutoHead() {
        super("AutoHead", Category.Combat, false);
    }

    @EventTarget
    public void onMotion(PreUpdateEvent event) {
        if (!timer.isDelayComplete(delay.getValue() * 1000))
            return;

        if (healMode.isCurrentMode("Regen") && (mc.thePlayer.getHealth() >= health.getValue() || mc.thePlayer.isPotionActive(Potion.regeneration)))
            return;

        if (healMode.isCurrentMode("Absorption") && mc.thePlayer.getAbsorptionAmount() != 0)
            return;

        InventoryPlayer inventory = mc.thePlayer.inventory;

        int slot = getHeadFromInventory();

        if (slot == -1)
            return;

        int tempSlot = inventory.currentItem;
        ChatUtils.debug(slot);

        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot - 36));
        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));

        timer.reset();
    }

    private int getHeadFromInventory() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (Item.getIdFromItem(stack.getItem()) != 397)
                continue;

            return i;
        }

        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (Item.getIdFromItem(stack.getItem()) != 397)
                continue;

            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, slot.getValue().intValue(), 2, mc.thePlayer);
        }

        return -1;
    }
}
