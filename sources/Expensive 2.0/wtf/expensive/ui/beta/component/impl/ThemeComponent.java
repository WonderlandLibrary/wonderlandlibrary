package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector4f;
import wtf.expensive.config.Config;
import wtf.expensive.managment.Managment;
import wtf.expensive.ui.beta.component.ColorThemeWindow;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;

public class ThemeComponent extends Component {

    public Style config;
    public ColorThemeWindow[] colors = new ColorThemeWindow[2];
    public static ColorThemeWindow selected = null;

    public boolean opened;


    public ThemeComponent(Style config) {
        this.config = config;
        if (config.name.equalsIgnoreCase("Свой")) {
            colors[0] = new ColorThemeWindow(new Color(config.colors[0]), new Vector4f(0, 0, 0, 0));
            colors[1] = new ColorThemeWindow(new Color(config.colors[1]), new Vector4f(0, 0, 0, 0));
        }
    }

    @Override
    public void onConfigUpdate() {
        super.onConfigUpdate();
        for (ColorThemeWindow colorThemeWindow : colors) {
            if (colorThemeWindow != null)
                colorThemeWindow.onConfigUpdate();
        }

        if (config.name.equalsIgnoreCase("Свой")) {
            if (colors.length >= 2) {
                colors[0] = new ColorThemeWindow(new Color(config.colors[0]), new Vector4f(0, 0, 0, 0));
                colors[1] = new ColorThemeWindow(new Color(config.colors[1]), new Vector4f(0, 0, 0, 0));
            }
        }

    }

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (opened) {
            height += 12;
        }
        if (!(colors[0] == null && colors[1] == null)) {
            config.colors[0] = colors[0].getColor();
            config.colors[1] = colors[1].getColor();
        }


        RenderUtil.Render2D.drawShadow(x + 3, y, width - 6, height, 12, new Color(32, 36, 42).darker().darker().getRGB());
        RenderUtil.Render2D.drawRoundedCorner(x + 3, y, width - 5, height, 4, Managment.STYLE_MANAGER.getCurrentStyle() == config ? new Color(32, 36, 42).brighter().getRGB() : new Color(32, 36, 42).getRGB());
        Fonts.gilroy[16].drawString(matrixStack, config.name, x + 8, y + 8.5f, -1);

        int color1 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 0, 0.7f, 1, 1) : config.getColor(0);
        int color2 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 90, 0.7f, 1, 1) : config.getColor(90);
        int color3 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 180, 0.7f, 1, 1) : config.getColor(180);
        int color4 = config.name.contains("Разноцветный") ? ColorUtil.astolfo(10, 270, 0.7f, 1, 1) : config.getColor(270);

        RenderUtil.Render2D.drawGradientRound(x + width - 25, y + 6, 15, 9, 1, color1, color2, color3, color4);

        if (opened) {
            RenderUtil.Render2D.drawRect(x + 10, y + 19, 8, 8, colors[0].getColor());
            RenderUtil.Render2D.drawRect(x + 10 + 12, y + 19, 8, 8, colors[1].getColor());
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY, 20)) {
            Managment.STYLE_MANAGER.setCurrentStyle(config);
        }
        if (isHovered(mouseX, mouseY, 20) && config.name.equalsIgnoreCase("Свой") && mouseButton == 1) {
            opened = !opened;
        }
        if (opened) {
            if (RenderUtil.isInRegion(mouseX, mouseY, x + 10, y + 19, 8, 8)) {
                if (selected == colors[0]) {
                    selected = null;
                    return;
                }
                selected = colors[0];
                selected.pos = new Vector4f(mouseX, mouseY, 0, 0);
            }
            if (RenderUtil.isInRegion(mouseX, mouseY, x + 10 + 12, y + 19, 8, 8)) {
                if (selected == colors[1]) {
                    selected = null;
                    return;
                }
                selected = colors[1];
                selected.pos = new Vector4f(mouseX, mouseY, 0, 0);
            }
        }
        if (selected != null)
            selected.click(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (selected != null)
            selected.unclick(mouseX, mouseY);
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }
}
