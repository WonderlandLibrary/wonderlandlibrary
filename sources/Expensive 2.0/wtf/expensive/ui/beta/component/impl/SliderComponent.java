package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector4f;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;

public class SliderComponent extends Component {

    public SliderSetting option;

    public SliderComponent(SliderSetting option) {
        this.option = option;
        this.s = option;

    }

    boolean drag;

    float anim;

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        height += 2;
        float sliderWidth = ((option.getValue().floatValue() - option.getMin()) / (option.getMax() - option.getMin())) * (width - 12);
        anim = AnimationMath.lerp(anim, sliderWidth, 10);
        Fonts.gilroy[14].drawString(matrixStack, option.getName(), x + 6, y + 4, -1);
        Fonts.gilroy[14].drawString(matrixStack, String.valueOf(option.getValue().floatValue()), x + width - Fonts.gilroy[14].getWidth(String.valueOf(option.getValue().floatValue())) - 6, y + 4, -1);
        RenderUtil.Render2D.drawRoundedCorner(x + 6, y + 13, width - 12, 3, new Vector4f(2,2,2,2), new Color(26, 29, 33).getRGB());

        RenderUtil.Render2D.drawShadow(x + 6, y + 14, anim, 3, 8, new Color( 74,166,218, 50).getRGB());

        RenderUtil.Render2D.drawRoundedCorner(x + 6, y + 13, anim, 3, new Vector4f(2,2,option.getMax() == option.getValue().floatValue() ? 2 : 0,option.getMax() == option.getValue().floatValue() ? 2 : 0), new Color(74,166,218).getRGB());

        RenderUtil.Render2D.drawRoundCircle(x + 5 + anim, y + 14.5f, 10, new Color(17, 18, 21).getRGB());
        RenderUtil.Render2D.drawRoundCircle(x + 5 + anim, y + 14.5f, 8, new Color(74, 166, 218).getRGB());
        RenderUtil.Render2D.drawRoundCircle(x + 5 + anim, y + 14.5f, 6, new Color(17, 18, 21).getRGB());
        if (drag) {
            float value = (float) ((mouseX - x - 6) / (width - 12) * (option.getMax() - option.getMin()) + option.getMin());
            value = (float) MathUtil.round(value, option.getIncrement());
            option.setValue(value);
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX,mouseY)) {
            drag = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        drag = false;
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }
}
