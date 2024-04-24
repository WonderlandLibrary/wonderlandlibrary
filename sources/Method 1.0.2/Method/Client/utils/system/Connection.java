package Method.Client.utils.system;

import Method.Client.module.ModuleManager;
import Method.Client.utils.EventsHandler;
import Method.Client.utils.visual.ChatUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

import java.util.Objects;
import java.util.PriorityQueue;

public class Connection extends ChannelDuplexHandler {
    private PriorityQueue<DelayedPacket> packetQueue;

    public Connection() {
        try {
        //    packetQueue = new PriorityQueue<>(); // Create the queue
            ChannelPipeline pipeline = Objects.requireNonNull(Wrapper.INSTANCE.mc().getConnection()).getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketHandler", this);
        } catch (Exception exception) {
            ChatUtils.error("Connection: Error on attaching");
            exception.printStackTrace();
        }
    }

    public void addPacket(Object packet, int delay) {
        packetQueue.add(new DelayedPacket(packet, delay));
    }

    public boolean onPacket(Object packet, Connection.Side side) {
        boolean suc;
        if (EventsHandler.isInit)
            suc = ModuleManager.getEnabledmodules().stream().map(m -> m.onPacket(packet, side)).reduce(true, (a, b) -> a && b);
        else
            suc = ModuleManager.getEnabledmodules().stream().map(m -> m.onDisablePacket(packet, side)).reduce(true, (a, b) -> a && b);
        return suc;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        if (!onPacket(packet, Side.IN)) {
            return;
        }
        super.channelRead(ctx, packet);
    }
    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        if (!onPacket(packet, Side.OUT)) {
            return;
        }
        /*
        // Decrease delay for each packet and send if delay == 0
        DelayedPacket delayedPacket;
        while ((delayedPacket = packetQueue.peek()) != null && delayedPacket.getDelay() == 0) {
            super.write(ctx, delayedPacket.getPacket(), promise);
            packetQueue.poll();
        }

        // Decrease the delay for each remaining packet
        packetQueue.forEach(DelayedPacket::decreaseDelay);
        */
        super.write(ctx, packet, promise);
    }

    public enum Side {
        IN,
        OUT
    }

    // Inner class to keep the packet with its delay
    private static class DelayedPacket implements Comparable<DelayedPacket> {
        private final Object packet;
        private int delay;

        public DelayedPacket(Object packet, int delay) {
            this.packet = packet;
            this.delay = delay;
        }

        public Object getPacket() {
            return packet;
        }

        public int getDelay() {
            return delay;
        }

        public void decreaseDelay() {
            if (delay > 0)
                this.delay--;
        }

        @Override
        public int compareTo(DelayedPacket o) {
            return this.delay - o.delay;
        }
    }
}

