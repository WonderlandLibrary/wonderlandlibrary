/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandStats
extends CommandBase {
    private static final String __OBFID = "CL_00002339";

    @Override
    public String getCommandName() {
        return "stats";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.stats.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        CommandResultStats var8;
        TileEntity var10;
        BlockPos var9;
        CommandResultStats.Type var6;
        int var4;
        boolean var3;
        if (args.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        if (args[0].equals("entity")) {
            var3 = false;
        } else {
            if (!args[0].equals("block")) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            var3 = true;
        }
        if (var3) {
            if (args.length < 5) {
                throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
            }
            var4 = 4;
        } else {
            if (args.length < 3) {
                throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
            }
            var4 = 2;
        }
        int var11 = var4 + 1;
        String var5 = args[var4];
        if ("set".equals(var5)) {
            if (args.length < var11 + 3) {
                if (var11 == 5) {
                    throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
        } else {
            if (!"clear".equals(var5)) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            if (args.length < var11 + 1) {
                if (var11 == 5) {
                    throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
            }
        }
        if ((var6 = CommandResultStats.Type.func_179635_a(args[var11++])) == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
        }
        World var7 = sender.getEntityWorld();
        if (var3) {
            var9 = CommandStats.func_175757_a(sender, args, 1, false);
            var10 = var7.getTileEntity(var9);
            if (var10 == null) {
                throw new CommandException("commands.stats.noCompatibleBlock", var9.getX(), var9.getY(), var9.getZ());
            }
            if (var10 instanceof TileEntityCommandBlock) {
                var8 = ((TileEntityCommandBlock)var10).func_175124_c();
            } else {
                if (!(var10 instanceof TileEntitySign)) {
                    throw new CommandException("commands.stats.noCompatibleBlock", var9.getX(), var9.getY(), var9.getZ());
                }
                var8 = ((TileEntitySign)var10).func_174880_d();
            }
        } else {
            Entity var12 = CommandStats.func_175768_b(sender, args[1]);
            var8 = var12.func_174807_aT();
        }
        if ("set".equals(var5)) {
            String var13 = args[var11++];
            String var14 = args[var11];
            if (var13.length() == 0 || var14.length() == 0) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            CommandResultStats.func_179667_a(var8, var6, var13, var14);
            CommandStats.notifyOperators(sender, (ICommand)this, "commands.stats.success", var6.func_179637_b(), var14, var13);
        } else if ("clear".equals(var5)) {
            CommandResultStats.func_179667_a(var8, var6, null, null);
            CommandStats.notifyOperators(sender, (ICommand)this, "commands.stats.cleared", var6.func_179637_b());
        }
        if (var3) {
            var9 = CommandStats.func_175757_a(sender, args, 1, false);
            var10 = var7.getTileEntity(var9);
            var10.markDirty();
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandStats.getListOfStringsMatchingLastWord(args, "entity", "block") : (args.length == 2 && args[0].equals("entity") ? CommandStats.getListOfStringsMatchingLastWord(args, this.func_175776_d()) : (!(args.length == 3 && args[0].equals("entity") || args.length == 5 && args[0].equals("block")) ? (!(args.length == 4 && args[0].equals("entity") || args.length == 6 && args[0].equals("block")) ? (!(args.length == 6 && args[0].equals("entity") || args.length == 8 && args[0].equals("block")) ? null : CommandStats.func_175762_a(args, this.func_175777_e())) : CommandStats.getListOfStringsMatchingLastWord(args, CommandResultStats.Type.func_179634_c())) : CommandStats.getListOfStringsMatchingLastWord(args, "set", "clear")));
    }

    protected String[] func_175776_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    protected List func_175777_e() {
        Collection var1 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
        ArrayList var2 = Lists.newArrayList();
        for (ScoreObjective var4 : var1) {
            if (var4.getCriteria().isReadOnly()) continue;
            var2.add(var4.getName());
        }
        return var2;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }
}

