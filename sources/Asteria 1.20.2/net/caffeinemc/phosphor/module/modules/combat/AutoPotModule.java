package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.caffeinemc.phosphor.module.setting.settings.ModeSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class AutoPotModule extends Module {
    private final NumberSetting minHealth = new NumberSetting("Min Health", this, 10, 1, 20, 1);
    private final NumberSetting switchDelay = new NumberSetting("Switch Delay", this, 0, 0, 10, 1);
    private final NumberSetting throwDelay = new NumberSetting("Throw Delay", this, 0, 0, 10, 1);
    private final ModeSetting mode = new ModeSetting("Mode", this, "Manual", "Dynamic", "Manual");
    private final BooleanSetting scroll = new BooleanSetting("Scroll", this, false);
    private final BooleanSetting goToPrevSlot = new BooleanSetting("Previous Slot", this, true);
    private final KeybindSetting activateKey = new KeybindSetting("Activate Key", -1, this);

    public AutoPotModule() {
        super("AutoPot", "Automatically throws pots to heal you.", Category.COMBAT);
    }

    private int switchClock, throwClock, prevSlot;
    private boolean dynamicActivated;

    @Override
    public void onEnable() {
        switchClock = 0;
        throwClock = 0;
        prevSlot = -1;
        dynamicActivated = false;
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (mc.currentScreen != null)
            return;

        if (KeyUtils.isKeyPressed(activateKey.getKeyCode())) {
            if (((mc.player.getHealth() <= minHealth.getFValue() || dynamicActivated) && mode.is("Dynamic")) || (mode.is("Manual"))) {
                if (dynamicActivated && mc.player.getHealth() >= mc.player.getMaxHealth()) {
                    dynamicActivated = false;
                    return;
                }

                if (!InvUtils.isThatSplash(6, 1, 1, mc.player.getMainHandStack())) {
                    if (switchClock < switchDelay.getValue()) {
                        switchClock++;
                        return;
                    }

                    if (goToPrevSlot.isEnabled() && prevSlot == -1) prevSlot = mc.player.getInventory().selectedSlot;

                    int potSlot = InvUtils.findSplash(6, 1, 1);

                    if (potSlot != -1) {
                        if (scroll.isEnabled()) {
                            InvUtils.setInvSlot(InvUtils.scrollToSlot(potSlot));
                        } else {
                            InvUtils.setInvSlot(potSlot);
                        }

                        switchClock = 0;
                    }
                }

                if (InvUtils.isThatSplash(6, 1, 1, mc.player.getMainHandStack())) {
                    if (throwClock < throwDelay.getValue()) {
                        throwClock++;
                        return;
                    }

                    ActionResult actionResult = mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    if (actionResult.shouldSwingHand())
                        mc.player.swingHand(Hand.MAIN_HAND);

                    throwClock = 0;
                }
            }
        } else if (prevSlot != -1) {
            InvUtils.setInvSlot(prevSlot);
            prevSlot = -1;
        }
    }
}
