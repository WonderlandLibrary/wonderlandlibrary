package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import today.flux.event.PreUpdateEvent;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.InvUtils;
import today.flux.utility.PlayerUtils;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;

public class AutoGApple extends Module {
    public FloatValue slotValue = new FloatValue("AutoGApple", "Slot", 6.0f, 1.0f, 9.0f, 1.0f);
    public FloatValue health = new FloatValue("AutoGApple", "Health", 14.0f, 1.0f, 20.0f, 0.5f);
    public ModeValue healMode = new ModeValue("AutoGApple", "HealMode", "Absorption", "Regen", "Absorption");

    int timer, eatTicks, slot, oldSlot;

    public AutoGApple() {
        super("AutoGapple", Category.Combat, false);
    }

    @EventTarget
    public void onPre(PreUpdateEvent e) {
        if (timer < 20)
            timer++;

        if(timer >= 20) {
            if (mc.thePlayer.getHealth() < health.getValue()
                    && ((!mc.thePlayer.isPotionActive(Potion.regeneration) && healMode.isCurrentMode("Regen"))
                    || (mc.thePlayer.getAbsorptionAmount() == 0 && healMode.isCurrentMode("Absorption")))) {
                slot = getAppleFromInventory();

                if(slot != -1) {
                    slot = slot - 36;
                    eatTicks = 0;
                    timer = 0;
                }
            }
        }

        if(eatTicks >= 0 && slot >= 0) {
            eatTicks++;

            if (eatTicks == 1) {
                oldSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;
            } else if(eatTicks >= 2) {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));

                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot + 1 >= 9 ? 0 : slot + 1));
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));

                mc.thePlayer.inventory.currentItem = oldSlot;
                eatTicks = -1;
            }
        }
    }

    private int getAppleFromInventory() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (stack.getItem() != Items.golden_apple)
                continue;

            return i;
        }

        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (stack.getItem() != Items.golden_apple)
                continue;

            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, slotValue.getValue().intValue(), 2, mc.thePlayer);
        }

        return -1;
    }
}
