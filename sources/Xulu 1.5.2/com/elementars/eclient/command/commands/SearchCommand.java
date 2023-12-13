// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import java.util.Iterator;
import net.minecraft.block.Block;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.Search;
import com.elementars.eclient.command.Command;

public class SearchCommand extends Command
{
    public SearchCommand() {
        super("search", "Manages Search", new String[] { "add", "remove", "list" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        if (args.length < 2) {
            Command.sendChatMessage("Specify an option. Try doing .search help to see command options");
            return;
        }
        if (args[1].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                Command.sendChatMessage("Please specify a block.");
                return;
            }
            if (Search.addBlock(args[2])) {
                Command.sendChatMessage("Added " + args[2] + " to Search!");
                SearchCommand.mc.renderGlobal.loadRenderers();
            }
            else {
                Command.sendChatMessage("Unknown block!");
            }
        }
        else if (args[1].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                Command.sendChatMessage("Please specify a block.");
                return;
            }
            if (Search.delBlock(args[2])) {
                Command.sendChatMessage("Removed " + args[2] + " from Search!");
                Xulu.MODULE_MANAGER.getModuleT(Search.class).posList.clear();
                SearchCommand.mc.renderGlobal.loadRenderers();
            }
            else {
                Command.sendChatMessage("Unknown block!");
            }
        }
        else if (args[1].equalsIgnoreCase("list")) {
            Command.sendChatMessage("Search blocks &7(" + Search.getBLOCKS().size() + ")&r: ");
            String out = "";
            boolean start = true;
            for (final Block b : Search.getBLOCKS()) {
                if (start) {
                    out = b.getLocalizedName();
                }
                else {
                    out = out + ", " + b.getLocalizedName();
                }
                start = false;
            }
            Command.sendChatMessage(out);
        }
        else {
            Command.sendChatMessage("Unknown arguments!");
        }
    }
}
