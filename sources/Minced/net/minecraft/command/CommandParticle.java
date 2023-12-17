// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.server.MinecraftServer;

public class CommandParticle extends CommandBase
{
    @Override
    public String getName() {
        return "particle";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.particle.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        }
        final boolean flag = false;
        final EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(args[0]);
        if (enumparticletypes == null) {
            throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
        }
        final String s = args[0];
        final Vec3d vec3d = sender.getPositionVector();
        final double d0 = (float)CommandBase.parseDouble(vec3d.x, args[1], true);
        final double d2 = (float)CommandBase.parseDouble(vec3d.y, args[2], true);
        final double d3 = (float)CommandBase.parseDouble(vec3d.z, args[3], true);
        final double d4 = (float)CommandBase.parseDouble(args[4]);
        final double d5 = (float)CommandBase.parseDouble(args[5]);
        final double d6 = (float)CommandBase.parseDouble(args[6]);
        final double d7 = (float)CommandBase.parseDouble(args[7]);
        int i = 0;
        if (args.length > 8) {
            i = CommandBase.parseInt(args[8], 0);
        }
        boolean flag2 = false;
        if (args.length > 9 && "force".equals(args[9])) {
            flag2 = true;
        }
        EntityPlayerMP entityplayermp;
        if (args.length > 10) {
            entityplayermp = CommandBase.getPlayer(server, sender, args[10]);
        }
        else {
            entityplayermp = null;
        }
        final int[] aint = new int[enumparticletypes.getArgumentCount()];
        for (int j = 0; j < aint.length; ++j) {
            if (args.length > 11 + j) {
                try {
                    aint[j] = Integer.parseInt(args[11 + j]);
                }
                catch (NumberFormatException var28) {
                    throw new CommandException("commands.particle.invalidParam", new Object[] { args[11 + j] });
                }
            }
        }
        final World world = sender.getEntityWorld();
        if (world instanceof WorldServer) {
            final WorldServer worldserver = (WorldServer)world;
            if (entityplayermp == null) {
                worldserver.spawnParticle(enumparticletypes, flag2, d0, d2, d3, i, d4, d5, d6, d7, aint);
            }
            else {
                worldserver.spawnParticle(entityplayermp, enumparticletypes, flag2, d0, d2, d3, i, d4, d5, d6, d7, aint);
            }
            CommandBase.notifyCommandListener(sender, this, "commands.particle.success", s, Math.max(i, 1));
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, EnumParticleTypes.getParticleNames());
        }
        if (args.length > 1 && args.length <= 4) {
            return CommandBase.getTabCompletionCoordinate(args, 1, targetPos);
        }
        if (args.length == 10) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "normal", "force");
        }
        return (args.length == 11) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 10;
    }
}
