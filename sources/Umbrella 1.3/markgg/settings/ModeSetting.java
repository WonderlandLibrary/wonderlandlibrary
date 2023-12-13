/*
 * Decompiled with CFR 0.150.
 */
package markgg.settings;

import java.util.Arrays;
import java.util.List;
import markgg.settings.Setting;

public class ModeSetting
extends Setting {
    public int index;
    public List<String> modes;

    public ModeSetting(String name, String defaultMode, String ... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
    }

    public String getMode() {
        return this.modes.get(this.index);
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean is(String mode) {
        return this.index == this.modes.indexOf(mode);
    }

    public void cycle() {
        this.index = this.index < this.modes.size() - 1 ? ++this.index : 0;
    }
}

