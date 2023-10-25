package tech.drainwalk.utility;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

    private boolean state;
    private String desc;

    public BooleanSetting(String name, String desc, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        setVisible(visible);
    }

    public BooleanSetting(String name, String desc, boolean state) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        this.setVisible(() -> true);
    }

    public BooleanSetting(String name, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.state = state;
        setVisible(visible);
    }

    public BooleanSetting(String name, boolean state) {
        this.name = name;
        this.state = state;
        this.setVisible(() -> true);
    }

    public BooleanSetting(String name) {
        this(name, false);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean getCurrentValue() {
        return state;
    }

    public void setBoolValue(boolean state) {
        this.state = state;
    }
}

