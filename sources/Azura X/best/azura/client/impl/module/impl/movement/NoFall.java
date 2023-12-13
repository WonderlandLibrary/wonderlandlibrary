package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "No Fall", description = "Cancel fall damage or Anti Void", category = Category.MOVEMENT)
public class NoFall extends Module {

    public final BooleanValue antiVoid = new BooleanValue("Anti Void", "Do not fall down", true);
    public final ModeValue antiVoidMode = new ModeValue("Anti Void Mode", "Do not fall down", antiVoid::getObject, "Watchdog", "Watchdog", "Watchdog Old", "Blink");
    public final BooleanValue noFall = new BooleanValue("No Fall", "Do not fall down", true);
    public final ModeValue noFallMode = new ModeValue("No Fall Mode", "Mode for preventing fall damage", noFall::getObject, "Watchdog", "Watchdog", "Collision", "Verus Blink", "RedeSky", "Spartan", "AAC4", "Flag", "Funcraft", "Minemora");
    public boolean disableAntiVoid;
    private float fallDist;

    private int ticks = 0;

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    public void handle(final Event event) {
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) fallDist = 0;
            else if (e.getY() < 0) fallDist -= e.getY();
        }
        if (event instanceof EventUpdate)
            setSuffix(noFall.getObject() ? noFallMode.getObject() : (antiVoid.getObject() ? antiVoidMode.getObject() : ""));
        if (noFall.getObject()) {
            switch (noFallMode.getObject()) {
                case "Watchdog":
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance > 3) {
                        final EventMotion e = (EventMotion) event;
                        e.onGround = true;
                        e.x += MathUtil.getRandom_double(-MathUtil.getRandom_double(1.0E-323, 1.0E-3), MathUtil.getRandom_double(1.0E-323, 1.0E-3));
                        e.z += MathUtil.getRandom_double(-MathUtil.getRandom_double(1.0E-323, 1.0E-3), MathUtil.getRandom_double(1.0E-323, 1.0E-3));
                        e.y += MathUtil.getRandom_double(-MathUtil.getRandom_double(1.0E-323, 1.0E-3), MathUtil.getRandom_double(1.0E-323, 1.0E-3));
                        mc.thePlayer.fallDistance = 0;
                        ticks++;
                    }
                    //mc cancels fall sound effect if we cancel the fall every 3 blocks
                    final PotionEffect jumpBoost = mc.thePlayer.getActivePotionEffect(Potion.jump);
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.onGround &&
                            ticks >= 1 + (jumpBoost == null ? 0 : jumpBoost.getAmplifier())) {
                        mc.thePlayer.playSound(ticks > 1 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small", 1.0F, 1.0F);
                        ticks = 0;
                    }
                    break;
                case "Verus Glide":
                case "Collision":
                    if (event instanceof EventMotion && ((EventMotion) event).isPre()) {
                        if (mc.thePlayer.fallDistance > 2) {
                            BlockAir.collision = true;
                            BlockAir.collisionMaxY = (int) mc.thePlayer.posY;
                            ticks++;
                        } else if (ticks != 0) {
                            BlockAir.collision = false;
                            BlockAir.collisionMaxY = -1;
                            ticks = 0;
                        }
                    }
                    break;
                case "Verus Blink":
                    if (event instanceof EventSentPacket && mc.thePlayer.fallDistance > 3 && ((EventSentPacket) event).getPacket() instanceof C03PacketPlayer) {
                        if (mc.thePlayer.ticksExisted % 3 != 0) ((EventSentPacket) event).setCancelled(true);
                        if (mc.thePlayer.ticksExisted % 4 == 0)
                            ((C03PacketPlayer) ((EventSentPacket) event).getPacket()).onGround = true;
                    }
                    break;
                case "RedeSky":
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance > 3 && mc.thePlayer.posY > 70) {
                        ((EventMotion) event).onGround = true;
                        mc.thePlayer.fallDistance = 0;
                    }
                    break;
                case "Spartan":
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance > 3) {
                        ((EventMotion) event).onGround = true;
                        mc.thePlayer.fallDistance = 0;
                    }
                    break;
                case "Funcraft":
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance > 3 && !mc.gameSettings.keyBindSneak.pressed)
                        mc.thePlayer.motionY = -0.1;
                    break;
                case "AAC4":
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance >= 3 && !MovementUtil.isOverVoid()) {
                        mc.thePlayer.motionY = -0.01;
                        ((EventMotion) event).onGround = true;
                        ((EventMotion) event).x = mc.thePlayer.posX = mc.thePlayer.lastTickPosX;
                        ((EventMotion) event).z = mc.thePlayer.posZ = mc.thePlayer.lastTickPosZ;
                        mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);
                    }
                case "Flag":
                    /*if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance > 4 &&
                            !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -2.0, 0)).isEmpty() && ticks == 0) {
                        ((EventMotion) event).y -= 1.5;
                        ticks = 1;
                    }*/
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance > 4 && ticks == 0) {
                        for (int i = 0; i < 3; i++) {
                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -i, 0)).isEmpty()) {
                                ((EventMotion) event).y -= i + .5;
                                ticks = 1;
                                break;
                            }
                        }
                    }
                    if (ticks != 0 && mc.thePlayer.onGround && event instanceof EventMotion) ticks = 0;
                    break;
                case "Minemora":
                    if (event instanceof EventMotion && ((EventMotion) event).isPre() && mc.thePlayer.fallDistance >= 3 && mc.thePlayer.ticksExisted % 8 == 0) {
                        ((EventMotion) event).y += 5.0;
                        ((EventMotion) event).onGround = true;
                    }
                    break;
            }
        }
        if (event instanceof EventSentPacket && antiVoid.getObject() && antiVoidMode.getObject().equals("Blink") && !disableAntiVoid && fallDist > 4) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer && MovementUtil.isOverVoid())
                e.setCancelled(true);
        }
        if (event instanceof EventMotion && fallDist > 4
                && antiVoid.getObject() && !mc.thePlayer.isSneaking() && !disableAntiVoid) {
            if (MovementUtil.isOverVoid()) {
                switch (antiVoidMode.getObject()) {
                    case "Watchdog":
                        ((EventMotion) event).x += MathUtil.getRandom_double(0.2, 0.3) * (mc.thePlayer.ticksExisted % 2 == 0 ? 1 : -1);
                        ((EventMotion) event).z += MathUtil.getRandom_double(0.2, 0.3) * (mc.thePlayer.ticksExisted % 2 == 0 ? 1 : -1);
                        break;
                    case "Watchdog Old":
                        ((EventMotion) event).y += MathUtil.getRandom_double(4, 7);
                        break;
                }
            }
        }
    }
}