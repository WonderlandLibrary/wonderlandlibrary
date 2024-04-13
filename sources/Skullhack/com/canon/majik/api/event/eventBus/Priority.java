package com.canon.majik.api.event.eventBus;

public enum Priority {
    LOWEST(1),
    LOW(2),
    DEFAULT(3),
    HIGH(4),
    HIGHEST(5);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
