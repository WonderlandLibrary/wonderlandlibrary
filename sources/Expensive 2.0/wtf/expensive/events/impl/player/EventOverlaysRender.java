package wtf.expensive.events.impl.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wtf.expensive.events.Event;

/**
 * @author dedinside
 * @since 09.06.2023
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class EventOverlaysRender extends Event {

    private final OverlayType overlayType;

    public enum OverlayType {
        FIRE_OVERLAY, BOSS_LINE, SCOREBOARD, TITLES, TOTEM, FOG
    }
}
