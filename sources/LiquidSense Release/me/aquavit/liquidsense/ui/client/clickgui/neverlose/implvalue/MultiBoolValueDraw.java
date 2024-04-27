package me.aquavit.liquidsense.ui.client.clickgui.neverlose.implvalue;

import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.module.CustomModule;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.MultiBoolValue;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class MultiBoolValueDraw {
    private CustomModule nModule;
    private MultiBoolValue values;

    private boolean isClick = false;

    public MultiBoolValueDraw(CustomModule module, Value<?> value) {
        nModule = module;
        values = (MultiBoolValue) value;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public void draw() {
        int index = 0;

        if (nModule.main.hovertoFloatL(nModule.positionX + 75f, nModule.positionY + 22f + nModule.valuePosY, nModule.positionX + 169f, nModule.positionY + 31f + nModule.valuePosY, nModule.mouseX, nModule.mouseY, true) && isClick()) values.openList = !values.openList;

        int borderColor;
        int textColor;
        int rectColor;

        switch (Impl.hue) {
            case "black":
                borderColor = new Color(13, 13, 13).getRGB();
                textColor = new Color(200, 200, 200).getRGB();
                rectColor = new Color(13, 13, 13).getRGB();
                break;
            case "white":
                borderColor = new Color(255, 255, 255).getRGB();
                textColor = new Color(90, 90, 90).getRGB();
                rectColor = new Color(255, 255, 255).getRGB();
                break;
            default:
                borderColor = new Color(7, 14, 22).getRGB();
                textColor = new Color(200, 200, 200).getRGB();
                rectColor = new Color(7, 24, 34).getRGB();
                break;
        }

        RenderUtils.drawRectBordered(nModule.positionX + 85.0, nModule.positionY + 21.0 + nModule.valuePosY, nModule.positionX + 170.0,
                nModule.positionY + 34.0 + nModule.valuePosY, 0.8, borderColor,
                new Color(21, 31, 41, (values.openList) ? 0 : 100).getRGB());

        GlStateManager.resetColor();

        Fonts.font15.drawString(values.getName(), nModule.positionX + 6, nModule.positionY + 25 + nModule.valuePosY, textColor);

        if (values.openList) {
            RenderUtils.drawRect(nModule.positionX + 95f, nModule.positionY + 33f + nModule.valuePosY, nModule.positionX + 155f,
                    nModule.positionY + 35f + nModule.valuePosY, rectColor);
        }

        nModule.valuePosY += (int) nModule.module.getOpenValue().getY();

        String enablerString = "";
        for (int valuesOfList = 0; valuesOfList <= values.values.length - 1; valuesOfList++) {

            if (values.value[valuesOfList]) enablerString += values.values[valuesOfList] + " ";

            if (values.openList) {
                boolean hover = nModule.main.hovertoFloatL(nModule.positionX + 95f, nModule.positionY + 13.5f + nModule.valuePosY, nModule.positionX + 169f, nModule.positionY + 25f + nModule.valuePosY, nModule.mouseX, nModule.mouseY, false);

                if (nModule.main.hovertoFloatL(nModule.positionX + 95f, nModule.positionY + 13.5f + nModule.valuePosY,
                        nModule.positionX + 169f, nModule.positionY + 25f + nModule.valuePosY, nModule.mouseX, nModule.mouseY, true) && isClick()) values.value[valuesOfList] = !values.value[valuesOfList];

                int selectColor;
                int textSelectColor;

                switch (Impl.hue) {
                    case "black":
                        selectColor = new Color(13, 13, 13).getRGB();
                        textSelectColor = values.value[valuesOfList] ? new Color(255, 255, 255).getRGB() : new Color(175, 175, 175).getRGB();
                        break;
                    case "white":
                        selectColor = new Color(255, 255, 255).getRGB();
                        textSelectColor = values.value[valuesOfList] ? new Color(1, 1, 1).getRGB() : new Color(90, 90, 90).getRGB();
                        break;
                    default:
                        selectColor = hover ? new Color(8, 18, 30).getRGB() : new Color(4, 12, 19).getRGB();
                        textSelectColor = values.value[valuesOfList] ? new Color(70, 111, 255).getRGB() : new Color(175, 175, 175).getRGB();
                        break;
                }

                RenderUtils.drawRect(nModule.positionX + 86f, nModule.positionY + 13f + nModule.valuePosY, nModule.positionX + 169f,
                        nModule.positionY + 25f + nModule.valuePosY, selectColor);

                nModule.main.drawText(values.values[valuesOfList], 14, Fonts.font15, nModule.positionX + 88, nModule.positionY + 17 + nModule.valuePosY
                        , textSelectColor);

                nModule.valuePosY += 12;
                nModule.module.setOutvalue(nModule.module.getOutvalue() + 1);
                index++;

            }
        }

        nModule.main.drawText(enablerString, 19, Fonts.font16, nModule.positionX + 88,
                nModule.positionY + 6 + nModule.valuePosY - (12 * index), new Color(175, 175, 175).getRGB());

    }
}
