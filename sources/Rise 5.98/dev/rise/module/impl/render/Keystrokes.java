package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.InstanceAccess;

import dev.rise.util.math.MathUtil;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.gui.Gui.*;

/**
 * @author Hazsi
 */
@ModuleInfo(name = "Keystrokes", description = "Shows your key and mouse presses", category = Category.LEGIT)
public class Keystrokes extends Module implements InstanceAccess {

    private final NoteSetting keysNote = new NoteSetting("Keys", this);
    private final BooleanSetting showSpace = new BooleanSetting("Draw Space", this, false);
    private final BooleanSetting showMouse = new BooleanSetting("Draw Mouse Buttons", this, true);
    private final BooleanSetting showCPS = new BooleanSetting("Draw CPS", this, true);

    private final NoteSetting appearanceNote = new NoteSetting("Appearance", this);
    private final NumberSetting scale = new NumberSetting("Scale", this, 0.75, 0.5, 1, 0.01);
    private final NumberSetting gap = new NumberSetting("Gap", this, 1.5, 0, 3, 0.5);
    private final NumberSetting bgOpacity = new NumberSetting("Background Opacity", this, 75, 0, 255, 0.5);
    private final NumberSetting bgOpacityPressed = new NumberSetting("Background Opacity (Pressed)", this, 125, 0, 255, 0.5);
    private final BooleanSetting border = new BooleanSetting("Border", this, false);
    private final NumberSetting borderOpacity = new NumberSetting("Border Opacity", this, 125, 0, 255, 0.5);
    private final NumberSetting borderOpacityPressed = new NumberSetting("Border Opacity (Pressed)", this, 175, 0, 255, 0.5);
    private final BooleanSetting chromaKeys = new BooleanSetting("Chroma Keys", this, false);
    private final BooleanSetting arrows = new BooleanSetting("Replace Keys With Arrows", this, false);

    private final Button forward, left, back, right, jump, lmb, rmb;
    private final FontRenderer fr = mc.fontRendererObj;
    private float posX, posY, width, height;

    private int borderWidth;

    public Keystrokes() {
        forward = new Button(mc.gameSettings.keyBindForward, "W", "▲");
        left = new Button(mc.gameSettings.keyBindLeft, "A", "◀");
        back = new Button(mc.gameSettings.keyBindBack, "S", "▼");
        right = new Button(mc.gameSettings.keyBindRight, "D", "▶");
        jump = new Button(mc.gameSettings.keyBindJump, "§m-----");
        lmb = new MouseButton(mc.gameSettings.keyBindAttack, "LMB");
        rmb = new MouseButton(mc.gameSettings.keyBindUseItem, "RMB");
    }

    @Override
    public void onUpdateAlwaysInGui() {
        borderOpacity.hidden = borderOpacityPressed.hidden = !border.isEnabled();
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        posX = 5;
        posY = 30;
        width = height = 10 + (float) (25 * scale.getValue());
        borderWidth = 1;

        double gap = this.gap.getValue();

        // If border is enabled, resize and move the keys a bit to make room
        if (border.isEnabled()) {
            posX += borderWidth;
            posY += borderWidth;

            width -= borderWidth * 2;
            height -= borderWidth * 2;
            gap += borderWidth * 2;
        }

        // Core four movement keys
        forward.draw(posX + width + gap, posY, width, height);
        left.draw(posX, posY + height + gap, width, height);
        back.draw(posX + width + gap, posY + height + gap, width, height);
        right.draw(posX + (width + gap) * 2, posY + height + gap, width, height);

        // Space and mouse buttons
        if (showMouse.isEnabled()) {
            lmb.draw(posX, posY + (height + gap) * 2, width * 1.5 + gap / 2, height);
            rmb.draw(posX + width * 1.5 + gap * 1.5, posY + (height + gap) * 2, width * 1.5 + gap / 2, height);
        }
        if (showSpace.isEnabled()) {
            double y = posY + (height + gap) * 2;
            if (showMouse.isEnabled()) y += height + gap;

            jump.draw(posX, y, width + (width + gap) * 2, height / 2);
        }
    }

    @Getter
    @Setter
    private class Button {

        private final KeyBinding keyBinding;
        private String text, altText, originalText, originalAltText;

        private boolean lastState;
        private long lastPress, lastRelease;

        public Button(KeyBinding keyBinding, String text, String altText) {
            this.keyBinding = keyBinding;
            this.text = this.originalText = text;
            this.altText = this.originalAltText = altText;
        }

        public Button(KeyBinding keyBinding, String text) {
            this(keyBinding, text, text);
        }

        public void draw(double x, double y, double width, double height) {

            int backgroundColor;
            int textColor = chromaKeys.isEnabled() ? ThemeUtil.getThemeColor((float) (x + y) / 50, ThemeType.GENERAL).getRGB() : -1;
            int borderColor = chromaKeys.isEnabled() ? textColor :
                    new Color(0, 0, 0, (float) (keyBinding.isKeyDown() ? borderOpacityPressed.getValue() : borderOpacity.getValue()) / 255F).getRGB();

            // Animate the background colour
            float i;
            if (lastState) {
                i = (float) MathUtil.lerp(bgOpacity.getValue(), bgOpacityPressed.getValue(),
                        MathHelper.clamp_double((System.currentTimeMillis() - lastPress) / 75D, 0, 1));
            } else {
                i = (float) MathUtil.lerp(bgOpacityPressed.getValue(), bgOpacity.getValue(),
                        MathHelper.clamp_double((System.currentTimeMillis() - lastRelease) / 75D, 0, 1));
            }
            backgroundColor = new Color(0, 0, 0, i / 255F).getRGB();

            // Update lastPress and lastRelease if needed
            if (keyBinding.isKeyDown() && !lastState) { // Released between the last frame and now
                lastPress = System.currentTimeMillis();
            } else if (!keyBinding.isKeyDown() && lastState) { // Pressed between the last frame and now
                lastRelease = System.currentTimeMillis();
            }

            lastState = keyBinding.isKeyDown();

            // Background
            drawRect(x, y, x + width, y + height, backgroundColor);

            // Border
            if (border.isEnabled()) {
                drawRect(x - borderWidth, y - borderWidth, x + width + borderWidth, y, borderColor); // Top
                drawRect(x - borderWidth, y + height, x + width + borderWidth, y + height + borderWidth, borderColor); // Bottom
                drawRect(x - borderWidth, y, x, y + height, borderColor); // Left
                drawRect(x + width, y, x + width + borderWidth, y + height, borderColor); // Right
            }

            // Text
            final float textHeight = fr.FONT_HEIGHT;
            if (this.keyBinding.equals(mc.gameSettings.keyBindJump)) text = altText = "§m-----"; // Restore back to version with formatting codes

            fr.drawCenteredStringWithShadow((arrows.isEnabled() && !(this instanceof MouseButton)) ? altText : text, (float) (x + width / 2), (float) (y + height / 2 - textHeight / 2 + 1), textColor);
        }
    }

    @Getter
    @Setter
    private class MouseButton extends Button {

        private final List<Long> clicks = new ArrayList<>();

        public MouseButton(KeyBinding keyBinding, String text) {
            super(keyBinding, text, text);
        }

        @Override
        public void draw(double x, double y, double width, double height) {
            if (showCPS.isEnabled()) {
                if (!isLastState() && getKeyBinding().isKeyDown()) { // Pressed between the last frame and now
                    clicks.add(System.currentTimeMillis());
                }

                this.clicks.removeIf((time) -> { // Remove all clicks older than 1000 ms
                    return System.currentTimeMillis() - time > 1000;
                });

                if (clicks.size() > 0) {
                    setText(clicks.size() + " CPS");
                } else {
                    setText(getOriginalText());
                }
            }

            super.draw(x, y, width, height);
        }
    }
}
