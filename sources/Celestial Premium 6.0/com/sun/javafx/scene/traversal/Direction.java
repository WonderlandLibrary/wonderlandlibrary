/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.NodeOrientation
 */
package com.sun.javafx.scene.traversal;

import javafx.geometry.NodeOrientation;

public final class Direction
extends Enum<Direction> {
    public static final /* enum */ Direction UP = new Direction(false);
    public static final /* enum */ Direction DOWN = new Direction(true);
    public static final /* enum */ Direction LEFT = new Direction(false);
    public static final /* enum */ Direction RIGHT = new Direction(true);
    public static final /* enum */ Direction NEXT = new Direction(true);
    public static final /* enum */ Direction NEXT_IN_LINE = new Direction(true);
    public static final /* enum */ Direction PREVIOUS = new Direction(false);
    private final boolean forward;
    private static final /* synthetic */ Direction[] $VALUES;

    public static Direction[] values() {
        return (Direction[])$VALUES.clone();
    }

    public static Direction valueOf(String string) {
        return Enum.valueOf(Direction.class, string);
    }

    private Direction(boolean bl) {
        this.forward = bl;
    }

    public boolean isForward() {
        return this.forward;
    }

    public Direction getDirectionForNodeOrientation(NodeOrientation nodeOrientation) {
        if (nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            switch (this) {
                case LEFT: {
                    return RIGHT;
                }
                case RIGHT: {
                    return LEFT;
                }
            }
        }
        return this;
    }

    private static /* synthetic */ Direction[] $values() {
        return new Direction[]{UP, DOWN, LEFT, RIGHT, NEXT, NEXT_IN_LINE, PREVIOUS};
    }

    static {
        $VALUES = Direction.$values();
    }
}

