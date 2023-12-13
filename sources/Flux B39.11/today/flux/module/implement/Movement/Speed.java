package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import today.flux.event.MoveEvent;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.TickEvent;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.Combat.TargetStrafe;
import today.flux.module.implement.Movement.speed.*;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;

public class Speed extends Module {
    public Speed() {
        super("Speed", Category.Movement, true, new NCPHop(), new SlowHop(), new Swift(), new HypixelDisabled(), new HypixelLow(), new Mineplex(), new Spartan(), new AACSlowHop());
    }

    public static boolean isTargetStrafing;
    public static BooleanValue lagbackCheck = new BooleanValue("Speed", "Lagback Check", true);
    public static BooleanValue inwater = new BooleanValue("Speed", "Disable In Water", true);
    public static BooleanValue onladder = new BooleanValue("Speed", "Disable On Ladder", true);
    public static BooleanValue onGround = new BooleanValue("Speed", "On Ground", true);
    public static FloatValue timerBoost = new FloatValue("Speed", "Timer Boost", 1f, 0.5f, 3f, 0.1f);
    public static FloatValue timerBoostAttack = new FloatValue("Speed", "Timer Boost (Attacking)", 1f, 0.5f, 3f, 0.1f);

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        isTargetStrafing = false;
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }

    public static void setMotion(MoveEvent em, double speed) {
        if (!TargetStrafe.ground.getValueState() && ModuleManager.speedMod.isEnabled() && ModuleManager.targetStrafeModule.isEnabled() && (!TargetStrafe.jumpKey.getValueState() || mc.gameSettings.keyBindJump.isKeyDown())) {
            if (KillAura.target != null) {
                isTargetStrafing = true;
                TargetStrafe.move(em, speed, KillAura.target);
                return;
            }
        }

        isTargetStrafing = false;

        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
            if (em != null) {
                em.setX(0.0D);
                em.setZ(0.0D);
            }
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90));
            double sin = Math.sin(Math.toRadians(yaw + 90));
            mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
            mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
            if (em != null) {
                em.setX(mc.thePlayer.motionX);
                em.setZ(mc.thePlayer.motionZ);
            }
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (ModuleManager.killAuraMod.isEnabled() && KillAura.target != null) {
            mc.timer.timerSpeed = timerBoostAttack.getValue();
        } else {
            mc.timer.timerSpeed = timerBoost.getValue();
        }

        if (inwater.getValueState() && mc.thePlayer.isInWater() || onladder.getValueState() && mc.thePlayer.isOnLadder()) {
            NotificationManager.show("Module", this.getName() + " Disabled (Check)", Notification.Type.WARNING);
            this.disable();
        }
    }

    @EventTarget
    public void onLagback(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && lagbackCheck.getValueState()) {
            this.toggle();
        }
    }

    @EventTarget
    public void onPacketTake(PacketReceiveEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            this.cooldown(500);
        }
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2872D;
        if (mc.thePlayer != null && mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
        }

        return baseSpeed;
    }

    public static void setMoveSpeed(final MoveEvent event, final double speed) {
        double forward = mc.thePlayer.moveForward;
        double strafe = mc.thePlayer.moveStrafing;
        float yaw = mc.thePlayer.rotationYaw;

        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));

            if (ModuleManager.speedMod.isEnabled() && ModuleManager.targetStrafeModule.isEnabled() && (!TargetStrafe.jumpKey.getValueState() || mc.gameSettings.keyBindJump.isKeyDown())) {
                if (KillAura.target != null) {
                    TargetStrafe.move(event, speed, KillAura.target);
                    return;
                }
            }
        }
    }

    public static void setMoveSpeedAAC(final MoveEvent event, final double speed) {
        double forward = 1;
        double strafe = 0;
        float yaw = mc.thePlayer.rotationYaw;

        event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
                + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
        event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
                - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));

        if (ModuleManager.targetStrafeModule.isEnabled()) {
            if (KillAura.target != null) {
                if (KillAura.target.getDistanceToEntity(mc.thePlayer) <= ModuleManager.targetStrafeModule.range
                        .getValue()) {
                    ModuleManager.targetStrafeModule.move(event, speed, KillAura.target);
                    return;
                }
            }
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void damage() {
        double offset = 0.0625;
        if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
            for (short i = 0; i <= ((4) / offset); i++) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((4) / offset))));
            }
        }
    }

}
