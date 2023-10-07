package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.*;
import best.azura.client.util.player.RaytraceUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.util.render.DrawUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.*;
import java.util.List;

public class FlightWatchdogNewImpl implements ModeImpl<Flight> {
    private int tick, ticks, lagbacks, bobTicks;

    private final NumberValue<Integer> lengthTicks = new NumberValue<>("Length ticks", "Amount of ticks to fly (0 is infinite)", 38, 1, 0, 150);
    private final NumberValue<Float> timerSpeed = new NumberValue<>("Timer Speed", "Custom Timer speed (< 1 = fly goes further)", 1.0f, 0.1f, 0.1f, 2.0f);
    private final BooleanValue boost = new BooleanValue("Boost", "Boost on timer", () -> timerSpeed.getObject() < 1.0f, false);
    private final NumberValue<Integer> boostVal = new NumberValue<>("Boost value", "Custom boost value", () -> boost.getObject() && boost.checkDependency(), 1, 1, 1, 20);
    private final NumberValue<Integer> boostTicks = new NumberValue<>("Boost ticks", "Custom boost ticks", () -> boost.getObject() && boost.checkDependency(), 2, 1, 1, 20);
    private final BooleanValue blink = new BooleanValue("Blink", "Choke movement packets", false);
    private final ModeValue blinkMode = new ModeValue("Blink Mode", "Mode for blinking", blink::getObject, "Normal", "Normal", "Delay");
    private final NumberValue<Integer> blinkSize = new NumberValue<>("Blink length", "Amount of packets to choke (0 is infinite)", blink::getObject, 25, 1, 1, 100);
    private final BooleanValue oldFeelings = new BooleanValue("Old Feelings", "Feel like 2020 watchdog", boost::getObject, false);
    private final BooleanValue experimental = new BooleanValue("Experimental", "Experimental movement-related bypass method", false);
    private final ArrayList<Packet<?>> movementPackets = new ArrayList<>(), otherPackets = new ArrayList<>();

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(lengthTicks, timerSpeed, boost, boostVal, boostTicks, blink, blinkMode, blinkSize, oldFeelings, experimental);
    }

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Watchdog New";
    }

    @Override
    public void onEnable() {
        tick = ticks = lagbacks = 0;
        bobTicks = 1;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        for (Packet<?> packet : movementPackets) mc.thePlayer.sendQueue.addToSendQueue(packet);
        movementPackets.clear();
        for (Packet<?> packet : otherPackets) mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
        otherPackets.clear();
    }

    @EventHandler
    public final Listener<Event> event = this::onEvent;

    private void onEvent(final Event event) {
        if (tick == 0 && (mc.thePlayer == null || !mc.thePlayer.onGround)) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Flight", "Please be on ground when activating", 3000, Type.INFO));
            getParent().setEnabled(false);
            return;
        }
        if (event instanceof EventSentPacket && blink.getObject() && ticks > 4) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.isCancelled()) return;
            if (e.getPacket() instanceof C03PacketPlayer) {
                e.setCancelled(true);
                movementPackets.add(e.getPacket());
            } else if (e.getPacket() instanceof C02PacketUseEntity || e.getPacket() instanceof C08PacketPlayerBlockPlacement || e.getPacket() instanceof C0APacketAnimation) {
                e.setCancelled(true);
                otherPackets.add(e.getPacket());
            }
        }
        if (event instanceof EventSentPacket && experimental.getObject() && lagbacks >= 1 && ticks >= 1 && ServerUtil.isHypixel()) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer) {
                if (e.isCancelled()) return;
                final Vec3 vec = new Vec3(MathUtil.getRandom_int(-200, 200), MathUtil.getRandom_int(-200, 200), MathUtil.getRandom_int(-200, 200));
                final float[] rotations = RotationUtil.getNeededRotations(vec);
                final Vec3 rotationVector = RaytraceUtil.getVectorForRotation(rotations[1], rotations[0]);
                final EnumFacing facing = EnumFacing.getFacingFromVector((float) rotationVector.xCoord, (float) rotationVector.yCoord, (float) rotationVector.zCoord);
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, null, new BlockPos(vec), facing, vec);
                if (ticks > 4 && ticks % 2 == 0)
                    mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(UUID.randomUUID()));
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.RIDING_JUMP, MathUtil.getRandom_int(-1000, 1000)));
                MovementUtil.vClip((4.3E-4D + MovementUtil.getWatchdogUnpatchValues()) * (bobTicks++ % 2 == 0 ? 1 : -1));
            }
        }
        if (event instanceof EventRender2D && lengthTicks.getObject() != 0) {
            RenderUtil.INSTANCE.scaleFix(1.0);
            int height = 75;
            int heightOffset = height - 10;
            DrawUtil.glDrawRect(mc.displayWidth / 2.0 - 101,
                    mc.displayHeight / 2.0 + heightOffset - 1,
                    mc.displayWidth / 2.0 + 101,
                    mc.displayHeight / 2.0 + height + 1, new Color(13, 14, 13));
            RenderUtil.INSTANCE.drawGradientRectSideways(mc.displayWidth / 2.0 - 100,
                    mc.displayHeight / 2.0 + heightOffset,
                    mc.displayWidth / 2.0 - 100 + (200 * (((double)ticks) / lengthTicks.getObject())),
                    mc.displayHeight / 2.0 + height, ColorUtil.getHudColor(0).getRGB(), ColorUtil.getHudColor(20).getRGB());
            RenderUtil.INSTANCE.invertScaleFix(1.0);
        }
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                if (ServerUtil.isHypixel()) {
                    if (tick == 0 && mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.075f;
                        e.y += 0.075f;
                        tick = 1;
                        return;
                    }
                    if (mc.thePlayer.onGround && ticks == 0) {
                        e.y -= 0.075f;
                        e.onGround = true;
                        ticks = 1;
                    }
                } else {
                    tick = lagbacks = 1;
                    ticks = Math.max(ticks, 1);
                    //anti packet-log xd
                }
                if (ticks >= lengthTicks.getObject() && lengthTicks.getObject() != 0) {
                    getParent().setEnabled(false);
                    return;
                }
                if (ticks == 3 && oldFeelings.getObject() && oldFeelings.checkDependency()) {
                    java.util.Random rand = new java.util.Random();
                    if (!Flight.silentDamage.getObject())
                        mc.thePlayer.playSound("game.player.hurt", 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    mc.thePlayer.performHurtAnimation();
                    mc.thePlayer.setHealth(mc.thePlayer.getHealth() - 1.0f);
                    mc.thePlayer.setPlayerSPHealth(mc.thePlayer.getHealth());
                }
                if (ticks >= 1 && lagbacks >= 1) {
                    mc.timer.timerSpeed = timerSpeed.getObject();
                    mc.thePlayer.motionY = 0;
                    if (!blink.getObject()) {
                        for (Packet<?> packet : movementPackets) mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                        movementPackets.clear();
                        for (Packet<?> packet : otherPackets) mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                        otherPackets.clear();
                    } else {
                        switch (blinkMode.getObject()) {
                            case "Normal":
                                if (movementPackets.size() >= blinkSize.getObject() && blinkSize.getObject() != 0) {
                                    for (Packet<?> packet : movementPackets)
                                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                                    movementPackets.clear();
                                    for (Packet<?> packet : otherPackets)
                                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                                    otherPackets.clear();
                                }
                                break;
                            case "Delay":
                                while (movementPackets.size() >= blinkSize.getObject() && blinkSize.getObject() != 0) {
                                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(movementPackets.remove(0));
                                    for (Packet<?> packet : otherPackets)
                                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                                    otherPackets.clear();
                                }
                                break;
                        }
                    }
                    ticks++;
                } else {
                    mc.thePlayer.posY = mc.thePlayer.lastTickPosY;
                    mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0;
                }
            }
            if (e.isPost() && ticks >= 1 && lagbacks >= 1 && mc.thePlayer.ticksExisted % boostTicks.getObject() == 0 && boost.getObject() && boost.checkDependency()) {
                double x = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
                double z = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
                if (x == 0 && z == 0) return;
                int length = ticks < 4 ? 5 : ticks < 8 ? 4 : ticks < 12 ? 3 : ticks < 16 ? 2 : 1;
                if (!(oldFeelings.getObject() && oldFeelings.checkDependency())) length = boostVal.getObject();
                for (int i = 0; i < length; i++) {
                    if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) {
                        break;
                    }
                    MovementUtil.spoof(x, 0, z, mc.thePlayer.onGround);
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                }
            }
            // fix für was weiß ich
            if (timerSpeed.getObject() < 1.0 && boost.getObject() && boost.checkDependency()) {
                if (e.isPre())
                    mc.thePlayer.prevDistanceWalkedModified = mc.thePlayer.distanceWalkedModified;
                if (e.isPost()) {
                    final double diffX = mc.thePlayer.posX - mc.thePlayer.lastTickPosX, diffY = mc.thePlayer.posY - mc.thePlayer.lastTickPosY,
                            diffZ = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
                    mc.thePlayer.distanceWalkedModified = (float) ((double) mc.thePlayer.distanceWalkedModified + (double) MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ) * 0.6D);
                    mc.thePlayer.distanceWalkedOnStepModified = (float) ((double) mc.thePlayer.distanceWalkedOnStepModified + (double) MathHelper.sqrt_double(diffX * diffX + diffY * diffY + diffZ * diffZ) * 0.6D);
                }
            }
        }
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook && lagbacks == 0)
                lagbacks++;
        }
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            final double speed;
            final PotionEffect effect = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed);
            if (effect != null) speed = 0.295 + (effect.getAmplifier() + 1) * 0.055;
            else speed = 0.29;
            if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(speed, e);
            if (lagbacks == 0 || !mc.thePlayer.isMoving()) {
                MovementUtil.setSpeed(0, e);
                if (lagbacks == 0 && ticks != 0) MovementUtil.setSpeed(0.00001, e);
            }
        }
    }
}