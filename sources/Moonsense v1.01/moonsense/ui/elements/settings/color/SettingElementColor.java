// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings.color;

import moonsense.utils.ColorObject;
import net.minecraft.client.Minecraft;
import moonsense.features.ThemeSettings;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.ui.elements.Element;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;
import moonsense.ui.elements.button.GuiButtonIcon;
import moonsense.ui.elements.Slider;
import moonsense.ui.elements.Checkbox;
import moonsense.ui.elements.settings.SettingElement;

public class SettingElementColor extends SettingElement
{
    public int color;
    public int alpha;
    public final Checkbox chroma;
    public final Slider chromaSpeed;
    public final ColorPane colorPane;
    public final AlphaPane alphaPane;
    public final HuePane huePane;
    public final GuiButtonIcon copyButton;
    public final GuiButtonIcon pasteButton;
    private final Consumer update;
    public boolean expanded;
    
    public SettingElementColor(final int x, final int y, final int width, final int height, final int xOffset, final int yOffset, final Setting setting, final Consumer update, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, xOffset, yOffset, setting, consumer, parent);
        this.expanded = false;
        this.front = true;
        this.chroma = new Checkbox(this.getX() - 132 - 50, this.getY(), 10, 10, "Chroma", setting.getColorObject().isChroma(), null);
        this.chromaSpeed = new Slider(this.getX() - 132 - 70 - 28, this.getY() + 12, 80, 5, 0.0f, 80.0f, 1.0f, (float)setting.getColorObject().getChromaSpeed(), "Speed", null, parent);
        this.colorPane = new ColorPane(this.getX() - 28 - 4 - 100, this.getY(), 100, 48);
        this.alphaPane = new AlphaPane(this.getX() - 28, this.getY(), 10, 48);
        this.huePane = new HuePane(this.getX() - 14, this.getY(), 10, 48);
        this.update = update;
        this.color = new Color(setting.getColor(), true).getRGB();
        final int[] hsv = GuiUtils.rgbToHsv(this.color & 0xFFFFFF);
        this.huePane.hue = ((hsv[0] == -1) ? 0 : hsv[0]);
        this.colorPane.saturation = hsv[1];
        this.colorPane.value = hsv[2];
        this.alphaPane.alpha = GuiUtils.getAlpha(this.color);
        this.copyButton = new GuiButtonIcon(100, this.getX(), this.getY() + 15, 12, 12, "copy.png", "", false);
        this.pasteButton = new GuiButtonIcon(101, this.getX() + 15, this.getY() + 15, 12, 12, "paste.png", "", false);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        this.chroma.y = this.getY();
        this.chromaSpeed.y = this.getY() + 12;
        this.colorPane.y = this.getY();
        this.alphaPane.y = this.getY();
        this.huePane.y = this.getY();
        this.color = GuiUtils.hsvToRgb(this.huePane.hue, this.colorPane.saturation, this.colorPane.value);
        final int alpha = this.alphaPane.alpha;
        MoonsenseClient.titleRenderer.drawString(String.format("#%02x%02x%02x%02x", alpha, GuiUtils.getColor(this.color).getRed(), GuiUtils.getColor(this.color).getGreen(), GuiUtils.getColor(this.color).getBlue()), this.getX() + 13, this.getY(), ThemeSettings.INSTANCE.generalTextColor.getColor());
        this.alpha = this.alphaPane.alpha;
        if (this.expanded) {
            this.chroma.render(this.mouseX, this.mouseY, partialTicks);
            this.chromaSpeed.render(this.mouseX, this.mouseY, partialTicks);
            this.colorPane.renderPane(this.huePane.hue, this.mouseX, this.mouseY);
            this.alphaPane.renderPane(this.color, this.mouseX, this.mouseY);
            this.huePane.renderPane(this.mouseX, this.mouseY);
            this.consumer.accept(this.setting, this);
        }
        if (this.expanded) {
            this.copyButton.xPosition = this.getX();
            this.copyButton.yPosition = this.getY() + 15;
            this.copyButton.drawButton(this.mc, this.mouseX, this.mouseY);
            this.pasteButton.xPosition = this.getX() + 15;
            this.pasteButton.yPosition = this.getY() + 15;
            this.pasteButton.drawButton(this.mc, this.mouseX, this.mouseY);
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.chroma.mouseClicked(mouseX, mouseY, mouseButton);
        this.chromaSpeed.mouseClicked(mouseX, mouseY, mouseButton);
        this.colorPane.mouseClicked(mouseX, mouseY, mouseButton);
        this.alphaPane.mouseClicked(mouseX, mouseY, mouseButton);
        this.huePane.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.enabled && this.hovered) {
            this.expanded = !this.expanded;
            this.update.apply(this, this.expanded);
        }
        if (this.copyButton.isMouseOver()) {
            this.copyButton.playPressSound(Minecraft.getMinecraft().getSoundHandler());
            this.setting.getColorObject().setAlpha(this.alphaPane.alpha);
            MoonsenseClient.INSTANCE.clipboardColor = this.setting.getColorObject();
            System.out.println(this.setting.getColorObject().getAlpha());
        }
        if (this.pasteButton.isMouseOver()) {
            this.copyButton.playPressSound(Minecraft.getMinecraft().getSoundHandler());
            if (MoonsenseClient.INSTANCE.clipboardColor != null) {
                final ColorObject c = MoonsenseClient.INSTANCE.clipboardColor;
                final int[] hsv = GuiUtils.rgbToHsv(new Color(c.getColor()).getRGB());
                this.colorPane.saturation = hsv[1];
                this.colorPane.value = hsv[2];
                this.alphaPane.alpha = c.getAlpha();
                this.huePane.hue = hsv[0];
                this.chroma.active = c.isChroma();
                this.chromaSpeed.sliderValue = c.getChromaSpeed() / this.chromaSpeed.max;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    private int getDecimal(String hex) {
        hex = hex.replace("#", "");
        final String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); ++i) {
            final char c = hex.charAt(i);
            final int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }
    
    public static Color getColor(String hex) {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6: {
                return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16), Integer.valueOf(hex.substring(4, 6), 16));
            }
            case 8: {
                return new Color(Integer.valueOf(hex.substring(2, 4), 16), Integer.valueOf(hex.substring(4, 6), 16), Integer.valueOf(hex.substring(6, 8), 16), Integer.valueOf(hex.substring(0, 2), 16));
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.chroma.mouseReleased(mouseX, mouseY, state);
        this.chromaSpeed.mouseReleased(mouseX, mouseY, state);
        this.colorPane.mouseReleased();
        this.alphaPane.mouseReleased();
        this.huePane.mouseReleased();
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 1.0f, this.setting.getColor());
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 1.0f, 2.0f, new Color(50, 50, 50, 200).getRGB());
    }
    
    @FunctionalInterface
    public interface Consumer
    {
        void apply(final SettingElementColor p0, final Boolean p1);
    }
}
