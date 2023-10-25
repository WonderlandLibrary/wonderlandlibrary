/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class CommandStats
extends CommandBase {
    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return stringArray.length > 0 && stringArray[0].equals("entity") && n == 1;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.stats.usage";
    }

    protected List<String> func_175777_e() {
        Collection<ScoreObjective> collection = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
        ArrayList arrayList = Lists.newArrayList();
        for (ScoreObjective scoreObjective : collection) {
            if (scoreObjective.getCriteria().isReadOnly()) continue;
            arrayList.add(scoreObjective.getName());
        }
        return arrayList;
    }

    protected String[] func_175776_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        CommandResultStats commandResultStats;
        Object object;
        Object object2;
        CommandResultStats.Type type;
        int n;
        boolean bl;
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        if (stringArray[0].equals("entity")) {
            bl = false;
        } else {
            if (!stringArray[0].equals("block")) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            bl = true;
        }
        if (bl) {
            if (stringArray.length < 5) {
                throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
            }
            n = 4;
        } else {
            if (stringArray.length < 3) {
                throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
            }
            n = 2;
        }
        String string = stringArray[n++];
        if ("set".equals(string)) {
            if (stringArray.length < n + 3) {
                if (n == 5) {
                    throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
        } else {
            if (!"clear".equals(string)) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            if (stringArray.length < n + 1) {
                if (n == 5) {
                    throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
            }
        }
        if ((type = CommandResultStats.Type.getTypeByName(stringArray[n++])) == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
        }
        World world = iCommandSender.getEntityWorld();
        if (bl) {
            object2 = CommandStats.parseBlockPos(iCommandSender, stringArray, 1, false);
            object = world.getTileEntity((BlockPos)object2);
            if (object == null) {
                throw new CommandException("commands.stats.noCompatibleBlock", ((Vec3i)object2).getX(), ((Vec3i)object2).getY(), ((Vec3i)object2).getZ());
            }
            if (object instanceof TileEntityCommandBlock) {
                commandResultStats = ((TileEntityCommandBlock)object).getCommandResultStats();
            } else {
                if (!(object instanceof TileEntitySign)) {
                    throw new CommandException("commands.stats.noCompatibleBlock", ((Vec3i)object2).getX(), ((Vec3i)object2).getY(), ((Vec3i)object2).getZ());
                }
                commandResultStats = ((TileEntitySign)object).getStats();
            }
        } else {
            object2 = CommandStats.func_175768_b(iCommandSender, stringArray[1]);
            commandResultStats = ((Entity)object2).getCommandStats();
        }
        if ("set".equals(string)) {
            object2 = stringArray[n++];
            object = stringArray[n];
            if (((String)object2).length() == 0 || ((String)object).length() == 0) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            CommandResultStats.func_179667_a(commandResultStats, type, (String)object2, (String)object);
            CommandStats.notifyOperators(iCommandSender, (ICommand)this, "commands.stats.success", type.getTypeName(), object, object2);
        } else if ("clear".equals(string)) {
            CommandResultStats.func_179667_a(commandResultStats, type, null, null);
            CommandStats.notifyOperators(iCommandSender, (ICommand)this, "commands.stats.cleared", type.getTypeName());
        }
        if (bl) {
            object2 = CommandStats.parseBlockPos(iCommandSender, stringArray, 1, false);
            object = world.getTileEntity((BlockPos)object2);
            ((TileEntity)object).markDirty();
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "stats";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandStats.getListOfStringsMatchingLastWord(stringArray, "entity", "block") : (stringArray.length == 2 && stringArray[0].equals("entity") ? CommandStats.getListOfStringsMatchingLastWord(stringArray, this.func_175776_d()) : (stringArray.length >= 2 && stringArray.length <= 4 && stringArray[0].equals("block") ? CommandStats.func_175771_a(stringArray, 1, blockPos) : (!(stringArray.length == 3 && stringArray[0].equals("entity") || stringArray.length == 5 && stringArray[0].equals("block")) ? (!(stringArray.length == 4 && stringArray[0].equals("entity") || stringArray.length == 6 && stringArray[0].equals("block")) ? (!(stringArray.length == 6 && stringArray[0].equals("entity") || stringArray.length == 8 && stringArray[0].equals("block")) ? null : CommandStats.getListOfStringsMatchingLastWord(stringArray, this.func_175777_e())) : CommandStats.getListOfStringsMatchingLastWord(stringArray, CommandResultStats.Type.getTypeNames())) : CommandStats.getListOfStringsMatchingLastWord(stringArray, "set", "clear"))));
    }
}

