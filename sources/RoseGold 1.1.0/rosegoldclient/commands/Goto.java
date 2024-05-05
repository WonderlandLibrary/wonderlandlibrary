package rosegoldclient.commands;

import gg.essential.api.utils.Multithreading;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import rosegoldclient.Main;
import rosegoldclient.features.Pathfinding;
import rosegoldclient.utils.VecUtils;
import rosegoldclient.utils.pathfinding.Pathfinder;

public class Goto extends CommandBase {
    @NotNull
    @Override
    public String getName() {
        return "goto";
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
        if(args.length != 3) return;
        String x = args[0];
        String y = args[1];
        String z = args[2];
        Multithreading.runAsync(() -> {
            if (Main.configFile.pathfindingGotoWalk) {
                Pathfinding.initWalk();
            } else {
                Pathfinding.initTeleport();
            }
            Pathfinder.setup(new BlockPos(VecUtils.floorVec(Main.mc.player.getPositionVector())), new BlockPos(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)), 0.0);
        });
    }
}
