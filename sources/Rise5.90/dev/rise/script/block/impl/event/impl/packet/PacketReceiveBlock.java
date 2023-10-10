package dev.rise.script.block.impl.event.impl.packet;

import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.event.EventBlock;

@BlockData(name = "onPacketReceive", description = "Instructions inside this event listener will get ran when the client receives a packet.")
public final class PacketReceiveBlock extends EventBlock {

    public PacketReceiveBlock() {
        super(PacketReceiveEvent.class);
    }
}
