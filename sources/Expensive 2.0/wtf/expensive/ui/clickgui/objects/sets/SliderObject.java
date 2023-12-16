package wtf.expensive.ui.clickgui.objects.sets;

import com.mojang.blaze3d.matrix.MatrixStack;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.ui.clickgui.objects.ModuleObject;
import wtf.expensive.ui.clickgui.objects.Object;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;

public class SliderObject extends Object {

    public ModuleObject object;
    public SliderSetting set;
    public boolean sliding;

    public float animatedVal;

    public SliderObject(ModuleObject object, SliderSetting set) {
        this.object = object;
        this.set = set;
        setting = set;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        super.draw(stack, mouseX, mouseY);
        y -= 1;
        if (sliding) {
            float value = (float) ((mouseX - x - 10) / (width - 20) * (set.getMax() - set.getMin()) + set.getMin());
            value = (float) MathUtil.round(value, set.getIncrement());
            set.setValue(value);
        }
        float sliderWidth = ((set.getValue().floatValue() - set.getMin()) / (set.getMax() - set.getMin())) * (width - 20);
        animatedVal = AnimationMath.fast(animatedVal, sliderWidth, 20);
        RenderUtil.Render2D.drawRoundedRect(x + 10, y + height / 2f + 2, width - 20, 3, 1, ColorUtil.rgba(21, 22, 25, 255));
        RenderUtil.Render2D.drawRoundedRect(x + 10, y + height / 2f + 2, animatedVal, 3, 1, ColorUtil.rgba(128, 133, 152, 255));
        RenderUtil.Render2D.drawShadow(x + 10 + animatedVal - 3.5f, y + height / 2f + 3.5f - 3.5f, 7,7,10, ColorUtil.rgba(128, 133, 152, 128));

        RenderUtil.Render2D.drawRoundCircle(x + 10 + animatedVal, y + height / 2f + 3.5f, 6, ColorUtil.rgba(128, 133, 152, 255));

        Fonts.msLight[12].drawString(stack, set.getName(), x + 10, y + height / 2f - 4, ColorUtil.rgba(161, 164, 177, 255));
        Fonts.msLight[12].drawString(stack, String.valueOf(set.getValue().floatValue()), x + width - 10 - Fonts.msLight[12].getWidth(String.valueOf(set.getValue().floatValue())), y + height / 2f - 4, ColorUtil.rgba(161, 164, 177, 255));

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX,mouseY)) {
            sliding = true;
        }
    }

    @Override
    public void exit() {
        super.exit();
        sliding = false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        sliding = false;
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }
}
