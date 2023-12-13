package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vestige.event.type.CancellableEvent;

@Getter
@Setter
@AllArgsConstructor
public class ChatSendEvent extends CancellableEvent {

    private String message;

}