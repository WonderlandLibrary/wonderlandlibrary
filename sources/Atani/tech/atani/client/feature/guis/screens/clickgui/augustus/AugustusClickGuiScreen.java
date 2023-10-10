package tech.atani.client.feature.guis.screens.clickgui.augustus;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
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
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;
import java.io.IOException;

public class AugustusClickGuiScreen extends GuiScreen {

    float posX = -1337, posY = -1337;
    float width = 600, height = 400;
    Category selectedCategory = Category.COMBAT;
    Module selectedModule;
    float draggingX, draggingY;
    boolean dragging = false;
    float valueScroll = 0F, moduleScroll = 0F;
    public boolean expandingRight;
    private ClickGui clickGui;
    DecelerateAnimation openingAnimation = new DecelerateAnimation(600, 1, Direction.BACKWARDS);
    float animationLeftRight = 0;
    float animationUpDown = 0;

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, super.width - 60, super.height - 25, 55, 20, "Reset Gui"));
        openingAnimation.setDirection(Direction.FORWARDS);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(!Mouse.isButtonDown(0))
            dragging = false;
        if(clickGui == null)
            clickGui = ModuleStorage.getInstance().getByClass(ClickGui.class);
        if (this.posX == -1337 || this.posY == -1337) {
            this.posX = super.width / 2 - this.width / 2;
            this.posY = super.height / 2 - this.height / 2;
        }
        /*
        width = 450;
        height = 250;
         */
        if(expandingRight) {
            float deltaY = mouseY - (posY + height);
            height = Math.max(250, height + deltaY);
            float deltaX = mouseX - (posX + width);
            width = Math.max(450, width + deltaX);
        }
        if(RenderUtil.isHovered(mouseX, mouseY, posX, posY, width, 17) && dragging) {
            posX = mouseX - draggingX;
            posY = mouseY - draggingY;
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
        FontRenderer esp = FontStorage.getInstance().findFont("ESP", 21);
        RoundedShader.drawRound(posX, posY, width, height, 7, new Color(25, 25, 25, 180));
        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(posX, posY, width, 17);
        RoundedShader.drawRound(posX, posY, width, 25, 7, new Color(34, 34, 34));
        RenderUtil.endScissorBox();
        esp.drawString("CLICKGUI", posX + 5, posY + 6, new Color(200, 200, 200).getRGB());
        RenderUtil.drawRect(posX + 110, posY + 0.5f, 2, height, new Color(34, 34, 34).getRGB());
        RenderUtil.drawRect(posX + 110, posY + 50, width - 110 + 0.5f, 2, new Color(34, 34, 34).getRGB());
        FontRenderer normal = FontStorage.getInstance().findFont("Consolas", 18);
        FontRenderer small = FontStorage.getInstance().findFont("Consolas", 17);
        float categoryX = posX + 110 + 20;
        for(Category category : Category.values()) {
            normal.drawString(category.getName().toUpperCase(), categoryX, posY + 30, new Color(200, 200, 200).getRGB());
            if(category == selectedCategory) {
                RenderUtil.drawRect(categoryX, posY + 30 + normal.FONT_HEIGHT, normal.getStringWidthInt(category.getName().toUpperCase()) - 0.5f, 1, new Color(200, 200, 200).getRGB());
            }
            categoryX += normal.getStringWidthInt(category.getName()) + 10;
        }
        int dWheel = Mouse.getDWheel();
        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(posX, posY + 16.5f, 110, height - 16.5f);
        if(selectedCategory != null) {
            if (RenderUtil.isHovered(mouseX, mouseY, posX, posY + 16.5f, 110, height - 16.5f)) {
                moduleScroll = Math.min(0, moduleScroll + dWheel / 10F);
            }
            float moduleY = posY + 25 + moduleScroll;
            for(Module module : ModuleStorage.getInstance().getModules(selectedCategory)) {
                if(module.shouldHide())
                    continue;
                normal.drawString(module.getName(), posX + 10, moduleY, module.isEnabled() ? new Color(clickGui.red.getValue(), clickGui.green.getValue(), clickGui.blue.getValue()).getRGB() : new Color(200, 200, 200).getRGB());
                moduleY += normal.FONT_HEIGHT + 5;
            }
        }
        RenderUtil.endScissorBox();
        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(posX + 110 + 1.5F + 0.5f, posY + 50 + 1.5f + 1, width - (110 + 1.5F), height - (50 + 1.5f));
        if(selectedModule != null) {
            float moduleY = posY + 50 + 10;
            if (RenderUtil.isHovered(mouseX, mouseY, posX + 110 + 1.5F, posY + 50 + 1.5f + 1, width - (110 + 1.5F), height - (50 + 1.5f))) {
                valueScroll = Math.min(0, valueScroll + dWheel / 10F);
            }
            moduleY += valueScroll;
            esp.drawString(selectedModule.getName(), posX + 120, moduleY, new Color(clickGui.red.getValue(), clickGui.green.getValue(), clickGui.blue.getValue()).getRGB());
            moduleY += esp.FONT_HEIGHT + 3;
            for(Value value : ValueStorage.getInstance().getValues(selectedModule)) {
                if(!value.isVisible())
                    continue;
                if(value instanceof CheckBoxValue) {
                    CheckBoxValue checkBoxValue = (CheckBoxValue) value;
                    normal.drawString(value.getName() + ": ", posX + 120, moduleY, new Color(200, 200, 200).getRGB());
                    normal.drawString(checkBoxValue.getValue() ? "true" : "false", posX + 120 + normal.getStringWidthInt(value.getName() + ": "), moduleY, checkBoxValue.getValue() ? new Color(0, 180, 0).getRGB() : new Color(180, 0, 0).getRGB());
                    moduleY += normal.FONT_HEIGHT + 2;
                } else if(value instanceof SliderValue) {
                    SliderValue sliderValue = (SliderValue)value;
                    normal.drawString(value.getName() + ": ", posX + 120, moduleY, new Color(200, 200, 200).getRGB());
                    RenderUtil.drawBorder(posX + 120 + normal.getStringWidthInt(value.getName() + ": "), moduleY - 2, (posX + 120 + normal.getStringWidthInt(value.getName() + ": ")) + 120, (moduleY - 2) + 10, 1, new Color(34, 34, 34).getRGB(), true);
                    float sliderX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ") + 1, sliderY = moduleY - 2 + 1, sliderWidth = 120 - 2, sliderHeight = 10 - 2;
                    float length = MathHelper
                            .floor_double(((sliderValue.getValue()).floatValue() - sliderValue.getMinimum().floatValue())
                                    / (sliderValue.getMaximum().floatValue() - sliderValue.getMinimum().floatValue()) * sliderWidth);
                    RenderUtil.drawRect(sliderX, sliderY, length, sliderHeight, new Color(clickGui.red.getValue(), clickGui.green.getValue(), clickGui.blue.getValue()).getRGB());
                    small.drawTotalCenteredString(sliderValue.getValue().floatValue() + "", sliderX + sliderWidth / 2, sliderY + sliderHeight / 2 + 1.5f, new Color(200, 200, 200).getRGB());
                    if(Mouse.isButtonDown(0) && RenderUtil.isHovered(mouseX, mouseY, sliderX, sliderY, sliderWidth, sliderHeight)) {
                        double min1 = sliderValue.getMinimum().floatValue();
                        double max1 = sliderValue.getMaximum().floatValue();
                        double newValue = MathUtil.round((mouseX - sliderX) * (max1 - min1) / (sliderWidth - 1.0f) + min1, sliderValue.getDecimalPlaces());
                        sliderValue.setValue(newValue);
                    }
                    moduleY += normal.FONT_HEIGHT + 2;
                } else if(value instanceof StringBoxValue) {
                    StringBoxValue stringBoxValue = ((StringBoxValue)value);
                    normal.drawString(value.getName() + ": ", posX + 120, moduleY, new Color(200, 200, 200).getRGB());
                    float modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                    for(String string : stringBoxValue.getValues()) {
                        if(modeX >= (posX + width - 50)) {
                            modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                            moduleY += normal.FONT_HEIGHT + 2;
                        }
                        normal.drawString(string, modeX, moduleY, value.getValue().equals(string) ? new Color(clickGui.red.getValue(), clickGui.green.getValue(), clickGui.blue.getValue()).getRGB() : new Color(200, 200, 200).getRGB());
                        modeX += normal.getStringWidthInt(string);
                        if(!stringBoxValue.getValues()[stringBoxValue.getValues().length - 1].equals(string)) {
                            normal.drawString(", ", modeX, moduleY, new Color(200, 200, 200).getRGB());
                            modeX += normal.getStringWidthInt(", ");
                        }
                    }
                    moduleY += normal.FONT_HEIGHT + 2;
                } else if (value instanceof MultiStringBoxValue) {
                    MultiStringBoxValue multiStringBoxValue = ((MultiStringBoxValue) value);
                    normal.drawString(value.getName() + ": ", posX + 120, moduleY, new Color(200, 200, 200).getRGB());
                    float modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                    for (String string : multiStringBoxValue.getValues()) {
                        if (modeX >= (posX + width - 50)) {
                            modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                            moduleY += normal.FONT_HEIGHT + 2;
                        }
                        normal.drawString(string, modeX, moduleY, multiStringBoxValue.get(string) ? new Color(clickGui.red.getValue(), clickGui.green.getValue(), clickGui.blue.getValue()).getRGB() : new Color(200, 200, 200).getRGB());
                        modeX += normal.getStringWidthInt(string);
                        if (!multiStringBoxValue.getValues()[multiStringBoxValue.getValues().length - 1].equals(string)) {
                            normal.drawString(", ", modeX, moduleY, new Color(200, 200, 200).getRGB());
                            modeX += normal.getStringWidthInt(", ");
                        }
                    }
                    moduleY += normal.FONT_HEIGHT + 2;
                }
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
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        expandingRight = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char key, int code) throws IOException {
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
        FontRenderer normal = FontStorage.getInstance().findFont("Consolas", 18);
        float categoryX = posX + 110 + 20;
        if(RenderUtil.isHovered(mouseX, mouseY, posX, posY, width, 17) && mouseButton == 0) {
            dragging = true;
            draggingX = mouseX - posX;
            draggingY = mouseY - posY;
        }
        if(RenderUtil.isHovered(mouseX, mouseY, posX + width - 8, posY + height - 8, 16, 16)) {
            expandingRight = true;
        }
        for(Category category : Category.values()) {
            if (RenderUtil.isHovered(mouseX, mouseY, categoryX, posY + 30, normal.getStringWidthInt(category.getName().toUpperCase()) - 0.5f, 1 + normal.FONT_HEIGHT)) {
                selectedCategory = category;
                selectedModule = null;
                valueScroll = 0f;
                moduleScroll = 0f;
            }
            categoryX += normal.getStringWidthInt(category.getName()) + 10;
        }
        if(selectedCategory != null) {
            float moduleY = posY + 25 + moduleScroll;
            for(Module module : ModuleStorage.getInstance().getModules(selectedCategory)) {
                if(module.shouldHide())
                    continue;
                if (RenderUtil.isHovered(mouseX, mouseY, posX + 10, moduleY - 4, normal.getStringWidthInt(module.getName()) - 0.5f, normal.FONT_HEIGHT + 4)) {
                    switch (mouseButton) {
                        case 0:
                            module.toggle();
                            break;
                        case 1:
                            selectedModule = module;
                            break;
                    }
                }
                moduleY += normal.FONT_HEIGHT + 5;
            }
        }
        FontRenderer small = FontStorage.getInstance().findFont("Consolas", 17);
        FontRenderer esp = FontStorage.getInstance().findFont("ESP", 21);
        if(selectedModule != null) {
            float moduleY = posY + 50 + 10 + valueScroll;
            esp.drawString(selectedModule.getName(), posX + 120, moduleY, -1);
            moduleY += esp.FONT_HEIGHT + 3;
            for(Value value : ValueStorage.getInstance().getValues(selectedModule)) {
                if(!value.isVisible())
                    continue;
                if(value instanceof CheckBoxValue) {
                    CheckBoxValue checkBoxValue = (CheckBoxValue) value;
                    if(RenderUtil.isHovered(mouseX, mouseY, posX + 120, moduleY - 2, normal.getStringWidthInt(checkBoxValue.getName() + ": " + (checkBoxValue.getValue() ? "true" : "false")), 10))
                        checkBoxValue.setValue(!checkBoxValue.getValue());
                    moduleY += normal.FONT_HEIGHT + 2;
                } else if(value instanceof SliderValue) {
                    moduleY += normal.FONT_HEIGHT + 2;
                } else if(value instanceof StringBoxValue) {
                    StringBoxValue stringBoxValue = ((StringBoxValue)value);
                    float modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                    for(String string : stringBoxValue.getValues()) {
                        if(modeX >= (posX + width - 50)) {
                            modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                            moduleY += normal.FONT_HEIGHT + 2;
                        }
                        if(RenderUtil.isHovered(mouseX, mouseY, modeX, moduleY - 2, normal.getStringWidthInt(string), 10))
                            stringBoxValue.setValue(string);
                        modeX += normal.getStringWidthInt(string);
                        if(!stringBoxValue.getValues()[stringBoxValue.getValues().length - 1].equals(string)) {
                            normal.drawString(", ", modeX, moduleY, new Color(200, 200, 200).getRGB());
                            modeX += normal.getStringWidthInt(", ");
                        }
                    }
                    moduleY += normal.FONT_HEIGHT + 2;
                } else if(value instanceof MultiStringBoxValue) {
                    MultiStringBoxValue multiStringBoxValue = ((MultiStringBoxValue)value);
                    float modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                    String toToggle = null;
                    for(String string : multiStringBoxValue.getValues()) {
                        if(modeX >= (posX + width - 50)) {
                            modeX = posX + 120 + normal.getStringWidthInt(value.getName() + ": ");
                            moduleY += normal.FONT_HEIGHT + 2;
                        }
                        if(RenderUtil.isHovered(mouseX, mouseY, modeX, moduleY - 2, normal.getStringWidthInt(string), 10))
                            toToggle = string;
                        modeX += normal.getStringWidthInt(string);
                        if(!multiStringBoxValue.getValues()[multiStringBoxValue.getValues().length - 1].equals(string)) {
                            normal.drawString(", ", modeX, moduleY, new Color(200, 200, 200).getRGB());
                            modeX += normal.getStringWidthInt(", ");
                        }
                    }
                    if(toToggle != null)
                        multiStringBoxValue.toggle(toToggle);
                    moduleY += normal.FONT_HEIGHT + 2;
                }
            }
        }
        posX = oldX;
        posY = oldY;
    }

}
