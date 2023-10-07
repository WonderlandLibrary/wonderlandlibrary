package viaversion.viarewind.utils;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.protocol.Protocol;

public interface ServerSender {

	void sendToServer(PacketWrapper packet, Class<? extends Protocol> packetProtocol, boolean skipCurrentPipeline, boolean currentThread) throws Exception;

}
