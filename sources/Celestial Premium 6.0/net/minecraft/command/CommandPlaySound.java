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
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CommandPlaySound
extends CommandBase {
    @Override
    public String getCommandName() {
        return "playsound";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.playsound.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String s1;
        SoundCategory soundcategory;
        if (args.length < 2) {
            throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
        }
        int i = 0;
        String s = args[i++];
        if ((soundcategory = SoundCategory.getByName(s1 = args[i++])) == null) {
            throw new CommandException("commands.playsound.unknownSoundSource", s1);
        }
        EntityPlayerMP entityplayermp = CommandPlaySound.getPlayer(server, sender, args[i++]);
        Vec3d vec3d = sender.getPositionVector();
        double d0 = args.length > i ? CommandPlaySound.parseDouble(vec3d.x, args[i++], true) : vec3d.x;
        double d1 = args.length > i ? CommandPlaySound.parseDouble(vec3d.y, args[i++], 0, 0, false) : vec3d.y;
        double d2 = args.length > i ? CommandPlaySound.parseDouble(vec3d.z, args[i++], true) : vec3d.z;
        double d3 = args.length > i ? CommandPlaySound.parseDouble(args[i++], 0.0, 3.4028234663852886E38) : 1.0;
        double d4 = args.length > i ? CommandPlaySound.parseDouble(args[i++], 0.0, 2.0) : 1.0;
        double d5 = args.length > i ? CommandPlaySound.parseDouble(args[i], 0.0, 1.0) : 0.0;
        double d6 = d3 > 1.0 ? d3 * 16.0 : 16.0;
        double d7 = entityplayermp.getDistance(d0, d1, d2);
        if (d7 > d6) {
            if (d5 <= 0.0) {
                throw new CommandException("commands.playsound.playerTooFar", entityplayermp.getName());
            }
            double d8 = d0 - entityplayermp.posX;
            double d9 = d1 - entityplayermp.posY;
            double d10 = d2 - entityplayermp.posZ;
            double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
            if (d11 > 0.0) {
                d0 = entityplayermp.posX + d8 / d11 * 2.0;
                d1 = entityplayermp.posY + d9 / d11 * 2.0;
                d2 = entityplayermp.posZ + d10 / d11 * 2.0;
            }
            d3 = d5;
        }
        entityplayermp.connection.sendPacket(new SPacketCustomSound(s, soundcategory, d0, d1, d2, (float)d3, (float)d4));
        CommandPlaySound.notifyCommandListener(sender, (ICommand)this, "commands.playsound.success", s, entityplayermp.getName());
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandPlaySound.getListOfStringsMatchingLastWord(args, SoundEvent.REGISTRY.getKeys());
        }
        if (args.length == 2) {
            return CommandPlaySound.getListOfStringsMatchingLastWord(args, SoundCategory.getSoundCategoryNames());
        }
        if (args.length == 3) {
            return CommandPlaySound.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return args.length > 3 && args.length <= 6 ? CommandPlaySound.getTabCompletionCoordinate(args, 3, pos) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 2;
    }
}

