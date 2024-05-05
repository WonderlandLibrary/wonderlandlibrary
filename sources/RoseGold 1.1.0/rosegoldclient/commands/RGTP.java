package rosegoldclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import rosegoldclient.Main;
import rosegoldclient.utils.Utils;

public class RGTP extends CommandBase {
    @Override
    public String getName() {
        return "rgtp";
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
        if(args.length == 3) {
            double[] newArgs = new double[3];
            for(int i = 0; i < 3; i++) {
                newArgs[i] = Integer.parseInt(args[i]);
            }
            Main.mc.player.setPosition(newArgs[0] + 0.5D, newArgs[1], newArgs[2] + 0.5D);
        } else {
            Utils.sendModMessage("&cInvalid coordinates");
        }
    }
}
