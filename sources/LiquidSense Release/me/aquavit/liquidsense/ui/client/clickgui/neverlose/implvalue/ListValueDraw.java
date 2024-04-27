package me.aquavit.liquidsense.ui.client.clickgui.neverlose.implvalue;

import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.module.CustomModule;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.ListValue;
import me.aquavit.liquidsense.value.Value;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ListValueDraw {
    private CustomModule nModule;
    private ListValue values;
    private boolean isClick = false;

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isClick() {
        return isClick;
    }

    public ListValueDraw(CustomModule module, Value<?> value) {
        this.nModule = module;
        this.values = (ListValue) value;
    }

    public void draw() {
        if (nModule.main.hovertoFloatL(nModule.positionX + 75f, nModule.positionY + 22f + nModule.valuePosY, nModule.positionX + 169f, nModule.positionY + 31f + nModule.valuePosY, nModule.mouseX, nModule.mouseY, true) && isClick) values.openList = !values.openList;

        int borderColor;
        int textColor;
        int rectColor;

        switch (Impl.hue) {
            case "black":
                borderColor = new Color(13, 13, 13).getRGB();
                textColor = new Color(175, 175, 175).getRGB();
                rectColor = new Color(13, 13, 13).getRGB();
                break;
            case "white":
                borderColor = new Color(255, 255, 255).getRGB();
                textColor = new Color(90, 90, 90).getRGB();
                rectColor = new Color(255, 255, 255).getRGB();
                break;
            default:
                borderColor = new Color(7, 14, 22).getRGB();
                textColor = new Color(175, 175, 175).getRGB();
                rectColor = new Color(7, 24, 34).getRGB();
                break;
        }

        RenderUtils.drawRectBordered(nModule.positionX + 85.0, nModule.positionY + 21.0 + nModule.valuePosY, nModule.positionX + 170.0, nModule.positionY + 34.0 + nModule.valuePosY, 0.8,
                borderColor,
                new Color(21, 31, 41, values.openList ? 0 : 100).getRGB());

        Fonts.font15.drawString(values.getName(), nModule.positionX + 6, nModule.positionY + 26 + nModule.valuePosY,
                textColor);

        nModule.main.drawText(values.get(), 99, Fonts.font16, nModule.positionX + 88, nModule.positionY + 26f + nModule.valuePosY,
                textColor);

        if (values.openList) RenderUtils.drawRect(nModule.positionX + 95f, nModule.positionY + 33f + nModule.valuePosY, nModule.positionX + 155f,
                    nModule.positionY + 35f + nModule.valuePosY, rectColor);

        nModule.valuePosY += (int) nModule.module.getOpenValue().getY();

        for (String valueOf : Arrays.stream(values.getValues()).filter(it -> !it.startsWith(values.get())).collect(Collectors.toList())) {
            if (values.openList) {
                boolean hover = nModule.main.hovertoFloatL(nModule.positionX + 86f, nModule.positionY + 13.5f + nModule.valuePosY, nModule.positionX + 169,
                        nModule.positionY + 25f + nModule.valuePosY, nModule.mouseX, nModule.mouseY, false);

                if (nModule.main.hovertoFloatL(nModule.positionX + 86f, nModule.positionY + 13.5f + nModule.valuePosY, nModule.positionX + 155f,
                        nModule.positionY + 25f + nModule.valuePosY, nModule.mouseX, nModule.mouseY, true) && isClick()) {
                    values.set(valueOf);
                    values.openList = !values.openList;
                }

                int hoverColor;
                int stringColor;

                switch (Impl.hue) {
                    case "black":
                        hoverColor = new Color(13, 13, 13).getRGB();
                        stringColor = hover ? new Color(255, 255, 255).getRGB() : new Color(175, 175, 175).getRGB();
                        break;
                    case "white":
                        hoverColor = new Color(255, 255, 255).getRGB();
                        stringColor = hover ? new Color(1, 1, 1).getRGB() : new Color(90, 90, 90).getRGB();
                        break;
                    default:
                        hoverColor = hover ? new Color(10, 22, 34).getRGB() : new Color(4, 12, 19).getRGB();
                        stringColor = hover ? new Color(255, 255, 255).getRGB() : new Color(175, 175, 175).getRGB();
                        break;
                }

                RenderUtils.drawRect(nModule.positionX + 86f, nModule.positionY + 13f + nModule.valuePosY, nModule.positionX + 169f,
                        nModule.positionY + 25f + nModule.valuePosY, hoverColor);

                nModule.main.drawText(valueOf, 14, Fonts.font15, nModule.positionX + 88f, nModule.positionY + 17 + nModule.valuePosY,
                        stringColor);

                nModule.valuePosY += 12;
                nModule.module.setOutvalue(nModule.module.getOutvalue() + 1);

            }
        }
    }

}
