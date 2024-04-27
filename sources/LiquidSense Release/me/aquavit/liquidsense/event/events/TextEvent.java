package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;

public class TextEvent extends Event {
    private String text;

    public TextEvent(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
