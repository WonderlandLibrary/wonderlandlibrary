// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import org.apache.logging.log4j.LogManager;
import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentProcessor;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S45PacketTitle;
import org.apache.logging.log4j.Logger;

public class CommandTitle extends CommandBase
{
    private static final Logger field_175774_a;
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.title.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
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
        final EntityPlayerMP var3 = CommandBase.getPlayer(sender, args[0]);
        final S45PacketTitle.Type var4 = S45PacketTitle.Type.func_179969_a(args[1]);
        if (var4 != S45PacketTitle.Type.CLEAR && var4 != S45PacketTitle.Type.RESET) {
            if (var4 == S45PacketTitle.Type.TIMES) {
                if (args.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                final int var5 = CommandBase.parseInt(args[2]);
                final int var6 = CommandBase.parseInt(args[3]);
                final int var7 = CommandBase.parseInt(args[4]);
                final S45PacketTitle var8 = new S45PacketTitle(var5, var6, var7);
                var3.playerNetServerHandler.sendPacket(var8);
                CommandBase.notifyOperators(sender, this, "commands.title.success", new Object[0]);
            }
            else {
                if (args.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                final String var9 = CommandBase.func_180529_a(args, 2);
                IChatComponent var10;
                try {
                    var10 = IChatComponent.Serializer.jsonToComponent(var9);
                }
                catch (JsonParseException var12) {
                    final Throwable var11 = ExceptionUtils.getRootCause((Throwable)var12);
                    throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { (var11 == null) ? "" : var11.getMessage() });
                }
                final S45PacketTitle var13 = new S45PacketTitle(var4, ChatComponentProcessor.func_179985_a(sender, var10, var3));
                var3.playerNetServerHandler.sendPacket(var13);
                CommandBase.notifyOperators(sender, this, "commands.title.success", new Object[0]);
            }
        }
        else {
            if (args.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            final S45PacketTitle var14 = new S45PacketTitle(var4, null);
            var3.playerNetServerHandler.sendPacket(var14);
            CommandBase.notifyOperators(sender, this, "commands.title.success", new Object[0]);
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.func_179971_a()) : null);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
    
    static {
        field_175774_a = LogManager.getLogger();
    }
}
