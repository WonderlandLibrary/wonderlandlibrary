package de.tired.base.event.events;

import de.tired.base.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MoveEvent extends Event {

    double modifyX, modifyY, modifyZ;

}
