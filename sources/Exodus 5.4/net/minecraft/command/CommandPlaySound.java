/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class CommandPlaySound
extends CommandBase {
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.playsound.usage";
    }

    @Override
    public String getCommandName() {
        return "playsound";
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 1;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 2) {
            throw new WrongUsageException(this.getCommandUsage(iCommandSender), new Object[0]);
        }
        int n = 0;
        String string = stringArray[n++];
        EntityPlayerMP entityPlayerMP = CommandPlaySound.getPlayer(iCommandSender, stringArray[n++]);
        Vec3 vec3 = iCommandSender.getPositionVector();
        double d = vec3.xCoord;
        if (stringArray.length > n) {
            d = CommandPlaySound.parseDouble(d, stringArray[n++], true);
        }
        double d2 = vec3.yCoord;
        if (stringArray.length > n) {
            d2 = CommandPlaySound.parseDouble(d2, stringArray[n++], 0, 0, false);
        }
        double d3 = vec3.zCoord;
        if (stringArray.length > n) {
            d3 = CommandPlaySound.parseDouble(d3, stringArray[n++], true);
        }
        double d4 = 1.0;
        if (stringArray.length > n) {
            d4 = CommandPlaySound.parseDouble(stringArray[n++], 0.0, 3.4028234663852886E38);
        }
        double d5 = 1.0;
        if (stringArray.length > n) {
            d5 = CommandPlaySound.parseDouble(stringArray[n++], 0.0, 2.0);
        }
        double d6 = 0.0;
        if (stringArray.length > n) {
            d6 = CommandPlaySound.parseDouble(stringArray[n], 0.0, 1.0);
        }
        double d7 = d4 > 1.0 ? d4 * 16.0 : 16.0;
        double d8 = entityPlayerMP.getDistance(d, d2, d3);
        if (d8 > d7) {
            if (d6 <= 0.0) {
                throw new CommandException("commands.playsound.playerTooFar", entityPlayerMP.getName());
            }
            double d9 = d - entityPlayerMP.posX;
            double d10 = d2 - entityPlayerMP.posY;
            double d11 = d3 - entityPlayerMP.posZ;
            double d12 = Math.sqrt(d9 * d9 + d10 * d10 + d11 * d11);
            if (d12 > 0.0) {
                d = entityPlayerMP.posX + d9 / d12 * 2.0;
                d2 = entityPlayerMP.posY + d10 / d12 * 2.0;
                d3 = entityPlayerMP.posZ + d11 / d12 * 2.0;
            }
            d4 = d6;
        }
        entityPlayerMP.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(string, d, d2, d3, (float)d4, (float)d5));
        CommandPlaySound.notifyOperators(iCommandSender, (ICommand)this, "commands.playsound.success", string, entityPlayerMP.getName());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 2 ? CommandPlaySound.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : (stringArray.length > 2 && stringArray.length <= 5 ? CommandPlaySound.func_175771_a(stringArray, 2, blockPos) : null);
    }
}

