package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kroko
 * @created on 21.01.2021 : 10:51
 */

@Getter @Setter @AllArgsConstructor
public class UpdateMotionEvent extends Event {
    final Type type;
    boolean isCurrentView;

    public enum Type {
        PRE, MID, POST;
    }
}
