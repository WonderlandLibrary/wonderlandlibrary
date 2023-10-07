package com.ohare.client.event.events.game;

import com.ohare.client.event.CancelableEvent;

public class ChatEvent extends CancelableEvent {
	private String msg;

	public ChatEvent(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
