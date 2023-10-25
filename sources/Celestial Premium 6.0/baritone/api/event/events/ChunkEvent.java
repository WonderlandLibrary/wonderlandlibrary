/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.events;

import baritone.api.event.events.type.EventState;

public final class ChunkEvent {
    private final EventState state;
    private final Type type;
    private final int x;
    private final int z;

    public ChunkEvent(EventState state, Type type, int x, int z) {
        this.state = state;
        this.type = type;
        this.x = x;
        this.z = z;
    }

    public final EventState getState() {
        return this.state;
    }

    public final Type getType() {
        return this.type;
    }

    public final int getX() {
        return this.x;
    }

    public final int getZ() {
        return this.z;
    }

    public static enum Type {
        LOAD,
        UNLOAD,
        POPULATE_FULL,
        POPULATE_PARTIAL;

    }
}

