// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import exhibition.util.misc.ChatUtil;
import java.util.Iterator;
import java.util.List;
import exhibition.util.FileUtils;
import exhibition.management.ColorManager;
import java.util.ArrayList;
import java.io.File;
import exhibition.management.command.Command;

public class Color extends Command
{
    private static final File COLOR_DIR;
    
    public static void saveStatus() {
        final List<String> fileContent = new ArrayList<String>();
        fileContent.add(String.format("%s:%s:%s:%s:%s", "friendlyVisible", ColorManager.fVis.getRed(), ColorManager.fVis.getGreen(), ColorManager.fVis.getBlue(), ColorManager.fVis.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "friendlyInvisible", ColorManager.fInvis.getRed(), ColorManager.fInvis.getGreen(), ColorManager.fInvis.getBlue(), ColorManager.fInvis.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "enemyVisible", ColorManager.eVis.getRed(), ColorManager.eVis.getGreen(), ColorManager.eVis.getBlue(), ColorManager.eVis.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "enemyInvisible", ColorManager.eInvis.getRed(), ColorManager.eInvis.getGreen(), ColorManager.eInvis.getBlue(), ColorManager.eInvis.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "friendlyTeam", ColorManager.fTeam.getRed(), ColorManager.fTeam.getGreen(), ColorManager.fTeam.getBlue(), ColorManager.fTeam.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "enemyTeam", ColorManager.eTeam.getRed(), ColorManager.eTeam.getGreen(), ColorManager.eTeam.getBlue(), ColorManager.eTeam.getAlpha()));
        fileContent.add(String.format("%s:%s:%s:%s:%s", "hudColor", ColorManager.hudColor.getRed(), ColorManager.hudColor.getGreen(), ColorManager.hudColor.getBlue(), ColorManager.hudColor.getAlpha()));
        FileUtils.write(Color.COLOR_DIR, fileContent, true);
    }
    
    public static void loadStatus() {
        try {
            final List<String> fileContent = FileUtils.read(Color.COLOR_DIR);
            for (final String line : fileContent) {
                final String[] split = line.split(":");
                final String object = split[0];
                final int red = Integer.parseInt(split[1]);
                final int green = Integer.parseInt(split[2]);
                final int blue = Integer.parseInt(split[3]);
                final int alpha = Integer.parseInt(split[4]);
                final String s = object;
                switch (s) {
                    case "friendlyVisible": {
                        ColorManager.fVis.updateColors(red, green, blue, alpha);
                        continue;
                    }
                    case "friendlyInvisible": {
                        ColorManager.fInvis.updateColors(red, green, blue, alpha);
                        continue;
                    }
                    case "enemyVisible": {
                        ColorManager.eVis.updateColors(red, green, blue, alpha);
                        continue;
                    }
                    case "enemyInvisible": {
                        ColorManager.eInvis.updateColors(red, green, blue, alpha);
                        continue;
                    }
                    case "friendlyTeam": {
                        ColorManager.eTeam.updateColors(red, green, blue, alpha);
                        continue;
                    }
                    case "enemyTeam": {
                        ColorManager.fTeam.updateColors(red, green, blue, alpha);
                        continue;
                    }
                    case "hudColor": {
                        ColorManager.hudColor.updateColors(red, green, blue, alpha);
                        continue;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Color(final String[] names, final String description) {
        super(names, description);
        loadStatus();
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        if (args.length < 2) {
            this.printUsage();
            return;
        }
        final String[] color = args[1].split(":");
        if (color.length < 4) {
            this.printUsage();
            return;
        }
        final int red = Integer.parseInt(color[0]);
        final int green = Integer.parseInt(color[1]);
        final int blue = Integer.parseInt(color[2]);
        final int alpha = Integer.parseInt(color[3]);
        final String s = args[0];
        switch (s) {
            case "fv": {
                ColorManager.fVis.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("§4[§cE§4]§8 Color set: §c" + red + "  §a" + green + "  §b" + blue + "  §f" + alpha);
                saveStatus();
            }
            case "fi": {
                ColorManager.fInvis.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("§4[§cE§4]§8 Color set: §c" + red + "  §a" + green + "  §b" + blue + "  §f" + alpha);
                saveStatus();
            }
            case "ev": {
                ColorManager.eVis.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("§4[§cE§4]§8 Color set: §c" + red + "  §a" + green + "  §b" + blue + "  §f" + alpha);
                saveStatus();
            }
            case "ei": {
                ColorManager.eInvis.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("§4[§cE§4]§8 Color set: §c" + red + "  §a" + green + "  §b" + blue + "  §f" + alpha);
                saveStatus();
            }
            case "et": {
                ColorManager.eTeam.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("§4[§cE§4]§8 Color set: §c" + red + "  §a" + green + "  §b" + blue + "  §f" + alpha);
                saveStatus();
            }
            case "ft": {
                ColorManager.fTeam.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("§4[§cE§4]§8 Color set: §c" + red + "  §a" + green + "  §b" + blue + "  §f" + alpha);
                saveStatus();
            }
            case "hc": {
                ColorManager.hudColor.updateColors(red, green, blue, alpha);
                ChatUtil.printChat("§4[§cE§4]§8 Color set: §c" + red + "  §a" + green + "  §b" + blue + "  §f" + alpha);
                saveStatus();
                break;
            }
        }
        this.printUsage();
    }
    
    @Override
    public String getUsage() {
        return "object <fv | ev | fi | ei | hd | et | ft> color <r:g:b:a>";
    }
    
    static {
        COLOR_DIR = FileUtils.getConfigFile("Colors");
    }
}
