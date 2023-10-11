package io.github.raze.registry.system.themes;

import de.florianmichael.rclasses.storage.Storage;
import io.github.raze.utilities.collection.visual.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThemeRegistry extends Storage<ColorUtil.Theme> {

    public ColorUtil.Theme currentTheme;

    public ColorUtil.Theme getCurrentTheme() {
        return currentTheme;
    }

    public ColorUtil.Theme getTheme(String input) {
        for (ColorUtil.Theme theme : this.getList()) {
            if (theme.getName().equalsIgnoreCase(input)) {
                return theme;
            }
        }
        return null;
    }

    @Override
    public void init() {
        for(ColorUtil.Theme theme : ColorUtil.Theme.values()) {
            add(theme);
        }

        this.currentTheme = this.getList().get(0);
    }
}
