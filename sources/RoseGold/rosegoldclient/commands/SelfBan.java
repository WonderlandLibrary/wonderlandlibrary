package rosegoldclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import rosegoldclient.Main;

import java.util.Random;

public class SelfBan extends CommandBase {
    @Override
    public String getName() {
        return "selfban";
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
        Main.firstLogin = true;
        Main.banned = false;
        Main.mc.getConnection().getNetworkManager().closeChannel(new TextComponentString("§cKicked whilst connecting to WC" + (new Random().nextInt(24) + 1) + ": §bYou have been banned\n§4Reason: §cAbuse of multiple exploits.\n§3Appeal at §6https://wynncraft.com/appeals/"));
    }
}
