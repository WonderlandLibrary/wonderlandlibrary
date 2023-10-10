package rosegoldclient.commands;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import rosegoldclient.Main;
import rosegoldclient.features.WynncraftChestESP;
import rosegoldclient.utils.Utils;

import java.util.ArrayList;

public class AddChest extends CommandBase {
    @Override
    public String getName() {
        return "addchest";
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
        if(args.length == 0) {
            BlockPos playerPos = Main.mc.player.getPosition();
            Iterable<BlockPos> iterable = BlockPos.getAllInBox(playerPos.subtract(new Vec3i(4, 4, 4)), playerPos.add(new Vec3i(4, 4, 4)));
            ArrayList<BlockPos> chests = new ArrayList<>();
            for(BlockPos pos : iterable) {
                Block block = Main.mc.world.getBlockState(pos).getBlock();
                if(block == Blocks.CHEST) {
                    chests.add(pos);
                }
            }

            double closest = Long.MAX_VALUE;
            BlockPos save = null;
            for(BlockPos pos : chests) {
                double dist = pos.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ());
                if (dist < closest) {
                    closest = dist;
                    save = pos;
                }
            }
            if(save != null) {
                int x = save.getX();
                int y = save.getY();
                int z = save.getZ();
                WynncraftChestESP.chests.add(new BlockPos(x, y, z));
                Utils.sendMessage(String.format("&aSuccessfully added Block at %s %s %s to Wynncraft Chest List", x, y, z));
            } else {
                Utils.sendMessage("&cNo chest in distance found");
            }
        }
        else if(args.length == 3) {
            for(String arg : args) {
                if(!isInt(arg)) {
                    Utils.sendModMessage("&b"+arg+"&c is not an integer!");
                    return;
                }
            }
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            WynncraftChestESP.chests.add(new BlockPos(x, y, z));
            Utils.sendMessage(String.format("&aSuccessfully added Block at %s %s %s to Wynncraft Chest List", x, y, z));
        } else {
            Utils.sendModMessage("&cInvalid coordinates");
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
