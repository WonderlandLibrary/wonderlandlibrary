package me.nyan.flush.clickgui.flush.component.components.settings;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.flush.component.Component;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ColorComponent extends Component {
    private final ColorSetting setting;

    private boolean expanded;
    private float expand;

    private final int border = 4;
    private final int speed = 4;

    private boolean hueSelector, sbSelector;

    public ColorComponent(ColorSetting setting) {
        this.setting = setting;
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay Medium", 18);

        Gui.drawRect(x, y, x + getWidth(), y + getHeight(), 0xFF1E1E1E);

        x += border;
        y += border;

        float prevWidth = 20;
        float prevHeight = 8;
        float prevX = x + getWidth() - border * 2 - prevWidth;
        float prevY = y + font.getFontHeight() / 2F - prevHeight / 2F;
        boolean hovered = MouseUtils.hovered(mouseX, mouseY, prevX, prevY, prevX + prevWidth, prevY + prevHeight);

        RenderUtils.fillRoundRect(prevX, prevY, prevWidth, prevHeight,
                3, ColorUtils.alpha(ColorUtils.brightness(setting.getRGB(), hovered ? 0.85F : 1),
                        (int) ((1 - expand) * 255)));

        float arrowWidth = 10;
        float arrowHeight = 4;
        float arrowX = x + getWidth() - border * 2 - prevWidth / 2F - arrowWidth / 2F;
        float arrowY = y + font.getFontHeight() / 2F - arrowHeight / 2F;
        drawArrow(arrowX, arrowY, arrowWidth, arrowHeight, mouseX, mouseY);

        font.drawString(setting.getName(), x, y, -1);
        y += font.getFontHeight() + border;

        drawHueSelector(x + getWidth() - border - 10 - 4 / 2F, y, 10, getPickerWidth() * expand, mouseX, mouseY);
        drawSBSelector(x, y, getPickerWidth(), getPickerWidth() * expand, mouseX, mouseY);

        expand += (expanded ? 1 : -1) / 100F * Flush.getFrameTime();
        expand = Math.max(Math.min(expand, 1), 0);
    }

    private void drawArrow(float x, float y, float width, float height, int mouseX, int mouseY) {
        if (expand <= 0) {
            return;
        }

        boolean hovered = MouseUtils.hovered(mouseX, mouseY, x, y, x + width, y + height);
        int color = ColorUtils.alpha(hovered ? 0xFFCCCCCC : -1, (int) (expand * 255));

        y += expand * height;
        height = height - expand * height * 2;
        glLineWidth(2);
        glEnable(GL_LINE_SMOOTH);
        RenderUtils.drawLine(x, y, x + width / 2F, y + height, color);
        RenderUtils.drawLine(x + width / 2F, y + height, x + width, y, color);
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (button != 0) {
            return;
        }

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay Medium", 18);

        x += border;
        y += border;

        float prevWidth = 20;
        float prevHeight = 8;
        float prevX = x + getWidth() - border * 2 - prevWidth;
        float prevY = y + font.getFontHeight() / 2F - prevHeight / 2F;
        if (expanded) {
            float arrowWidth = 10;
            float arrowHeight = 4;
            float arrowX = x + getWidth() - border * 2 - prevWidth / 2F - arrowWidth / 2F;
            float arrowY = y + font.getFontHeight() / 2F - arrowHeight / 2F;
            if (MouseUtils.hovered(mouseX, mouseY, arrowX, arrowY, arrowX + arrowWidth, arrowY + arrowHeight)) {
                expanded = false;
                hueSelector = false;
                sbSelector = false;
            }
        } else if (MouseUtils.hovered(mouseX, mouseY, prevX, prevY, prevX + prevWidth, prevY + prevHeight)) {
            expanded = true;
        }

        if (expand < 1) {
            return;
        }

        y += font.getFontHeight() + border;

        if (MouseUtils.hovered(mouseX, mouseY, x + getWidth() - border - 10 - 4 / 2F, y,
                x + getWidth() - border - 4 / 2F, y + getPickerWidth())) {
            hueSelector = true;
        }

        if (MouseUtils.hovered(mouseX, mouseY, x, y, x + getPickerWidth(), y + getPickerWidth())) {
            sbSelector = true;
        }
    }

    @Override
    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        if (button != 0) {
            return;
        }

        hueSelector = false;
        sbSelector = false;
    }

    private float[] getHSB() {
        int rgb = setting.getRGB();
        return Color.RGBtoHSB(ColorUtils.getRed(rgb), ColorUtils.getGreen(rgb), ColorUtils.getBlue(rgb), null);
    }

    private void drawHueSelector(float x, float y, float width, float height, int mouseX, int mouseY) {
        if (height <= 0) {
            return;
        }

        for (float currentY = 0; currentY < height; currentY++) {
            float hue = currentY / height;
            int color = Color.HSBtoRGB(hue, 1, 1);
            RenderUtils.fillRectangle(x + width / 2F - 2, y + currentY, 4, 1, color);
        }

        RenderUtils.fillCircle(x + width / 2F, y + getHSB()[0] * height, width / 2F, -1);

        if (hueSelector) {
            float hue = MathUtils.clamp((mouseY - y) / height, 0, 1);
            float[] hsb = getHSB();
            setting.setRGB(Color.HSBtoRGB(hue, hsb[1], hsb[2]));
        }
    }

    private void drawSBSelector(float x, float y, float width, float height, double mouseX, double mouseY) {
        if (height <= 0) {
            return;
        }

        float hue = getHSB()[0];

        glPointSize(speed);
        glBegin(GL_POINTS);
        for (float currentY = 0; currentY < height; currentY += speed / 2F) {
            for (float currentX = 0; currentX < width; currentX += speed / 2F) {
                float saturation = currentX / width;
                float brightness = 1 - currentY / height;
                RenderUtils.glColor(Color.HSBtoRGB(hue, saturation, brightness));
                glVertex2f(speed / 4F + x + currentX, speed / 4F + y + currentY);
            }
        }
        glEnd();

        if (sbSelector) {
            float saturation = MathUtils.clamp((float) (mouseX - x) / width, 0, 1);
            float brightness = MathUtils.clamp(1 - (float) (mouseY - y) / height, 0, 1);
            setting.setRGB(Color.HSBtoRGB(hue, saturation, brightness));
        }

        float saturation = getHSB()[1];
        float brightness = getHSB()[2];
        RenderUtils.fillCircle(x + saturation * width, y + (1 - brightness) * height,
                3, setting.getRGB());
        RenderUtils.drawCircle(x + saturation * width, y + (1 - brightness) * height,
                3, -1, 1);

        RenderUtils.glColor(-1);
    }

    public float getPickerWidth() {
        return getWidth() - border * 2 - 10;
    }

    @Override
    public float getHeight() {
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay Medium", 18);
        return font.getFontHeight() + border * 2 + (getPickerWidth() + border) * expand;
    }

    @Override
    public boolean show() {
        return setting.shouldShow();
    }
}