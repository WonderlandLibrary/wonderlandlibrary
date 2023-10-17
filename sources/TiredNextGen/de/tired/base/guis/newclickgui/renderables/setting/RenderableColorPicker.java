package de.tired.base.guis.newclickgui.renderables.setting;

import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.base.guis.newclickgui.abstracts.ClickGUIHandler;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorPicker;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;

import java.awt.*;

public class RenderableColorPicker extends ClickGUIHandler {

    private int x, y;

    public final ColorPickerSetting colorPickerSetting;

    private final ColorPicker colorPicker;

    private boolean hover;

    public boolean extended = false;

    public int calcHeight = 20;

    public RenderableColorPicker(ColorPickerSetting colorPickerSetting) {
        this.colorPickerSetting = colorPickerSetting;
        this.colorPicker = new ColorPicker(colorPickerSetting);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {

    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseKey) {
        if (!hover) return;
        if (mouseKey == 1)
            this.extended = !extended;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int x, int y) {
        this.x = x;
        this.y = y;
        //    Gui.drawRect(x, y, x + width, y + calcHeight, new Color(35, 36, 42).getRGB());

        this.hover = isHovered(mouseX, mouseY, x, y, width, 20);

        if (!extended)
            calcHeight = 20;
        else
            calcHeight = 80;

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + 2, y + 4, width - 20, calcHeight, 3, colorPickerSetting.getModule().isState() ? new Color(194, 194, 194, 142) : new Color(47, 47, 47));

        if (extended) {

        }

        RenderUtil.instance.doRenderShadow( colorPickerSetting.ColorPickerC.brighter(), ((x + 2) + (width - 20) / 2) - 10, y + 6, 20, 5, 6);
        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(((x + 2) + (width - 20) / 2) - 10, y + 6, 20, 5, 2, colorPickerSetting.ColorPickerC);
        String name = colorPickerSetting.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        FontManager.interMedium14.drawString(name, calculateMiddle(name, FontManager.interMedium14, x + 2, width - 20), y + 18, !colorPickerSetting.getModule().isState() ? -1 :  new Color(40, 40, 40).getRGB());

        if (extended)
            colorPicker.draw(x + 3, y + 28, 90, 55, mouseX, mouseY, colorPickerSetting.ColorPickerC);


    }
}
