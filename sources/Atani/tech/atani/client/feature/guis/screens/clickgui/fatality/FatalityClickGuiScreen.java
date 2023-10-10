package tech.atani.client.feature.guis.screens.clickgui.fatality;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.hud.ClickGui;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.utility.interfaces.ClientInformationAccess;
import tech.atani.client.utility.interfaces.ColorPalette;
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.shader.shaders.GradientShader;

import java.awt.*;
import java.util.ArrayList;

public class FatalityClickGuiScreen extends GuiScreen implements ClientInformationAccess, ColorPalette {

    private final float width = 460, height = 360;
    private float posX = -1337, posY = -1337;
    private Category selectedCategory = null;
    private Module selectedModule, bindingModule;
    private ArrayList<Value> expanded = new ArrayList<>();
    private float modScroll, valScroll;
    DecelerateAnimation openingAnimation = new DecelerateAnimation(600, 1, Direction.BACKWARDS);
    ClickGui clickGui;
    float animationLeftRight = 0;
    float animationUpDown = 0;

    @Override
    public void initGui() {
        this.clickGui = ModuleStorage.getInstance().getByClass(ClickGui.class);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, super.width - 60, super.height - 25, 55, 20, "Reset Gui"));
        openingAnimation.setDirection(Direction.FORWARDS);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.posX == -1337 || this.posY == -1337) {
            this.posX = super.width / 2 - this.width / 2;
            this.posY = super.height / 2 - this.height / 2;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        animationLeftRight = 0;
        animationUpDown = 0;
        if(clickGui.openingAnimation.getValue()) {
            switch (clickGui.nonDropdownAnimation.getValue()) {
                case "Left to Right":
                    animationLeftRight = (float) ((width + posX) * (1 - openingAnimation.getOutput()));
                    if(openingAnimation.getDirection() == Direction.FORWARDS) {
                        animationLeftRight = (float) -((width + posX) * (1 - openingAnimation.getOutput()));
                    }
                    break;
                case "Right to Left":
                    animationLeftRight = (float) (sr.getScaledWidth() * (1 - openingAnimation.getOutput()));
                    if(openingAnimation.getDirection() == Direction.BACKWARDS) {
                        animationLeftRight = (float) -(sr.getScaledWidth() * (1 - openingAnimation.getOutput()));
                    }
                    break;
                case "Up to Down":
                    animationUpDown = (float) ((height + posY) * (1 - openingAnimation.getOutput()));
                    if(openingAnimation.getDirection() == Direction.FORWARDS) {
                        animationUpDown = (float) -((height + posY) * (1 - openingAnimation.getOutput()));
                    }
                    break;
                case "Down to Up":
                    animationUpDown = (float) (sr.getScaledHeight() * (1 - openingAnimation.getOutput()));
                    if(openingAnimation.getDirection() == Direction.BACKWARDS) {
                        animationUpDown = (float) -(sr.getScaledHeight() * (1 - openingAnimation.getOutput()));
                    }
                    break;
            }
        }
        float oldX = posX, oldY = posY;
        posX += animationLeftRight;
        posY += animationUpDown;
        RenderUtil.drawRect(posX, posY, width, height, new Color(30, 26, 68).getRGB());
        FontRenderer logoFont = FontStorage.getInstance().findFont("Porter Bold", 23);
        logoFont.drawCenteredString(CLIENT_NAME.toUpperCase(), posX + width / 2, posY + 7, -1);
        GradientShader.drawGradientLR(posX, posY + 10 + logoFont.FONT_HEIGHT, width, 2, 1, new Color(FATALITY_FIRST), new Color(FATALITY_SECOND));
        FontRenderer categoryFont = FontStorage.getInstance().findFont("Arial", 19);
        float categoryX = 0;
        for (Category category : Category.values()) {
            categoryX += categoryFont.getStringWidthInt(category.getName().toUpperCase()) + 10;
        }
        categoryX -= 10;
        categoryX = posX + width / 2 - categoryX / 2;
        float startX = categoryX;
        for (Category category : Category.values()) {
            categoryFont.drawString(category.getName().toUpperCase(), categoryX, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 10, category == selectedCategory ? -1 : new Color(170, 170, 170).getRGB());
            categoryX += categoryFont.getStringWidthInt(category.getName().toUpperCase()) + 10;
        }
        GradientShader.drawGradientLR(posX, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30, width, 2, 1, new Color(FATALITY_FIRST), new Color(FATALITY_SECOND));
        RenderUtil.drawRect(posX + 2 * (width / 5), posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2, 1.5f, height - (24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2), new Color(170, 170, 170, 100).getRGB());
        FontRenderer bigFont = FontStorage.getInstance().findFont("Arial", 21);
        FontRenderer normalFont = FontStorage.getInstance().findFont("Arial", 18);
        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(posX, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2F, 2 * (width / 5), height - (24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 1.5F));
        if(selectedCategory != null) {
            if(RenderUtil.isHovered(mouseX, mouseY, posX, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2F, 2 * (width / 5), height - (24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 1.5F)))
                modScroll = Math.min(0, modScroll + Mouse.getDWheel() / 10F);
            float moduleY = posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2 + modScroll;
            for(Module module : ModuleStorage.getInstance().getModules(selectedCategory)) {
                if(module.shouldHide())
                    continue;
                //RenderUtil.drawRect(posX, moduleY, 2 * (width / 5), 40, Color.red.getRGB());
                bigFont.drawString(module.getName(), posX + 11, moduleY + 9, module.isEnabled() ? -1 : new Color(170, 170, 170).getRGB());
                normalFont.drawString(String.format("Bind (%s)", bindingModule == module ? "?" : module.getKey() == 0 ? "  " : Keyboard.getKeyName(module.getKey())), posX + 11, moduleY + 9 + bigFont.FONT_HEIGHT + 2, new Color(170, 170, 170).getRGB());
                moduleY += 30;
            }
        }
        RenderUtil.endScissorBox();
        FontRenderer smallFont = FontStorage.getInstance().findFont("Arial", 17);
        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(posX + 2 * (width / 5) + 1.5F, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2F, width - ( 2 * (width / 5) + 1.5F), height - (24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 1.5F));
        if(selectedModule != null) {
            if(RenderUtil.isHovered(mouseX, mouseY, posX + 2 * (width / 5) + 1.5F, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2F, width - ( 2 * (width / 5) + 1.5F), height - (24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 1.5F)))
                valScroll = Math.min(0, valScroll + Mouse.getDWheel() / 10F);
            float valueY = posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2 + 10 + valScroll;
            for (Value value : ValueStorage.getInstance().getValues(selectedModule)) {
                if(!value.isVisible())
                    continue;
                float center = posX + width / 2 + (2 * (width / 5) + 1.5F) / 2;
                float valueX = center - 80;
                float valueX2 = center - 80 + 160;
                smallFont.drawString(value.getName(), valueX, valueY,-1);
                if(value instanceof CheckBoxValue) {
                    RenderUtil.drawBorderedRect(valueX2 - 10, valueY - 2, valueX2, valueY - 2 + 10, 0.5f, new Color(30, 26, 68).getRGB(), new Color(170, 170, 170).getRGB(), true);
                    float sliderX = valueX2 - 10 + 1, sliderY = valueY - 2 + 1, sliderWidth = valueX2 - (valueX2 - 10) - 2, sliderHeight = 8;
                    if(((CheckBoxValue)value).getValue())
                        RenderUtil.drawRect(sliderX, sliderY, sliderWidth, sliderHeight, FATALITY_SECOND);
                } else if(value instanceof StringBoxValue) {
                    StringBoxValue stringBoxValue = (StringBoxValue) value;
                    RenderUtil.drawBorderedRect(valueX2 - 65, valueY - 2, valueX2, valueY - 2 + 10 + (expanded.contains(value) ? stringBoxValue.getValues().length * 10 : 0), 0.5f, new Color(30, 26, 68).getRGB(), new Color(170, 170, 170).getRGB(), true);
                    smallFont.drawCenteredString(((StringBoxValue)value).getValue(), valueX2 - 32.5f, valueY, -1);
                    valueY += 10;
                    if(expanded.contains(value)) {
                        for(String string : stringBoxValue.getValues()) {
                            smallFont.drawCenteredString(string, valueX2 - 32.5f, valueY, -1);
                            valueY += 10;
                        }
                    }
                    valueY -= 10;
                } else if(value instanceof MultiStringBoxValue) {
                    MultiStringBoxValue multiStringBoxValue = (MultiStringBoxValue) value;
                    RenderUtil.drawBorderedRect(valueX2 - 65, valueY - 2, valueX2, valueY - 2 + 10 + (expanded.contains(value) ? multiStringBoxValue.getValues().length * 10 : 0), 0.5f, new Color(30, 26, 68).getRGB(), new Color(170, 170, 170).getRGB(), true);
                    smallFont.drawCenteredString((((MultiStringBoxValue)value).getValue().size() - 1) + " Enabled", valueX2 - 32.5f, valueY, -1);
                    valueY += 10;
                    if(expanded.contains(value)) {
                        for(String string : multiStringBoxValue.getValues()) {
                            smallFont.drawCenteredString((multiStringBoxValue.get(string) ? ChatFormatting.BOLD : "") + string, valueX2 - 32.5f, valueY, multiStringBoxValue.getValue().contains(string) ? -1 : new Color(200, 200, 200).getRGB());
                            valueY += 10;
                        }
                    }
                    valueY -= 10;
                } else if(value instanceof SliderValue) {
                    SliderValue sliderValue = (SliderValue) value;
                    RenderUtil.drawBorderedRect(valueX2 - 65, valueY - 2, valueX2, valueY - 2 + 10, 0.5f, new Color(30, 26, 68).getRGB(), new Color(170, 170, 170).getRGB(), true);
                    float sliderX = valueX2 - 65 + 1, sliderY = valueY - 2 + 1, sliderWidth = valueX2 - (valueX2 - 65) - 2, sliderHeight = 8;
                    float length = MathHelper
                            .floor_double(((sliderValue.getValue()).floatValue() - sliderValue.getMinimum().floatValue())
                                    / (sliderValue.getMaximum().floatValue() - sliderValue.getMinimum().floatValue()) * sliderWidth);
                    RenderUtil.drawRect(sliderX, sliderY, length, sliderHeight, FATALITY_SECOND);
                    smallFont.drawCenteredString(sliderValue.getValue().floatValue() + "", sliderX + length, sliderY + 1, -1);
                    if(Mouse.isButtonDown(0) && RenderUtil.isHovered(mouseX, mouseY, sliderX, sliderY, sliderWidth, sliderHeight)) {
                        double min1 = sliderValue.getMinimum().floatValue();
                        double max1 = sliderValue.getMaximum().floatValue();
                        double newValue = MathUtil.round((mouseX - sliderX) * (max1 - min1) / (sliderWidth - 1.0f) + min1, sliderValue.getDecimalPlaces());
                        sliderValue.setValue(newValue);
                    }
                }
                valueY += smallFont.FONT_HEIGHT + 5;
            }
        }
        RenderUtil.endScissorBox();
        if (clickGui.openingAnimation.getValue()) {
            if (this.openingAnimation.finished(Direction.BACKWARDS))
                mc.displayGuiScreen(null);
        }
        posX = oldX;
        posY = oldY;
    }

    @Override
    public void keyTyped(char key, int code){
        if(bindingModule != null) {
            bindingModule.setKey(code == Keyboard.KEY_BACK ? 0 : code);
            bindingModule = null;
        }
        if (code == 1) {
            if (clickGui.openingAnimation.getValue()) {
                this.openingAnimation.setDirection(Direction.BACKWARDS);
            } else {
                mc.displayGuiScreen(null);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float oldX = posX, oldY = posY;
        posX += animationLeftRight;
        posY += animationUpDown;
        FontRenderer categoryFont = FontStorage.getInstance().findFont("Arial", 19);
        float categoryX = 0;
        for (Category category : Category.values()) {
            categoryX += categoryFont.getStringWidthInt(category.getName().toUpperCase()) + 10;
        }
        categoryX -= 10;
        categoryX = posX + width / 2 - categoryX / 2;
        float startX = categoryX;
        for (Category category : Category.values()) {
            if (RenderUtil.isHovered(mouseX, mouseY, categoryX, posY + 23 + categoryFont.FONT_HEIGHT / 2 - 3 + 9, categoryFont.getStringWidthInt(category.getName().toUpperCase()), categoryFont.FONT_HEIGHT) && mouseButton == 0) {
                selectedCategory = category;
                modScroll = 0;
                valScroll = 0;
                selectedModule = null;
            }
            categoryX += categoryFont.getStringWidthInt(category.getName().toUpperCase()) + 10;
        }
        if (RenderUtil.isHovered(mouseX, mouseY, posX, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2F, 2 * (width / 5), height - (24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 1.5F))) {
            if(selectedCategory != null) {
                float moduleY = posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2 + modScroll;
                for(Module module : ModuleStorage.getInstance().getModules(selectedCategory)) {
                    if(module.shouldHide())
                        continue;
                    if(RenderUtil.isHovered(mouseX, mouseY, posX, moduleY, 2 * (width / 5), 40)) {
                        switch (mouseButton) {
                            case 0:
                                module.toggle();
                            case 1:
                                selectedModule = module;
                                break;
                            case 2:
                                if(bindingModule == null)
                                    bindingModule = module;
                                else
                                    bindingModule = null;
                                break;
                        }
                    }
                    moduleY += 30;
                }
            }
        }
        FontRenderer smallFont = FontStorage.getInstance().findFont("Arial", 17);
        if (RenderUtil.isHovered(mouseX, mouseY, posX + 2 * (width / 5) + 1.5F, posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2F, width - (2 * (width / 5) + 1.5F), height - (24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 1.5F))) {
            if(selectedModule != null) {
                float valueY = posY + 24 + categoryFont.FONT_HEIGHT / 2 - 3 + 30 + 2 + 10 + valScroll;
                for (Value value : ValueStorage.getInstance().getValues(selectedModule)) {
                    if(!value.isVisible())
                        continue;
                    float center = posX + width / 2 + (2 * (width / 5) + 1.5F) / 2;
                    float valueX = center - 80;
                    float valueX2 = center - 80 + 160;
                    smallFont.drawString(value.getName(), valueX, valueY,-1);
                    if(value instanceof CheckBoxValue) {
                        if(RenderUtil.isHovered(mouseX, mouseY, valueX2 - 10, valueY - 2, 10, 10)) {
                            value.setValue(!((CheckBoxValue)value).getValue());
                        }
                    } else if(value instanceof StringBoxValue) {
                        StringBoxValue stringBoxValue = (StringBoxValue) value;
                        if(RenderUtil.isHovered(mouseX, mouseY, valueX2 - 65, valueY - 2, 65, 10)) {
                            if(expanded.contains(value))
                                expanded.remove(value);
                            else
                                expanded.add(stringBoxValue);
                        }
                        if(expanded.contains(value)) {
                            valueY += 10;
                            if(expanded.contains(value)) {
                                for(String string : stringBoxValue.getValues()) {
                                    if(RenderUtil.isHovered(mouseX, mouseY, valueX2 - 65, valueY - 2, 65, 10)) {
                                        stringBoxValue.setValue(string);
                                    }
                                    valueY += 10;
                                }
                            }
                            valueY -= 10;
                        }
                    } else if(value instanceof MultiStringBoxValue) {
                        MultiStringBoxValue multiStringBoxValue = (MultiStringBoxValue) value;
                        if(RenderUtil.isHovered(mouseX, mouseY, valueX2 - 65, valueY - 2, 65, 10)) {
                            if(expanded.contains(value))
                                expanded.remove(value);
                            else
                                expanded.add(multiStringBoxValue);
                        }
                        if(expanded.contains(value)) {
                            valueY += 10;
                            if(expanded.contains(value)) {
                                String toToggle = null;
                                for(String string : multiStringBoxValue.getValues()) {
                                    if(RenderUtil.isHovered(mouseX, mouseY, valueX2 - 65, valueY - 2, 65, 10)) {
                                        toToggle = string;
                                    }
                                    valueY += 10;
                                }
                                if(toToggle != null)
                                    multiStringBoxValue.toggle(toToggle);
                            }
                            valueY -= 10;
                        }
                    }
                    valueY += smallFont.FONT_HEIGHT + 5;
                }
            }
        }
        posX = oldX;
        posY = oldY;
    }

}
