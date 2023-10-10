package tech.atani.client.feature.module.impl.misc;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.status.client.C01PacketPing;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.math.random.RandomUtil;

@Native
@ModuleData(name = "PingSpoof", description = "Spoofs your ping", category = Category.MISCELLANEOUS)
public class PingSpoof extends Module {
    private SliderValue<Long> minDelay = new SliderValue<>("Minimum Delay", "What'll be the minimum delay for freezing packets?", this, 1500L, 0L, 5000L, 0),
            maxDelay = new SliderValue<>("Maximum Delay", "What'll be the maximum delay for freezing packets?", this, 2000L, 0L, 5000L, 0);

    @Listen
    public void onPacket(PacketEvent packetEvent) {
    	if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
    		return;
        if(packetEvent.getType() == PacketEvent.Type.OUTGOING) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packet instanceof C00PacketKeepAlive || packet instanceof C01PacketPing || packet instanceof C0FPacketConfirmTransaction) {
                packetEvent.setCancelled(true);
                new Thread(() -> {
                    try {
                        Thread.sleep((long) RandomUtil.randomBetween(minDelay.getValue(), maxDelay.getValue()));
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Methods.mc.getNetHandler().getNetworkManager().channel.writeAndFlush(packet);
                }).start();
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
