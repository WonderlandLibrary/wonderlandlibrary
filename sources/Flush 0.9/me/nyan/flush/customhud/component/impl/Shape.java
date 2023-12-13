package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.BooleanSetting;
import me.nyan.flush.customhud.setting.impl.ColorSetting;
import me.nyan.flush.customhud.setting.impl.ModeSetting;
import me.nyan.flush.customhud.setting.impl.NumberSetting;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Shape extends Component {
    private ModeSetting type, theme;
    private BooleanSetting fill;
    private NumberSetting lineWidth, cornerRadius, saturation, speed, alpha;
    private ColorSetting color;

    @Override
    public void onAdded() {
        settings.add(type = new ModeSetting("Type", "Rectangle", "Rectangle", "RoundRectangle", "Circle"));
        settings.add(fill = new BooleanSetting("Fill", true));
        settings.add(lineWidth = new NumberSetting("Line Width", 2, 0.5, 10, 0.5,
                () -> !fill.getValue()));
        settings.add(cornerRadius = new NumberSetting("Corner Radius", 10, 2, 50, 1,
                () -> type.is("RoundRectangle")));
        settings.add(theme = new ModeSetting("Theme", "Custom", "Rainbow", "Pulsing", "Astolfo", "Custom"));
        settings.add(saturation = new NumberSetting("Color Saturation", 0.8, 0.3, 1, 0.1,
                () -> theme.is("rainbow") || theme.is("astolfo")));
        settings.add(speed = new NumberSetting("Color Speed", 2, 0.4, 3, 0.1,
                () -> theme.is("rainbow")));
        settings.add(color = new ColorSetting("Color", -1,
                () -> theme.is("pulsing") || theme.is("custom")));
        settings.add(alpha = new NumberSetting("Alpha", 255, 0, 255, 1));
    }

    @Override
    public void draw(float x, float y) {
        float scaleY = getResizeType() != Component.ResizeType.CUSTOM ? scaleX : this.scaleY;
        GlStateManager.pushMatrix();
        GlStateManager.scale(1 / scaleX, 1 / scaleY, 0);
        switch (type.getValue().toUpperCase()) {
            case "RECTANGLE":
                if (fill.getValue()) {
                    Gui.drawRect(x * scaleX, y * scaleY, (x + width()) * scaleX, (y + height()) * scaleY, getColor());
                } else {
                    RenderUtils.drawRectangle(x * scaleX, y * scaleY, width() * scaleX, height() * scaleY, getColor(), lineWidth.getValueFloat());
                }
                break;
            case "ROUNDRECTANGLE":
                if (fill.getValue()) {
                    RenderUtils.fillRoundRect(x * scaleX, y * scaleY, width() * scaleX, height() * scaleY, cornerRadius.getValueInt(), getColor());
                } else {
                    RenderUtils.drawRoundRect(x * scaleX, y * scaleY, width() * scaleX, height() * scaleY, cornerRadius.getValueInt(), getColor(), lineWidth.getValueFloat());
                }
                break;
            case "CIRCLE":
                if (fill.getValue()) {
                    RenderUtils.fillCircle((x + width() / 2F) * scaleX, (y + height() / 2F) * scaleY, width() * scaleX / 2F, getColor());
                } else {
                    RenderUtils.drawCircle((x + width() / 2F) * scaleX, (y + height() / 2F) * scaleY, width() * scaleX / 2F, getColor(), lineWidth.getValueFloat());
                }
                break;
            case "TRIANGLE":
                break;
        }
        GlStateManager.popMatrix();
    }

    public int getColor() {
        int c;
        switch (theme.getValue().toLowerCase()) {
            case "custom":
                c = color.getRGB();
                break;
            case "pulsing":
                c = ColorUtils.fade(color.getRGB(), 100, 1);
                break;
            case "astolfo":
                c = ColorUtils.getAstolfo(saturation.getValueFloat(), 3000F * 0.75F, 1);
                break;
            default:
                c = ColorUtils.getRainbow(1, saturation.getValueFloat(), speed.getValueFloat());
        }
        return ColorUtils.alpha(c, alpha.getValueInt());
    }

    @Override
    public int width() {
        return 10;
    }

    @Override
    public int height() {
        return 10;
    }

    @Override
    public ResizeType getResizeType() {
        return type.is("circle") ? ResizeType.NORMAL : ResizeType.CUSTOM;
    }
}
