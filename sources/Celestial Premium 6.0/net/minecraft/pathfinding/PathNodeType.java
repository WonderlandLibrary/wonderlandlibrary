/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

public enum PathNodeType {
    BLOCKED(-1.0f),
    OPEN(0.0f),
    WALKABLE(0.0f),
    TRAPDOOR(0.0f),
    FENCE(-1.0f),
    LAVA(-1.0f),
    WATER(8.0f),
    RAIL(0.0f),
    DANGER_FIRE(8.0f),
    DAMAGE_FIRE(16.0f),
    DANGER_CACTUS(8.0f),
    DAMAGE_CACTUS(-1.0f),
    DANGER_OTHER(8.0f),
    DAMAGE_OTHER(-1.0f),
    DOOR_OPEN(0.0f),
    DOOR_WOOD_CLOSED(-1.0f),
    DOOR_IRON_CLOSED(-1.0f);

    private final float priority;

    private PathNodeType(float priorityIn) {
        this.priority = priorityIn;
    }

    public float getPriority() {
        return this.priority;
    }
}

