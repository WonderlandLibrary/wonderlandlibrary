package wtf.expensive.ui.clickgui.objects.sets;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.joml.Vector4i;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.ui.clickgui.objects.ModuleObject;
import wtf.expensive.ui.clickgui.objects.Object;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;

import static wtf.expensive.ui.clickgui.Window.light;
import static wtf.expensive.ui.clickgui.Window.medium;
import static wtf.expensive.util.render.RenderUtil.Render2D.*;
import static wtf.expensive.util.render.RenderUtil.reAlphaInt;

public class BooleanObject extends Object {

    public ModuleObject object;
    public BooleanOption set;
    public float enabledAnimation;

    public BooleanObject(ModuleObject object, BooleanOption set) {
        this.object = object;
        this.set = set;
        setting = set;
    }

    @Override
    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        super.draw(stack, mouseX, mouseY);
        y-=1;
        double max = !set.get() ? 0 : 6.5f;
        this.enabledAnimation = AnimationMath.fast(enabledAnimation, (float) max, 10);

        Fonts.msLight[13].drawString(stack, set.getName(), x + 10, y + height / 2f - Fonts.msLight[13].getFontHeight() / 2f + 2, ColorUtil.rgba(161, 166, 179,255));
        RenderUtil.Render2D.drawRoundedRect(x + width - 23.5f,y + 5,13.5f, 6, 3,ColorUtil.rgba(20, 21, 24, 255));
        int color = ColorUtil.interpolateColor(ColorUtil.rgba(42, 56, 73, 255), ColorUtil.rgba(127, 134, 154, 255), enabledAnimation / 6.5f);
        RenderUtil.Render2D.drawShadow(x + width - 23 + 3 + enabledAnimation - 2.5f,y + 8 - 2.5f,5, 5, 15, color);
        RenderUtil.Render2D.drawRoundCircle(x + width - 23 + 3 + enabledAnimation,y + 8,5, color);

//        Style current = Managment.STYLE_MANAGER.getCurrentStyle();
//        Vector4i colors = new Vector4i(
//                current.getColor(0),
//                current.getColor(90),
//                current.getColor(180),
//                current.getColor(270)
//        );
//        drawRoundedRect(x + width - 22, y + 4, 17, 9, 4, reAlphaInt(light, 100));
//        drawShadow(x + width - 17 + enabledAnimation - 4, y + 3 + 5.5F - 4, 8, 8, 10,reAlphaInt(ColorUtil.interpolateColor(Color.BLACK.getRGB(), colors.x, enabledAnimation / 7f), 100));
//        drawRoundCircle(x + width - 17 + enabledAnimation, y + 3 + 5.5F, 5,
//                reAlphaInt(ColorUtil.interpolateColor(light, colors.x, enabledAnimation / 7f), 100),
//                reAlphaInt(ColorUtil.interpolateColor(light, colors.y, enabledAnimation / 7f), 100),
//                reAlphaInt(ColorUtil.interpolateColor(light, colors.z, enabledAnimation / 7f), 100),
//                reAlphaInt(ColorUtil.interpolateColor(light, colors.w, enabledAnimation / 7f), 100));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isHovered(mouseX, mouseY)) {
                set.toggle();
            }
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
