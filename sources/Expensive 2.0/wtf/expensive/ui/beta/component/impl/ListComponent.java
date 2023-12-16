package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.SmartScissor;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;

public class ListComponent extends Component {

    public MultiBoxSetting option;

    public boolean opened;

    public ListComponent(MultiBoxSetting option) {
        this.option = option;
        this.s = option;
    }

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        float off = 4;
        float offset = 17 - 8;
        for (BooleanOption s : option.options) {
            offset += 9;
        }
        if (!opened) offset = 0;
        Fonts.gilroy[14].drawString(matrixStack, option.getName(), x + 5, y + 3, -1);
        off += Fonts.gilroy[14].getFontHeight() / 2f + 2;
        height += offset + 7;
        RenderUtil.Render2D.drawShadow(x + 5, y + off, width - 10, 20 - 6, 10, new Color(26, 29, 33, 50).getRGB());
        RenderUtil.Render2D.drawRoundedCorner(x + 5, y + off, width - 10, 20 - 6, 4, new Color(26, 29, 33).getRGB());
        RenderUtil.Render2D.drawShadow(x + 5, y + off + 17, width - 10, offset, 12, new Color(0, 0, 0, 100).getRGB());
        RenderUtil.Render2D.drawRoundedCorner(x + 5, y + off + 17, width - 10, offset, 4, new Color(17, 18, 21).getRGB());
        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(x + 5, y + off, width - 10, 20 - 6);
        Fonts.gilroy[14].drawString(matrixStack, option.get(), x + 10, y + 20 - 4, -1);
        SmartScissor.unset();
        SmartScissor.pop();
        if (opened) {
            int i = 1;
            for (BooleanOption s : option.options) {
                boolean hovered = RenderUtil.isInRegion(mouseX, mouseY, x, y + off + 20 + i, width, 8);
                s.anim = AnimationMath.lerp(s.anim, (hovered ? 2 : 0), 10);
                Fonts.gilroy[14].drawString(matrixStack, s.getName(), x + 9 + s.anim, y + off + 23.5F + i , option.get(s.getName()) ? new Color(74, 166, 218).getRGB() : new Color(163, 176, 188).getRGB());
                i += 9;
            }
            height += 3;
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        float off = 3;
        off += Fonts.gilroy[14].getFontHeight() / 2f + 2;
        if (RenderUtil.isInRegion(mouseX, mouseY, x + 5, y + off, width - 10, 20 - 5)) {
            opened = !opened;
        }


        if (!opened) return;
        int i = 1;
        for (BooleanOption s : option.options) {
            if (RenderUtil.isInRegion(mouseX, mouseY, x, y + off + 20F + i, width, 8))
                option.set((i - 1) / 9, !option.get(s.getName()));
            i += 9;
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
