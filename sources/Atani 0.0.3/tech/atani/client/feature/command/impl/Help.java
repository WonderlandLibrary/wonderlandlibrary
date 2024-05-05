package tech.atani.client.feature.command.impl;

import net.minecraft.util.MathHelper;
import tech.atani.client.feature.command.storage.CommandStorage;
import tech.atani.client.feature.command.Command;
import tech.atani.client.feature.command.data.CommandInfo;

@CommandInfo(name = "help", aliases = {"?"}, description = "for help")
public class Help extends Command {
    @Override
    public boolean execute(String[] args) {
        double pages = CommandStorage.getInstance().getList().size()/15D;
        int formattedPages = (int) pages;
        if(pages > formattedPages)
            formattedPages++;
        if(args.length == 0) {
            sendMessage("§7------§cHelp §e1§7/§c" + formattedPages+ "§7------");
            for(int i = 0; i < 15; i++) {
                if (i < CommandStorage.getInstance().getList().size()) {
                    final Command command = CommandStorage.getInstance().getList().get(i);
                    if (command != null) {
                        String shortAlias = command.getName();
                        for (String alias : command.getAliases()) {
                            if (alias.length() < shortAlias.length())
                                shortAlias = alias;
                        }
                        final String name = command.getName().length() > 6 ? shortAlias : command.getName();
                        sendMessage("§b" + getPrefix() + name + " §7-> §a" + command.getDescription());
                    }
                }
            }
            sendMessage("§7--------------------");
        }else {
            int page = Integer.parseInt(args[0]);
            page = MathHelper.clamp_int(page, 1, formattedPages);
            sendMessage("§7------§cHelp §e" + page + "§7/§c" + formattedPages+ "§7------");
            for(int i = 15 * (page - 1); i < 15 * page; i++) {
                if (i < CommandStorage.getInstance().getList().size()) {
                    final Command command = CommandStorage.getInstance().getList().get(i);
                    if (command != null) {
                        String shortAlias = command.getName();
                        for (String alias : command.getAliases()) {
                            if (alias.length() < shortAlias.length())
                                shortAlias = alias;
                        }
                        final String name = command.getName().length() > 6 ? shortAlias : command.getName();
                        sendMessage("§b" + getPrefix() + name + " §7-> §a" + command.getDescription());
                    }
                }
            }
            sendMessage("§7--------------------");
        }
        return true;
    }
}