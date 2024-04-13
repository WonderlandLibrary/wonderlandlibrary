package dev.tenacity.module.impl.movement;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.TimerUtil;
import dev.tenacity.util.player.MovementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.optifine.expr.Token;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.input.Keyboard.KEY_SPACE;

public final class LongJumpModule extends Module {

    public final ModeSetting mode = new ModeSetting("Mode",  "Mineland", "Verus", "Fireball", "Vulcan", "NCP", "Grim");

    public final ModeSetting verusSettings = new ModeSetting("Type",  "Fast", "High");

    private boolean ftd = false;
    private final TimerUtil timerUtil = new TimerUtil();
    private int jumps, stage, damageStage;
    private boolean firstVelocity, skipTick;

    private float velocityMotionY;

    private final List<C03PacketPlayer.C04PacketPlayerPosition> jumpPackets = new ArrayList<>();

    public LongJumpModule() {
        super("LongJump", "Makes you jump farther/higher", ModuleCategory.MOVEMENT);
        verusSettings.addParent(mode, modeSetting -> mode.isMode("Verus"));
        initializeSettings(mode, verusSettings);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        switch (mode.getCurrentMode()) {
            case "Mineland": {
                //    mc.thePlayer.motionY = 0.4f;
                    MovementUtil.setSpeed(0.1);
                    toggle();
                break;
            }

            case "Verus": {
                switch (verusSettings.getCurrentMode()) {
                    case "Fast": {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                            mc.thePlayer.rotationPitch = 90.0f;
                        }
                        if (timerUtil.hasTimeElapsed(10, true)) {
                            int fireballSlot = -1;
                            for (int i = 0; i < 9; i++) {
                                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                                if (stack != null && (stack.getItem().getUnlocalizedName().equalsIgnoreCase("item.fireball") || stack.getItem().getUnlocalizedName().equalsIgnoreCase("item.firecharge"))) {
                                    fireballSlot = i;
                                    mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                                    mc.thePlayer.rotationPitch = 0f;
                                    break;
                                }
                            }
                            if (fireballSlot != -1) {
                                mc.thePlayer.inventory.currentItem = fireballSlot;
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                                mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                                mc.thePlayer.rotationPitch = 0f;
                            }
                        }
                        if (mc.thePlayer.hurtTime > 0) {
                            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                            mc.thePlayer.rotationPitch = 0f;
                            mc.thePlayer.motionY = 1f;
                            Timer.timerSpeed = 10f;
                            MovementUtil.setSpeed(9.6);
                            toggle();
                        }
                        break;
                    }
                    case "High": {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                            mc.thePlayer.rotationPitch = 90.0f;
                        }
                        if (timerUtil.hasTimeElapsed(10, true)) {
                            int fireballSlot = -1;
                            for (int i = 0; i < 9; i++) {
                                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                                if (stack != null && (stack.getItem().getUnlocalizedName().equalsIgnoreCase("item.fireball") || stack.getItem().getUnlocalizedName().equalsIgnoreCase("item.firecharge"))) {
                                    fireballSlot = i;
                                    mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                                    mc.thePlayer.rotationPitch = 0f;
                                    break;
                                }
                            }
                            if (fireballSlot != -1) {
                                mc.thePlayer.inventory.currentItem = fireballSlot;
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                                mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                                mc.thePlayer.rotationPitch = 0f;
                            }
                        }
                        if (mc.thePlayer.hurtTime > 0) {
                            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                            mc.thePlayer.rotationPitch = 0f;
                            mc.thePlayer.motionY = 4f;
                            Timer.timerSpeed = 30f;
                            MovementUtil.setSpeed(9);
                            toggle();
                        }
                        break;
                    }
                }
                break;
            }

            case "Fireball": {
                if (event.isPre() && mc.thePlayer.hurtTime >= 7) {
                    ChatUtil.enable("Enabled");
                        MovementUtil.setSpeed(1.2);
                    ChatUtil.disable("Disabled");
                    toggle();
                }
                break;
            }

            case "Grim": {
                if (event.isPre()) {
                    ChatUtil.enable("Enabled");
                    mc.thePlayer.motionY = 0.4f;
                    Timer.timerSpeed = 50f;
                    MovementUtil.setSpeed(9);
                    ChatUtil.disable("Disabled");
                    toggle();
                }
                break;
            }

            case "NCP": {
                if (mc.thePlayer.hurtTime > 0) {
                    mc.thePlayer.motionY = 0.5f;
                    toggle();
                }
                break;
            }

            case "Vulcan": {
                ftd = false;
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                        mc.thePlayer.rotationPitch = 90.0f;
                    }
                    if (timerUtil.hasTimeElapsed(10, true)) {
                        int fireballSlot = -1;
                        for (int i = 0; i < 9; i++) {
                            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                            if (stack != null && (stack.getItem().getUnlocalizedName().equalsIgnoreCase("item.fireball") || stack.getItem().getUnlocalizedName().equalsIgnoreCase("item.firecharge"))) {
                                fireballSlot = i;
                                mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                                mc.thePlayer.rotationPitch = 0f;
                                break;
                            }
                        }
                        if (fireballSlot != -1) {
                            mc.thePlayer.inventory.currentItem = fireballSlot;
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
                            mc.thePlayer.rotationPitch = 0f;
                        }
                    }
                    if (mc.thePlayer.hurtTime > 8) {
                        Timer.timerSpeed = 0.1f;
                        mc.thePlayer.motionY = 4f;
                        MovementUtil.setSpeed(2.6);
                        toggle();
                        ftd = true;
                    }
                    break;
            }
        }
    };

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            if (mc.thePlayer.hurtTime == 0) {
                ChatUtil.notify("Take damage to start long jump");
            }
        }
        Timer.timerSpeed = 1f;
        jumps = 0;
        stage = 0;
        firstVelocity = true;
        velocityMotionY = 0;
        damageStage = 0;
        jumpPackets.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Timer.timerSpeed = 1f;
        if (mode.isMode("High")) {
            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw;
            mc.thePlayer.rotationPitch = 0f;
        }
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }
    private final IEventListener<UpdateEvent> onUpdateEvent = event -> setSuffix(mode.getCurrentMode());
}
