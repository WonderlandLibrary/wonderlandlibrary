package xyz.northclient.commands.impl;

import org.apache.commons.io.FileUtils;
import xyz.northclient.NorthSingleton;
import xyz.northclient.commands.Command;
import xyz.northclient.config.Config;

import java.io.File;
import java.util.Arrays;

public class CommandConfig extends Command {
    public CommandConfig() {
        super("config","config load okok");
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length == 3) {
            if(args[1].equalsIgnoreCase("load")) {
                String name = args[2];
                if(new File("./North/Configs/" + name + ".json").exists()) {
                    try {
                        Config.load(FileUtils.readFileToString(new File("./North/Configs/" + name + ".json"),"UTF-8"));
                    }catch (Exception e) {

                    }
                }
            } else if(args[1].equalsIgnoreCase("create")) {
                String name = args[2];
                String con = Config.save();
                try {
                    if(new File("./North/Configs/" + name + ".json").exists()) {
                        new File("./North/Configs/" + name + ".json").delete();
                    }
                    new File("./North/Configs/" + name + ".json").createNewFile();
                    FileUtils.writeStringToFile(new File("./North/Configs/" + name + ".json"),con);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            NorthSingleton.logChat(".config <load/create> <name>");
        }
    }
}
