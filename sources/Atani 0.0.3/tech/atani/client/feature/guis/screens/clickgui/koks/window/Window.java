package tech.atani.client.feature.guis.screens.clickgui.koks.window;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Window extends GuiScreen{

    private final Module module;
    private final GuiScreen prevScreen;
    private final ArrayList<Value> expanded = new ArrayList<>();

    public Window(Module module, GuiScreen prevScreen) {
        this.module = module;
        this.prevScreen = prevScreen;
    }

    float scroll;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        scroll += Mouse.getDWheel() / 10F;
        scroll = Math.min(scroll, 0);
        float x = super.width / 2 - 170, y = super.height / 2 - 230, width = 340, height = 460;
        RoundedShader.drawRound(x, y, width, height, 10, new Color(21, 23, 24, 255));
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Roboto", 18);
        float valueY = y + 20 + scroll;
        float valueX = x + 20;
        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(x, y, width, height);
        for(Value value : ValueStorage.getInstance().getValues(module)) {
            if(!value.isVisible())
                continue;
            if(value instanceof CheckBoxValue) {
                CheckBoxValue checkBoxValue = (CheckBoxValue)value;
                fontRenderer.drawStringWithShadow(value.getName(), valueX, valueY, -1);
                RoundedShader.drawRound(valueX + width - 40 - 20, valueY - 1, 20, 10, 5, checkBoxValue.isEnabled() ? new Color(5, 160, 27) : new Color(105, 105, 105));
                if(checkBoxValue.getValue()) {
                    RoundedShader.drawRound(valueX + width - 40 - 10 - 1, valueY - 1 - 1, 12, 12, 5, new Color(14, 223, 39));
                } else {
                    RoundedShader.drawRound(valueX + width - 40 - 20 - 1, valueY - 1 - 1, 12, 12, 5, new Color(151, 153, 154));
                }
                valueY += fontRenderer.FONT_HEIGHT + 8;
            } else if(value instanceof MultiStringBoxValue) {
                MultiStringBoxValue multiStringBoxValue = (MultiStringBoxValue)value;
                float length = fontRenderer.getStringWidthInt(multiStringBoxValue.getValue().size() + " Enabled");
                fontRenderer.drawStringWithShadow(value.getName(), valueX, valueY, -1);
                if(this.expanded.contains(multiStringBoxValue)) {
                    RoundedShader.drawRound(valueX + width - 50 - length, valueY - 1, length + 10, 15 + multiStringBoxValue.getValues().length * 15, 3, new Color(31, 33, 35));
                    float stringsY = valueY - 1 + 15;
                    for(String string : multiStringBoxValue.getValues()) {
                        fontRenderer.drawStringWithShadow(string, valueX + width - 50 - length + 5.5f, stringsY + 3, multiStringBoxValue.getValue().contains(string) ? new Color(14, 223, 39).getRGB() : -1);
                        stringsY += 15;
                    }
                    fontRenderer.drawStringWithShadow((multiStringBoxValue.getValue().size() - 1) + " Enabled", valueX + width - 50 - length + 5.5f, valueY + 3, -1);
                } else {
                    RoundedShader.drawRound(valueX + width - 50 - length, valueY - 1, length + 10, 15, 3, new Color(31, 33, 35));
                    fontRenderer.drawStringWithShadow((multiStringBoxValue.getValue().size() - 1) + " Enabled", valueX + width - 50 - length + 5.5f, valueY + 3, -1);
                }
                valueY += fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(multiStringBoxValue) ? multiStringBoxValue.getValues().length * 15 : 0);
            } else if(value instanceof StringBoxValue) {
                StringBoxValue stringBoxValue = (StringBoxValue)value;
                float length = fontRenderer.getStringWidthInt(stringBoxValue.getValue());
                fontRenderer.drawStringWithShadow(value.getName(), valueX, valueY, -1);
                if(this.expanded.contains(stringBoxValue)) {
                    String longest = "";
                    for(String string : stringBoxValue.getValues()) {
                        if(fontRenderer.getStringWidthInt(string) > fontRenderer.getStringWidthInt(longest))
                            longest = string;
                    }
                    float longestLength = fontRenderer.getStringWidthInt(longest);
                    RoundedShader.drawRound(valueX + width - 50 - longestLength, valueY - 1, longestLength + 10, 15 + stringBoxValue.getValues().length * 15, 3, new Color(31, 33, 35));
                    float stringsY = valueY - 1 + 15;
                    for(String string : stringBoxValue.getValues()) {
                        fontRenderer.drawStringWithShadow(string, valueX + width - 50 - longestLength + 5.5f, stringsY + 3, -1);
                        stringsY += 15;
                    }
                    fontRenderer.drawStringWithShadow(stringBoxValue.getValue(), valueX + width - 50 - length + 5.5f, valueY + 3, -1);
                } else {
                    RoundedShader.drawRound(valueX + width - 50 - length, valueY - 1, length + 10, 15, 3, new Color(31, 33, 35));
                    fontRenderer.drawStringWithShadow(stringBoxValue.getValue(), valueX + width - 50 - length + 5.5f, valueY + 3, -1);
                }
                valueY += fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(stringBoxValue) ? stringBoxValue.getValues().length * 15 : 0);
            } else if(value instanceof SliderValue) {
                SliderValue sliderValue = (SliderValue) value;
                fontRenderer.drawStringWithShadow(value.getName(), valueX, valueY, -1);
                fontRenderer.drawStringWithShadow(sliderValue.getValue().floatValue() + "", valueX + width - fontRenderer.getStringWidthInt(sliderValue.getValue().floatValue() + "") - 40, valueY, -1);
                RenderUtil.drawRect(valueX + 3, valueY + fontRenderer.FONT_HEIGHT + 2, 160, 2.5f, new Color(21, 23, 24, 255).darker().getRGB());
                float sliderX = valueX + 3, sliderY = valueY + fontRenderer.FONT_HEIGHT + 2, sliderWidth = 160, sliderHeight = 2.5f;
                float length = MathHelper
                        .floor_double(((sliderValue.getValue()).floatValue() - sliderValue.getMinimum().floatValue())
                                / (sliderValue.getMaximum().floatValue() - sliderValue.getMinimum().floatValue()) * sliderWidth);
                RenderUtil.drawRect(sliderX, sliderY, length, sliderHeight, new Color(14, 223, 39).getRGB());
                if(Mouse.isButtonDown(0) && RenderUtil.isHovered(mouseX, mouseY, sliderX, sliderY, sliderWidth, sliderHeight)) {
                    double min1 = sliderValue.getMinimum().floatValue();
                    double max1 = sliderValue.getMaximum().floatValue();
                    double newValue = MathUtil.round((mouseX - sliderX) * (max1 - min1) / (sliderWidth - 1.0f) + min1, sliderValue.getDecimalPlaces());
                    sliderValue.setValue(newValue);
                }
                valueY += fontRenderer.FONT_HEIGHT + 13;
            }
        }
        RenderUtil.endScissorBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        float x = super.width / 2 - 170, y = super.height / 2 - 230, width = 340, height = 460;
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Roboto", 18);
        float valueY = y + 20 + scroll;
        float valueX = x + 20;
        for(Value value : ValueStorage.getInstance().getValues(module)) {
            if(!value.isVisible())
                continue;
            if(value instanceof CheckBoxValue) {
                if (RenderUtil.isHovered(mouseX, mouseY, valueX + width - 40 - 20, valueY - 1, 20, 10)) {
                    CheckBoxValue checkBoxValue = (CheckBoxValue) value;
                    checkBoxValue.setValue(!checkBoxValue.getValue());
                }
                valueY += fontRenderer.FONT_HEIGHT + 8;
            } else if(value instanceof StringBoxValue) {
                StringBoxValue stringBoxValue = (StringBoxValue)value;
                float length = fontRenderer.getStringWidthInt(stringBoxValue.getValue());
                if(this.expanded.contains(stringBoxValue)) {
                    String longest = "";
                    for(String string : stringBoxValue.getValues()) {
                        if(fontRenderer.getStringWidthInt(string) > fontRenderer.getStringWidthInt(longest))
                            longest = string;
                    }
                    float longestLength = fontRenderer.getStringWidthInt(longest);
                    if(RenderUtil.isHovered(mouseX, mouseY, valueX + width - 50 - longestLength, valueY - 1, longestLength + 10, 15 + stringBoxValue.getValues().length * 15)) {
                        this.expanded.remove(stringBoxValue);
                    }
                    float stringsY = valueY - 1 + 15;
                    for(String string : stringBoxValue.getValues()) {
                        if(RenderUtil.isHovered(mouseX, mouseY, valueX + width - 50 - longestLength, stringsY, longestLength + 10, 13)) {
                            stringBoxValue.setValue(string);
                        }
                        stringsY += 15;
                    }
                } else {
                    if (RenderUtil.isHovered(mouseX, mouseY, valueX + width - 50 - length, valueY - 1, length + 10, 15)) {
                        this.expanded.add(stringBoxValue);
                    }
                }
                valueY += fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(stringBoxValue) ? stringBoxValue.getValues().length * 15 : 0);
            } else if(value instanceof MultiStringBoxValue) {
                MultiStringBoxValue multiStringBoxValue = (MultiStringBoxValue)value;
                float length = fontRenderer.getStringWidthInt(multiStringBoxValue.getValue().size() + " Enabled");
                if(this.expanded.contains(multiStringBoxValue)) {
                    if(RenderUtil.isHovered(mouseX, mouseY, valueX + width - 50 - length, valueY - 1, length + 10, 15 + multiStringBoxValue.getValues().length * 15)) {
                        this.expanded.remove(multiStringBoxValue);
                    }
                    float stringsY = valueY - 1 + 15;
                    for(String string : multiStringBoxValue.getValues()) {
                        if(RenderUtil.isHovered(mouseX, mouseY, valueX + width - 50 - length, stringsY, length + 10, 13)) {
                            if(multiStringBoxValue.getValue().contains(string)) {
                                multiStringBoxValue.getValue().remove(string);
                            } else {
                                multiStringBoxValue.getValue().add(string);
                            }
                        }
                        stringsY += 15;
                    }
                } else {
                    if (RenderUtil.isHovered(mouseX, mouseY, valueX + width - 50 - length, valueY - 1, length + 10, 15)) {
                        this.expanded.add(multiStringBoxValue);
                    }
                }
                valueY += fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(multiStringBoxValue) ? multiStringBoxValue.getValues().length * 15 : 0);
            } else if(value instanceof SliderValue) {
                valueY += fontRenderer.FONT_HEIGHT + 13;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char key, int code) {
        if (code == 1) {
            mc.displayGuiScreen(prevScreen);
        }
    }

}
