package rosegoldclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import rosegoldclient.Main;
import rosegoldclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends CommandBase {

    @Override
    public String getName() {
        return "rosegoldclient";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{
            add("rgc");
        }};
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + getName();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Main.display = Main.configFile.gui();
    }
}
