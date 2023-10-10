package rosegoldclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import rosegoldclient.features.AutoLootRun;

import java.util.ArrayList;
import java.util.List;

public class AutoLootRunCommand extends CommandBase {
    @NotNull
    @Override
    public String getName() {
        return "autolootrun";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{
            add("autolr");
        }};
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @NotNull
    @Override
    public String getUsage(@NotNull ICommandSender sender) {
        return "/" + getName();
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, @NotNull String[] args) throws CommandException {
        AutoLootRun.doing = true;
    }
}