/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.management.ColorManager;
import me.arithmo.management.ColorObject;
import me.arithmo.management.command.Command;
import me.arithmo.util.FileUtils;
import me.arithmo.util.misc.ChatUtil;

public class Color
extends Command {
    private static final File COLOR_DIR = FileUtils.getConfigFile("Colors");

    public static void saveStatus() {
        ArrayList<String> fileContent = new ArrayList<String>();
        fileContent.add(String.format("%s:%s:%s:%s:%s", "hudColor", ColorManager.hudColor.getRed(), ColorManager.hudColor.getGreen(), ColorManager.hudColor.getBlue(), ColorManager.hudColor.getAlpha()));
        FileUtils.write(COLOR_DIR, fileContent, true);
    }

    public static void loadStatus() {
        try {
            List<String> fileContent = FileUtils.read(COLOR_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                String object = split[0];
                int red = Integer.parseInt(split[1]);
                int green = Integer.parseInt(split[2]);
                int blue = Integer.parseInt(split[3]);
                int alpha = Integer.parseInt(split[4]);
                switch (object) {
                    case "hudColor": {
                        ColorManager.hudColor.updateColors(red, green, blue, alpha);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Color(String[] names, String description) {
        super(names, description);
        Color.loadStatus();
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        if (args.length < 2) {
            this.printUsage();
            return;
        }
        String[] color = args[1].split(":");
        if (color.length < 4) {
            this.printUsage();
            return;
        }
        int red = Integer.parseInt(color[0]);
        int green = Integer.parseInt(color[1]);
        int blue = Integer.parseInt(color[2]);
        int alpha = Integer.parseInt(color[3]);
        switch (args[0]) {
            case "hc": {
                ColorManager.hudColor.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 Color set: \u00a7c" + red + "  \u00a7a" + green + "  \u00a7b" + blue + "  \u00a7f" + alpha);
                Color.saveStatus();
                break;
            }
            default: {
                this.printUsage();
            }
        }
    }

    @Override
    public String getUsage() {
        return "object <fv | ev | fi | ei | hc > color <r:g:b:a>";
    }

    public void onEvent(Event event) {
    }
}

