package org.dreamcore.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class HighJump extends Feature {

    public ListSetting mode;
    public NumberSetting motionBoost;
    public BooleanSetting motionYBoost = new BooleanSetting("MotionY boost", false, () -> true);
    public boolean jump = false;
    public TimerHelper helper = new TimerHelper();

    private int ticks = 0;
    private boolean timerEnable = false;

    public HighJump() {
        super("HighJump", "Вы подлетаете на большую высоту", Type.Movement);
        mode = new ListSetting("HighJump Mode", "AntiCheatA", () -> true, "AntiCheatA", "AntiCheatB", "Matrix", "Vanilla", "Matrix 6.3.0");
        motionBoost = new NumberSetting("Motion Boost", 0.6F, 0.1F, 8F, 0.1F, () -> mode.currentMode.equals("AntiCheatA") && motionYBoost.getBoolValue() || mode.currentMode.equals("Vanilla"));
        addSettings(mode, motionYBoost, motionBoost);
    }

    @EventTarget
    public void onPreUpdate(EventUpdate event) {
        String highJumpMode = mode.currentMode;
        this.setSuffix(highJumpMode);
        if (!this.getState())
            return;

        if (highJumpMode.equals("Matrix")) {
            if (mc.gameSettings.keyBindJump.pressed) {
                mc.player.motionY += 0.42;
            }
        } else if (highJumpMode.equalsIgnoreCase("AntiCheatB")) {
            if (mc.player.hurtTime > 0) {
                mc.player.motionY += 1.30;
            }
        } else if (highJumpMode.equals("Matrix 6.3.0")) {
            if (timerEnable) {
                mc.timer.timerSpeed = 1;
                timerEnable = false;
            }
            if ((!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0).expand(0.0, 0.0, 0.0)).isEmpty() || !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -4.0, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) && mc.player.fallDistance > 10) {
                if (!mc.player.onGround) {
                    mc.timer.timerSpeed = 0.1f;
                    timerEnable = true;
                }
            }
            if (helper.hasReached(1000) && ticks == 1) {
                mc.timer.timerSpeed = 1.0f;
                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
                ticks = 0;
            }
            if (ticks == 1 && mc.player.hurtTime > 0) {
                mc.timer.timerSpeed = 1;
                mc.player.motionY = 9;
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                ticks = 0;
            }
            if (ticks == 2) {
                for (int i = 0; i < 9; i++) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.3990, mc.player.posZ, false));
                }
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                mc.timer.timerSpeed = 0.6F;
                ticks = 1;
                helper.reset();
            }
            if (mc.player.isCollidedHorizontally && ticks == 0 && mc.player.onGround) {
                ticks = 2;
                mc.timer.timerSpeed = 0.05f;
            }
            if (mc.player.isCollidedHorizontally && mc.player.onGround) {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                mc.player.onGround = false;
            }
        } else if (highJumpMode.equalsIgnoreCase("AntiCheatA")) {
            if (mc.player.hurtTime > 0) {
                mc.player.motionY += motionBoost.getNumberValue();
            }
        } else if (highJumpMode.equalsIgnoreCase("Vanilla") && !jump) {
            jump = true;
            if (mc.gameSettings.keyBindJump.pressed) {
                mc.player.motionY += motionBoost.getNumberValue();
            }
        }
        if (mc.player.onGround) {
            jump = false;
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        ticks = 0;
        timerEnable = false;
        super.onDisable();
    }
}
