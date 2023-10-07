package best.azura.client.impl.module.impl.combat.autoheal;

import best.azura.eventbus.core.Event;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class SoupHealSub extends HealSub {

    private final BooleanValue goldenHeadValue = new BooleanValue("Golden Heads", "Use golden heads.", true);
    private final NumberValue<Double> healthValue = new NumberValue<>("Health", "Heal once the you are on the HP level", 10D, 1D, 5D, 20D);
    private final BooleanValue refillValue = new BooleanValue("Refill", "Refill the soups.", true);
    private final NumberValue<Double> delayValue = new NumberValue<>("Delay", "Delay between heals.", 100D, 1D, 50D, 1000D);
    private final DelayUtil timer = new DelayUtil(), refillTimer = new DelayUtil();
    private int lastSlot = -1;
    public static int state = 0;

    @Override
    public String getName() {
        return "AutoSoup";
    }

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(goldenHeadValue, healthValue, refillValue, delayValue);
    }

    @Override
    public void handle(Event e) {
        if(e instanceof EventUpdate) {

            if(refillTimer.hasReached(delayValue.getObject()) && refillValue.getObject()) {
                int freeSlot = -1;
                for(int i = 0; i < 9; i++) {
                    if(mc.thePlayer.inventory.getStackInSlot(i) == null) freeSlot = i;
                }
                if(freeSlot != -1) {
                    for (int i = 9; i < 36; i++) {
                        ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if ((stack != null) && (stack.getItem() == Items.mushroom_stew)) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, freeSlot, 2, mc.thePlayer);
                            return;
                        }
                    }
                }
                refillTimer.reset();
            }

            int currentSlot = findSlot();
            if(timer.hasReached(delayValue.getObject())) {
                timer.reset();

                if(state >= 1) {
                    if(state == 2) {
                        mc.gameSettings.keyBindDrop.pressed = false;
                        state = 0;
                        if(lastSlot != -1) mc.thePlayer.inventory.currentItem = lastSlot;
                        return;
                    }
                    mc.gameSettings.keyBindDrop.pressed = true;
                    state++;
                    return;
                }

                for (int i = 9; i < 45; i++)
                {
                    ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if ((stack != null) && (stack.getItem() == Items.bowl))
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                }

                if(currentSlot != -1 && mc.thePlayer.getHealth() < healthValue.getObject()) {

                    mc.thePlayer.inventory.currentItem = currentSlot;
                    mc.rightClickMouse();
                    state++;

                }

            } else if(state == 0) {
                lastSlot = mc.thePlayer.inventory.currentItem;
            }

        }
    }

    private int findSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) continue;
            Item item = stack.getItem();
            if ((mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemSoup
                    || (item == Items.skull && goldenHeadValue.getObject() && mc.thePlayer.getAbsorptionAmount() == 0)
                    || (item == Items.magma_cream && goldenHeadValue.getObject() &&
                    EnumChatFormatting.getTextWithoutFormattingCodes(stack.getDisplayName()).equals("Fractured Soul")) && mc.thePlayer.getAbsorptionAmount() == 0)
                    || (item == Items.mutton && goldenHeadValue.getObject() &&
                    EnumChatFormatting.getTextWithoutFormattingCodes(stack.getDisplayName()).contains("Steak") && !mc.thePlayer.isPotionActive(Potion.resistance))
                    || (item == Items.baked_potato && goldenHeadValue.getObject() &&
                    EnumChatFormatting.getTextWithoutFormattingCodes(stack.getDisplayName()).contains("Rage")) && mc.thePlayer.getAbsorptionAmount() == 0)
            return i;
        }
        return -1;
    }
}