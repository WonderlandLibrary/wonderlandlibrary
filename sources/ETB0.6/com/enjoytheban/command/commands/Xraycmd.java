package com.enjoytheban.command.commands;

import java.util.Arrays;

import com.enjoytheban.Client;
import com.enjoytheban.command.Command;
import com.enjoytheban.module.modules.render.Xray;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.math.MathUtil;

public class Xraycmd extends Command {

    public Xraycmd() {
        super("xray", new String[]{"oreesp"}, "", "nigga");
    }

    @Override
    public String execute(String[] args) {
        Xray xray = (Xray)Client.instance.getModuleManager().getModuleByClass(Xray.class);
        if(args.length == 2) {
            if(MathUtil.parsable(args[1], MathUtil.NumberType.DOUBLE)) {
                int id = Integer.parseInt(args[1]);
                if (args[0].equalsIgnoreCase("add")) {
                    xray.blocks.add(id);
                    Helper.sendMessage("Added Block ID " + id);
                } else if(args[0].equalsIgnoreCase("remove")) {
                    xray.blocks.remove(id);
                    Helper.sendMessage("Removed Block ID " + id);
                } else {
                    Helper.sendMessage("Invalid syntax");
                }
            } else {
                Helper.sendMessage("Invalid block ID");
            }
        } else if(args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                Arrays.toString(xray.blocks.toArray());
            }
        }
        return null;
    }
}
