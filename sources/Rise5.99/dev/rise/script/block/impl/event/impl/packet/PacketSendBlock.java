package dev.rise.script.block.impl.event.impl.packet;

import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.script.block.api.BlockData;
import dev.rise.script.block.impl.event.EventBlock;

@BlockData(name = "onPacketSend", description = "Instructions inside this event listener will get ran when the client sends a packet.")
public final class PacketSendBlock extends EventBlock {

    public PacketSendBlock() {
        super(PacketSendEvent.class);
    }
}
