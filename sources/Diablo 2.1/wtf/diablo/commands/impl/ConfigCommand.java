package wtf.diablo.commands.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.lwjgl.Sys;
import wtf.diablo.Diablo;
import wtf.diablo.commands.Command;
import wtf.diablo.commands.CommandData;
import wtf.diablo.config.ConfigManager;
import wtf.diablo.utils.chat.ChatUtil;

import java.io.File;
import java.util.Arrays;

@CommandData(
        name = "config",
        description = "Handles configs"
)
public class ConfigCommand extends Command {

    @Override
    public void run(String[] args) {
        try {
            System.out.println(Arrays.toString(args));

            switch (args[0].toLowerCase()) {
                case "save": {
                    Diablo.getInstance().getConfigManager().saveConfig(args[1]);
                    ChatUtil.log("Saved Config " + args[1]);
                    return;
                }
                case "load": {
                    if (args.length == 3) {
                        Diablo.getInstance().getConfigManager().loadConfig(args[1], args[2].equals("keys"));
                    } else {
                        Diablo.getInstance().getConfigManager().loadConfig(args[1], false);
                    }
                    // .CONFIG BLOCKS ADESGOJHAOG
                    return;
                }
                case "reload": {
                    Diablo.getInstance().getConfigManager().loadConfigs();
                    ChatUtil.log("Reloaded Configs");
                    return;
                }
                case "delete":
                case "remove": {
                    Diablo.getInstance().getConfigManager().removeConfig(args[1]);
                    ChatUtil.log("Removed Config " + args[1]);
                    return;
                }
                case "list": {
                    for (File file : Diablo.getInstance().getConfigManager().getDir().listFiles()) {
                        ChatUtil.log("- " + file.getName().replaceAll(".sleek", ""));
                    }
                    return;
                }
                /*
                case "upload": {
                    Cloud cloud = new Cloud(Diablo.API_KEY);
                    ChatUtil.log(cloud.postConfig(args[1],args[2],Diablo.intentAccount.username,Arrays.toString(Diablo.getInstance().getConfigManager().getConfigData(args[3]))));
                    ChatUtil.log("Posted config " + args[1] + "!");
                    return;
                }

                case "listcloud": {
                    Cloud cloud = new Cloud(Diablo.API_KEY);
                    for(JsonElement json : cloud.listConfigs("")){
                        ChatUtil.log("Name: " + json.getAsJsonObject().get("name").getAsString() + " | Author: " + json.getAsJsonObject().get("meta").getAsString() + " | Code: " + json.getAsJsonObject().get("share_code").getAsString());
                    }

                    return;
                }

                case "download": {
                    Cloud cloud = new Cloud(Diablo.API_KEY);
                    Diablo.getInstance().getConfigManager().saveConfig("test",cloud.getConfig(args[1]));
                    ChatUtil.log("SUS!");
                    return;
                }

                 */


            }
            ChatUtil.log(".config save <configName>");
            ChatUtil.log(".config load <configName>");
            ChatUtil.log(".config remove <configName>");
            ChatUtil.log(".config online <list | load> [name]");
            ChatUtil.log(".config reload");
            ChatUtil.log(".config list");
        } catch (Throwable e) {
            e.printStackTrace();
            ChatUtil.log(".config save <configName>");
            ChatUtil.log(".config load <configName>");
            ChatUtil.log(".config remove <configName>");
            ChatUtil.log(".config online <list | load> [name]");
            ChatUtil.log(".config reload");
            ChatUtil.log(".config list");
        }
    }
}
