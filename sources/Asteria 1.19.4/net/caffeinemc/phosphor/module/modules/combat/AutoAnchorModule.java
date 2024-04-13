package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.events.TickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.BlockUtils;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.mixin.ClientPlayerInteractionManagerAccessor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class AutoAnchorModule extends Module {
    public final NumberSetting switchDelay = new NumberSetting("Switch Delay", this, 0, 0, 20, 1);
    public final NumberSetting placeDelay = new NumberSetting("Place Delay", this, 0, 0, 20, 1);
    public final NumberSetting cooldown = new NumberSetting("Cooldown", this, 0, 0, 20, 1);
    public final NumberSetting itemSwap = new NumberSetting("Item Swap", this, 1, 1, 9, 1);
    public final BooleanSetting chargeOnly = new BooleanSetting("Charge Only", this, false);

    public AutoAnchorModule() {
        super("AutoAnchor", "Automatically achors", Category.COMBAT);
    }

    private static boolean hasAnchored;
    private static int switchClock, placeClock, cooldownClock;

    private void reset() {
        hasAnchored = false;
        switchClock = 0;
        placeClock = 0;
        cooldownClock = 0;
    }

    @Override
    public void onEnable() {
        reset();
    }

    @EventHandler
    private void onPlayerTick(PlayerTickEvent event) {
        if (mc.world == null || mc.player == null || mc.currentScreen != null)
            return;

        if (hasAnchored) {
            if (cooldownClock < cooldown.getIValue()) {
                cooldownClock++;
                return;
            }

            reset();
        }

        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS) {
            reset();
            return;
        }

        if (mc.player.isUsingItem())
            return;

        if (mc.crosshairTarget instanceof BlockHitResult hit) {
            if (hit.getType() == HitResult.Type.MISS)
                return;

            BlockPos pos = hit.getBlockPos();

            if (BlockUtils.isAnchorUncharged(pos)) {
                if (!mc.player.isHolding(Items.GLOWSTONE)) {
                    if (switchClock < switchDelay.getIValue()) {
                        switchClock++;
                        return;
                    }

                    InvUtils.selectItemFromHotbar(Items.GLOWSTONE);

                    switchClock = 0;
                }

                if (placeClock < placeDelay.getIValue()) {
                    placeClock++;
                    return;
                }

                ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                if (actionResult.isAccepted() && actionResult.shouldSwingHand()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }

                placeClock = 0;
            }
            if (BlockUtils.isAnchorCharged(pos) && !chargeOnly.isEnabled()) {
                if (mc.player.getInventory().selectedSlot != itemSwap.getIValue() - 1) {
                    if (switchClock < switchDelay.getIValue()) {
                        switchClock++;
                        return;
                    }

                    mc.player.getInventory().selectedSlot = itemSwap.getIValue() - 1;
                    ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).callSyncSelectedSlot();

                    switchClock = 0;
                }

                if (placeClock < placeDelay.getIValue()) {
                    placeClock++;
                    return;
                }

                ActionResult actionResult2 = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                if (actionResult2.isAccepted() && actionResult2.shouldSwingHand()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }

                placeClock = 0;
                hasAnchored = true;
            }
        }
    }
}
