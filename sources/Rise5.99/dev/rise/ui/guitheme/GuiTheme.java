package dev.rise.ui.guitheme;

import dev.rise.util.render.ColorUtil;
import lombok.Getter;
import lombok.Setter;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.util.Objects;

@Setter
@Getter
@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public class GuiTheme {
    public Theme currentTheme = Theme.LIGHTMODE;

    public Color getThemeColor(final Theme theme) {
        switch (Objects.requireNonNull(theme)) {
            case DARKMODE:
                return new Color(60, 60, 60);
        }

        return new Color(ColorUtil.getRainbow());
    }

    public Color getThemeColor() {
        return getThemeColor(currentTheme);
    }
}