// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import java.util.Set;
import net.minecraft.util.MathHelper;
import java.util.EnumSet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTeleport extends CommandBase
{
    private static final String __OBFID = "CL_00001180";
    
    @Override
    public String getCommandName() {
        return "tp";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.tp.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        byte var3 = 0;
        Object var4;
        if (args.length != 2 && args.length != 4 && args.length != 6) {
            var4 = CommandBase.getCommandSenderAsPlayer(sender);
        }
        else {
            var4 = CommandBase.func_175768_b(sender, args[0]);
            var3 = 1;
        }
        if (args.length != 1 && args.length != 2) {
            if (args.length < var3 + 3) {
                throw new WrongUsageException("commands.tp.usage", new Object[0]);
            }
            if (((Entity)var4).worldObj != null) {
                int var5 = var3 + 1;
                final CoordinateArg var6 = CommandBase.func_175770_a(((Entity)var4).posX, args[var3], true);
                final CoordinateArg var7 = CommandBase.func_175767_a(((Entity)var4).posY, args[var5++], 0, 0, false);
                final CoordinateArg var8 = CommandBase.func_175770_a(((Entity)var4).posZ, args[var5++], true);
                final CoordinateArg var9 = CommandBase.func_175770_a(((Entity)var4).rotationYaw, (args.length > var5) ? args[var5++] : "~", false);
                final CoordinateArg var10 = CommandBase.func_175770_a(((Entity)var4).rotationPitch, (args.length > var5) ? args[var5] : "~", false);
                if (var4 instanceof EntityPlayerMP) {
                    final EnumSet var11 = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
                    if (var6.func_179630_c()) {
                        var11.add(S08PacketPlayerPosLook.EnumFlags.X);
                    }
                    if (var7.func_179630_c()) {
                        var11.add(S08PacketPlayerPosLook.EnumFlags.Y);
                    }
                    if (var8.func_179630_c()) {
                        var11.add(S08PacketPlayerPosLook.EnumFlags.Z);
                    }
                    if (var10.func_179630_c()) {
                        var11.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
                    }
                    if (var9.func_179630_c()) {
                        var11.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
                    }
                    float var12 = (float)var9.func_179629_b();
                    if (!var9.func_179630_c()) {
                        var12 = MathHelper.wrapAngleTo180_float(var12);
                    }
                    float var13 = (float)var10.func_179629_b();
                    if (!var10.func_179630_c()) {
                        var13 = MathHelper.wrapAngleTo180_float(var13);
                    }
                    if (var13 > 90.0f || var13 < -90.0f) {
                        var13 = MathHelper.wrapAngleTo180_float(180.0f - var13);
                        var12 = MathHelper.wrapAngleTo180_float(var12 + 180.0f);
                    }
                    ((Entity)var4).mountEntity(null);
                    ((EntityPlayerMP)var4).playerNetServerHandler.func_175089_a(var6.func_179629_b(), var7.func_179629_b(), var8.func_179629_b(), var12, var13, var11);
                    ((Entity)var4).setRotationYawHead(var12);
                }
                else {
                    float var14 = (float)MathHelper.wrapAngleTo180_double(var9.func_179628_a());
                    float var12 = (float)MathHelper.wrapAngleTo180_double(var10.func_179628_a());
                    if (var12 > 90.0f || var12 < -90.0f) {
                        var12 = MathHelper.wrapAngleTo180_float(180.0f - var12);
                        var14 = MathHelper.wrapAngleTo180_float(var14 + 180.0f);
                    }
                    ((Entity)var4).setLocationAndAngles(var6.func_179628_a(), var7.func_179628_a(), var8.func_179628_a(), var14, var12);
                    ((Entity)var4).setRotationYawHead(var14);
                }
                CommandBase.notifyOperators(sender, this, "commands.tp.success.coordinates", ((Entity)var4).getName(), var6.func_179628_a(), var7.func_179628_a(), var8.func_179628_a());
            }
        }
        else {
            final Entity var15 = CommandBase.func_175768_b(sender, args[args.length - 1]);
            if (var15.worldObj != ((Entity)var4).worldObj) {
                throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            }
            ((Entity)var4).mountEntity(null);
            if (var4 instanceof EntityPlayerMP) {
                ((EntityPlayerMP)var4).playerNetServerHandler.setPlayerLocation(var15.posX, var15.posY, var15.posZ, var15.rotationYaw, var15.rotationPitch);
            }
            else {
                ((Entity)var4).setLocationAndAngles(var15.posX, var15.posY, var15.posZ, var15.rotationYaw, var15.rotationPitch);
            }
            CommandBase.notifyOperators(sender, this, "commands.tp.success", ((Entity)var4).getName(), var15.getName());
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length != 1 && args.length != 2) ? null : CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
