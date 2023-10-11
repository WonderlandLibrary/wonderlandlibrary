package markgg.event.core;

public enum EventPriority {
    LOWEST(0),
    LOWER(1),
    DEFAULT(2),
    HIGHER(3),
    HIGHEST(4);

    private final int priority;

    EventPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
