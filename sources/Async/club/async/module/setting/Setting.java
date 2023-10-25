package club.async.module.setting;

import club.async.module.Module;

import java.util.function.Supplier;

public class Setting {

    private Module parent;
    private String name;
    private Supplier<Boolean> visible;

    /*
    Simple constructor's for cleaner code
    */

    public Setting(String name, Module parent, Supplier<Boolean> visible) {
        this.name = name;
        this.parent = parent;
        this.visible = visible;
        parent.addSetting(this);
    }

    public Setting(String name, Module parent) {
        this(name, parent, () -> true);
    }

    /*
    Some getters and setters
    */
    public final String getName() {
        return name;
    }
    public final boolean isVisible() {
        return visible.get();
    }
    public final void setVisible(boolean visible) {
        this.visible = () -> visible;
    }

}
