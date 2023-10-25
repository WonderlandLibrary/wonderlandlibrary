package net.minecraft.command;

import java.util.Collection;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandExecuteAt;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandExecuteAt
extends CommandBase {
    public String getCommandName() {
        return "execute";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.execute.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        Entity entity = CommandExecuteAt.getEntity((ICommandSender)sender, (String)args[0], Entity.class);
        double d0 = CommandExecuteAt.parseDouble((double)entity.posX, (String)args[1], (boolean)false);
        double d1 = CommandExecuteAt.parseDouble((double)entity.posY, (String)args[2], (boolean)false);
        double d2 = CommandExecuteAt.parseDouble((double)entity.posZ, (String)args[3], (boolean)false);
        BlockPos blockpos = new BlockPos(d0, d1, d2);
        int i = 4;
        if ("detect".equals((Object)args[4]) && args.length > 10) {
            World world = entity.getEntityWorld();
            double d3 = CommandExecuteAt.parseDouble((double)d0, (String)args[5], (boolean)false);
            double d4 = CommandExecuteAt.parseDouble((double)d1, (String)args[6], (boolean)false);
            double d5 = CommandExecuteAt.parseDouble((double)d2, (String)args[7], (boolean)false);
            Block block = CommandExecuteAt.getBlockByText((ICommandSender)sender, (String)args[8]);
            int k = CommandExecuteAt.parseInt((String)args[9], (int)-1, (int)15);
            BlockPos blockpos1 = new BlockPos(d3, d4, d5);
            IBlockState iblockstate = world.getBlockState(blockpos1);
            if (iblockstate.getBlock() != block || k >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != k) {
                throw new CommandException("commands.execute.failed", new Object[]{"detect", entity.getName()});
            }
            i = 10;
        }
        String s = CommandExecuteAt.buildString((String[])args, (int)i);
        1 icommandsender = new /* Unavailable Anonymous Inner Class!! */;
        ICommandManager icommandmanager = MinecraftServer.getServer().getCommandManager();
        try {
            int j = icommandmanager.executeCommand((ICommandSender)icommandsender, s);
            if (j < 1) {
                throw new CommandException("commands.execute.allInvocationsFailed", new Object[]{s});
            }
        }
        catch (Throwable var23) {
            throw new CommandException("commands.execute.failed", new Object[]{s, entity.getName()});
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandExecuteAt.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames()) : (args.length > 1 && args.length <= 4 ? CommandExecuteAt.func_175771_a((String[])args, (int)1, (BlockPos)pos) : (args.length > 5 && args.length <= 8 && "detect".equals((Object)args[4]) ? CommandExecuteAt.func_175771_a((String[])args, (int)5, (BlockPos)pos) : (args.length == 9 && "detect".equals((Object)args[4]) ? CommandExecuteAt.getListOfStringsMatchingLastWord((String[])args, (Collection)Block.blockRegistry.getKeys()) : null)));
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
