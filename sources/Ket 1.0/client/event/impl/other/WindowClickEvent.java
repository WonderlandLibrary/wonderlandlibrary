package client.event.impl.other;

import client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WindowClickEvent extends CancellableEvent {
    private final int windowId, slotId, mouseButtonClicked, mode;
}
