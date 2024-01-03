package de.theBest.MythicX.events;

import eventapi.events.Event;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class EventSyncItem implements Event {
    private int slot;

    public EventSyncItem(int i) {
        this.slot = i;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
