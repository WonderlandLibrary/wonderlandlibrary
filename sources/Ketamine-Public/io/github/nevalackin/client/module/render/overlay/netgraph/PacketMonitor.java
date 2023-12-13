package io.github.nevalackin.client.module.render.overlay.netgraph;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

class PacketMonitor {
    private final Deque<Integer> packets = new ArrayDeque<>();

    private int packetCounter;

    private long lastReset;

    void increment() {
        this.packetCounter++;
    }

    Collection<Integer> getPacketRecord() {
        return this.packets;
    }

    void update(final int maxSamples) {
        if (System.currentTimeMillis() - this.lastReset > 1000L) {
            this.packets.addFirst(this.packetCounter);
            this.lastReset = System.currentTimeMillis();
            this.packetCounter = 0;

            if (this.packets.size() > maxSamples) {
                this.packets.removeLast();
            }
        }
    }

    void reset() {
        this.packets.clear();
    }
}