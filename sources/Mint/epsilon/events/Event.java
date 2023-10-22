package epsilon.events;



import java.lang.reflect.InvocationTargetException;

import epsilon.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Event<T> {
	
	
	public boolean cancelled;
	public EventType type;
	public Packet packet;
	public EventDirection direction;
	
	
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled() {
		this.cancelled = true;
	}
	public EventType getType() {
		return type;
	}
	public Packet getPacket()
    {
        return packet;
    }
	
	public void setType(EventType type) {
		this.type = type;
	}
	public EventDirection getDirection() {
		return direction;
	}
	public void setDirection(EventDirection direction) {
		this.direction = direction;
	}
	
	public boolean isPre(){
		if(type == null)
			return false;
		
		return type == EventType.PRE;
		}
	
	public boolean isPost(){
		if(type == null)
			return false;
		
		return type == EventType.POST;
		}
	
	public boolean isIncoming(){
		if(direction == null)
			return false;
		
		return direction == EventDirection.INCOMING;
		}
	
	public boolean isOutgoing(){
		if(direction == null)
			return false;
		
		return direction == EventDirection.OUTGOING;
	}

}