package com.enjoytheban.api.events.misc;

import com.enjoytheban.api.Event;

public class EventKey extends Event {

	/**
	 * @author Purity
	 * @desc A basic event for getting key input
	 * @called Minecraft runTick()
	 */

	private int key;
	    
	//constructor
	public EventKey(final int key) {
	    this.key = key;
	}
	    
	//gets key
	public int getKey() {
	    return this.key;
	}
	
	//sets key
	public void setKey(final int key) {
	    this.key = key;
	}
}