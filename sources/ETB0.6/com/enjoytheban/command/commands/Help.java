package com.enjoytheban.command.commands;

import org.lwjgl.input.Keyboard;

import com.enjoytheban.Client;
import com.enjoytheban.command.Command;
import com.enjoytheban.module.Module;
import com.enjoytheban.utils.Helper;

public class Help extends Command {

    public Help() {
        super("Help", new String[] {"list"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 0) {
        	Helper.sendMessageWithoutPrefix("§7§m§l----------------------------------");
        	Helper.sendMessageWithoutPrefix("                    §b§lETB Client");
            Helper.sendMessageWithoutPrefix("§b.help >§7 list commands");
            Helper.sendMessageWithoutPrefix("§b.bind >§7 bind a module to a key");
            Helper.sendMessageWithoutPrefix("§b.t >§7 toggle a module on/off");
            Helper.sendMessageWithoutPrefix("§b.friend >§7 friend a player");
            Helper.sendMessageWithoutPrefix("§b.cheats >§7 list all modules");
            Helper.sendMessageWithoutPrefix("§b.config >§7 load a premade config");
            Helper.sendMessageWithoutPrefix("§7§m§l----------------------------------");

        } else {
            Helper.sendMessage("invalid syntax " + "Valid .help");
        }
        return null;
    }
}