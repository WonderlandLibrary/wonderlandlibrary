/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.util.MovementInput;

public final class MovementInputEvent
implements Event {
    private MovementInput movementInput;

    public MovementInputEvent(MovementInput movementInput) {
        this.movementInput = movementInput;
    }

    public MovementInput getMovementInput() {
        return this.movementInput;
    }
}

