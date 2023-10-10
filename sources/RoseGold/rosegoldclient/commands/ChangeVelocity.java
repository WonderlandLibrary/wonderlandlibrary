package rosegoldclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import rosegoldclient.Main;
import rosegoldclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChangeVelocity extends CommandBase {
    @Override
    public String getName() {
        return "velocity";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{
            add("velo");
        }};
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + getName() + " [x] [y] [z";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        switch (args.length) {
            case 0:
                Utils.sendMessage(String.format("&aVelocity is %s %s %s", Main.configFile.velocityX, Main.configFile.velocityY, Main.configFile.velocityZ));
                break;
            case 1:
                if(isInt(args[0])) Main.configFile.velocityY = Integer.parseInt(args[0]);
                Utils.sendMessage(String.format("&aVelocity is now %s %s %s", Main.configFile.velocityX, Main.configFile.velocityY, Main.configFile.velocityZ));
                break;
            case 2:
                if(isInt(args[0])) Main.configFile.velocityX = Integer.parseInt(args[0]);
                if(isInt(args[1])) Main.configFile.velocityZ = Integer.parseInt(args[1]);
                Utils.sendMessage(String.format("&aVelocity is now %s %s %s", Main.configFile.velocityX, Main.configFile.velocityY, Main.configFile.velocityZ));
                break;
            case 3:
                if(isInt(args[0])) Main.configFile.velocityX = Integer.parseInt(args[0]);
                if(isInt(args[1])) Main.configFile.velocityY = Integer.parseInt(args[1]);
                if(isInt(args[2])) Main.configFile.velocityZ = Integer.parseInt(args[2]);
                Utils.sendMessage(String.format("&aVelocity is now %s %s %s", Main.configFile.velocityX, Main.configFile.velocityY, Main.configFile.velocityZ));
                break;
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
