// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui.buttons;

import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.lenni0451.eventapi.events.IEvent;
import net.augustus.utils.EventHandler;
import net.augustus.events.EventClickSetting;
import net.minecraft.client.gui.GuiScreen;
import net.augustus.clickgui.screens.ColorPickerGui;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import org.lwjgl.input.Keyboard;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.Setting;
import net.augustus.modules.Module;
import java.awt.Color;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.gui.GuiButton;

public class SettingsButton extends GuiButton implements MC
{
    private Color color;
    private Module module;
    private Setting setting;
    private boolean visible;
    private boolean dragging;
    private boolean dropdownVisible;
    private String name;
    private int optionsCounter;
    private final TimeHelper timeHelper;
    
    public SettingsButton(final int id, final int x, final int y, final int width, final int height, final String message, final Color color, final Module module, final Setting setting) {
        super(id, x, y, width, height, message);
        this.dropdownVisible = false;
        this.optionsCounter = 0;
        this.timeHelper = new TimeHelper();
        this.color = color;
        this.module = module;
        this.setting = setting;
        this.name = message;
    }
    
    private int getHoverColor(final Color color, final double addBrightness) {
        int colorRGB;
        if (this.hovered) {
            final float[] hsbColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            float f = (float)(hsbColor[2] + addBrightness);
            if (hsbColor[2] + addBrightness > 1.0) {
                f = 1.0f;
            }
            else if (hsbColor[2] + addBrightness < 0.0) {
                f = 0.0f;
            }
            colorRGB = Color.HSBtoRGB(hsbColor[0], hsbColor[1], f);
        }
        else {
            colorRGB = color.getRGB();
        }
        return colorRGB;
    }
    
    protected int getHoverColor2(final Color color, final double x, final double y, final double width, final double height, final double mouseX, final double mouseY, final double addBrightness) {
        int colorRGB;
        if (this.mouseOver(mouseX, mouseY, x, y, width, height)) {
            final float[] hsbColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            float f = (float)(hsbColor[2] + addBrightness);
            if (hsbColor[2] + addBrightness > 1.0) {
                f = 1.0f;
            }
            else if (hsbColor[2] + addBrightness < 0.0) {
                f = 0.0f;
            }
            colorRGB = Color.HSBtoRGB(hsbColor[0], hsbColor[1], f);
        }
        else {
            colorRGB = color.getRGB();
        }
        return colorRGB;
    }
    
    public boolean mouseOver(final double mouseX, final double mouseY, final double posX, final double posY, final double width, final double height) {
        return mouseX >= posX && mouseX < posX + width && mouseY >= posY && mouseY < posY + height;
    }
    
    public void onKey(final int key) {
        if (this.hovered && (key == 203 || key == 205)) {
            this.timeHelper.reset();
            this.arrow();
        }
    }
    
    public void tick() {
        if (this.hovered && this.timeHelper.reached(500L) && (Keyboard.isKeyDown(203) || Keyboard.isKeyDown(205))) {
            this.arrow();
        }
    }
    
    private void arrow() {
        double add = 0.001;
        if (this.setting instanceof DoubleValue) {
            final DoubleValue doubleValue = (DoubleValue)this.setting;
            if (doubleValue.getDecimalPlaces() == 0) {
                add = 1.0;
            }
            else if (doubleValue.getDecimalPlaces() == 1) {
                add = 0.1;
            }
            else if (doubleValue.getDecimalPlaces() == 2) {
                add = 0.01;
            }
            double var1 = 0.0;
            if (Keyboard.isKeyDown(203)) {
                var1 = doubleValue.getValue() - add;
            }
            else if (Keyboard.isKeyDown(205)) {
                var1 = doubleValue.getValue() + add;
            }
            if (var1 <= doubleValue.getMaxValue() && var1 >= doubleValue.getMinValue() && (Keyboard.isKeyDown(203) || Keyboard.isKeyDown(205))) {
                doubleValue.setValue(var1);
            }
        }
    }
    
    public void click3(final double mouseX, final double mouseY, final int button) {
        if (this.visible && (button == 0 || button == 1)) {
            final boolean bl = this.mousePressed(SettingsButton.mc, (int)mouseX, (int)mouseY);
            if (bl) {
                if (this.setting instanceof BooleanValue) {
                    final BooleanValue booleanValue = (BooleanValue)this.setting;
                    booleanValue.setBoolean(!booleanValue.getBoolean());
                }
                else if (this.setting instanceof DoubleValue) {
                    this.dragging = true;
                }
                else if (this.setting instanceof StringValue || this.setting instanceof BooleansSetting) {
                    this.dropdownVisible = !this.dropdownVisible;
                }
                else if (this.setting instanceof ColorSetting) {
                    SettingsButton.mc.displayGuiScreen(new ColorPickerGui((ColorSetting)this.setting));
                }
                this.playPressSound(SettingsButton.mc.getSoundHandler());
            }
            else if (this.setting instanceof StringValue && this.isDropdownVisible()) {
                final StringValue stringValue = (StringValue)this.setting;
                int i = 1;
                for (final String options : stringValue.getStringList()) {
                    final double dropY = this.yPosition + this.height * i;
                    if (this.mouseOver(mouseX, mouseY, this.xPosition, dropY, this.width, this.height)) {
                        stringValue.setString(options);
                        this.playPressSound(SettingsButton.mc.getSoundHandler());
                    }
                    ++i;
                }
            }
            else if (this.setting instanceof BooleansSetting && this.isDropdownVisible()) {
                final BooleansSetting booleansSetting = (BooleansSetting)this.setting;
                int i = 1;
                for (final Setting s : booleansSetting.getSettingList()) {
                    final double dropY = this.yPosition + this.height * i;
                    if (this.mouseOver(mouseX, mouseY, this.xPosition, dropY, this.width, this.height)) {
                        if (s instanceof BooleanValue) {
                            final BooleanValue booleanValue2 = (BooleanValue)s;
                            booleanValue2.setBoolean(!booleanValue2.getBoolean());
                        }
                        this.playPressSound(SettingsButton.mc.getSoundHandler());
                    }
                    ++i;
                }
            }
            final EventClickSetting eventClickSetting = new EventClickSetting();
            EventHandler.call(eventClickSetting);
        }
    }
    
    public void mouseReleased() {
        this.dragging = false;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (!this.visible) {
            this.hovered = false;
            return;
        }
        this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
        final FontRenderer fontrenderer = mc.fontRendererObj;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        if (this.setting instanceof BooleanValue) {
            this.drawCheckBox(fontrenderer, mouseX, mouseY);
        }
        else if (this.setting instanceof DoubleValue) {
            this.drawSlider(fontrenderer, mouseX, mouseY);
        }
        else if (this.setting instanceof StringValue) {
            this.drawOptions(fontrenderer, mouseX, mouseY);
        }
        else if (this.setting instanceof BooleansSetting) {
            this.drawMultipleOptions(fontrenderer, mouseX, mouseY);
        }
        else if (this.setting instanceof ColorSetting) {
            this.drawColorButton(fontrenderer, mouseX, mouseY);
        }
    }
    
    private void drawCheckBox(final FontRenderer fontRenderer, final int mouseX, final int mouseY) {
        final BooleanValue setting = (BooleanValue)this.setting;
        final Color colorTrue = new Color(69, 118, 161);
        final Color colorFalse = new Color(139, 141, 145);
        int colorRGB;
        if (setting.getBoolean()) {
            colorRGB = this.getHoverColor(colorTrue, 0.2);
        }
        else {
            colorRGB = this.getHoverColor(colorFalse, -0.2);
        }
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.color.getRGB());
        fontRenderer.drawStringWithShadow("x", (float)(this.xPosition + 5), (float)(this.yPosition + this.height / 2 - fontRenderer.FONT_HEIGHT / 2), colorRGB);
        fontRenderer.drawStringWithShadow(this.displayString, (float)(this.xPosition + 11 + fontRenderer.getStringWidth("x")), (float)(this.yPosition + this.height / 2 - fontRenderer.FONT_HEIGHT / 2), colorFalse.getRGB());
    }
    
    private void drawSlider(final FontRenderer fr, final int mouseX, final int mouseY) {
        final DoubleValue setting = (DoubleValue)this.setting;
        final double sliderWidth = this.width - 4;
        final double sliderHeight = 10.0;
        final double sliderX = this.xPosition + 2;
        final double sliderY = this.yPosition + 2;
        final double sliderRight = sliderX + sliderWidth;
        final double sliderBottom = sliderY + sliderHeight;
        final Color backgroundSliderColor = new Color(75, 75, 75, 60);
        final Color sliderColor = new Color(40, 146, 224, 220);
        String currentValue;
        if (setting.getDecimalPlaces() == 0) {
            currentValue = String.valueOf((int)this.round(setting.getValue(), setting.getDecimalPlaces()));
        }
        else {
            currentValue = String.valueOf(this.round(setting.getValue(), setting.getDecimalPlaces()));
        }
        final String text = "" + this.name + ": " + currentValue;
        setting.setValue(this.round(setting.getValue(), setting.getDecimalPlaces()));
        final double percentageOfCurrentValue = (setting.getValue() - setting.getMinValue()) / (setting.getMaxValue() - setting.getMinValue());
        if (this.dragging) {
            final double var1 = setting.getMinValue() + MathHelper.clamp_double((mouseX - sliderX) / sliderWidth, 0.0, 1.0) * (setting.getMaxValue() - setting.getMinValue());
            setting.setValue(var1);
        }
        final int backgroundSliderColorRGB = this.getHoverColor(backgroundSliderColor, 0.20000000298023224);
        final int sliderColorRGB = this.getHoverColor(sliderColor, 0.11999999731779099);
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.color.getRGB());
        final double value1 = sliderX + percentageOfCurrentValue * sliderWidth;
        Gui.drawRect((int)sliderX, (int)sliderY, (int)sliderRight, (int)sliderBottom, backgroundSliderColorRGB);
        Gui.drawRect((int)sliderX, (int)sliderY, (int)value1, (int)sliderBottom, sliderColorRGB);
        this.drawCenteredString(fr, text, (int)((int)sliderX + sliderWidth / 2.0), (int)(sliderY + 1.0), new Color(139, 141, 145).getRGB());
    }
    
    private void drawOptions(final FontRenderer fr, final int mouseX, final int mouseY) {
        final StringValue stringValue = (StringValue)this.setting;
        if (!this.visible) {
            return;
        }
        final Color color = new Color(139, 141, 145);
        final int colorRGB = this.getHoverColor(color, -0.20000000298023224);
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.color.getRGB());
        Gui.drawRect(this.xPosition + 1, this.yPosition + this.height - 3, this.xPosition + this.width - 2, this.yPosition + this.height - 1, new Color(72, 79, 89, 160).getRGB());
        fr.drawStringWithShadow(this.name, (float)(int)(this.xPosition + this.width / 2.0f - fr.getStringWidth(this.name) / 2.0f), (float)(int)(this.yPosition + this.height / 2.0f - fr.FONT_HEIGHT / 2.0f), colorRGB);
        if (this.dropdownVisible) {
            int counter = 1;
            for (final String options : stringValue.getStringList()) {
                final double dropY = this.yPosition + counter * this.height;
                final Color isCurrentString = new Color(69, 118, 161);
                final Color isNotCurrentString = new Color(139, 141, 145);
                int stringColorRGB;
                if (options.equals(stringValue.getSelected())) {
                    stringColorRGB = this.getHoverColor2(isCurrentString, this.xPosition, dropY, this.width, this.height, mouseX, mouseY, 0.20000000298023224);
                }
                else {
                    stringColorRGB = this.getHoverColor2(isNotCurrentString, this.xPosition, dropY, this.width, this.height, mouseX, mouseY, -0.20000000298023224);
                }
                Gui.drawRect(this.xPosition, (int)dropY, this.xPosition + this.width, (int)(dropY + this.height), this.color.getRGB());
                fr.drawStringWithShadow(options, (float)(int)(this.xPosition + this.width / 2.0f - fr.getStringWidth(options) / 2.0f), (float)(int)(dropY + this.height / 2.0f - fr.FONT_HEIGHT / 2.0f), stringColorRGB);
                ++counter;
            }
        }
    }
    
    private void drawMultipleOptions(final FontRenderer fr, final int mouseX, final int mouseY) {
        final BooleansSetting booleansSetting = (BooleansSetting)this.setting;
        if (!this.visible) {
            return;
        }
        final Color color = new Color(139, 141, 145);
        final int colorRGB = this.getHoverColor(color, -0.20000000298023224);
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.color.getRGB());
        Gui.drawRect(this.xPosition + 1, this.yPosition + this.height - 3, this.xPosition + this.width - 2, this.yPosition + this.height - 1, new Color(72, 79, 89, 160).getRGB());
        fr.drawStringWithShadow(this.name, (float)(int)(this.xPosition + this.width / 2.0f - fr.getStringWidth(this.name) / 2.0f), (float)(int)(this.yPosition + this.height / 2.0f - fr.FONT_HEIGHT / 2.0f), colorRGB);
        if (this.dropdownVisible) {
            int counter = 1;
            for (final Setting s : booleansSetting.getSettingList()) {
                if (s == null) {
                    break;
                }
                final double dropY = this.yPosition + counter * this.height;
                final Color isCurrentString = new Color(69, 118, 161);
                final Color isNotCurrentString = new Color(139, 141, 145);
                if (s instanceof BooleanValue) {
                    int stringColorRGB;
                    if (((BooleanValue)s).getBoolean()) {
                        stringColorRGB = this.getHoverColor2(isCurrentString, this.xPosition, dropY, this.width, this.height, mouseX, mouseY, 0.20000000298023224);
                    }
                    else {
                        stringColorRGB = this.getHoverColor2(isNotCurrentString, this.xPosition, dropY, this.width, this.height, mouseX, mouseY, -0.20000000298023224);
                    }
                    Gui.drawRect(this.xPosition, (int)dropY, this.xPosition + this.width, (int)(dropY + this.height), this.color.getRGB());
                    fr.drawStringWithShadow(s.getName(), (float)(int)(this.xPosition + this.width / 2.0f - fr.getStringWidth(s.getName()) / 2.0f), (float)(int)(dropY + this.height / 2.0f - fr.FONT_HEIGHT / 2.0f), stringColorRGB);
                    ++counter;
                }
            }
        }
    }
    
    private void drawColorButton(final FontRenderer fr, final int mouseX, final int mouseY) {
        final ColorSetting setting = (ColorSetting)this.setting;
        Color color = new Color(139, 141, 145);
        if (this.hovered) {
            color = setting.getColor();
        }
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.color.getRGB());
        fr.drawStringWithShadow(this.name, (float)(int)(this.xPosition + this.width / 2.0f - fr.getStringWidth(this.name) / 2.0f), (float)((int)(this.yPosition + this.height / 2.0f - fr.FONT_HEIGHT / 2.0f) + 1), color.getRGB());
    }
    
    private double round(final double value, final int decimalPoints) {
        if (decimalPoints == 0) {
            return (int)value;
        }
        final double d = Math.pow(10.0, decimalPoints);
        return Math.round(value * d) / d;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public void setModule(final Module module) {
        this.module = module;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public Setting getSetting() {
        return this.setting;
    }
    
    public void setSetting(final Setting setting) {
        this.setting = setting;
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public void setDragging(final boolean dragging) {
        this.dragging = dragging;
    }
    
    public boolean isDropdownVisible() {
        return this.dropdownVisible;
    }
    
    public void setDropdownVisible(final boolean dropdownVisible) {
        this.dropdownVisible = dropdownVisible;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getOptionsCounter() {
        return this.optionsCounter;
    }
    
    public void setOptionsCounter(final int optionsCounter) {
        this.optionsCounter = optionsCounter;
    }
}
