/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.exception.ExceptionUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle
extends CommandBase {
    private static final Logger field_175774_a = LogManager.getLogger();
    private static final String __OBFID = "CL_00002338";

    @Override
    public String getCommandName() {
        return "title";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.title.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        if (args.length < 3) {
            if ("title".equals(args[1]) || "subtitle".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            }
            if ("times".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.times", new Object[0]);
            }
        }
        EntityPlayerMP var3 = CommandTitle.getPlayer(sender, args[0]);
        S45PacketTitle.Type var4 = S45PacketTitle.Type.func_179969_a(args[1]);
        if (var4 != S45PacketTitle.Type.CLEAR && var4 != S45PacketTitle.Type.RESET) {
            if (var4 == S45PacketTitle.Type.TIMES) {
                if (args.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                int var11 = CommandTitle.parseInt(args[2]);
                int var12 = CommandTitle.parseInt(args[3]);
                int var13 = CommandTitle.parseInt(args[4]);
                S45PacketTitle var14 = new S45PacketTitle(var11, var12, var13);
                var3.playerNetServerHandler.sendPacket(var14);
                CommandTitle.notifyOperators(sender, (ICommand)this, "commands.title.success", new Object[0]);
            } else {
                IChatComponent var6;
                if (args.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                String var10 = CommandTitle.func_180529_a(args, 2);
                try {
                    var6 = IChatComponent.Serializer.jsonToComponent(var10);
                }
                catch (JsonParseException var9) {
                    Throwable var8 = ExceptionUtils.getRootCause((Throwable)var9);
                    throw new SyntaxErrorException("commands.tellraw.jsonException", var8 == null ? "" : var8.getMessage());
                }
                S45PacketTitle var7 = new S45PacketTitle(var4, ChatComponentProcessor.func_179985_a(sender, var6, var3));
                var3.playerNetServerHandler.sendPacket(var7);
                CommandTitle.notifyOperators(sender, (ICommand)this, "commands.title.success", new Object[0]);
            }
        } else {
            if (args.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            S45PacketTitle var5 = new S45PacketTitle(var4, null);
            var3.playerNetServerHandler.sendPacket(var5);
            CommandTitle.notifyOperators(sender, (ICommand)this, "commands.title.success", new Object[0]);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandTitle.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length == 2 ? CommandTitle.getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.func_179971_a()) : null);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

