package tech.atani.client.feature.theme.data.enums;

public enum ThemeObjectType {
    ELEMENT("Element"), SCREEN("Screen");

    private final String name;

    ThemeObjectType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
