package me.aquavit.liquidsense.ui.client.clickgui.neverlose.implvalue;

import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.module.CustomModule;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class BooleanValueDraw {
    private CustomModule nModule;
    private BoolValue values;
    private boolean isClick = false;

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isClick() {
        return isClick;
    }

    public BooleanValueDraw(CustomModule n, Value<?> value) {
        this.nModule = n;
        this.values = (BoolValue)value;
    }

    public void draw() {
        if (nModule.main.hovertoFloatL(nModule.positionX + 150, nModule.positionY + 21 + nModule.valuePosY, nModule.positionX + 170f,
                nModule.positionY + 33f + nModule.valuePosY, nModule.mouseX, nModule.mouseY, true) && isClick()) values.set(!values.get());

        values.boolvalue.translate(values.get() ? 10f : 0f, 0f);

        Color rectColor;
        Color circleColor;
        Color textColor;

        switch (Impl.hue) {
            case "blue":
            case "black":
                rectColor = values.get() ? new Color(3, 23, 46) : new Color(7, 19, 31);
                circleColor = values.get() ? new Color(3, 168, 245) : new Color(74, 87, 97);
                textColor = values.get() ? new Color(255, 255, 255) : new Color(175, 175, 175);
                break;
            default:
                rectColor = values.get() ? new Color(0, 120, 194) : new Color(230, 230, 230);
                circleColor = values.get() ? new Color(94, 222, 255) : new Color(255, 255, 255);
                textColor = values.get() ? new Color(1, 1, 1) : new Color(90, 90, 90);
                break;
        }

        RenderUtils.drawNLRect(nModule.positionX + 153, nModule.positionY + 27 + nModule.valuePosY, nModule.positionX + 167f,
                nModule.positionY + 33f + nModule.valuePosY, 2.1f, rectColor.getRGB());
        RenderUtils.drawFullCircle(nModule.positionX + 155 + values.boolvalue.getX(), nModule.positionY + 30 + nModule.valuePosY, 3.5f, 0f, circleColor);
        GlStateManager.resetColor();

        Fonts.font15.drawString(values.name, nModule.positionX + 6, nModule.positionY + 27 + nModule.valuePosY, textColor.getRGB());


        nModule.valuePosY += (int) nModule.module.getOpenValue().getY();
    }

}

