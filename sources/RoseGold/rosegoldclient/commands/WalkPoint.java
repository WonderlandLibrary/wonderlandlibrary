package rosegoldclient.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import rosegoldclient.Main;
import rosegoldclient.features.AutoWalk;
import rosegoldclient.utils.Point;
import rosegoldclient.utils.Utils;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class WalkPoint extends CommandBase {

    @Override
    public String getName() {
        return "walkpoint";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + getName();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            switch (args[0]) {
                case "add":
                    if (args.length == 1) {
                        AutoWalk.points.add(new Point(new Vec3d(Main.mc.player.posX, Main.mc.player.posY, Main.mc.player.posZ), 0L));
                        Utils.sendModMessage("&aAdded point to Auto Walk points with &b0&a milliseconds wait time with no pathfinding");
                    } else if (args.length == 2 && isInt(args[1])) {
                        AutoWalk.points.add(new Point(new Vec3d(Main.mc.player.posX, Main.mc.player.posY, Main.mc.player.posZ), Long.parseLong(args[1])));
                        Utils.sendModMessage("&aAdded point to Auto Walk points with &b" + args[1] + "&a milliseconds wait time with no pathfinding");
                    } else if (args.length == 3 && isInt(args[1]) && (args[2].equals("true") || args[2].equals("false"))) {
                        AutoWalk.points.add(new Point(new Vec3d(Main.mc.player.posX, Main.mc.player.posY, Main.mc.player.posZ), Long.parseLong(args[1]), Boolean.parseBoolean(args[2])));
                        Utils.sendModMessage("&aAdded point to Auto Walk points with &b" + args[1] + "&a milliseconds wait time with " + (args[2].equals("true") ? " " : "no ") + "pathfinding");
                    } else {
                        Utils.sendModMessage("&cInvalid arguments! use /walkpoint add [delay <Integer>] [pathfind <false/true>]");
                    }
                    break;
                case "remove":
                    double closest = Long.MAX_VALUE;
                    Point save = null;
                    for (Point point : AutoWalk.points) {
                        double dist = Main.mc.player.getDistance(point.getLocation().x, point.getLocation().y, point.getLocation().z);
                        if (dist < closest) {
                            closest = dist;
                            save = point;
                        }
                    }
                    Utils.sendModMessage("&aRemoved &b" + (save == null ? "nothing" : save.getLocation()) + "&a from Auto Walk points");
                    AutoWalk.points.remove(save);
                    break;
                case "clear":
                    Utils.sendModMessage("&aCleared Auto Walk points");
                    AutoWalk.points.clear();
                    break;
                case "save":
                    if (args.length == 2) {
                        saveMacros(args[1]);
                    } else {
                        Utils.sendModMessage("&cNo profile name specified");
                    }
                    break;
                case "load":
                    if (args.length == 2) {
                        loadMacros(args[1]);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder("Auto Walk Profiles: ");
                        for(String profileName: AutoWalk.profiles.keySet()) {
                            stringBuilder.append(profileName).append(", ");
                        }
                        Utils.sendModMessage(stringBuilder.toString());
                    }
                    break;
                default:
                    Utils.sendModMessage("&cUnknown arguments, use /walkpoint add, remove, clear, save or load");
            }
        } else {
            StringBuilder print = new StringBuilder("Current Auto Walk points:\n");
            for (Point point : AutoWalk.points) {
                print.append("&b").append(point.getLocation()).append(" with wait time of ").append(point.getLocation()).append(" milliseconds").append("&f\n");
            }
            print.delete(print.length() - 1, print.length());
            Utils.sendModMessage("&a" + print);
        }
    }

    private void saveMacros(String profileName) {
        try {
            ArrayList<Point> copied = new ArrayList<>(AutoWalk.points);
            AutoWalk.profiles.put(profileName, copied);
            String json = new Gson().toJson(AutoWalk.profiles);
            Files.write(Paths.get("./config/rosegoldclient/autoWalkProfiles.json"), json.getBytes(StandardCharsets.UTF_8));
            Utils.sendModMessage("&aSaved profile &b" + profileName);
        } catch (Exception error) {
            System.out.println("Error saving config file");
            error.printStackTrace();
        }
    }

    private void loadMacros(String profileName) {
        File autoWalkProfiles = new File(Main.configDirectory, "autoWalkProfiles.json");
        try {
            if (autoWalkProfiles.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/rosegoldclient/autoWalkProfiles.json"));
                Type type = new TypeToken<HashMap<String, ArrayList<Point>>>() {}.getType();
                AutoWalk.profiles = new Gson().fromJson(reader, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AutoWalk.profiles.containsKey(profileName)) {
            AutoWalk.points = AutoWalk.profiles.get(profileName);
            Utils.sendModMessage("Loaded profile " + profileName);
        } else {
            Utils.sendModMessage("Profile " + profileName + " not found");
        }
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
