package tech.atani.client.feature.guis.screens.clickgui.tarasande.window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.guis.screens.clickgui.tarasande.TarasandeClickGuiScreen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.utility.interfaces.ColorPalette;
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.utility.render.particle.Particle;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Window extends GuiScreen implements ColorPalette {

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
        TarasandeClickGuiScreen tarasandeClickGuiScreen = (TarasandeClickGuiScreen) prevScreen;
        RenderableShaders.renderAndRun(false, true, () -> {
            RenderUtil.drawRect(0, 0, super.width, super.height, ColorUtil.setAlpha(new Color(TARASANDE), (float) (tarasandeClickGuiScreen.openingAnimation.getOutput() * 0.6f)).getRGB());
        });
        int numPoints = 100;
        if (tarasandeClickGuiScreen.particles.size() < numPoints)
            tarasandeClickGuiScreen.particles.add(new Particle(width / 2.0, height / 2.0, new ScaledResolution(mc)));
        for(Particle particle : tarasandeClickGuiScreen.particles) {
            particle.render(mouseX, mouseY, tarasandeClickGuiScreen.openingAnimation.getOutput());
        }
        scroll += Mouse.getDWheel() / 10F;
        scroll = Math.min(scroll, 0);
        float x = super.width / 2 - 170, y = super.height / 2 - 230, width = 340, height = 460;
        Color tarasande = new Color(TARASANDE);
        float tabHeight = (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2);
        RenderUtil.drawRect(x, y, width, height, ColorUtil.setAlpha(tarasande, 180).getRGB());
        RenderUtil.drawRect(x, y, width, tabHeight, TARASANDE);
        mc.fontRendererObj.drawStringWithShadow(module.getName(), x + 0.5f, y + 0.5f, -1);
        FontRenderer fontRenderer = mc.fontRendererObj;
        float valueY = y + 20 + scroll + 5;
        float valueX = x + 20;
        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(x, y + tabHeight, width, height - tabHeight);
        for(Value value : ValueStorage.getInstance().getValues(module)) {
            if(!value.isVisible())
                continue;
            if(value instanceof CheckBoxValue) {
                RenderUtil.drawRect(valueX - 2, valueY - 3, width - 40 + 4, fontRenderer.FONT_HEIGHT + 8, new Color(0, 0, 0, 100).getRGB());
                CheckBoxValue checkBoxValue = (CheckBoxValue)value;
                RenderUtil.scaleStart(valueX + 1, valueY - 1, 0.7f);
                fontRenderer.drawStringWithShadow(value.getName(), valueX + 1, valueY - 1, -1);
                fontRenderer.drawStringWithShadow(value.getDescription(), valueX + 1, valueY + fontRenderer.FONT_HEIGHT + 1 - 1, new Color(180, 180, 180).getRGB());
                RenderUtil.drawRect(valueX + width + 80 - fontRenderer.getStringWidthInt(checkBoxValue.isEnabled() + ""), valueY + 3.5f - 3, fontRenderer.getStringWidthInt(checkBoxValue.isEnabled() + "") + 6, 14, new Color(0, 0, 0, 80).getRGB());
                fontRenderer.drawStringWithShadow(checkBoxValue.isEnabled() + "", valueX + width + 80 - fontRenderer.getStringWidthInt(checkBoxValue.isEnabled() + "") + 3.5f, valueY + 3.5f, -1);
                RenderUtil.scaleEnd();
                valueY += fontRenderer.FONT_HEIGHT + 10;
            } else if(value instanceof MultiStringBoxValue) {
                MultiStringBoxValue multiStringBoxValue = (MultiStringBoxValue)value;
                RenderUtil.drawRect(valueX - 2, valueY - 3, width - 40 + 4, (fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(multiStringBoxValue) ? multiStringBoxValue.getValues().length * 15 : 0)) - 2, new Color(0, 0, 0, 100).getRGB());
                float length = fontRenderer.getStringWidthInt(multiStringBoxValue.getValue().size() + " Enabled");
                RenderUtil.scaleStart(valueX + 1, valueY - 1, 0.7f);
                fontRenderer.drawStringWithShadow(value.getName(), valueX + 1, valueY - 1, -1);
                fontRenderer.drawStringWithShadow(value.getDescription(), valueX + 1, valueY + fontRenderer.FONT_HEIGHT + 1 - 1, new Color(180, 180, 180).getRGB());
                RenderUtil.scaleEnd();
                if(this.expanded.contains(multiStringBoxValue)) {
                    RenderUtil.drawRect(valueX + width - 50 - length, valueY - 1, length + 10, 15 + multiStringBoxValue.getValues().length * 15, new Color(0, 0, 0, 80).getRGB());
                    float stringsY = valueY - 1 + 15;
                    for(String string : multiStringBoxValue.getValues()) {
                        fontRenderer.drawStringWithShadow(string, valueX + width - 50 - length + 5.5f, stringsY + 3, multiStringBoxValue.getValue().contains(string) ? TARASANDE : -1);
                        stringsY += 15;
                    }
                    fontRenderer.drawStringWithShadow((multiStringBoxValue.getValue().size() - 1) + " Enabled", valueX + width - 50 - length + 5.5f, valueY + 3, -1);
                } else {
                    RenderUtil.drawRect(valueX + width - 50 - length, valueY - 1, length + 10, 15, new Color(0, 0, 0, 80).getRGB());
                    fontRenderer.drawStringWithShadow((multiStringBoxValue.getValue().size() - 1) + " Enabled", valueX + width - 50 - length + 5.5f, valueY + 3, -1);
                }
                valueY += fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(multiStringBoxValue) ? multiStringBoxValue.getValues().length * 15 : 0);
            } else if(value instanceof StringBoxValue) {
                StringBoxValue stringBoxValue = (StringBoxValue)value;
                RenderUtil.drawRect(valueX - 2, valueY - 3, width - 40 + 4, (fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(stringBoxValue) ? stringBoxValue.getValues().length * 15 : 0)) - 2, new Color(0, 0, 0, 100).getRGB());
                float length = fontRenderer.getStringWidthInt(stringBoxValue.getValue());
                RenderUtil.scaleStart(valueX + 1, valueY - 1, 0.7f);
                fontRenderer.drawStringWithShadow(value.getName(), valueX + 1, valueY - 1, -1);
                fontRenderer.drawStringWithShadow(value.getDescription(), valueX + 1, valueY + fontRenderer.FONT_HEIGHT + 1 - 1, new Color(180, 180, 180).getRGB());
                RenderUtil.scaleEnd();
                if(this.expanded.contains(stringBoxValue)) {
                    String longest = "";
                    for(String string : stringBoxValue.getValues()) {
                        if(fontRenderer.getStringWidthInt(string) > fontRenderer.getStringWidthInt(longest))
                            longest = string;
                    }
                    float longestLength = fontRenderer.getStringWidthInt(longest);
                    RenderUtil.drawRect(valueX + width - 50 - longestLength, valueY - 1, longestLength + 10, 15 + stringBoxValue.getValues().length * 15, new Color(0, 0, 0, 80).getRGB());
                    float stringsY = valueY - 1 + 15;
                    for(String string : stringBoxValue.getValues()) {
                        fontRenderer.drawStringWithShadow(string, valueX + width - 50 - longestLength + 5.5f, stringsY + 3, -1);
                        stringsY += 15;
                    }
                    fontRenderer.drawStringWithShadow(stringBoxValue.getValue(), valueX + width - 50 - length + 5.5f, valueY + 3, -1);
                } else {
                    RenderUtil.drawRect(valueX + width - 50 - length, valueY - 1, length + 10, 15, new Color(0, 0, 0, 80).getRGB());
                    fontRenderer.drawStringWithShadow(stringBoxValue.getValue(), valueX + width - 50 - length + 5.5f, valueY + 2.5f, -1);
                }
                valueY += fontRenderer.FONT_HEIGHT + 13 + (this.expanded.contains(stringBoxValue) ? stringBoxValue.getValues().length * 15 : 0);
            } else if(value instanceof SliderValue) {
                RenderUtil.drawRect(valueX - 2, valueY - 3, width - 40 + 4, fontRenderer.FONT_HEIGHT + 7 + 6, new Color(0, 0, 0, 100).getRGB());
                SliderValue sliderValue = (SliderValue) value;
                RenderUtil.scaleStart(valueX + 1, valueY - 1, 0.7f);
                fontRenderer.drawStringWithShadow(value.getName(), valueX + 1, valueY - 1, -1);
                fontRenderer.drawStringWithShadow(value.getDescription(), valueX + 1, valueY + fontRenderer.FONT_HEIGHT + 1 - 1, new Color(180, 180, 180).getRGB());
                RenderUtil.drawRect(valueX + width + 80 - fontRenderer.getStringWidthInt(sliderValue.getValue().floatValue() + ""), valueY + 3.5f - 3, fontRenderer.getStringWidthInt(sliderValue.getValue().floatValue() + "") + 6, 14, new Color(0, 0, 0, 80).getRGB());
                fontRenderer.drawStringWithShadow(sliderValue.getValue().floatValue() + "", valueX + width + 80 - fontRenderer.getStringWidthInt(sliderValue.getValue().floatValue() + "") + 3.5f, valueY + 3.5f, -1);
                RenderUtil.scaleEnd();
                float sliderX = valueX + 3, sliderY = valueY + fontRenderer.FONT_HEIGHT + 5, sliderWidth = width - 40 - 6, sliderHeight = 2.5f;
                RenderUtil.drawRect(sliderX, sliderY, sliderWidth, sliderHeight, new Color(0, 0, 0, 100).getRGB());
                float length = MathHelper
                        .floor_double(((sliderValue.getValue()).floatValue() - sliderValue.getMinimum().floatValue())
                                / (sliderValue.getMaximum().floatValue() - sliderValue.getMinimum().floatValue()) * sliderWidth);
                RenderUtil.drawRect(sliderX, sliderY, length, sliderHeight, TARASANDE);
                if(Mouse.isButtonDown(0) && RenderUtil.isHovered(mouseX, mouseY, sliderX, sliderY, sliderWidth, sliderHeight)) {
                    double min1 = sliderValue.getMinimum().floatValue();
                    double max1 = sliderValue.getMaximum().floatValue();
                    double newValue = MathUtil.round((mouseX - sliderX) * (max1 - min1) / (sliderWidth - 1.0f) + min1, sliderValue.getDecimalPlaces());
                    sliderValue.setValue(newValue);
                }
                valueY += fontRenderer.FONT_HEIGHT + 15;
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
        FontRenderer fontRenderer = mc.fontRendererObj;
        float tabHeight = (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2);
        float valueY = y + 20 + scroll + 5;
        float valueX = x + 20;
        for(Value value : ValueStorage.getInstance().getValues(module)) {
            if(!value.isVisible())
                continue;
            if(value instanceof CheckBoxValue) {
                if (RenderUtil.isHovered(mouseX, mouseY, valueX, valueY, width - 40, fontRenderer.FONT_HEIGHT + 10)) {
                    CheckBoxValue checkBoxValue = (CheckBoxValue) value;
                    checkBoxValue.setValue(!checkBoxValue.getValue());
                }
                valueY += fontRenderer.FONT_HEIGHT + 10;
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
                float length = fontRenderer.getStringWidthInt(multiStringBoxValue.getValue().size()   + " Enabled");
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
