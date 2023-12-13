package io.github.nevalackin.client.binding;

public enum BindType {
    TOGGLE("Toggle"),
    HOLD("Hold"),
    NONE("None");

    private final String name;

    BindType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
