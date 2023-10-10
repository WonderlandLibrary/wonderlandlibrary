package client.module.impl.movement.flight;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.PacketSendEvent;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.Flight;
import client.util.ChatUtil;
import client.util.MoveUtil;
import client.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
public class TestFlight extends Mode<Flight> {

    int ticks = 0;
    private final Deque<Packet<?>> packetDeque = new ConcurrentLinkedDeque<>();

    public TestFlight(final String name, final Flight parent) {
        super(name, parent);
    }
    @Override
    public void onEnable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784, mc.thePlayer.posZ, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        /*
        mc.timer.timerSpeed = 0.5f;
        mc.thePlayer.speedInAir = 0.00f;

         */
        ticks = 0;
    }

    @Override
    public void onDisable() {
        while (!packetDeque.isEmpty())
            mc.getNetHandler().addToSendQueue(packetDeque.poll());
        packetDeque.clear();
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.speedInAir = 0.02f;
        ticks = 0;
    }

    @EventLink
    public final Listener<PacketSendEvent> hgfhf = event -> {


        if (mc.thePlayer.ticksExisted <= 1) {
            packetDeque.clear();
        }

        if (event.getPacket() instanceof C03PacketPlayer){
            if (mc.thePlayer.ticksExisted % 2 == 0) {

                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;


                event.setCancelled(true);
            }
        }


    };
    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {

        if (ticks % 25 == 0) {
            if (packetDeque.size() > 15) {
                ticks -= 5;
                for (int i = 15; i > 0; --i)
                    mc.getNetHandler().addToSendQueueUnregistered(packetDeque.poll());

            }
        }
        mc.timer.timerSpeed = 0.5f;



        mc.thePlayer.setPosition(event.getX(), Math.round(event.getY() -1E-10D),event.getZ());



        ticks++;
        if (mc.thePlayer.ticksExisted % 2 == 0) {
            event.setCancelled(true);
     //       mc.thePlayer.onGround = true;

        }
        if (mc.thePlayer.ticksExisted % 1 == 0) {

       //     event.setCancelled(true);
            packetDeque.clear();
            event.setCancelled(true);
            MoveUtil.strafe(0.14f);


            if (ticks == 1) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                MoveUtil.strafe(1.9f);
                event.setCancelled(true);
                mc.thePlayer.lastReportedPosY = mc.thePlayer.lastTickPosY;

            } else if (ticks == 2) {
                mc.thePlayer.motionY += 0.1;
                MoveUtil.strafe(2.79f);
            } else if (ticks == 3) {
                MoveUtil.strafe(2.56f);
            }else if (ticks == 4) {
                MoveUtil.strafe(0.49);

            } else if (ticks == 5) {
                MoveUtil.strafe(0.19);
                event.setCancelled(true);
            }else if (ticks == 6) {
                MoveUtil.strafe(0.3);
                event.setCancelled(true);
            }

        } else {
            event.setCancelled(true);


            mc.thePlayer.motionX = 0;
                            mc.thePlayer.motionZ = 0;
        }



    };

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {


    };
}
