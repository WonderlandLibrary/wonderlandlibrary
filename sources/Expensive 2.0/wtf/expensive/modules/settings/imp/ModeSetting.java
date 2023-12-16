package wtf.expensive.modules.settings.imp;

import lombok.Getter;
import lombok.Setter;
import wtf.expensive.modules.settings.Setting;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;

@Getter
public class ModeSetting extends Setting {
    @Setter
    private int index;
    public String[] modes;


    public ModeSetting(String name, String current, String... modes) {
        super(name);
        this.modes = modes;
        this.index = Arrays.asList(modes).indexOf(current);
    }

    public boolean is(String mode) {
        return get().equals(mode);
    }

    public String get() {
        try {
            if (index < 0 || index >= modes.length) {
                return modes[0];
            }
            return modes[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "ERROR";
        }
    }

    public void set(String mode) {
        this.index = Arrays.asList(modes).indexOf(mode);
    }

    public void set(int mode) {
        this.index = mode;
    }

    public ModeSetting setVisible(Supplier<Boolean> bool) {
        visible = bool;
        return this;
    }

    @Override
    public SettingType getType() {
        return SettingType.MODE_SETTING;
    }
}
