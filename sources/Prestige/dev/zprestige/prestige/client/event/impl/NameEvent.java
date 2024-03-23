package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;

public class NameEvent extends Event {
    public String name;
    public String fakeName;

    public NameEvent(String fakeName, String name) {
        this.fakeName = fakeName;
        this.name = name;
    }

    public String getFakeName() {
        return this.fakeName;
    }

    public void setFakeName(String fakeName) {
        this.fakeName = fakeName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
