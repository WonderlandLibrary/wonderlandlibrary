package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class SnowballThrowModule extends Module {
    private final BooleanSetting clickSimulation = new BooleanSetting("Click Simulation", this, true);
    private final NumberSetting useDelay = new NumberSetting("Use Delay", this, 0d, 0d, 10d, 1d);
    private final NumberSetting switchDelay = new NumberSetting("Switch Delay", this, 0d, 0d, 10d, 1d);
    private final KeybindSetting activateKey = new KeybindSetting("Activate Key", -1, this);

    public SnowballThrowModule() {
        super("SnowballThrow", "Throws snowballs/eggs on key", Category.COMBAT);
    }

    private int prevSlot;
    private int placeClock, switchClock;
    private boolean selectedSnowballs;

    private void reset() {
        prevSlot = 0;

        placeClock = 0;
        switchClock = 0;

        selectedSnowballs = false;
    }

    @Override
    public void onEnable() {
        reset();
    }

    private void setPrevSlot() {
        if (switchClock < switchDelay.getIValue()) {
            switchClock++;
            return;
        }

        InvUtils.setInvSlot(prevSlot);

        switchClock = 0;
    }

    private int getSnowballSlot() {
        int snowSlot = InvUtils.getItemSlot(Items.SNOWBALL);
        if (snowSlot != -1) return snowSlot;

        return InvUtils.getItemSlot(Items.EGG);
    }

    private boolean checkStack(ItemStack handStack) {
        return handStack.isOf(Items.SNOWBALL) || handStack.isOf(Items.EGG);
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (mc.currentScreen != null) return;

        if (KeyUtils.isKeyPressed(activateKey.getKeyCode())) {
            int snowSlot = getSnowballSlot();

            if (!checkStack(mc.player.getMainHandStack()) && !selectedSnowballs) {
                if (snowSlot == -1) return;

                if (switchClock < switchDelay.getIValue()) {
                    switchClock++;
                    return;
                }

                prevSlot = mc.player.getInventory().selectedSlot;
                InvUtils.setInvSlot(snowSlot);

                selectedSnowballs = true;
                switchClock = 0;
            }
            if (checkStack(mc.player.getMainHandStack())) {
                if (placeClock < useDelay.getIValue()) {
                    placeClock++;
                    return;
                }

                if (clickSimulation.isEnabled()) Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

                ActionResult interactionResult = mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                if (interactionResult.isAccepted() && interactionResult.shouldSwingHand()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }

                placeClock = 0;

                setPrevSlot();
                selectedSnowballs = false;
            }
        } else {
            if (mc.player.getInventory().selectedSlot != prevSlot) InvUtils.setInvSlot(prevSlot);
            reset();
        }
    }
}
