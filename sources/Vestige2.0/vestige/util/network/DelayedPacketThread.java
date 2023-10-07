package vestige.util.network;

import lombok.AllArgsConstructor;
import net.minecraft.network.Packet;

@AllArgsConstructor
public class DelayedPacketThread extends Thread {
	
	private Packet packet;
	private long delay;
	
	@Override
	public void run() {
		try {
			this.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			PacketUtil.sendPacketNoEvent(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
