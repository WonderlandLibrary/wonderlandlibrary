/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package net.minecraft.command;

import io.netty.buffer.Unpooled;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class CommandStopSound
extends CommandBase {
    @Override
    public String getCommandName() {
        return "stopsound";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.stopsound.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1 && args.length <= 3) {
            int i = 0;
            EntityPlayerMP entityplayermp = CommandStopSound.getPlayer(server, sender, args[i++]);
            String s = "";
            String s1 = "";
            if (args.length >= 2) {
                String s2;
                SoundCategory soundcategory;
                if ((soundcategory = SoundCategory.getByName(s2 = args[i++])) == null) {
                    throw new CommandException("commands.stopsound.unknownSoundSource", s2);
                }
                s = soundcategory.getName();
            }
            if (args.length == 3) {
                s1 = args[i++];
            }
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeString(s);
            packetbuffer.writeString(s1);
            entityplayermp.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetbuffer));
            if (s.isEmpty() && s1.isEmpty()) {
                CommandStopSound.notifyCommandListener(sender, (ICommand)this, "commands.stopsound.success.all", entityplayermp.getName());
            } else if (s1.isEmpty()) {
                CommandStopSound.notifyCommandListener(sender, (ICommand)this, "commands.stopsound.success.soundSource", s, entityplayermp.getName());
            } else {
                CommandStopSound.notifyCommandListener(sender, (ICommand)this, "commands.stopsound.success.individualSound", s1, s, entityplayermp.getName());
            }
        } else {
            throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandStopSound.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        if (args.length == 2) {
            return CommandStopSound.getListOfStringsMatchingLastWord(args, SoundCategory.getSoundCategoryNames());
        }
        return args.length == 3 ? CommandStopSound.getListOfStringsMatchingLastWord(args, SoundEvent.REGISTRY.getKeys()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

