package net.optifine.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.optifine.Lang;
import net.optifine.gui.TooltipProviderOptions;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.shaders.gui.GuiButtonShaderOption;
import net.optifine.util.StrUtils;

public class TooltipProviderShaderOptions
extends TooltipProviderOptions {
    public String[] getTooltipLines(GuiButton btn, int width) {
        if (!(btn instanceof GuiButtonShaderOption)) {
            return null;
        }
        GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)btn;
        ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
        String[] astring = this.makeTooltipLines(shaderoption, width);
        return astring;
    }

    private String[] makeTooltipLines(ShaderOption so, int width) {
        String s = so.getNameText();
        String s1 = Config.normalize((String)so.getDescriptionText()).trim();
        Object[] astring = this.splitDescription(s1);
        GameSettings gamesettings = Config.getGameSettings();
        String s2 = null;
        if (!s.equals((Object)so.getName()) && gamesettings.advancedItemTooltips) {
            s2 = "\u00a78" + Lang.get((String)"of.general.id") + ": " + so.getName();
        }
        String s3 = null;
        if (so.getPaths() != null && gamesettings.advancedItemTooltips) {
            s3 = "\u00a78" + Lang.get((String)"of.general.from") + ": " + Config.arrayToString((Object[])so.getPaths());
        }
        String s4 = null;
        if (so.getValueDefault() != null && gamesettings.advancedItemTooltips) {
            String s5 = so.isEnabled() ? so.getValueText(so.getValueDefault()) : Lang.get((String)"of.general.ambiguous");
            s4 = "\u00a78" + Lang.getDefault() + ": " + s5;
        }
        ArrayList list = new ArrayList();
        list.add((Object)s);
        list.addAll((Collection)Arrays.asList((Object[])astring));
        if (s2 != null) {
            list.add((Object)s2);
        }
        if (s3 != null) {
            list.add((Object)s3);
        }
        if (s4 != null) {
            list.add((Object)s4);
        }
        String[] astring1 = this.makeTooltipLines(width, (List<String>)list);
        return astring1;
    }

    private String[] splitDescription(String desc) {
        if (desc.length() <= 0) {
            return new String[0];
        }
        desc = StrUtils.removePrefix((String)desc, (String)"//");
        String[] astring = desc.split("\\. ");
        for (int i = 0; i < astring.length; ++i) {
            astring[i] = "- " + astring[i].trim();
            astring[i] = StrUtils.removeSuffix((String)astring[i], (String)".");
        }
        return astring;
    }

    private String[] makeTooltipLines(int width, List<String> args) {
        FontRenderer fontrenderer = Config.getMinecraft().fontRendererObj;
        ArrayList list = new ArrayList();
        for (int i = 0; i < args.size(); ++i) {
            String s = (String)args.get(i);
            if (s == null || s.length() <= 0) continue;
            for (String s1 : fontrenderer.listFormattedStringToWidth(s, width)) {
                list.add((Object)s1);
            }
        }
        String[] astring = (String[])list.toArray((Object[])new String[list.size()]);
        return astring;
    }
}
