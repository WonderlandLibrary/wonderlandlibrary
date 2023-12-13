package summer.cheat.eventsystem.events.client;

import net.minecraft.util.IChatComponent;
import summer.cheat.eventsystem.Event;

public class EventChat extends Event {

    private IChatComponent chat;
	private boolean incoming;
    
	public EventChat(IChatComponent chat, boolean incoming) {
		this.chat = chat;
		this.incoming = incoming;
	}
	
	public boolean isSending() {
		return !this.incoming;
	}
	
	public boolean isRecieving() {
		return this.incoming;
	}
	
	public IChatComponent getChatComponent() {
		return chat;
	}
	
	public void setChat(IChatComponent chat) {
		this.chat = chat;
	}
	
}
