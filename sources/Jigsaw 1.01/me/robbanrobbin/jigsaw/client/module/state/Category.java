package me.robbanrobbin.jigsaw.client.module.state;

public enum Category {

	COMBAT, MOVEMENT, RENDER, MISC, MINIGAMES, INFO, HIDDEN, TARGET, AI("AI"), FUN, EXPLOITS, PLAYER, GLOBAL, WORLD, PRESETS;
	
	private String displayName;
	
	private Category(String displayName) {
		this.displayName = displayName;
	}
	
	private Category() {
		
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
