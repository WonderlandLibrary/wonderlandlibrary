package tech.atani.client.feature.theme.data.enums;

public enum ScreenType {
    NOT("Not"), CLICK_GUI("Click GUI"), MAIN_MENU("Main Menu");

    private final String name;

    ScreenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
