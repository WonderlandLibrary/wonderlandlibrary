package rosegoldclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import rosegoldclient.features.WynncraftChestESP;
import rosegoldclient.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class SaveChests extends CommandBase {

    private final String tab = "  ";

    @Override
    public String getName() {
        return "savechests";
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
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        BlockPos last = null;
        for(BlockPos blockPos : WynncraftChestESP.chests) {
            last = blockPos;
        }
        for(BlockPos blockPos : WynncraftChestESP.chests) {
            json.append(tab + "{\n");
            json.append(tab + tab + "\"x\": ").append(blockPos.getX()).append(",\n");
            json.append(tab + tab + "\"y\": ").append(blockPos.getY()).append(",\n");
            json.append(tab + tab + "\"z\": ").append(blockPos.getZ()).append(",\n");
            if (last.equals(blockPos)) {
                json.append(tab + "}\n");
            } else {
                json.append(tab + "},\n");
            }
        }
        json.append("]");
        try {
            Files.write(Paths.get("./config/rosegoldclient/chests.json"), json.toString().getBytes(StandardCharsets.UTF_8));
            Utils.sendModMessage("&aSuccessfully saved chests to minecraft/config/rosegoldclient/chests.json");
        } catch (Exception error) {
            Utils.sendModMessage("&cError saving config file");
            error.printStackTrace();
        }
    }
}
