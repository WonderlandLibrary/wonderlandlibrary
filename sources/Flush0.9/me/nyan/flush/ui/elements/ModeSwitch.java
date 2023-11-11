package me.nyan.flush.ui.elements;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ModeSwitch {
    private String name;
    private String value;
    private int color;
    private final List<String> options;
    private final ArrayList<ModeSwitchValue> switches = new ArrayList<>();
    private boolean expanded;
    private float expand;
    private float x;
    private float y;
    private float width;
    private float height;

    private final Function<String, Boolean> shouldDraw;

    public ModeSwitch(String name, float x, float y, float width, float height, String value, int color, List<String> options, Function<String, Boolean> shouldDraw) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.value = value;
        this.color = color;
        this.options = options;
        this.shouldDraw = shouldDraw;

        for (String option : options) {
            switches.add(new ModeSwitchValue(option, this, color));
        }
    }

    public ModeSwitch(String name, float x, float y, float width, float height, String value, int color, Function<String, Boolean> shouldDraw, String... options) {
        this(name, x, y, width, height, value, color, Arrays.asList(options), shouldDraw);
    }

    public ModeSwitch(String name, float x, float y, float width, float height, String value, int color, String... options) {
        this(name, x, y, width, height, value, color, null, options);
    }

    public ModeSwitch(String name, float x, float y, float width, float height, String value, int color, List<String> options) {
        this(name, x, y, width, height, value, color, options, null);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        expand += (expanded ? 1 : -1) * 0.175F * partialTicks;
        expand = Math.max(Math.min(expand, 1), 0);

        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 18);

        Gui.drawRect(x, y, x + width, y + height, 0xFF1E1E1E);
        font.drawXYCenteredString(name, x + width / 2F, y + height / 2F, 0xFFE8E8E8);

        if (expand == 0) {
            return;
        }

        float optionY = y + this.height;
        for (ModeSwitchValue switchValue : switches) {
            if (shouldDraw != null && !shouldDraw.apply(switchValue.getValue())) {
                continue;
            }
            switchValue.draw(x, optionY, width, this.height - 6, mouseX, mouseY, partialTicks);
            optionY += this.height - 6;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + width, y + height - 0.5) &&
                button == 1) {
            expanded = !expanded;
        }

        if (!expanded || expand != 1) {
            return;
        }

        float optionY = y + height;
        for (ModeSwitchValue switchValue : switches) {
            if (shouldDraw != null && !shouldDraw.apply(switchValue.getValue())) {
                continue;
            }
            switchValue.mouseClicked(x, optionY, width, height - 6, mouseX, mouseY, button);
            optionY += height - 6;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = options.stream().filter(option -> option.equalsIgnoreCase(value)).findFirst().orElse(this.value);
    }

    public boolean is(String value) {
        return this.value.equalsIgnoreCase(value);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<String> getOptions() {
        return options;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getFullHeight() {
        float height = 0;
        for (ModeSwitchValue ignored : switches) {
            height += this.height - 6;
        }
        return this.height + height * expand;
    }

    private static class ModeSwitchValue {
        private String value;
        private final ModeSwitch modeSwitch;
        public boolean extended;
        private final int enabledColor;

        private int color = 0xFF121212;
        private int fontColor = 0xFFF2F2F2;

        public ModeSwitchValue(String value, ModeSwitch modeSwitch, int enabledColor) {
            this.value = value;
            this.modeSwitch = modeSwitch;
            this.enabledColor = enabledColor;
        }

        public void draw(float x, float y, float width, float height, int mouseX, int mouseY, float partialTicks) {
            int targetColor = modeSwitch.getValue().equals(value) ? enabledColor : 0xFF121212;
            color = ColorUtils.animateColor(color, targetColor, 16);

            int targetFontColor = modeSwitch.getValue().equals(value) ? 0xFF121212 : 0xF2F2F2;
            fontColor = ColorUtils.animateColor(fontColor, targetFontColor, 16);

            GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 16);

            Gui.drawRect(x, y, x + width, y + height, color);
            font.drawXYCenteredString(value, x + width / 2F, y + height / 2F, fontColor);
        }

        public void mouseClicked(float x, float y, float width, float height, int mouseX, int mouseY, int button) {
            if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + width, y + height - 0.5) && button == 0) {
                modeSwitch.setValue(value);
            }
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}