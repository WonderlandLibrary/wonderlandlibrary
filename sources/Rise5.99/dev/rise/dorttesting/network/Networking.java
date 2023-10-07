package dev.rise.dorttesting.network;

import com.google.common.collect.Lists;
import dev.rise.dorttesting.network.packet.MappedPacket;
import dev.rise.dorttesting.network.packet.packets.client.player.ClientPlayerPosition;

import java.util.List;

public class Networking {

    private final List<MappedPacket> mappedPacketList = Lists.newArrayList();

    public Networking() {
        mappedPacketList.add(new ClientPlayerPosition());
    }

    public List<MappedPacket> getMappedPacketList() {
        return mappedPacketList;
    }

}
