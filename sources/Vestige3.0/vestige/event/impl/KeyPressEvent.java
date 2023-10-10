package vestige.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vestige.event.Event;

@Getter
@AllArgsConstructor
public class KeyPressEvent extends Event {

    private int key;

}