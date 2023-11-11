package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.*;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.util.EnumChatFormatting;

public class Text extends Component {
    private GlyphPageFontRenderer fr;
    public TextSetting text;
    private ModeSetting theme;
    private NumberSetting saturation, speed;
    private ColorSetting color;
    private FontSetting font;
    private BooleanSetting fontShadow;

    @Override
    public void onAdded() {
        settings.add(text = new TextSetting(
                "Text",
                 "$clientName&F $clientVersion"
        ));
        settings.add(theme = new ModeSetting("Theme", "Rainbow", "Rainbow", "Pulsing", "Astolfo", "Custom"));
        settings.add(saturation = new NumberSetting("Color Saturation", 0.8, 0.3, 1, 0.1,
                () -> theme.is("rainbow") || theme.is("astolfo")));
        settings.add(speed = new NumberSetting("Color Speed", 2, 0.4, 3, 0.1,
                () -> theme.is("rainbow")));
        settings.add(color = new ColorSetting("Color", 0xFFFF0000,
                () -> theme.is("pulsing") || theme.is("custom")));
        settings.add(font = new FontSetting("Font", "GoogleSansDisplay", 18));
        settings.add(fontShadow = new BooleanSetting("Font Shadow", true));
        fr = Flush.getFont(font.getFont(), font.getSize());
    }

    @Override
    public void draw(float x, float y) {
        if (font.isMinecraftFont()) {
            mc.fontRendererObj.drawString(text.getFormattedValue(), x, y, getColor(), fontShadow.getValue());
            return;
        }
        fr = Flush.getFont(font.getFont(), font.getSize());
        fr.drawString(text.getFormattedValue(), x, y, getColor(), fontShadow.getValue());
    }

    public int getColor() {
        switch (theme.getValue().toLowerCase()) {
            case "custom":
                return color.getRGB();
            case "pulsing":
                return ColorUtils.fade(color.getRGB(), 100, 1);
            case "astolfo":
                return ColorUtils.getAstolfo(saturation.getValueFloat(), 3000F * 0.75F, 1);
            default:
                return ColorUtils.getRainbow(1, saturation.getValueFloat(), speed.getValueFloat());
        }
    }

    @Override
    public int width() {
        if (EnumChatFormatting.getTextWithoutFormattingCodes(text.getFormattedValue()).isEmpty()) {
            return height();
        }
        return font.isMinecraftFont() ? mc.fontRendererObj.getStringWidth(text.getFormattedValue()) : fr.getStringWidth(text.getFormattedValue());
    }

    @Override
    public int height() {
        return font.isMinecraftFont() ? mc.fontRendererObj.FONT_HEIGHT : fr.getFontHeight();
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.NORMAL;
    }
}
