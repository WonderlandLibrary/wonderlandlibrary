// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.font.CFontManager;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.command.Command;

public class CustomFontCommand extends Command
{
    public CustomFontCommand() {
        super("customfont", "Tweaks the custom font", new String[] { "default", "defaultxdolf", "defaultinfo", "currentfont", "currentfontxdolf", "fonts", "setfont", "setfontxdolf" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("help")) {
                this.showSyntax(args[0]);
                return;
            }
            if (args[1].equalsIgnoreCase("default")) {
                Xulu.setcFontRendererDefault();
            }
            if (args[1].equalsIgnoreCase("defaultxdolf")) {
                Xulu.setXdolfFontRendererDefault();
            }
            if (args[1].equalsIgnoreCase("defaultinfo")) {
                Command.sendChatMessage("Font: Verdana, Size: 18");
            }
            if (args[1].equalsIgnoreCase("currentfont")) {
                Command.sendChatMessage("Font: " + CFontManager.customFont.getFont().getFontName() + ", Size: " + CFontManager.customFont.getFont().getSize());
            }
            if (args[1].equalsIgnoreCase("currentfontxdolf")) {
                Command.sendChatMessage("Font: " + CFontManager.xFontRenderer.getFont().getFont().getName() + ", Size: " + CFontManager.xFontRenderer.getFont().getFont().getSize());
            }
            if (args[1].equalsIgnoreCase("fonts")) {
                Command.sendChatMessage("Fonts:");
                String out = "";
                boolean start = true;
                for (final String s : Xulu.getFonts()) {
                    if (start) {
                        out = s;
                    }
                    else {
                        out = out + ", " + s;
                    }
                    start = false;
                }
                Command.sendChatMessage(out);
            }
            if (args[1].equalsIgnoreCase("setfont")) {
                if (args.length < 3) {
                    Command.sendChatMessage("Specify your font!");
                    return;
                }
                if (args.length < 4) {
                    Command.sendChatMessage("Specify your font size!");
                    return;
                }
                if (Integer.parseInt(args[3]) == 0) {
                    return;
                }
                Xulu.setCFontRenderer(args[2], Integer.parseInt(args[3]));
            }
            if (args[1].equalsIgnoreCase("setfontxdolf")) {
                if (args.length < 3) {
                    Command.sendChatMessage("Specify your font!");
                    return;
                }
                if (args.length < 4) {
                    Command.sendChatMessage("Specify your font size!");
                    return;
                }
                if (Integer.parseInt(args[3]) == 0) {
                    return;
                }
                Xulu.setXdolfFontRenderer(args[2], Integer.parseInt(args[3]));
            }
        }
        else {
            Command.sendChatMessage("Do .customfont help for options");
        }
    }
}
