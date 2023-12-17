// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import net.minecraft.util.SoundEvent;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.server.MinecraftServer;

public class CommandPlaySound extends CommandBase
{
    @Override
    public String getName() {
        return "playsound";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.playsound.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(this.getUsage(sender), new Object[0]);
        }
        int i = 0;
        final String s = args[i++];
        final String s2 = args[i++];
        final SoundCategory soundcategory = SoundCategory.getByName(s2);
        if (soundcategory == null) {
            throw new CommandException("commands.playsound.unknownSoundSource", new Object[] { s2 });
        }
        final EntityPlayerMP entityplayermp = CommandBase.getPlayer(server, sender, args[i++]);
        final Vec3d vec3d = sender.getPositionVector();
        double d0 = (args.length > i) ? CommandBase.parseDouble(vec3d.x, args[i++], true) : vec3d.x;
        double d2 = (args.length > i) ? CommandBase.parseDouble(vec3d.y, args[i++], 0, 0, false) : vec3d.y;
        double d3 = (args.length > i) ? CommandBase.parseDouble(vec3d.z, args[i++], true) : vec3d.z;
        double d4 = (args.length > i) ? CommandBase.parseDouble(args[i++], 0.0, 3.4028234663852886E38) : 1.0;
        final double d5 = (args.length > i) ? CommandBase.parseDouble(args[i++], 0.0, 2.0) : 1.0;
        final double d6 = (args.length > i) ? CommandBase.parseDouble(args[i], 0.0, 1.0) : 0.0;
        final double d7 = (d4 > 1.0) ? (d4 * 16.0) : 16.0;
        final double d8 = entityplayermp.getDistance(d0, d2, d3);
        if (d8 > d7) {
            if (d6 <= 0.0) {
                throw new CommandException("commands.playsound.playerTooFar", new Object[] { entityplayermp.getName() });
            }
            final double d9 = d0 - entityplayermp.posX;
            final double d10 = d2 - entityplayermp.posY;
            final double d11 = d3 - entityplayermp.posZ;
            final double d12 = Math.sqrt(d9 * d9 + d10 * d10 + d11 * d11);
            if (d12 > 0.0) {
                d0 = entityplayermp.posX + d9 / d12 * 2.0;
                d2 = entityplayermp.posY + d10 / d12 * 2.0;
                d3 = entityplayermp.posZ + d11 / d12 * 2.0;
            }
            d4 = d6;
        }
        entityplayermp.connection.sendPacket(new SPacketCustomSound(s, soundcategory, d0, d2, d3, (float)d4, (float)d5));
        CommandBase.notifyCommandListener(sender, this, "commands.playsound.success", s, entityplayermp.getName());
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, SoundEvent.REGISTRY.getKeys());
        }
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, SoundCategory.getSoundCategoryNames());
        }
        if (args.length == 3) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return (args.length > 3 && args.length <= 6) ? CommandBase.getTabCompletionCoordinate(args, 3, targetPos) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 2;
    }
}
