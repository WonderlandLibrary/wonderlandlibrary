/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle
extends CommandBase {
    @Override
    public String getCommandName() {
        return "particle";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.particle.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        }
        boolean flag = false;
        EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(args[0]);
        if (enumparticletypes == null) {
            throw new CommandException("commands.particle.notFound", args[0]);
        }
        String s = args[0];
        Vec3d vec3d = sender.getPositionVector();
        double d0 = (float)CommandParticle.parseDouble(vec3d.x, args[1], true);
        double d1 = (float)CommandParticle.parseDouble(vec3d.y, args[2], true);
        double d2 = (float)CommandParticle.parseDouble(vec3d.z, args[3], true);
        double d3 = (float)CommandParticle.parseDouble(args[4]);
        double d4 = (float)CommandParticle.parseDouble(args[5]);
        double d5 = (float)CommandParticle.parseDouble(args[6]);
        double d6 = (float)CommandParticle.parseDouble(args[7]);
        int i = 0;
        if (args.length > 8) {
            i = CommandParticle.parseInt(args[8], 0);
        }
        boolean flag1 = false;
        if (args.length > 9 && "force".equals(args[9])) {
            flag1 = true;
        }
        EntityPlayerMP entityplayermp = args.length > 10 ? CommandParticle.getPlayer(server, sender, args[10]) : null;
        int[] aint = new int[enumparticletypes.getArgumentCount()];
        for (int j = 0; j < aint.length; ++j) {
            if (args.length <= 11 + j) continue;
            try {
                aint[j] = Integer.parseInt(args[11 + j]);
                continue;
            }
            catch (NumberFormatException var28) {
                throw new CommandException("commands.particle.invalidParam", args[11 + j]);
            }
        }
        World world = sender.getEntityWorld();
        if (world instanceof WorldServer) {
            WorldServer worldserver = (WorldServer)world;
            if (entityplayermp == null) {
                worldserver.spawnParticle(enumparticletypes, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
            } else {
                worldserver.spawnParticle(entityplayermp, enumparticletypes, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
            }
            CommandParticle.notifyCommandListener(sender, (ICommand)this, "commands.particle.success", s, Math.max(i, 1));
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandParticle.getListOfStringsMatchingLastWord(args, EnumParticleTypes.getParticleNames());
        }
        if (args.length > 1 && args.length <= 4) {
            return CommandParticle.getTabCompletionCoordinate(args, 1, pos);
        }
        if (args.length == 10) {
            return CommandParticle.getListOfStringsMatchingLastWord(args, "normal", "force");
        }
        return args.length == 11 ? CommandParticle.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 10;
    }
}

