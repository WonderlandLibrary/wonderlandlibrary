package wtf.expensive.ui.clickgui.objects.sets;

import com.mojang.blaze3d.matrix.MatrixStack;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.ui.clickgui.objects.ModuleObject;
import wtf.expensive.ui.clickgui.objects.Object;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;

public class MultiObject extends Object {

    public MultiBoxSetting set;
    public ModuleObject object;

    public MultiObject(ModuleObject object, MultiBoxSetting set) {
        this.object = object;
        this.set = set;
        setting = set;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        super.draw(stack, mouseX, mouseY);
        int offset = 0;
        float offsetY = 0;
        int lines = 1;
        float size = 0;
        for (BooleanOption mode : set.options) {
            float preOffset = size + Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
            if (preOffset > width - 20) {
                break;
            }
            size += Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
        }

        for (BooleanOption mode : set.options) {
            float preOffset = offset + Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
            if (preOffset > size) {
                lines++;
                offset = 0;
            }
            offset += Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
        }

        height += 8;
        Fonts.msLight[12].drawString(stack, set.getName(), x + 10, y + height / 2f - 8, ColorUtil.rgba(161, 164, 177, 255));

        RenderUtil.Render2D.drawRoundedRect(x + 10, y + 9, size + 7, 11 * lines, 3, ColorUtil.rgba(11, 12, 15, 255));
        height += 11 * (lines - 1);
        offset = 0;
        offsetY = 0;
        int i = 0;
        for (BooleanOption mode : set.options) {

            float preOff = offset + Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
            if (preOff > size) {
                offset = 0;
                offsetY += 11;
            }
            if (set.get(i)) {
                int finalOffset = offset;
                float finalOffsetY = offsetY;
                BloomHelper.registerRenderCall(() -> {
                    Fonts.msSemiBold[11].drawString(stack, mode.getName(), x + 15 + finalOffset, y + 14f + finalOffsetY, ColorUtil.rgba(119, 121, 134, 255));
                });
                Fonts.msSemiBold[11].drawString(stack, mode.getName(), x + 15 + offset, y + 14f + offsetY, ColorUtil.rgba(119, 121, 134, 255));
            } else
                Fonts.msSemiBold[11].drawString(stack, mode.getName(), x + 15 + offset, y + 14f + offsetY, ColorUtil.rgba(26, 30, 41, 255));
            offset += Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
            i++;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float offset = 0;
        float offsetY = 0;
        int i = 0;
        float size = 0;
        for (BooleanOption mode : set.options) {

            float preOffset = size + Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
            if (preOffset > width - 20) {
                break;
            }
            size += Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
        }

        for (BooleanOption mode : set.options) {
            float preOff = offset + Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
            if (preOff > size) {
                offset = 0;
                offsetY += 11;
            }
            if (RenderUtil.isInRegion(mouseX, mouseY, x + 15 + offset, y + 12f + offsetY, Fonts.msSemiBold[11].getWidth(mode.getName()), Fonts.msSemiBold[11].getFontHeight() / 2f + 3)) {
                set.set(i, !set.get(i));
            }

            offset += Fonts.msSemiBold[11].getWidth(mode.getName()) + 3;
            i++;
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
