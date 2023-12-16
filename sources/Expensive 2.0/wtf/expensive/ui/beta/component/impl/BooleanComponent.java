package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.SmartScissor;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;

import static wtf.expensive.util.render.RenderUtil.reAlphaInt;

public class BooleanComponent extends Component {

    public BooleanOption option;

    public BooleanComponent(BooleanOption option) {
        this.option = option;
        this.s = option;
    }

    public float animationToggle;

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        height = 15;
        float off = 0.5f;
        animationToggle = AnimationMath.lerp(animationToggle, option.get() ? 1 : 0, 10);

        int color = ColorUtil.interpolateColor(RenderUtil.IntColor.rgba(26, 29, 33, 255),
                RenderUtil.IntColor.rgba(74, 166, 218, 255), animationToggle);

        RenderUtil.Render2D.drawShadow(x + 5, y + 1 + off, 10, 10, 8, reAlphaInt(color, 50));
        RenderUtil.Render2D.drawRoundedRect(x + 5, y + 1 + off, 10, 10, 2f, color);
        SmartScissor.push();

        SmartScissor.setFromComponentCoordinates(x + 5, y + 1 + off, 10 * animationToggle, 10);
        Fonts.icons[12].drawString(matrixStack, "A", x + 7, y + 6 + off, -1);
        SmartScissor.unset();
        SmartScissor.pop();

        Fonts.gilroy[14].drawString(matrixStack, option.getName(), x + 18f, y + 4.5f + off, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInRegion(mouseX, mouseY, x, y, width - 10, 15)) {

            option.toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }
}
