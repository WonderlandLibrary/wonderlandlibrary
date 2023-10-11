package io.github.raze.modules.collection.misc;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.collection.network.EventPacketSend;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.Flight;
import io.github.raze.modules.collection.movement.Speed;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.utilities.collection.math.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPosition;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Disabler extends AbstractModule {

    private final ArraySetting mode;

    private final TimeUtil timer;

    private int voidCheckInt;
    private boolean teleported = false;

    private final Deque<Packet<?>> packets = new ConcurrentLinkedDeque<>();


    public Disabler() {
        super("Disabler", "Disables Anti-Cheat solutions.", ModuleCategory.MISC);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Vulcan", "Vulcan", "Verus", "BlocksMC", "Negativity v1", "Lagback", "Keep Alive")

        );

        timer = new TimeUtil();
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            switch(mode.get()) {
                case "Vulcan":
                    if(Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(Flight.class).isEnabled() || Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(Speed.class).isEnabled()) {
                        if(mc.thePlayer.ticksExisted % 40 == 0) {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2 + Math.random(), mc.thePlayer.posZ, true));
                        }
                    }

                    if(mc.thePlayer.isBlocking()) {
                        mc.getNetHandler().addToSendQueueSilent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                        mc.getNetHandler().addToSendQueueSilent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    }
                    break;
                case "Verus":
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 14.37, mc.thePlayer.posZ, true));

                    if (mc.theWorld.isAirBlock(new BlockPosition(mc.thePlayer.posX, (mc.thePlayer.posY - 15) - voidCheckInt, mc.thePlayer.posZ))) {
                        if (mc.thePlayer.ticksExisted % 40 == 0) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, ((mc.thePlayer.posY - 15) - voidCheckInt) - 1, mc.thePlayer.posZ, mc.thePlayer.onGround));
                        }
                    } else {
                        voidCheckInt--;
                    }
                    break;
                case "Negativity v1":
                    if (mc.thePlayer.ticksExisted % 20 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook());
                        teleported = true;
                    }
                    break;
                case "BlocksMC":
                    if (timer.elapsed(755)) {
                        if (!packets.isEmpty()) {
                            for (Packet<?> packet : packets) {
                                mc.thePlayer.sendQueue.addToSendQueueSilent(packet);
                                this.packets.remove(packet);
                            }
                        }

                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C00PacketKeepAlive(10000));
                        timer.reset();
                    }
                    break;
                case "Lagback":
                    double oldPosX = mc.thePlayer.posX, oldPosY = mc.thePlayer.posY, oldPosZ = mc.thePlayer.posZ;
                    if (timer.elapsed(999, true)) {
                        mc.thePlayer.posX = oldPosX;
                        mc.thePlayer.posY = oldPosY;
                        mc.thePlayer.posZ = oldPosZ;
                        mc.thePlayer.motionX *= 0.001F;
                        mc.thePlayer.motionY *= -0.5F;
                        mc.thePlayer.motionZ *= 0.001F;
                    }
                    break;
            }
        }
    }

    @Listen
    public void onPacketSend(EventPacketSend event) {
        Packet<?> packet = event.getPacket();

        switch(mode.get()) {
            case "Vulcan":
                if(event.getPacket() instanceof C0FPacketConfirmTransaction)
                    event.setCancelled(true);

                if (packet instanceof C17PacketCustomPayload)
                    event.setCancelled(true);
                break;
            case "Verus":
                if (event.getPacket() instanceof C0FPacketConfirmTransaction)
                    event.setCancelled(true);
                break;

            case "Negativity v1":
                if (event.getPacket() instanceof S08PacketPlayerPosLook && teleported) {
                    event.setCancelled(true);
                    teleported = false;

                    S08PacketPlayerPosLook position = (S08PacketPlayerPosLook) packet;

                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(
                            position.getY(),
                            position.getZ(),
                            position.getX(),
                            position.getYaw(),
                            position.getPitch(),
                            true
                    ));
                }
                break;

            case "BlocksMC":
                if (packet instanceof C08PacketPlayerBlockPlacement)
                    ((C08PacketPlayerBlockPlacement) packet).stack = null;

                if (mc.thePlayer.ticksExisted <= 40 && packet instanceof C0FPacketConfirmTransaction) {
                    event.setCancelled(true);
                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C0FPacketConfirmTransaction(0, (short) -32768, true));
                }

                if (event.getPacket() instanceof C00PacketKeepAlive) {
                    this.packets.add(event.getPacket());
                    event.setCancelled(true);
                }
                break;

            case "Keep Alive":
                if(event.getPacket() instanceof C00PacketKeepAlive)
                    event.setCancelled(true);
                break;
        }
    }

    @Override
    public void onEnable() {
        teleported = false;
        packets.clear();
    }
}
