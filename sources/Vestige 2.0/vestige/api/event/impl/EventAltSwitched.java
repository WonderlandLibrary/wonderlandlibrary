package vestige.api.event.impl;

public class EventAltSwitched {
	
	private String username, lastUsername;
	
	public EventAltSwitched(String username) {
		this.lastUsername = this.username;
		this.username = username;
	}
	
	public boolean changedAlt() {
		return lastUsername == null || !lastUsername.equals(username);
	}

	public String getUsername() {
		return username;
	}

	public String getLastUsername() {
		return lastUsername;
	}
	
}