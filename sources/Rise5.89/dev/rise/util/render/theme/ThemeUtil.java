package dev.rise.util.render.theme;

import dev.rise.Rise;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.InstanceAccess;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.render.ColorUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.util.Objects;

@UtilityClass
@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public final class ThemeUtil implements InstanceAccess {

    @Getter
    @Setter
    private String customClientName = "";
    private final Color color1 = new Color(71, 148, 253);
    private final Color color2 = new Color(71, 253, 160);
    private Color color;
    private String theme;
    private boolean switcher;

    private final TimeUtil timer = new TimeUtil();

    public Color getThemeColor(final ThemeType type) {
        return getThemeColor(0, type, 1);
    }

    public int getThemeColorInt(final ThemeType type) {
        return getThemeColor(type).hashCode();
    }

    public int getThemeColorInt(final float colorOffset, final ThemeType type) {
        return getThemeColor(colorOffset, type, 1).hashCode();
    }

    public int getThemeColorInt(final float colorOffset, final ThemeType type, final float timeMultiplier) {
        return getThemeColor(colorOffset, type, timeMultiplier).hashCode();
    }

    public Color getThemeColor(final float colorOffset, final ThemeType type) {
        return getThemeColor(colorOffset, type, 1);
    }

    public Color getThemeColor(float colorOffset, final ThemeType type, final float timeMultiplier) {
        if (timer.hasReached(50 * 5)) {
            timer.reset();
            theme = ((ModeSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Interface", "Theme"))).getMode();
            color = new Color(Rise.CLIENT_THEME_COLOR);
        }

        if (theme == null || color == null) return color;

        float colorOffsetMultiplier = 1;

        if (type == ThemeType.GENERAL) {
            switch (theme) {
                case "Rise":
                case "Skeet":
                case "Comfort":
                case "Minecraft":
                case "Never Lose":
                    colorOffsetMultiplier = 2.2f;
                    break;

                case "Rise Rainbow":
                case "Comfort Rainbow":
                case "Minecraft Rainbow":
                case "Never Lose Rainbow":
                    colorOffsetMultiplier = 5f;
                    break;

                case "Rice":
                case "Rise Christmas":
                case "Rise Blend":
                    colorOffsetMultiplier = 2.5f;
                    break;

                case "One Tap":
                    break;
            }
        }

        colorOffset *= colorOffsetMultiplier;

        final double timer = (System.currentTimeMillis() / 1E+8 * timeMultiplier) * 4E+5;

        final double factor = (Math.sin(timer + colorOffset * 0.55f) + 1) * 0.5f;
        switch (type) {
            case GENERAL:
            case ARRAYLIST:
                switch (theme) {
                    case "Rise":
                    case "Skeet":
                    case "Comfort":
                    case "Minecraft":
                    case "Never Lose":
                        final float offset1 = (float) (Math.abs(Math.sin(timer + colorOffset * 0.45)) / 2) + 1;
                        color = ColorUtil.liveColorBrighter(Rise.CLIENT_THEME_COLOR_BRIGHT_COLOR, offset1);
                        break;

                    case "Rise Rainbow":
                    case "Comfort Rainbow":
                    case "Minecraft Rainbow":
                    case "Never Lose Rainbow":
                        color = new Color(ColorUtil.getColor(-(1 + colorOffset * 1.7f), 0.7f, 1));
                        break;

                    case "Rise Blend":
                        color = ColorUtil.mixColors(color1, color2, factor);
                        break;

                    case "Rise Christmas":
                        color = ColorUtil.mixColors(Color.WHITE, Color.RED, factor);
                        break;

                    case "Rice":
                        color = ColorUtil.mixColors(new Color(190, 0, 255, 255), new Color(0, 190, 255, 255), factor);
                        break;

                    case "One Tap":
                        color = Color.WHITE;
                        break;
                }

                break;

            case LOGO:
                switch (theme) {
                    case "Rise":
                    case "Comfort":
                    case "Minecraft":
                        color = new Color(Rise.CLIENT_THEME_COLOR_BRIGHT);
                        break;

                    case "Rise Rainbow":
                    case "Never Lose Rainbow":
                    case "Minecraft Rainbow":
                        color = new Color(ColorUtil.getColor(1 + colorOffset * 1.4f, 0.5f, 1));
                        break;

                    case "Rise Christmas":
                        color = ColorUtil.mixColors(Color.WHITE, Color.RED, factor);
                        break;

                    case "Rice":
                        color = ColorUtil.mixColors(new Color(190, 0, 255, 255), new Color(0, 190, 255, 255), factor);
                        break;

                    case "Rise Blend":
                        color = ColorUtil.mixColors(color1, color2, factor);
                        break;
                }

                break;

            default:
            case FLAT_COLOR:
                color = new Color(Rise.CLIENT_THEME_COLOR);
        }

        return color;
    }
}