package ru.smertnix.celestial.event.events;


public class EventChat implements Event  {
	public String chatLine;
	public EventChat(String chatLine) {
		this.chatLine = chatLine;
	}
	public EventChat() {
		this.chatLine = "";
	}
}
