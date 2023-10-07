package com.ohare.client.event.events.input;


import com.ohare.client.event.Event;

public class KeyPressEvent extends Event {
	private int key;

	public KeyPressEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return this.key;
	}
}
