package dev.zprestige.prestige.client.event;

public enum Priority {
    HIGHEST(5),
    HIGH(4),
    DEFAULT(3),
    LOW(2),
    LOWEST(1);

    int priority;


    Priority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
