/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSnow;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Objects;

@ModuleInfo(name = "Step", description = "Lets you step higher", category = Category.MOVEMENT)
public final class Step extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Normal", "Low", "Delay", "Low2", "Low3", "Timer", "Matrix", "Hypixel", "NCP");
    private final NumberSetting height = new NumberSetting("Height", this, 1.5, 1.0, 10, 0.5);
    private final NumberSetting timer = new NumberSetting("Timer", this, 0.5, 0.1, 10, 0.1);

    public static String modeString;
    private int stepTicks;
    private float stepHeight;
    private double posX, posY, posZ;
    private static int ticksOnGround, ticksSinceLastStep;

    @Override
    public void onUpdateAlwaysInGui() {
        height.hidden = !(mode.is("Vanilla") || mode.is("NCP") || mode.is("Hypixel"));

        timer.hidden = !(mode.is("NCP") || mode.is("Hypixel"));

        switch (mode.getMode()) {
            case "Vanilla":
                height.maximum = 10;
                break;

            case "NCP":
                height.maximum = 2.5;

                if (height.getValue() > 2.5)
                    height.setValue(2.5);
                break;

            case "Hypixel":
                height.maximum = 1.5;

                if (height.getValue() > 1.5)
                    height.setValue(1.5);
                break;
        }
    }

    @Override
    protected void onEnable() {
        stepTicks = ticksOnGround = ticksSinceLastStep = 0;
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.stepHeight = 0.6f;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        stepTicks++;
        modeString = mode.getMode();
        final double direction2 = MoveUtil.getDirection();

        final double x = -Math.sin(direction2) * 0.9;
        final double z = Math.cos(direction2) * 0.9;

        ticksSinceLastStep++;
        if (mc.thePlayer.onGround) {
            ticksOnGround++;
        } else {
            ticksOnGround = 0;
        }

        if (mode.is("Vanilla"))
            mc.thePlayer.stepHeight = (float) (height.getValue() + 0.00069);
        else if (mc.thePlayer.stepHeight == (float) (height.getValue() + 0.00069))
            mc.thePlayer.stepHeight = 0.6f;

        if ((!Objects.requireNonNull(instance.getModuleManager().getModule("Speed")).isEnabled() || mode.is("Hypixel")) && mc.thePlayer.onGround && !mc.thePlayer.isInLiquid()) {
            switch (mode.getMode()) {
                case "NCP":
                case "Hypixel":
                    mc.thePlayer.stepHeight = (float) (height.getValue() + 0.00069);
                    break;
            }
        } else {
            mc.thePlayer.stepHeight = 0.6f;
        }

        final Block above = PlayerUtil.getBlockRelativeToPlayer(x, 1.1, z);

        switch (mode.getMode()) {
            case "Hypixel":
            case "NCP":
                if (ticksSinceLastStep > 1 && mc.timer.timerSpeed == ((float) (timer.getValue() + 0.00069))) {
                    mc.timer.timerSpeed = 1;
                }
                break;
        }

        if (!(mode.is("NCP") || mode.is("Hypixel") || mode.is("Vanilla")) && mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally && MoveUtil.isMoving()) {
            if ((above instanceof BlockAir || above instanceof BlockBush || above instanceof BlockSnow) && !(PlayerUtil.getBlockRelativeToPlayer(x, 0.5, z) instanceof BlockAir)) {
                stepTicks = 0;
                stepHeight = 1;
                mc.thePlayer.motionY = 0.42f;

                posY = mc.thePlayer.posY;
                posZ = mc.thePlayer.posZ;
                posX = mc.thePlayer.posX;

                switch (mode.getMode()) {
                    case "Low":
                        mc.thePlayer.motionY = (0.37 + Math.random() / 500);
                        break;

                    case "Low2":
                        mc.thePlayer.motionY = (0.39 + Math.random() / 500);
                        break;

                    case "Low3":
                        mc.thePlayer.motionY = (0.404 + Math.random() / 500);
                        break;

                    case "Matrix":
                        mc.thePlayer.jump();
                        break;
                }
            }
        }

        if (MoveUtil.isMoving()) {
            switch (mode.getMode()) {
                case "Normal":
                    if (stepTicks == 3 && stepHeight == 1) {
                        mc.thePlayer.motionY = -0.14;
                    }
                    break;

                case "Delay":
                    if (stepTicks == 2) {
                        mc.thePlayer.motionY = 0.17;
                    }
                    break;

                case "Matrix":
                    if (stepTicks == 1) {
                        mc.thePlayer.onGround = true;
                    }
                    break;
            }
        }
    }

    /*
     * Used event outside of the eventsystem to not impact performance
     */
    public static void onStep() {
        if (!Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Step")).isEnabled() || modeString == null || (Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Speed")).isEnabled() && !modeString.equals("Hypixel")) || mc.thePlayer == null || !mc.thePlayer.onGround || mc.thePlayer.isInLiquid())
            return;

        final double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
        double[] values = new double[0];

        switch (modeString) {
            case "NCP":
                if (height > 2.019) {
                    values = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919};
                } else if (height > 1.869) {
                    values = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
                } else if (height > 1.5) {
                    values = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652};
                } else if (height > 1.015) {
                    values = new double[]{0.42, 0.7532, 1.01, 1.093, 1.015};
                } else if (height > 0.875) {
                    values = new double[]{0.42, 0.7532};
                } else if (height > 0.6) {
                    values = new double[]{0.39, 0.6938};
                }

                if (height > 0.6) {
                    mc.timer.timerSpeed = (float) (((NumberSetting) Objects.requireNonNull(instance.getModuleManager().getSetting("Step", "Timer"))).getValue() + 0.00069);
                    ticksSinceLastStep = 0;
                }

                for (final double d : values) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (d + Math.random() / 2000), mc.thePlayer.posZ, false));
                }
                break;

            case "Hypixel":
                if (height > 1.015) {
                    values = PlayerUtil.isOnServer("Hypixel") ? new double[]{0.42, 0.7532, 1, 0.98} : new double[]{0.42, 0.7532, 1.01, 1.093, 1.015};
                } else if (height > 0.875) {
                    values = new double[]{0.42, 0.7532};
                } else if (height > 0.7) {
                    values = new double[]{0.39, 0.6938};
                }

                if (height > 0.7) {
                    mc.timer.timerSpeed = (float) (((NumberSetting) Objects.requireNonNull(instance.getModuleManager().getSetting("Step", "Timer"))).getValue() + 0.00069);
                    ticksSinceLastStep = 0;
                }

                for (final double d : values) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (d + Math.random() / 200), mc.thePlayer.posZ, false));
                }
                break;
        }
    }
}