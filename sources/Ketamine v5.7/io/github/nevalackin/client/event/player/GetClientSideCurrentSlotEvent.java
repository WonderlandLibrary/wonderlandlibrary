package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.Event;

public final class GetClientSideCurrentSlotEvent implements Event {

    private int currentItem;

    public GetClientSideCurrentSlotEvent(int currentItem) {
        this.currentItem = currentItem;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }
}
