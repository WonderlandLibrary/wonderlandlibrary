package sudo.module.client;

import net.minecraft.util.profiling.jfr.sample.NetworkIoStatistics.Packet;
import sudo.Client;
import sudo.core.event.EventTarget;
import sudo.events.PacketEvent;
import sudo.module.Mod;

public class PacketLogger extends Mod {
	
    public PacketLogger() {
        super("PacketLogger", "Logs packet in the console", Category.CLIENT, 0);
    }
    
    @EventTarget
    private void onReceivePacket(PacketEvent.Receive event) {
    	if (event.packet.getClass() != null) {
    		Client.logger.info(Packet.class.getName().toString());
    	}
    		
    }
    
    @EventTarget
    private void onSendPacket(PacketEvent.Send event) {
    	if (event.packet.getClass() != null) {
    		Client.logger.info(Packet.class.getName().toString());
    	}
    }

}
