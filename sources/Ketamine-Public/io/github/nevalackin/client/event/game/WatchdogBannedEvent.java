package io.github.nevalackin.client.event.game;

import io.github.nevalackin.client.event.Event;
import net.minecraft.util.IChatComponent;

public final class WatchdogBannedEvent implements Event {

    private final IChatComponent reason;

    public WatchdogBannedEvent(IChatComponent reason) {
        this.reason = reason;
    }

    public IChatComponent getReason() {
        return reason;
    }
}
