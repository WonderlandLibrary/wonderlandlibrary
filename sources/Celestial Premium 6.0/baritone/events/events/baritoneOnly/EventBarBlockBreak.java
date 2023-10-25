/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.baritoneOnly;

import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.events.Event;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventBarBlockBreak
extends EventCancellable
implements Event {
    public BlockPos position;

    public EventBarBlockBreak(BlockPos pos) {
        this.position = pos;
    }
}

