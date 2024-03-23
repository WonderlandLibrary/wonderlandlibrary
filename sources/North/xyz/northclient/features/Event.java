package xyz.northclient.features;

import lombok.Getter;
import lombok.Setter;

public class Event {
    @Getter
    @Setter
    private boolean cancelled;

    @Getter
    @Setter
    private boolean post = false;

    public boolean isPre() {
        return !post;
    }
}
