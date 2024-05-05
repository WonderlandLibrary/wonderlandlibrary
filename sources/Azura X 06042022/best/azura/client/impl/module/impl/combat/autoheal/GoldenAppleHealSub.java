package best.azura.client.impl.module.impl.combat.autoheal;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.client.impl.module.impl.combat.KillAura;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GoldenAppleHealSub extends HealSub {
    private final BooleanValue disableAuraValue = new BooleanValue("Disable Aura", "Disables the Aura once you heal", false);
    private final NumberValue<Double> healthValue = new NumberValue<>("Health", "Heal once the you are on the HP level", 10D, 1D, 5D, 18D);
    private final DelayUtil delay = new DelayUtil();
    private boolean healing;
    private boolean reset;
    private int lastSlot = -1, lastStackAmount, disableTicks;
    private KillAura killAura = null;

    @Override
    public String getName() {
        return "AutoGoldenApple";
    }

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(disableAuraValue, healthValue);
    }

    @Override
    public void handle(Event event) {
        if (killAura == null) killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (event instanceof EventWorldChange) reset = true;
        if (event instanceof EventMotion) {
            EventMotion e = (EventMotion) event;
            if (e.isUpdate()) {
                if (mc.thePlayer.getHealth() < healthValue.getObject()) healing = true;
                else if (healing) reset = true;
                if (reset) {
                    mc.gameSettings.keyBindUse.pressed = false;
                    if (lastSlot != -1) {
                        mc.thePlayer.inventory.currentItem = lastSlot;
                        lastSlot = -1;
                    }
                    lastStackAmount = -1;
                    healing = false;
                    reset = false;
                }
                if (healing) {
                    if (disableAuraValue.getObject()) {
                        disableTicks = 1;
                        killAura.disable = true;
                        killAura.targets.clear();
                    }
                    if (mc.thePlayer.isUsingItem()) {
                        mc.gameSettings.keyBindUse.pressed = false;
                        mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    }
                    int currentSlot = -1;
                    for (int i = 0; i <= 8; i++) {
                        ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                        if (stack != null && stack.getItem() instanceof ItemFood && stack.getItem() == Items.golden_apple) {
                            currentSlot = i;
                            break;
                        }
                    }
                    if (currentSlot != -1) {
                        if (mc.thePlayer.inventory.currentItem != currentSlot) {
                            lastSlot = mc.thePlayer.inventory.currentItem;
                            mc.thePlayer.inventory.currentItem = currentSlot;
                            if (mc.thePlayer.getHeldItem() != null) {
                                lastStackAmount = mc.thePlayer.getHeldItem().stackSize;
                                mc.gameSettings.keyBindUse.pressed = true;
                                delay.reset();
                            } else {
                                reset = true;
                            }
                        }
                    } else {
                        reset = true;
                    }
                    reset = mc.thePlayer.getHeldItem().stackSize != lastStackAmount || delay.hasReached(1000);
                } else if (disableTicks != 0) {
                    disableTicks = 0;
                    killAura.disable = false;
                }
            }
        }
    }
}
