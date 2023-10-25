package tech.dort.dortware.impl.utils.networking;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import skidmonke.Client;
import skidmonke.Minecraft;
import tech.dort.dortware.impl.events.PacketEvent;

import java.util.function.Consumer;

public class PacketRunnableWrapper {

    private final PacketRunnable runnable;

    public PacketRunnableWrapper(PacketRunnable runnable, long stop_after) {
        this.runnable = runnable;

        new Thread(() -> {
            Client.INSTANCE.getEventBus().register(PacketRunnableWrapper.this);
            try {
                Thread.sleep(stop_after);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client.INSTANCE.getEventBus().unregister(PacketRunnableWrapper.this);
        }).start();
    }

    public static final Consumer<PacketEvent> aacExploit = packetEvent -> {
        Minecraft mc = Minecraft.getMinecraft();
        if (packetEvent.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packetPlayerPosLook = packetEvent.getPacket();
            double x = packetPlayerPosLook.func_148932_c() - mc.thePlayer.posX;
            double y = packetPlayerPosLook.func_148928_d() - mc.thePlayer.posY;
            double z = packetPlayerPosLook.func_148933_e() - mc.thePlayer.posZ;
            double diff = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            double distance = 22.5D;
            if (diff <= distance) {
                packetEvent.setCancelled(true);
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosLook.func_148932_c(), packetPlayerPosLook.func_148928_d(), packetPlayerPosLook.func_148933_e(), packetPlayerPosLook.func_148931_f(), packetPlayerPosLook.func_148930_g(), true));
            }
        } else if (packetEvent.getPacket() instanceof C03PacketPlayer) {
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.renderYawHead, mc.thePlayer.renderPitchHead, true));
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 20.0D, mc.thePlayer.renderYawHead, mc.thePlayer.renderPitchHead, true));
            }
            packetEvent.setCancelled(true);
        } else if (packetEvent.getPacket() instanceof C0BPacketEntityAction) {
            packetEvent.setCancelled(true);
        }
    };

    public static void preventLagback() {
        new PacketRunnableWrapper(aacExploit::accept, 300L);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        this.getAction().run(event);
    }

    public PacketRunnable getAction() {
        return runnable;
    }

    @FunctionalInterface
    public interface PacketRunnable {
        void run(PacketEvent packet);
    }


}
