package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.api.value.Value;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.events.EventWorldChange;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class VerusTest2DisablerImpl implements ModeImpl<Disabler> {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private final BooleanValue movement = new BooleanValue("Test Movement", "Disable movement checks", true);
    private final BooleanValue movementPlus = new BooleanValue("Test Movement +", "Disable more movement checks (VERY UNSTABLE)", movement::getObject, true);
    private final BooleanValue silentValue = new BooleanValue("Silent", "Silence lag-backs", movement::getObject, false);
    private final NumberValue<Integer> ticksValue = new NumberValue<Integer>("Ticks", "Ticks for lag-backs", movement::getObject, 120, 0, 1000);
    private int ticks, state, stage, lagBacks;
    private boolean silenceNext;

    @Override
    public List<Value<?>> getValues() {
        return Arrays.asList(movement, movementPlus, silentValue, ticksValue);
    }

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Verus Test 2";
    }

    @Override
    public void onEnable() {
        packets.clear();
        ticks = lagBacks = state = stage = 0;
    }

    @Override
    public void onDisable() {
        for (final Packet<?> packet : packets) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
        }
        packets.clear();
        ticks = lagBacks = state = stage = 0;
    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(final Event event) {
        if (event instanceof EventWorldChange) {
            packets.clear();
            ticks = lagBacks = state = stage = 0;
        }
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 10 && movement.getObject()) {
                final S08PacketPlayerPosLook s08 = e.getPacket();
                s08.yaw = mc.thePlayer.rotationYaw;
                s08.pitch = mc.thePlayer.rotationPitch;
                e.setCancelled(true);
                EntityPlayer entityplayer = mc.thePlayer;
                double d0 = s08.getX();
                double d1 = s08.getY();
                double d2 = s08.getZ();
                float f = s08.getYaw();
                float f1 = s08.getPitch();

                if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X))
                {
                    d0 += entityplayer.posX;
                }
                else
                {
                    entityplayer.motionX = 0.0D;
                }

                if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y))
                {
                    d1 += entityplayer.posY;
                }
                else
                {
                    entityplayer.motionY = 0.0D;
                }

                if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z))
                {
                    d2 += entityplayer.posZ;
                }
                else
                {
                    entityplayer.motionZ = 0.0D;
                }

                if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
                {
                    f1 += entityplayer.rotationPitch;
                }

                if (s08.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
                {
                    f += entityplayer.rotationYaw;
                }

                entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));
                lagBacks++;
            }
        }
        if (event instanceof EventUpdate) {
            if (packets.size() > 300) {
                for (final Packet<?> packet : packets) {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                }
                packets.clear();
            }
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer c03PacketPlayer = e.getPacket();
                if (mc.thePlayer.ticksExisted < 3 || !movement.getObject()) {
                    ticks = 0;
                    return;
                }
                e.setCancelled(true);
                if (ticks % 10 == 0) c03PacketPlayer.onGround = true;
                if (ticks % 5 == 0) e.setCancelled(false);
                if (ticks % ticksValue.getObject() == 0) {
                    e.setCancelled(false);
                    c03PacketPlayer.y = 1.0D;
                    c03PacketPlayer.onGround = true;
                }
                ticks++;
            }
            if ((e.getPacket() instanceof C02PacketUseEntity ||
                    e.getPacket() instanceof C08PacketPlayerBlockPlacement ||
                    e.getPacket() instanceof C07PacketPlayerDigging ||
                    e.getPacket() instanceof C0APacketAnimation) && movement.getObject() && movementPlus.getObject() && ticks % 5 != 0) {
                e.setCancelled(true);
            }
            if (e.getPacket() instanceof C0BPacketEntityAction) e.setCancelled(true);
            if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) {
                e.setCancelled(true);
                packets.add(e.getPacket());
            }
        }
    }
}