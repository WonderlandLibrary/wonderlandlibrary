// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;

public class CommandPlaySound extends CommandBase
{
    private static final String __OBFID = "CL_00000774";
    
    @Override
    public String getCommandName() {
        return "playsound";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.playsound.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
        }
        final byte var3 = 0;
        int var4 = var3 + 1;
        final String var5 = args[var3];
        final EntityPlayerMP var6 = CommandBase.getPlayer(sender, args[var4++]);
        final Vec3 var7 = sender.getPositionVector();
        double var8 = var7.xCoord;
        if (args.length > var4) {
            var8 = CommandBase.func_175761_b(var8, args[var4++], true);
        }
        double var9 = var7.yCoord;
        if (args.length > var4) {
            var9 = CommandBase.func_175769_b(var9, args[var4++], 0, 0, false);
        }
        double var10 = var7.zCoord;
        if (args.length > var4) {
            var10 = CommandBase.func_175761_b(var10, args[var4++], true);
        }
        double var11 = 1.0;
        if (args.length > var4) {
            var11 = CommandBase.parseDouble(args[var4++], 0.0, 3.4028234663852886E38);
        }
        double var12 = 1.0;
        if (args.length > var4) {
            var12 = CommandBase.parseDouble(args[var4++], 0.0, 2.0);
        }
        double var13 = 0.0;
        if (args.length > var4) {
            var13 = CommandBase.parseDouble(args[var4], 0.0, 1.0);
        }
        final double var14 = (var11 > 1.0) ? (var11 * 16.0) : 16.0;
        final double var15 = var6.getDistance(var8, var9, var10);
        if (var15 > var14) {
            if (var13 <= 0.0) {
                throw new CommandException("commands.playsound.playerTooFar", new Object[] { var6.getName() });
            }
            final double var16 = var8 - var6.posX;
            final double var17 = var9 - var6.posY;
            final double var18 = var10 - var6.posZ;
            final double var19 = Math.sqrt(var16 * var16 + var17 * var17 + var18 * var18);
            if (var19 > 0.0) {
                var8 = var6.posX + var16 / var19 * 2.0;
                var9 = var6.posY + var17 / var19 * 2.0;
                var10 = var6.posZ + var18 / var19 * 2.0;
            }
            var11 = var13;
        }
        var6.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(var5, var8, var9, var10, (float)var11, (float)var12));
        CommandBase.notifyOperators(sender, this, "commands.playsound.success", var5, var6.getName());
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 2 && args.length <= 5) ? CommandBase.func_175771_a(args, 2, pos) : null);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 1;
    }
}
