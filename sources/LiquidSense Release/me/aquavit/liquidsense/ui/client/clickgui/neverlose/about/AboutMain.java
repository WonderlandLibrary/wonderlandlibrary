package me.aquavit.liquidsense.ui.client.clickgui.neverlose.about;

import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.ui.font.Fonts;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Year;

public class AboutMain extends Main {

    public static void drawAbout(int mouseX, int mouseY, Main main) {
        if (main.hoverConfig(Impl.coordinateX + 410, Impl.coordinateY + 14, Impl.coordinateX + 420, Impl.coordinateY + 26, mouseX, mouseY, true)) {
            Impl.openAbout = !Impl.openAbout;
        }
        if (Impl.openAbout) return;
        Color bgColor;
        switch (Impl.hue) {
            case "white":
                bgColor = new Color(255, 255, 255, 230);
                break;
            case "black":
                bgColor = new Color(11, 11, 11, 230);
                break;
            case "blue":
                bgColor = new Color(6, 16, 28, 245);
                break;
            default:
                bgColor = new Color(6, 16, 28, 245);
                break;
        }
        Color lineColor;
        switch (Impl.hue) {
            case "white":
                lineColor = new Color(217, 217, 217);
                break;
            case "black":
                lineColor = new Color(27, 29, 31);
                break;
            case "blue":
                lineColor = new Color(5, 26, 38);
                break;
            default:
                lineColor = new Color(5, 26, 38);
                break;
        }
        RenderUtils.drawNLRect(Impl.coordinateX + 470, Impl.coordinateY + 70, Impl.coordinateX + 630, Impl.coordinateY + 220, 2f, bgColor.getRGB()); //背景
        RenderUtils.drawRect(Impl.coordinateX + 470, Impl.coordinateY + 84, Impl.coordinateX + 630, Impl.coordinateY + 85, lineColor.getRGB()); // 分割线
        RenderUtils.drawRect(Impl.coordinateX + 470, Impl.coordinateY + 179, Impl.coordinateX + 630, Impl.coordinateY + 180, lineColor.getRGB()); // 分割线
        RenderUtils.drawRect(Impl.coordinateX + 470, Impl.coordinateY + 124, Impl.coordinateX + 630, Impl.coordinateY + 125, lineColor.getRGB()); // 分割线
        Color textColor;
        switch (Impl.hue) {
            case "white":
                textColor = new Color(8, 8, 8);
                break;
            case "black":
                textColor = new Color(255, 255, 255);
                break;
            case "blue":
                textColor = new Color(255, 255, 255);
                break;
            default:
                textColor = new Color(255, 255, 255);
                break;
        }

        if (!Impl.hue.equals("white")) Fonts.bold25.drawCenteredString("LIQUIDSENSE.CC", Impl.coordinateX + 548, Impl.coordinateY + 100, new Color(11, 160, 255).getRGB(), false);
        Fonts.bold25.drawCenteredString("LIQUIDSENSE.CC", Impl.coordinateX + 549, Impl.coordinateY + 100, Impl.hue.equals("white") ? new Color(51, 51, 51).getRGB() : new Color(255, 255, 255).getRGB(), false);
        Fonts.font16.drawString("About LiquidSense", Impl.coordinateX + 485, Impl.coordinateY + 75, textColor.getRGB());

        Fonts.font16.drawString("Version:", Impl.coordinateX + 475, Impl.coordinateY + 130, textColor.getRGB());
        Fonts.font16.drawString("BuildType:", Impl.coordinateX + 475, Impl.coordinateY + 142, textColor.getRGB());
        Fonts.font16.drawString("Date:", Impl.coordinateX + 475, Impl.coordinateY + 154, textColor.getRGB());
        Fonts.font16.drawCenteredString(LiquidSense.CLIENT_WEB+" @"+ Year.now().getValue(), Impl.coordinateX + 548, Impl.coordinateY + 169, textColor.getRGB(), false);

        Color styleColor;
        switch (Impl.hue) {
            case "white":
                styleColor = new Color(105, 105, 105);
                break;
            case "black":
                styleColor = new Color(133, 155, 168);
                break;
            case "blue":
                styleColor = new Color(133, 155, 168);
                break;
            default:
                styleColor = new Color(133, 155, 168);
                break;
        }

        Fonts.font16.drawString("Style", Impl.coordinateX + 475, Impl.coordinateY + 200, styleColor.getRGB());


        Fonts.csgo40.drawString("G", Impl.coordinateX + 472, Impl.coordinateY + 74, new Color(11, 160, 255).getRGB(), false);

        Fonts.font16.drawString(LiquidSense.CLIENT_VERSION, Impl.coordinateX + 475 + Fonts.font16.getStringWidth("Version: "),
                Impl.coordinateY + 130, new Color(24, 179, 246).getRGB());

        Fonts.font16.drawString((LiquidSense.IN_DEV) ? "Dev" : "Release", Impl.coordinateX + 475 + Fonts.font16.getStringWidth("BuildType: "),
                Impl.coordinateY + 142, new Color(24, 179, 246).getRGB());

        Fonts.font16.drawString(new SimpleDateFormat("dd.MM.YY").format(System.currentTimeMillis()), Impl.coordinateX + 475 + Fonts.font16.getStringWidth("Date: "),
                Impl.coordinateY + 154, new Color(24, 179, 246).getRGB());

        Impl.hue = main.hoverConfig(
                Impl.coordinateX + 574, Impl.coordinateY + 200,
                Impl.coordinateX + 587, Impl.coordinateY + 210,
                mouseX, mouseY, true
        ) ? "blue" : main.hoverConfig(
                Impl.coordinateX + 594, Impl.coordinateY + 200,
                Impl.coordinateX + 607, Impl.coordinateY + 210,
                mouseX, mouseY, true
        ) ? "white" : main.hoverConfig(
                Impl.coordinateX + 614, Impl.coordinateY + 200,
                Impl.coordinateX + 627, Impl.coordinateY + 210,
                mouseX, mouseY, true
        ) ? "black" : Impl.hue;

        float x;
        switch (Impl.hue) {
            case "black":
                x = Impl.coordinateX + 620;
                break;
            case "white":
                x = Impl.coordinateX + 600;
                break;
            default:
                x = Impl.coordinateX + 580;
                break;
        }
        RenderUtils.drawNCircle(x, Impl.coordinateY + 203, 5.0f, new Color(24, 179, 246));

        RenderUtils.drawFullCircle(Impl.coordinateX + 580, Impl.coordinateY + 203, 4f, 0f, new Color(7, 50, 93));
        RenderUtils.drawFullCircle(Impl.coordinateX + 600, Impl.coordinateY + 203, 4f, 0f, new Color(255, 255, 255));
        RenderUtils.drawFullCircle(Impl.coordinateX + 620, Impl.coordinateY + 203, 4f, 0f, new Color(1, 1, 1));

    }

}
