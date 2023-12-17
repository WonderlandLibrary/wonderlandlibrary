// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import net.minecraft.util.SoundEvent;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.util.SoundCategory;
import net.minecraft.server.MinecraftServer;

public class CommandStopSound extends CommandBase
{
    @Override
    public String getName() {
        return "stopsound";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.stopsound.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length >= 1 && args.length <= 3) {
            int i = 0;
            final EntityPlayerMP entityplayermp = CommandBase.getPlayer(server, sender, args[i++]);
            String s = "";
            String s2 = "";
            if (args.length >= 2) {
                final String s3 = args[i++];
                final SoundCategory soundcategory = SoundCategory.getByName(s3);
                if (soundcategory == null) {
                    throw new CommandException("commands.stopsound.unknownSoundSource", new Object[] { s3 });
                }
                s = soundcategory.getName();
            }
            if (args.length == 3) {
                s2 = args[i++];
            }
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeString(s);
            packetbuffer.writeString(s2);
            entityplayermp.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetbuffer));
            if (s.isEmpty() && s2.isEmpty()) {
                CommandBase.notifyCommandListener(sender, this, "commands.stopsound.success.all", entityplayermp.getName());
            }
            else if (s2.isEmpty()) {
                CommandBase.notifyCommandListener(sender, this, "commands.stopsound.success.soundSource", s, entityplayermp.getName());
            }
            else {
                CommandBase.notifyCommandListener(sender, this, "commands.stopsound.success.individualSound", s2, s, entityplayermp.getName());
            }
            return;
        }
        throw new WrongUsageException(this.getUsage(sender), new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, SoundCategory.getSoundCategoryNames());
        }
        return (args.length == 3) ? CommandBase.getListOfStringsMatchingLastWord(args, SoundEvent.REGISTRY.getKeys()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
