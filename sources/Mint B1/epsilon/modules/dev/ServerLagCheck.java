package epsilon.modules.dev;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class ServerLagCheck extends Module {

	public ServerLagCheck() {
		super("ServerLagCheck", Keyboard.KEY_NONE, Category.DEV, "Displays server response time");
	}
	
	public static int lastResponseTime, CalculateTPS;
	private int responses;
	
	public void onEnable() {
		lastResponseTime = CalculateTPS = responses = 0;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			CalculateTPS++;
		
		}
		if(e instanceof EventReceivePacket) {


			Packet p = e.getPacket();
			if(p instanceof S03PacketTimeUpdate) { //Time is sent every 20 ticks which is 2* more often then S00
				int t = (int) (System.currentTimeMillis() - lastResponseTime);
				CalculateTPS = 0;
				lastResponseTime = (int) System.currentTimeMillis();
				responses++;
			}
			if(p instanceof S00PacketKeepAlive) { //Always sent every 40 ticks, check cause some servers dont send time updates
				int t = (int) (System.currentTimeMillis() - lastResponseTime);
				CalculateTPS = 0;
				lastResponseTime = (int) System.currentTimeMillis();
				responses++;
			}
			if(p instanceof S32PacketConfirmTransaction) { //Not the best method since on servers without a packet based AC this wont be sent every tick
				int t = (int) (System.currentTimeMillis() - lastResponseTime);
				CalculateTPS = 0;
				lastResponseTime = (int) System.currentTimeMillis();
				responses++;
			}
			
			
		}
		
		
	}

}
