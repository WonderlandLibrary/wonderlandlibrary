package net.smoothboot.client.module.settings;

public class Setting {

    private String name;
    private boolean visible = true;

    public Setting(String name) {
        this.name = name;
    }
    public boolean isVisible() {
        return visible;
    }
    public String getName() {
        return name;
    }
}
