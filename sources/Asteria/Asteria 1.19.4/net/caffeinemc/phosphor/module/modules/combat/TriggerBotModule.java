package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.HandleInputEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.CPSCounter;
import net.caffeinemc.phosphor.api.util.MathUtils;
import net.caffeinemc.phosphor.api.util.PlayerUtils;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class TriggerBotModule extends Module {
    public final BooleanSetting clickSimulation = new BooleanSetting("Click Simulation", this, true);
    public final NumberSetting minRange = new NumberSetting("Min Range", this, 2.5d, 2d, 4d, 0.1d);
    public final NumberSetting maxRange = new NumberSetting("Max Range", this, 3d, 2d, 4d, 0.1d);
    public final BooleanSetting permTrigger = new BooleanSetting("Permament Trigger", this, true);
    public final BooleanSetting weaponOnly = new BooleanSetting("Weapon Only", this, true);
    public final BooleanSetting invisible = new BooleanSetting("Invisible", this, true);
    public final BooleanSetting smartCPS = new BooleanSetting("Smart CPS", this, true);
    public final NumberSetting minCPS = new NumberSetting("Min CPS", this, 5d, 1d, 20d, 1d);
    public final NumberSetting maxCPS = new NumberSetting("Max CPS", this, 10d, 1d, 20d, 1d);
    public final BooleanSetting focusMode = new BooleanSetting("Focus Mode", this, false);
    public final NumberSetting focusRange = new NumberSetting("Focus Range", this, 10d, 5d, 10d, 1d);

    public TriggerBotModule() {
        super("TriggerBot", "Automatically hits entity on crosshair", Category.COMBAT);
    }

    private boolean isHoldingWeapon() {
        ItemStack heldItem = mc.player.getMainHandStack();

        return heldItem.getItem() instanceof SwordItem || heldItem.getItem() instanceof AxeItem;
    }

    private double currentRange;
    private LivingEntity focusedTarget;

    @Override
    public void onEnable() {
        currentRange = 0;
        focusedTarget = null;
    }

    @EventHandler
    private void onPlayerTick(HandleInputEvent.Post event) {
        if (mc.player == null)
            return;

        if (focusMode.isEnabled()) {
            if (focusedTarget != null) {
                if (!focusedTarget.isAlive() ||
                        focusedTarget.isDead() ||
                        focusedTarget.isRemoved() ||
                        focusedTarget.distanceTo(mc.player) > focusRange.getValue())
                    focusedTarget = null;
            }
        }

        Entity target = mc.crosshairTarget instanceof EntityHitResult result ? result.getEntity() : null;

        if (target == null || mc.interactionManager == null || mc.currentScreen != null)
            return;

        if (target.getName().getString().equals(mc.player.getName().getString()))
            return;

        if (target instanceof PlayerEntity player && PlayerUtils.isFriend(player))
            return;

        if (!permTrigger.isEnabled() && !mc.options.attackKey.isPressed())
            return;

        if (weaponOnly.isEnabled() && !isHoldingWeapon())
            return;

        if (mc.player.isBlocking() || mc.player.isUsingItem() || !(target instanceof LivingEntity livingTarget) || ((LivingEntity) target).getHealth() <= 0.0f)
            return;

        if (livingTarget.isInvisible() && !invisible.isEnabled())
            return;

        if ((mc.player.isOnGround() && mc.player.getAttackCooldownProgress(0.5f) < 0.92f) || (!mc.player.isOnGround() && mc.player.getAttackCooldownProgress(0.5f) < 0.95f))
            return;

        if (currentRange == 0) {
            currentRange = (minRange.getValue() >= maxRange.getValue()) ? minRange.getValue() : MathUtils.getRandomDouble(minRange.getValue(), maxRange.getValue());
            currentRange *= currentRange;
        }

        if (mc.player.getCameraPosVec(mc.getTickDelta()).squaredDistanceTo(mc.crosshairTarget.getPos()) > currentRange)
            return;

        if (focusMode.isEnabled()) {
            if (focusedTarget == null) focusedTarget = livingTarget;
            if (focusedTarget != livingTarget) return;
        }

        if (smartCPS.isEnabled()) {
            int expectedCps = CPSCounter.getLeftCPS() + 1;
            double avgDiff = expectedCps - CPSCounter.getLeftCpsAverage();

            if (!((expectedCps <= minCPS.getIValue() && MathUtils.getRandomInt(0, 100) <= 70) || (maxCPS.getIValue() >= expectedCps && MathUtils.getRandomInt(0, 100) <= 50 && avgDiff < 1.8))) {
                return;
            }
        }

        if (clickSimulation.isEnabled()) {
            Phosphor.mouseSimulation().mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);
        } else {
            CPSCounter.leftClick.add(System.currentTimeMillis());
        }

        mc.interactionManager.attackEntity(mc.player, livingTarget);
        mc.player.swingHand(Hand.MAIN_HAND);

        currentRange = 0;
    }
}
