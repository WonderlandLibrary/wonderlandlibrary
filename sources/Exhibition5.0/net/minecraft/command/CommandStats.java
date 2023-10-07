// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.scoreboard.ScoreObjective;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityCommandBlock;

public class CommandStats extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.stats.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        boolean var3;
        if (args[0].equals("entity")) {
            var3 = false;
        }
        else {
            if (!args[0].equals("block")) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            var3 = true;
        }
        byte var4;
        if (var3) {
            if (args.length < 5) {
                throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
            }
            var4 = 4;
        }
        else {
            if (args.length < 3) {
                throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
            }
            var4 = 2;
        }
        int var5 = var4 + 1;
        final String var6 = args[var4];
        if ("set".equals(var6)) {
            if (args.length < var5 + 3) {
                if (var5 == 5) {
                    throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
        }
        else {
            if (!"clear".equals(var6)) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            if (args.length < var5 + 1) {
                if (var5 == 5) {
                    throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
            }
        }
        final CommandResultStats.Type var7 = CommandResultStats.Type.func_179635_a(args[var5++]);
        if (var7 == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
        }
        final World var8 = sender.getEntityWorld();
        CommandResultStats var11;
        if (var3) {
            final BlockPos var9 = CommandBase.func_175757_a(sender, args, 1, false);
            final TileEntity var10 = var8.getTileEntity(var9);
            if (var10 == null) {
                throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { var9.getX(), var9.getY(), var9.getZ() });
            }
            if (var10 instanceof TileEntityCommandBlock) {
                var11 = ((TileEntityCommandBlock)var10).func_175124_c();
            }
            else {
                if (!(var10 instanceof TileEntitySign)) {
                    throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { var9.getX(), var9.getY(), var9.getZ() });
                }
                var11 = ((TileEntitySign)var10).func_174880_d();
            }
        }
        else {
            final Entity var12 = CommandBase.func_175768_b(sender, args[1]);
            var11 = var12.func_174807_aT();
        }
        if ("set".equals(var6)) {
            final String var13 = args[var5++];
            final String var14 = args[var5];
            if (var13.length() == 0 || var14.length() == 0) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            CommandResultStats.func_179667_a(var11, var7, var13, var14);
            CommandBase.notifyOperators(sender, this, "commands.stats.success", var7.func_179637_b(), var14, var13);
        }
        else if ("clear".equals(var6)) {
            CommandResultStats.func_179667_a(var11, var7, null, null);
            CommandBase.notifyOperators(sender, this, "commands.stats.cleared", var7.func_179637_b());
        }
        if (var3) {
            final BlockPos var9 = CommandBase.func_175757_a(sender, args, 1, false);
            final TileEntity var10 = var8.getTileEntity(var9);
            var10.markDirty();
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "entity", "block") : ((args.length == 2 && args[0].equals("entity")) ? CommandBase.getListOfStringsMatchingLastWord(args, this.func_175776_d()) : (((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block"))) ? (((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block"))) ? (((args.length != 6 || !args[0].equals("entity")) && (args.length != 8 || !args[0].equals("block"))) ? null : CommandBase.func_175762_a(args, this.func_175777_e())) : CommandBase.getListOfStringsMatchingLastWord(args, CommandResultStats.Type.func_179634_c())) : CommandBase.getListOfStringsMatchingLastWord(args, "set", "clear")));
    }
    
    protected String[] func_175776_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    protected List func_175777_e() {
        final Collection var1 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
        final ArrayList var2 = Lists.newArrayList();
        for (final ScoreObjective var4 : var1) {
            if (!var4.getCriteria().isReadOnly()) {
                var2.add(var4.getName());
            }
        }
        return var2;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 0 && args[0].equals("entity") && index == 1;
    }
}
