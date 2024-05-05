/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.gui.components.settings;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.LoseBypassColor;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.ColorUtil;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MathUtil;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ColorPicker
extends SettingComponent {
    ColorSetting setting;
    boolean open;
    BSPicker picker = new BSPicker(0, 0, 0, 0);
    HueSlider hSlider = new HueSlider(0, 0, 0, 0);
    ColorSlider rSlider = new ColorSlider(Type.RED, 0, 0, 0, 0);
    ColorSlider gSlider = new ColorSlider(Type.GREEN, 0, 0, 0, 0);
    ColorSlider bSlider = new ColorSlider(Type.BLUE, 0, 0, 0, 0);
    AlphaSlider aSlider = new AlphaSlider(0, 0, 0, 0);
    private final Animation openAnimation;
    boolean alwaysOpen;

    public ColorPicker(ColorSetting setting, boolean alwaysOpen) {
        super(setting);
        this.setting = setting;
        this.alwaysOpen = alwaysOpen;
        if (alwaysOpen) {
            this.open = true;
            LoseBypass.logger.info(setting.getName());
        }
        this.openAnimation = new Animation(alwaysOpen ? 1.0f : 0.0f, 0.015f);
    }

    @Override
    public int drawComponent(int posX, int posY, double deltaTime, int click, int mouseX, int mouseY) {
        int offset = (int)(70.0f * this.openAnimation.value());
        if (!(this.alwaysOpen || click != 0 && click != 1 || mouseX <= posX || mouseX >= posX + 200 || mouseY <= posY || mouseY >= posY + 17)) {
            this.open = !this.open;
        }
        this.openAnimation.update(this.open ? 1.0f : 0.0f, deltaTime);
        LoseBypass.fontManager.drawString(this.setting.getName(), posX + 6, posY + 5, -1);
        GuiUtil.rounded(posX + (this.alwaysOpen ? 178 : 169), posY + 2, posX + 190, posY + 14, ClickGui.background3(), 3);
        GuiUtil.rounded(posX + (this.alwaysOpen ? 180 : 171), posY + 4, posX + (this.alwaysOpen ? 188 : 179), posY + 12, this.setting.getColor().getRGB(), 2, false);
        if (!this.alwaysOpen) {
            GuiUtil.drawCompleteImageRotated(posX + 182, (float)posY + 5.5f, 5.0f, 5.0f, 90.0f * this.openAnimation.value(), ClickGui.arrow, Color.WHITE);
        }
        if (!(this.openAnimation.value() >= 0.1f)) return 17 + offset;
        GuiUtil.glScissor(posX, Math.max(posY + 17, ClickGui.minOffset), 200, ClickGui.minOffset > posY + 17 ? ClickGui.maxOffset : Math.max(ClickGui.maxOffset + ClickGui.minOffset - (posY + 17), 0));
        this.drawPicker(posX, (int)((float)(posY + 17) - 70.0f * (1.0f - this.openAnimation.value())), this.openAnimation.value() >= 0.9f ? click : -1, mouseX, mouseY);
        return 17 + offset;
    }

    @Override
    public boolean visible() {
        return this.setting.isVisible();
    }

    private void drawPicker(int posX, int posY, int click, int mouseX, int mouseY) {
        DataFlavor flavor;
        Clipboard clipboard;
        boolean hoverPaste;
        boolean hoverCopy;
        boolean hoverRainbow;
        this.picker.updatePositionAndSize(posX + 5, posY + 2, 80, 52);
        this.hSlider.updatePositionAndSize(posX + 90, posY + 2, 7, 66);
        this.rSlider.updatePositionAndSize(posX + 110, posY + 2, 85, 8);
        this.gSlider.updatePositionAndSize(posX + 110, posY + 15, 85, 8);
        this.bSlider.updatePositionAndSize(posX + 110, posY + 28, 85, 8);
        this.aSlider.updatePositionAndSize(posX + 110, posY + 41, 85, 8);
        if (click == 0) {
            if (this.picker.mouseOver(mouseX, mouseY)) {
                this.picker.picking = true;
            } else if (this.hSlider.mouseOver(mouseX, mouseY)) {
                this.hSlider.picking = true;
            } else if (this.rSlider.mouseOver(mouseX, mouseY)) {
                this.rSlider.picking = true;
            } else if (this.gSlider.mouseOver(mouseX, mouseY)) {
                this.gSlider.picking = true;
            } else if (this.bSlider.mouseOver(mouseX, mouseY)) {
                this.bSlider.picking = true;
            } else if (this.aSlider.mouseOver(mouseX, mouseY)) {
                this.aSlider.picking = true;
            }
        }
        float hue = this.hSlider.pick(mouseY, Float.valueOf(this.setting.getHue())).floatValue();
        float saturation = this.picker.pickSaturation(mouseX, this.setting.getSaturation(), hue);
        float brightness = this.picker.pickBrightness(mouseY, this.setting.getBrightness(), hue);
        int red = this.rSlider.pick(mouseX, this.setting.getColor().getRed());
        int green = this.gSlider.pick(mouseX, this.setting.getColor().getGreen());
        int blue = this.bSlider.pick(mouseX, this.setting.getColor().getBlue());
        int alpha = this.aSlider.pick(mouseX, this.setting.getColor().getAlpha());
        this.hSlider.updateSliderLocation(Float.valueOf(hue));
        this.picker.updatePickerLocation(saturation, brightness);
        this.rSlider.updateSliderLocation(red);
        this.gSlider.updateSliderLocation(green);
        this.bSlider.updateSliderLocation(blue);
        this.aSlider.updateSliderLocation(alpha);
        this.rSlider.drawSlider(red, green, blue);
        this.gSlider.drawSlider(red, green, blue);
        this.bSlider.drawSlider(red, green, blue);
        this.aSlider.drawSlider(red, green, blue);
        this.hSlider.draw(ColorUtil.fromHSB(hue, 1.0f, 1.0f));
        this.picker.draw(ColorUtil.fromHSB(hue, 1.0f, 1.0f));
        boolean bl = hoverRainbow = mouseX > posX + 142 && mouseX < posX + 195 && mouseY > posY + 56 && mouseY < posY + 68;
        if (hoverRainbow && click == 0) {
            this.setting.setRainbow(this.setting.getRainbow().next());
        }
        GuiUtil.rounded(posX + 100, posY + 56, posX + 195, posY + 68, ClickGui.background3(), 3);
        GuiUtil.setup(hoverRainbow ? ClickGui.background4() : ClickGui.background7());
        GL11.glVertex2d((double)(posX + 142), (double)(posY + 56));
        GL11.glVertex2d((double)(posX + 142), (double)(posY + 68));
        GuiUtil.corner(posX + 192, posY + 65, 3.0, 0, 90);
        GuiUtil.corner(posX + 192, posY + 59, 3.0, 90, 180);
        GuiUtil.finish();
        int length = LoseBypass.fontManager.getTextWidth(this.setting.getRainbow().getName()) / -2;
        LoseBypass.fontManager.drawString("Rainbow:", posX + 105, posY + 59, -1);
        LoseBypass.fontManager.drawString(this.setting.getRainbow().getName(), length += posX + 168, posY + 59, -1);
        GuiUtil.rounded(posX + 5, posY + 56, posX + 85, posY + 68, ClickGui.background3(), 3);
        String colorTag = String.format("#%06x", this.setting.getColor().getRGB() & 0xFFFFFF);
        LoseBypass.fontManager.drawString(colorTag, posX + 10, posY + 59, -1);
        boolean bl2 = hoverCopy = mouseX > posX + 49 && mouseX < posX + 67 && mouseY > posY + 56 && mouseY < posY + 68;
        if (hoverCopy && click == 0) {
            Clipboard clipboard2 = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard2.setContents(new StringSelection(colorTag), null);
        }
        GuiUtil.drawRect(posX + 49, posY + 56, posX + 67, posY + 68, hoverCopy ? ClickGui.background4() : ClickGui.background7());
        GuiUtil.drawCompleteImage(posX + 53, posY + 57, 10.0, 10.0, ClickGui.copy, Color.WHITE);
        boolean bl3 = hoverPaste = mouseX > posX + 67 && mouseX < posX + 85 && mouseY > posY + 56 && mouseY < posY + 68;
        if (hoverPaste && click == 0 && (clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()).isDataFlavorAvailable(flavor = DataFlavor.stringFlavor)) {
            try {
                String text = (String)clipboard.getData(flavor);
                this.setColor(text);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        GuiUtil.setup(hoverPaste ? ClickGui.background4() : ClickGui.background7());
        GL11.glVertex2d((double)(posX + 67), (double)(posY + 56));
        GL11.glVertex2d((double)(posX + 67), (double)(posY + 68));
        GuiUtil.corner(posX + 82, posY + 65, 3.0, 0, 90);
        GuiUtil.corner(posX + 82, posY + 59, 3.0, 90, 180);
        GuiUtil.finish();
        GuiUtil.drawCompleteImage(posX + 71, posY + 57, 10.0, 10.0, ClickGui.paste, Color.WHITE);
    }

    public void setColor(String color) {
        try {
            Color c = Color.decode(color);
            this.setting.setRainbow(LoseBypassColor.Rainbow.OFF);
            this.setting.setColor(c);
            return;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public int getHeight() {
        return (int)(17.0f + 70.0f * this.openAnimation.value());
    }

    public static enum Type {
        RED("R"),
        GREEN("G"),
        BLUE("B"),
        ALPHA("A");

        public final String display;

        private Type(String display) {
            this.display = display;
        }
    }

    public class BSPicker {
        int x;
        int y;
        int pickerX;
        int pickerY;
        int width;
        int height;
        boolean picking;

        public BSPicker(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.pickerX = 0;
            this.pickerY = 0;
            this.picking = false;
        }

        public void draw(Color color) {
            float selectedRed = (float)color.getRed() / 255.0f;
            float selectedGreen = (float)color.getGreen() / 255.0f;
            float selectedBlue = (float)color.getBlue() / 255.0f;
            GuiUtil.drawPickerBase(this.x, this.y, this.x + this.width, this.y + this.height, selectedRed, selectedGreen, selectedBlue);
            GuiUtil.drawRect(this.x + this.pickerX - 2, this.y + this.pickerY - 2, this.x + this.pickerX + 2, this.y + this.pickerY + 2, Color.darkGray.getRGB());
        }

        public float pickSaturation(int mouseX, float original, float hue) {
            if (!this.picking) return original;
            if (!Mouse.isButtonDown((int)0)) {
                this.picking = false;
                return original;
            }
            int mX = mouseX - this.x;
            float saturation = (float)mX / (float)this.width;
            saturation = MathUtil.clamp(saturation, 0.0f, 1.0f);
            ColorPicker.this.setting.setColor(ColorUtil.fromHSB(hue, saturation, ColorPicker.this.setting.getBrightness()));
            return saturation;
        }

        public float pickBrightness(int mouseY, float original, float hue) {
            if (!this.picking) return original;
            if (!Mouse.isButtonDown((int)0)) {
                this.picking = false;
                return original;
            }
            int mY = mouseY - this.y;
            float brightness = (float)mY / (float)this.height;
            brightness = MathUtil.clamp(1.0f - brightness, 0.0f, 1.0f);
            ColorPicker.this.setting.setColor(ColorUtil.fromHSB(hue, ColorPicker.this.setting.getSaturation(), brightness));
            return brightness;
        }

        public void updatePickerLocation(float saturation, float brightness) {
            this.pickerX = (int)((float)this.width * saturation);
            this.pickerY = (int)((float)this.height * (1.0f - brightness));
        }

        public void updatePositionAndSize(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean mouseOver(int mouseX, int mouseY) {
            if (mouseX < this.x) return false;
            if (mouseY < this.y) return false;
            if (mouseX > this.x + this.width) return false;
            if (mouseY > this.y + this.height) return false;
            return true;
        }
    }

    public class HueSlider
    extends Slider<Float> {
        public HueSlider(int x, int y, int width, int height) {
            super("", x, y, width, height);
        }

        @Override
        public void updateSliderLocation(Float hue) {
            this.sliderOffset = (int)((float)this.height * hue.floatValue());
        }

        @Override
        public Float pick(int mouseY, Float original) {
            if (!this.picking) return original;
            if (!Mouse.isButtonDown((int)0)) {
                this.picking = false;
                return original;
            }
            int mY = mouseY - this.y;
            float hue = (float)mY / (float)this.height;
            hue = MathUtil.clamp(hue, 0.0f, 0.999f);
            ColorPicker.this.setting.setColor(ColorUtil.fromHSB(hue, ColorPicker.this.setting.getSaturation(), ColorPicker.this.setting.getBrightness()));
            return Float.valueOf(hue);
        }

        public void draw(Color color) {
            int step = 0;
            int colorIndex = 0;
            while (true) {
                if (colorIndex >= 6) {
                    GuiUtil.drawRect(this.x - 1, this.y + this.sliderOffset - 2, this.x + this.width + 1, this.y + this.sliderOffset + 2, Color.darkGray.getRGB());
                    GuiUtil.drawRect(this.x, this.y + this.sliderOffset - 1, this.x + this.width, this.y + this.sliderOffset + 1, color.getRGB());
                    return;
                }
                int previousStep = Color.HSBtoRGB((float)step / 6.0f, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float)(step + 1) / 6.0f, 1.0f, 1.0f);
                GuiUtil.drawGradientRect(this.x, this.y + step * (this.height / 6), this.x + this.width, this.y + (step + 1) * (this.height / 6), previousStep, nextStep, previousStep, nextStep, 0);
                ++step;
                ++colorIndex;
            }
        }
    }

    public class AlphaSlider
    extends RGBASlider {
        public AlphaSlider(int x, int y, int width, int height) {
            super(Type.ALPHA, x, y, width, height);
        }

        public void drawSlider(int r, int g, int b) {
            boolean left = true;
            int checkerBoardSquareSize = this.height / 2;
            LoseBypass.fontManager.drawString("A", this.x - 9, this.y + 1, -1);
            int squareIndex = -checkerBoardSquareSize;
            while (true) {
                if (squareIndex >= this.width) {
                    GuiUtil.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(r, g, b, 0).getRGB(), new Color(r, g, b, 0).getRGB(), new Color(r, g, b, 255).getRGB(), new Color(r, g, b, 255).getRGB(), 0);
                    GuiUtil.drawRect(this.x + this.sliderOffset - 1, this.y, this.x + this.sliderOffset + 1, this.y + this.height, Color.darkGray.getRGB());
                    return;
                }
                if (!left) {
                    int minX = this.x + squareIndex;
                    int maxX = Math.min(this.x + this.width, this.x + squareIndex + checkerBoardSquareSize);
                    GuiUtil.drawRect(minX, this.y, maxX, this.y + this.height, new Color(0x2A2A2A).getRGB());
                    GuiUtil.drawRect(minX, this.y + checkerBoardSquareSize, maxX, this.y + this.height, new Color(0x595959).getRGB());
                    if (squareIndex < this.width - checkerBoardSquareSize) {
                        minX = this.x + squareIndex + checkerBoardSquareSize;
                        maxX = Math.min(this.x + this.width, this.x + squareIndex + checkerBoardSquareSize * 2);
                        GuiUtil.drawRect(minX, this.y, maxX, this.y + this.height, new Color(0x595959).getRGB());
                        GuiUtil.drawRect(minX, this.y + checkerBoardSquareSize, maxX, this.y + this.height, new Color(0x2A2A2A).getRGB());
                    }
                }
                left = !left;
                squareIndex += checkerBoardSquareSize;
            }
        }
    }

    public class ColorSlider
    extends RGBASlider {
        public ColorSlider(Type type, int x, int y, int width, int height) {
            super(type, x, y, width, height);
        }

        public void drawSlider(int red, int green, int blue) {
            int r1 = red;
            int r2 = red;
            int g1 = green;
            int g2 = green;
            int b1 = blue;
            int b2 = blue;
            switch (this.type) {
                case RED: {
                    r1 = 0;
                    r2 = 255;
                    break;
                }
                case GREEN: {
                    g1 = 0;
                    g2 = 255;
                    break;
                }
                case BLUE: {
                    b1 = 0;
                    b2 = 255;
                    break;
                }
            }
            LoseBypass.fontManager.drawString(this.displayString, this.x - 9, this.y + 1, -1);
            GuiUtil.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(r1, g1, b1).getRGB(), new Color(r1, g1, b1).getRGB(), new Color(r2, g2, b2).getRGB(), new Color(r2, g2, b2).getRGB(), 0);
            GuiUtil.drawRect(this.x + this.sliderOffset - 2, this.y - 1, this.x + this.sliderOffset + 2, this.y + this.height + 1, Color.darkGray.getRGB());
            GuiUtil.drawRect(this.x + this.sliderOffset - 1, this.y, this.x + this.sliderOffset + 1, this.y + this.height, new Color(red, green, blue).getRGB());
        }
    }

    public class RGBASlider
    extends Slider<Integer> {
        Type type;

        public RGBASlider(Type type, int x, int y, int width, int height) {
            super(type.display, x, y, width, height);
            this.type = type;
        }

        @Override
        public void updateSliderLocation(Integer color) {
            this.sliderOffset = (int)((float)(this.width * color) / 255.0f);
        }

        @Override
        public Integer pick(int mouseX, Integer original) {
            if (!this.picking) return original;
            if (!Mouse.isButtonDown((int)0)) {
                this.picking = false;
                return original;
            }
            int mX = mouseX - this.x;
            float percent = (float)mX / (float)this.width;
            int value = (int)MathUtil.clamp(percent * 255.0f, 0.0f, 255.0f);
            switch (1.$SwitchMap$com$wallhacks$losebypass$gui$components$settings$ColorPicker$Type[this.type.ordinal()]) {
                case 1: {
                    ColorPicker.this.setting.setColor(new Color(value, ColorPicker.this.setting.getColor().getGreen(), ColorPicker.this.setting.getColor().getBlue(), ColorPicker.this.setting.getColor().getAlpha()));
                    return value;
                }
                case 2: {
                    ColorPicker.this.setting.setColor(new Color(ColorPicker.this.setting.getColor().getRed(), value, ColorPicker.this.setting.getColor().getBlue(), ColorPicker.this.setting.getColor().getAlpha()));
                    return value;
                }
                case 3: {
                    ColorPicker.this.setting.setColor(new Color(ColorPicker.this.setting.getColor().getRed(), ColorPicker.this.setting.getColor().getGreen(), value, ColorPicker.this.setting.getColor().getAlpha()));
                    return value;
                }
                case 4: {
                    ColorPicker.this.setting.setColor(new Color(ColorPicker.this.setting.getColor().getRed(), ColorPicker.this.setting.getColor().getGreen(), ColorPicker.this.setting.getColor().getBlue(), value));
                    return value;
                }
            }
            return value;
        }
    }

    public static abstract class Slider<T> {
        String displayString;
        public boolean picking;
        int x;
        int y;
        int width;
        int height;
        int sliderOffset;

        public Slider(String diplayString, int x, int y, int width, int height) {
            this.displayString = diplayString;
            this.picking = false;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.sliderOffset = 0;
        }

        public abstract void updateSliderLocation(T var1);

        public abstract T pick(int var1, T var2);

        public void updatePositionAndSize(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean mouseOver(int mouseX, int mouseY) {
            if (mouseX < this.x) return false;
            if (mouseY < this.y) return false;
            if (mouseX > this.x + this.width) return false;
            if (mouseY > this.y + this.height) return false;
            return true;
        }
    }
}

