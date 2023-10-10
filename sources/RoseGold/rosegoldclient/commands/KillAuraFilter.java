package rosegoldclient.commands;

import com.google.gson.Gson;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import rosegoldclient.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class KillAuraFilter extends CommandBase {

    public static HashSet<String> KASettings = new HashSet<>();

    @Override
    public String getName() {
        return "ka";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>() {{
            add("killaura");
        }};
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + getName();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0]) {
                case "add":
                    for(String arg : args) {
                        if(arg.equals("add")) continue;
                        KASettings.add(arg);
                        Utils.sendModMessage("&aAdded &b" + arg + "&a to custom Kill Aura filter");
                    }
                    saveMacros();
                    break;
                case "remove":
                    for(String arg : args) {
                        if(arg.equals("remove")) continue;
                        for (String name : KASettings) {
                            if (name.equals(arg)) {
                                Utils.sendModMessage("&aRemoved &b" + arg + "&a from custom Kill Aura filter");
                                KASettings.remove(name);
                            }
                        }
                    }
                    saveMacros();
                    break;
                case "clear":
                    Utils.sendModMessage("&aCleared custom Kill Aura filter");
                    KASettings.clear();
                    saveMacros();
                    break;
                default:
                    Utils.sendModMessage("Unknown arguments, use /ka add, /ka remove or /ka clear");
            }
        } else {
            StringBuilder print = new StringBuilder("Current custom Kill Aura filters: ");
            for (String str : KASettings) {
                print.append("&b").append(str).append("&f, ");
            }
            Utils.sendModMessage("&a" + print);
        }
    }

    private void saveMacros() {
        try {
            String json = new Gson().toJson(KASettings);
            Files.write(Paths.get("./config/rosegoldclient/kaSettings.json"), json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception error) {
            System.out.println("Error saving config file");
            error.printStackTrace();
        }
    }
}
