package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import net.minecraft.block.AirBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.client.util.InputMappings;
import org.lwjgl.glfw.GLFW;
public class BSpeed {
    private int tick = 0;
    private double yCoord = 0;
    private boolean spoofGround = false;
    private boolean spoof = false; // Assuming this is a boolean variable that can be accessed
    private boolean checkGround = false;
    private boolean lagBackCheck = false;
    private int lagBackTicks = 0;
    private int speedLevel = 0;
    private int airTick = 0;
    private boolean groundSpeedState = false;
    private int notLiquidTick = 0;
    private double mx = 0;
    private double mz = 0;
    private double realPos = 0;
    private Object modules;

    public void myMethod() {
        long windowHandle = Minecraft.getInstance().getMainWindow().getHandle();
        boolean isKeyDown = InputMappings.isKeyDown(windowHandle, GLFW.GLFW_KEY_SPACE);
        if (isKeyDown) {
            // The space key is down
        }
    }


    public void speedMulti(double multiple) {
        Minecraft mc = Minecraft.getInstance();
        mc.player.setMotion(mc.player.getMotion().mul(multiple, 1, multiple));
    }

    public double speedValue(double v0, double v1, double v2) {
        Minecraft mc = Minecraft.getInstance();
        Effect speedEffect = mc.player.getActivePotionEffect(Effects.SPEED).getPotion();
        if (speedEffect != null) {
            if (speedEffect.getAmplifier() == 0) {
                return v1;
            } else if (speedEffect.getAmplifier() >= 1) {
                return v2;
            }
        }
        return v0;
    }

    public void onDisable() {
        // Assuming modules.setState is a method that can be accessed
            speedMulti(0.3);
        }
    }