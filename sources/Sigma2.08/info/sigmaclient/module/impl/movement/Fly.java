package info.sigmaclient.module.impl.movement;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.users.impl.Staff;
import info.sigmaclient.management.users.impl.Upgraded;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

public class Fly extends Module {

    public static String SPEED = "SPEED";
    public static final String MODE = "MODE";
    public static final String BYPASS = "HYPIXEL";
    Timer kickTimer = new Timer();
    private double flyHeight;
    private double startY;

    public Fly(ModuleData data) {
        super(data);
        settings.put(SPEED, new Setting<>(SPEED, 2.0f, "Movement speed.", 0.25, 0.25, 5));
        settings.put(MODE, new Setting<>(MODE, new Options("Fly Mode", "Motion", new String[]{"Vanilla", "AntiKick", "Glide", "Motion"}), "Fly method."));
    }


    private boolean allowBypass() {
        Setting setting;
        try {
            setting = settings.get(BYPASS);
        } catch (Exception e) {
            return false;
        }
        return (setting != null) && (Boolean) setting.getValue() && (Client.um.getUser() instanceof Upgraded || Client.um.getUser() instanceof Staff);
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null)
            return;
        mc.timer.timerSpeed = 1f;
        startY = mc.thePlayer.posY;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.allowFlying = false;
    }

    @Override
    @RegisterEvent(events = {EventMove.class, EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            double speed = Math.max(((Number) settings.get(SPEED).getValue()).floatValue(), getBaseMoveSpeed());
            if (em.isPre()) {
                setSuffix(((Options) settings.get(MODE).getValue()).getSelected());
                mc.thePlayer.fallDistance = 0;
                if (allowBypass()) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ);
                    if (mc.thePlayer.ticksExisted % 3 == 0)
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                }
                switch (((Options) settings.get(MODE).getValue()).getSelected()) {
                    case "Glide": {
                        final boolean shouldBlock = mc.thePlayer.posY + 0.1 >= startY && mc.gameSettings.keyBindJump.getIsKeyPressed();
                        if (mc.thePlayer.isSneaking()) {
                            mc.thePlayer.motionY = -0.4f;
                        } else if (mc.gameSettings.keyBindJump.getIsKeyPressed() && !shouldBlock) {

                            mc.thePlayer.motionY = 0.4f;
                        } else {
                            mc.thePlayer.motionY = -0.01;
                        }
                        break;
                    }
                    case "Vanilla": {
                        mc.thePlayer.capabilities.isFlying = true;
                        mc.thePlayer.capabilities.allowFlying = true;
                        break;
                    }
                    case "Motion": {
                        if (mc.thePlayer.movementInput.jump) {
                            mc.thePlayer.motionY = speed * 0.6;
                        } else if (mc.thePlayer.movementInput.sneak) {
                            mc.thePlayer.motionY = -speed * 0.6;
                        } else {
                            mc.thePlayer.motionY = 0;
                        }
                        break;
                    }
                    case "AntiKick": {
                        if (mc.thePlayer.movementInput.jump) {
                            mc.thePlayer.motionY = speed * 0.6;
                        } else if (mc.thePlayer.movementInput.sneak) {
                            mc.thePlayer.motionY = -speed * 0.6;
                        } else {
                            mc.thePlayer.motionY = 0;
                        }
                        updateFlyHeight();
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                        if (((this.flyHeight <= 290.0D) && (kickTimer.delay(500L)))
                                || ((this.flyHeight > 290.0D) && (kickTimer.delay(100L)))) {
                            goToGround();
                            kickTimer.reset();
                        }
                        break;
                    }
                }
            }
        }
        if (event instanceof EventMove) {
            EventMove em = (EventMove) event;
            String mode = ((Options) settings.get(MODE).getValue()).getSelected();
            if (!(mode.equalsIgnoreCase("Vanilla")) && !allowBypass()) {
                double speed = ((Number) settings.get(SPEED).getValue()).floatValue();
                double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float yaw = mc.thePlayer.rotationYaw;
                if ((forward == 0.0D) && (strafe == 0.0D)) {
                    em.setX(0.0D);
                    em.setZ(0.0D);
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
                    em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
                            + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                    em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
                            - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
                }
            }
        }
    }


    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }

    public void updateFlyHeight() {
        double h = 1.0D;
        AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D);
        for (this.flyHeight = 0.0D; this.flyHeight < mc.thePlayer.posY; this.flyHeight += h) {
            AxisAlignedBB nextBox = box.offset(0.0D, -this.flyHeight, 0.0D);
            if (mc.theWorld.checkBlockCollision(nextBox)) {
                if (h < 0.0625D) {
                    break;
                }
                this.flyHeight -= h;
                h /= 2.0D;
            }
        }
    }

    public void goToGround() {
        if (this.flyHeight > 300.0D) {
            return;
        }
        double minY = mc.thePlayer.posY - this.flyHeight;
        if (minY <= 0.0D) {
            return;
        }
        for (double y = mc.thePlayer.posY; y > minY; ) {
            y -= 8.0D;
            if (y < minY) {
                y = minY;
            }
            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        for (double y = minY; y < mc.thePlayer.posY; ) {
            y += 8.0D;
            if (y > mc.thePlayer.posY) {
                y = mc.thePlayer.posY;
            }
            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
    }

}
