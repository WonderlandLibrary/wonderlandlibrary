package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class RainbowPacket implements Packet {

	@Override
	public void readPacketData(PacketBuffer data) throws IOException {		
	}

	@Override
	public void writePacketData(PacketBuffer data) throws IOException {
	}

	@Override
	public void processPacket(INetHandler handler) {
	}

}
