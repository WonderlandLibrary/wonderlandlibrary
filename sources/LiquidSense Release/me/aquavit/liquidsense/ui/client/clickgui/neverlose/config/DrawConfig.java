package me.aquavit.liquidsense.ui.client.clickgui.neverlose.config;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.ui.font.Fonts;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.File;

public class DrawConfig {
    public void flie(int mouseX, int mouseY, Main main) {
        File[] file = LiquidSense.fileManager.settingsDir.listFiles();
        if (file == null) return;

        // Save background
        boolean hoverSave = main.hoverConfig(Impl.coordinateX + 105, Impl.coordinateY + 8, Impl.coordinateX + 153, Impl.coordinateY + 25, mouseX, mouseY, false);
        RenderUtils.drawRectBordered(Impl.coordinateX + 105.0, Impl.coordinateY + 8.0, Impl.coordinateX + 153.0, Impl.coordinateY + 25.0, 0.5,
                hoverSave ? new Color(15, 13, 18).getRGB() : new Color(9, 8, 12).getRGB(), new Color(16, 19, 26).getRGB());
        Fonts.font17.drawString("Save", Impl.coordinateX + 131f, Impl.coordinateY + 15f, new Color(200, 200, 200).getRGB());

        if (hoverSave && !main.mouseLDown && Mouse.isButtonDown(0)) {
            LiquidSense.commandManager.executeCommands(LiquidSense.commandManager.getPrefix() + "localsetting save " + Impl.configname + " all");
        }

        // Config background
        RenderUtils.drawRectBordered(Impl.coordinateX + 160.0, Impl.coordinateY + 8.0, Impl.coordinateX + 230.0,
                Impl.coordinateY + 25.0 + (main.openConfig ? (file.length * 15) : 0), 0.5, new Color(2, 5, 12).getRGB(),
                new Color(16, 19, 26).getRGB());

        // Load background
        boolean hoverLoad = main.hoverConfig(Impl.coordinateX + 237f, Impl.coordinateY + 8f, Impl.coordinateX + 285f, Impl.coordinateY + 25f, mouseX, mouseY, false);
        RenderUtils.drawRectBordered(Impl.coordinateX + 237.0, Impl.coordinateY + 8.0, Impl.coordinateX + 285.0, Impl.coordinateY + 25.0, 0.5,
                hoverLoad ? new Color(8, 15, 24).getRGB() : new Color(2, 5, 12).getRGB(), new Color(16, 19, 26).getRGB());

        if (hoverLoad && !main.mouseLDown && Mouse.isButtonDown(0)) {
            LiquidSense.commandManager.executeCommands(LiquidSense.commandManager.getPrefix() + "localsetting load " + Impl.configname);
        }

        Fonts.font17.drawString("Load", Impl.coordinateX + 257f, Impl.coordinateY + 15f, new Color(200, 200, 200).getRGB());

        main.drawText(Impl.configname, 13, Fonts.font17, (int) Impl.coordinateX + 163, (int) Impl.coordinateY + 15, new Color(200, 200, 200).getRGB());

        if (main.hoverConfig(Impl.coordinateX + 160f, Impl.coordinateY + 10f, Impl.coordinateX + 230f, Impl.coordinateY + 25f, mouseX, mouseY, true)) {
            main.openConfig = !main.openConfig;
        }

        if (main.openConfig) {
            int fileHeight = 0;
            for (File json : file) {
                boolean hover = main.hoverConfig(Impl.coordinateX + 160f, Impl.coordinateY + 25f + fileHeight, Impl.coordinateX + 230f, Impl.coordinateY + 40f + fileHeight, mouseX, mouseY, false);
                if (main.hoverConfig(Impl.coordinateX + 160f, Impl.coordinateY + 25f + fileHeight, Impl.coordinateX + 230f, Impl.coordinateY + 40f + fileHeight, mouseX, mouseY, true)) {
                    Impl.configname = json.getName();
                    main.openConfig = !main.openConfig;
                }
                if (hover) {
                    RenderUtils.drawRect(Impl.coordinateX + 160.0, Impl.coordinateY + 25.0 + fileHeight, Impl.coordinateX + 230.0,
                            Impl.coordinateY + 40.0 + fileHeight, new Color(16, 19, 26).getRGB());
                }
                main.drawText(json.getName(), 13, Fonts.font16, (int) Impl.coordinateX + 163, (int) Impl.coordinateY + 31 + fileHeight, new Color(200, 200, 200).getRGB());
                fileHeight += 15;
            }
        }
    }
}

