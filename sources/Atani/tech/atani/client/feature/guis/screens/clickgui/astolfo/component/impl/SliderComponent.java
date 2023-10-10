package tech.atani.client.feature.guis.screens.clickgui.astolfo.component.impl;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.SliderValue;

import java.awt.*;

public class SliderComponent extends ValueComponent {

    private boolean expanded = false;

    public SliderComponent(Value value, float posX, float posY, float baseWidth, float baseHeight) {
        super(value, posX, posY, baseWidth, baseHeight);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        FontRenderer normal = FontStorage.getInstance().findFont("SFUI Medium", 16);
        normal.drawString(value.getName(), getPosX() + 5 + getAddX(), getPosY() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, -1);
        normal.drawString(((Number)value.getValue()).floatValue() + "", getPosX() + this.getBaseWidth() - 5 - normal.getStringWidthInt(((Number)value.getValue()).floatValue() + "") + getAddX(), getPosY() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, -1);
        if(this.expanded) {
            SliderValue sliderValue = (SliderValue) value;
            String min = String.valueOf(sliderValue.getMinimum().floatValue());
            String max = String.valueOf(sliderValue.getMaximum().floatValue());
            normal.drawString(min, getPosX() + 5 + getAddX(), getPosY() + getBaseHeight() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, -1);
            normal.drawString(max, getPosX() + this.getBaseWidth() - 5 - normal.getStringWidthInt(max) + getAddX(), getPosY() + getBaseHeight() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, -1);
            float sliderX = getPosX() + 5 + normal.getStringWidthInt(min) + 3 + getAddX();
            float sliderWidth = getBaseWidth() - (5 + normal.getStringWidthInt(min) + 3) - (5 + normal.getStringWidthInt(max) + 3);
            float sliderY = getPosY() + getBaseHeight() + getBaseHeight() / 4 - 1;
            float sliderHeight = getBaseHeight() / 4 * 2;
            RenderUtil.drawRect(sliderX, sliderY, sliderWidth, sliderHeight, new Color(0, 0, 0, 50).getRGB());
            float length = MathHelper
                    .floor_double(((sliderValue.getValue()).floatValue() - sliderValue.getMinimum().floatValue())
                            / (sliderValue.getMaximum().floatValue() - sliderValue.getMinimum().floatValue()) * sliderWidth);
            RenderUtil.drawRect(sliderX, sliderY, length, sliderHeight, ColorUtil.getAstolfoColor(getModuleCategory(value.getOwner())));
            if(Mouse.isButtonDown(0) && RenderUtil.isHovered(mouseX, mouseY, this.getPosX() + getAddX(), this.getPosY() + this.getBaseHeight(), this.getBaseWidth(), this.getBaseHeight())) {
                double min1 = sliderValue.getMinimum().floatValue();
                double max1 = sliderValue.getMaximum().floatValue();
                double newValue = MathUtil.round((mouseX - sliderX) * (max1 - min1) / (sliderWidth - 1.0f) + min1, sliderValue.getDecimalPlaces());
                sliderValue.setValue(newValue);
            }
        }
    }

    @Override
    public float getFinalHeight() {
        return this.expanded ? this.getBaseHeight() * 2 : this.getBaseHeight();
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getBaseWidth(), this.getBaseHeight())) {
            expanded = !expanded;
        }
    }

    private Category getModuleCategory(Object owner) {
        if (owner instanceof Module) {
            return ((Module) owner).getCategory();
        }
        return Category.HUD;
    }
}
