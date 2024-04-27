package me.aquavit.liquidsense.event;

public enum EventType {
    SEND("SEND"),
    RECEIVE("RECEIVE");

    private String typeName;

    EventType(final String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
