package tech.atani.client.feature.theme.data.enums;

public enum ElementType {
    NOT("Not"), WATERMARK("Watermark"), MODULE_LIST("Module List"), TARGET_HUD("Target HUD");

    private final String name;

    ElementType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
