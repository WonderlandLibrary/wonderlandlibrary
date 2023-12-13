package today.flux.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class ChatSendEvent extends EventCancellable {

	public String message;

	public ChatSendEvent(String message) {
		this.message = message;
	}
	
}
