package best.azura.client.impl.module.impl.combat;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Velocity", category = Category.COMBAT, description = "Change the way servers you receive knock back")
public class Velocity extends Module {
    private int velocityTicks;
    private final ModeValue mode = new ModeValue("Mode", "Change the mode of the Velocity", "Simple", "Simple", "AAC4", "AAC5", "Spoof", "Teleport", "Intave B14", "Verus Zero", "Reverse", "Reverse Push", "Push", "AGC", "Custom");
    private final BooleanValue debug = new BooleanValue("Debug", "Debug the velocity taken", false);
    private final BooleanValue staffVelocityCheck = new BooleanValue("Staff check", "Check if the velocity is from a staff bot (Hypixel)", ServerUtil::isHypixel, false);
    private final NumberValue<Double> horizontalValue = new NumberValue<>("Horizontal", "Horizontal velocity for custom mode", () -> mode.getObject().equals("Custom"), 0.0, 1.0, 0.0, 100.0);
    private final NumberValue<Double> verticalValue = new NumberValue<>("Vertical", "Vertical velocity for custom mode", () -> mode.getObject().equals("Custom"), 0.0, 1.0, 0.0, 100d);


    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if (e.isPre() && mc.thePlayer != null) {
            setSuffix(mode.getObject());
            switch (mode.getObject()) {
                case "Verus Zero":
                    if (velocityTicks > 0) {
                        velocityTicks--;
                        if (velocityTicks <= 1) e.y = -100;
                    }
                    break;
                case "Intave B14":
                    if (velocityTicks > 20) velocityTicks--;
                    if (velocityTicks == 20) velocityTicks = 0;
                    break;
            }
        }
    };

    @EventHandler
    public final Listener<EventReceivedPacket> receivedPacketListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && mode.getObject().equals("Verus Zero") && velocityTicks > 0) {
            final S08PacketPlayerPosLook s08 = e.getPacket();
            s08.yaw = mc.thePlayer.rotationYaw;
            s08.pitch = mc.thePlayer.rotationPitch;
        }
        if (staffVelocityCheck.getObject() && staffVelocityCheck.checkDependency()) {
            if (e.getPacket() instanceof S27PacketExplosion) {
                S27PacketExplosion s27 = e.getPacket();
                if (s27.func_149149_c() == 0 || s27.func_149144_d() == 0 || s27.func_149147_e() == 0) return;
                if (Math.abs(s27.func_149149_c()) < 0.16 && Math.abs(s27.func_149144_d()) < 0.16 && Math.abs(s27.func_149147_e()) < 0.16 &&
                        ((s27.func_149149_c() == s27.func_149144_d() && s27.func_149144_d() == s27.func_149147_e()) ||
                                (s27.func_149149_c() != s27.func_149144_d() || s27.func_149144_d() != s27.func_149147_e()
                                        || s27.func_149144_d() != s27.func_149147_e()))) {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Velocity","Possible staff velocity taken",4000, Type.INFO));
                    ChatUtil.sendDebug("Staff knockback taken x: " + s27.func_149149_c() + " y: " + s27.func_149144_d() + " z: " + s27.func_149147_e());
                    return;
                }
            }
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity s12 = e.getPacket();
                final double x = s12.getMotionX() / 8000.0D, y = s12.getMotionY() / 8000.0D, z = s12.getMotionZ() / 8000.0D;
                if (x != 0 && y != 0 && z != 0 && s12.getEntityID() == mc.thePlayer.getEntityId()) {
                    if (Math.abs(x) < 0.16 && Math.abs(y) < 0.16 && Math.abs(z) < 0.16) {
                        ChatUtil.sendDebug("Staff knockback taken x: " + x + " y: " + y + " z: " + z);
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Velocity","Possible staff velocity taken",4000, Type.INFO));
                        return;
                    }
                }
            }
        }
        if (debug.getObject()) {
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity s12 = e.getPacket();
                final double x = s12.getMotionX() / 8000.0D, y = s12.getMotionX() / 8000.0D, z = s12.getMotionX() / 8000.0D;
                if (x != 0 && y != 0 && z != 0 && s12.getEntityID() == mc.thePlayer.getEntityId())
                    ChatUtil.sendDebug("Knockback taken x: " + x + " y: " + y + " z: " + z);
            }
        }
        switch (mode.getObject()) {
            case "Simple":
                if ((e.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) e.getPacket()).getEntityID() == mc.thePlayer.getEntityId())
                        || e.getPacket() instanceof S27PacketExplosion)
                    e.setCancelled(true);
                break;
            case "Custom":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    e.setCancelled(true);
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                    if (entity == mc.thePlayer) {
                        mc.thePlayer.motionX *= horizontalValue.getObject() / 100;
                        mc.thePlayer.motionY *= verticalValue.getObject() / 100;
                        mc.thePlayer.motionZ *= horizontalValue.getObject() / 100;
                    }
                }
                if (e.getPacket() instanceof S27PacketExplosion) {
                    e.setCancelled(true);
                    S27PacketExplosion packetIn = e.getPacket();
                    mc.thePlayer.motionX += packetIn.func_149149_c() * horizontalValue.getObject() / 100;
                    mc.thePlayer.motionY+= packetIn.func_149144_d() * verticalValue.getObject() / 100;
                    mc.thePlayer.motionZ += packetIn.func_149147_e() * horizontalValue.getObject() / 100;
                }
                break;
            case "Verus Zero":
                if (e.getPacket() instanceof S08PacketPlayerPosLook && velocityTicks > 0) {
                    S08PacketPlayerPosLook s08 = e.getPacket();
                    s08.yaw = mc.thePlayer.rotationYaw;
                    s08.pitch = mc.thePlayer.rotationPitch;
                }
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    if (entity == mc.thePlayer && entity.posY > 0 && packetIn.getMotionY() > 0) velocityTicks = 2;
                }
                break;
            case "AAC4":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    e.setCancelled(true);
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                    if (entity == mc.thePlayer) {
                        mc.thePlayer.motionX *= 0.6;
                        mc.thePlayer.motionZ *= 0.6;
                    }
                }
                break;
            case "Reverse":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    e.setCancelled(true);
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                    if (entity == mc.thePlayer) {
                        mc.thePlayer.motionX *= -1;
                        mc.thePlayer.motionZ *= -1;
                    }
                }
                break;
            case "AGC":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    e.setCancelled(true);
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                    if (entity == mc.thePlayer) {
                        mc.thePlayer.motionX *= 0.4;
                        mc.thePlayer.motionZ *= 0.4;
                        new Thread(() -> {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            mc.thePlayer.motionX *= -0.19;
                            mc.thePlayer.motionZ *= -0.19;
                        }).start();
                        return;
                    }
                }
                break;
            case "Push":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    e.setCancelled(true);
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                    if (entity == mc.thePlayer) {
                        new Thread(() -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            mc.thePlayer.setSpeed(mc.thePlayer.getSpeed());
                        }).start();
                    }
                }
                break;
            case "Reverse Push":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    e.setCancelled(true);
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                    if (entity == mc.thePlayer) {
                        new Thread(() -> {
                            final Vec3 vec = mc.thePlayer.getPositionVector().addVector(-mc.thePlayer.motionX, 0, -mc.thePlayer.motionZ);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            final float[] rots = RotationUtil.getNeededRotations(vec);
                            mc.thePlayer.motionX = -Math.sin(Math.toRadians(rots[0])) * mc.thePlayer.getSpeed();
                            mc.thePlayer.motionZ = Math.cos(Math.toRadians(rots[0])) * mc.thePlayer.getSpeed();
                        }).start();
                    }
                }
                break;
            case "AAC5":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    e.setCancelled(true);
                    if (entity == mc.thePlayer && entity.posY > 0) {
                        MovementUtil.spoof(1.7E+20, 1.7E+80, 1.7E+20, true);
                        //return;
                    }
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                }
                break;
            case "Spoof":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    e.setCancelled(true);
                    if (entity == mc.thePlayer && entity.posY > 0) {
                        double x = 0;
                        for (double d = 0; d < (double) packetIn.getMotionX() / 8000.0D; d += 0.1) {
                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(d, 0, 0)).isEmpty())
                                break;
                            x = d;
                        }
                        double y = 0;
                        for (double d = 0; d < (double) packetIn.getMotionY() / 8000.0D; d += 0.1) {
                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, d, 0)).isEmpty())
                                break;
                            y = d;
                        }
                        double z = 0;
                        for (double d = 0; d < (double) packetIn.getMotionZ() / 8000.0D; d += 0.1) {
                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 0, d)).isEmpty())
                                break;
                            z = d;
                        }
                        MovementUtil.spoof(x, y, z, mc.thePlayer.onGround);
                        return;
                    }
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                }
                break;
            case "Teleport":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    e.setCancelled(true);
                    if (entity == mc.thePlayer && entity.posY > 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX + (double) packetIn.getMotionX() / 8000.0D,
                                mc.thePlayer.posY + (double) packetIn.getMotionY() / 8000.0D,
                                mc.thePlayer.posZ + (double) packetIn.getMotionZ() / 8000.0D);
                        return;
                    }
                    if (entity != null)
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                }
                break;
            case "Intave B14":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packetIn = e.getPacket();
                    Entity entity = mc.theWorld.getEntityByID(packetIn.getEntityID());
                    e.setCancelled(true);
                    if (entity != null) {
                        entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
                        if (entity == mc.thePlayer) {
                            if (entity.ticksExisted < 15) return;
                            if (Math.abs(entity.motionY) < 0.1 && Math.abs(entity.motionX) < 0.1 && Math.abs(entity.motionZ) < 0.1) {
                                velocityTicks = 40;
                                return;
                            }
                            new Thread(() -> {
                                if (velocityTicks++ > MathUtil.getRandom_int(8, 12)) {
                                    velocityTicks = 0;
                                    return;
                                }
                                try {
                                    Thread.sleep(MathUtil.getRandom_int(400, 600));
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                entity.motionX *= MathUtil.getRandom_double(0.7, 0.8);
                                entity.motionZ *= MathUtil.getRandom_double(0.7, 0.8);
                                try {
                                    Thread.sleep(MathUtil.getRandom_int(100, 300));
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                entity.motionX *= MathUtil.getRandom_double(0.5, 0.6);
                                entity.motionZ *= MathUtil.getRandom_double(0.5, 0.6);
                                try {
                                    Thread.sleep(MathUtil.getRandom_int(100, 300));
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                entity.motionX *= MathUtil.getRandom_double(0.3, 0.4);
                                entity.motionZ *= MathUtil.getRandom_double(0.3, 0.4);
                            }).start();
                        }
                    }
                }
                break;
        }
    };
    @EventHandler
    @SuppressWarnings("unused")
    public final Listener<EventWorldChange> eventWorldChangeListener = e -> velocityTicks = 0;
}