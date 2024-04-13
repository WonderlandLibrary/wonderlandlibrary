package dev.tenacity.util.render;

import dev.tenacity.module.impl.render.HUDModule;
import dev.tenacity.util.tuples.Pair;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Theme {
    TENACITY("Tenacity", new Color(236, 133, 209), new Color(28, 167, 222)),
    CHRISTMAS("Christmas", new Color(255, 0, 0), new Color(255, 255, 255) ),
    Guff("Guff", new Color(255, 0, 255), new Color(12, 0, 255) ),
    SKEET("Skeet", new Color(0, 255, 0), new Color(0, 0, 0) ),
    SNOWYSKY("Snowy Sky", new Color(0, 190, 255), new Color(255, 255, 255) ),
    WINTER("Winter", new Color(200, 200, 200), new Color(255, 255, 255) ),
    ORANGEJUICE("Orange Juice", new Color(255, 150, 0), new Color(255, 185, 0) ),
    JAVA("Java", new Color(111, 78, 55), new Color(130, 90, 70) ),
    WATER("Water", new Color(0, 190, 255), new Color(0, 110, 255) ),
    THOQ("Thoq", new Color(120, 0, 255), new Color(10, 0, 120) ),
    CORALPINK("Coral Pink", new Color(248, 131, 121), new Color(120, 0, 110)),
    BATMAN("Batman", new Color(0, 0, 0), new Color(255, 255, 0)),
    STEEL("Steel", new Color(65, 131, 247), new Color(56, 70, 96)),
    CUSTOM("Custom", HUDModule.primaryColor.getColor(), HUDModule.secondaryColor.getColor(),true);

    private static final Map<String, Theme> themeMap = new HashMap<>();

    private final String name;
    private final Pair<Color, Color> colors;
    private final boolean gradient;

    Theme(String name, Color color, Color colorAlternative) {
        this(name, color, colorAlternative, false);
    }

    Theme(String name, Color color, Color colorAlternative, boolean gradient) {
        this.name = name;
        colors = Pair.of(color, colorAlternative);
        this.gradient = gradient;
    }

    public static void init() {
        Arrays.stream(values()).forEach(theme -> themeMap.put(theme.name, theme));
    }

    public Pair<Color, Color> getColors() {
        if (this.equals(Theme.CUSTOM)) {
            return Pair.of(HUDModule.primaryColor.getColor(), HUDModule.secondaryColor.getColor());
        } else return colors;
    }

    public static Pair<Color, Color> getThemeColors(String name) {
        return get(name).getColors();
    }

    public static Theme get(String name) {
        return themeMap.get(name);
    }

    public static Theme getCurrentTheme() {
        return Theme.get(HUDModule.theme.getCurrentMode());
    }

}
