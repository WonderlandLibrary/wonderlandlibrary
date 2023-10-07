// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraft.util.EnumParticleTypes;

public class CommandParticle extends CommandBase
{
    private static final String __OBFID = "CL_00002341";
    
    @Override
    public String getCommandName() {
        return "particle";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.particle.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        }
        boolean var3 = false;
        EnumParticleTypes var4 = null;
        for (final EnumParticleTypes var8 : EnumParticleTypes.values()) {
            if (var8.func_179343_f()) {
                if (args[0].startsWith(var8.func_179346_b())) {
                    var3 = true;
                    var4 = var8;
                    break;
                }
            }
            else if (args[0].equals(var8.func_179346_b())) {
                var3 = true;
                var4 = var8;
                break;
            }
        }
        if (!var3) {
            throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
        }
        final String var9 = args[0];
        final Vec3 var10 = sender.getPositionVector();
        final double var11 = (float)CommandBase.func_175761_b(var10.xCoord, args[1], true);
        final double var12 = (float)CommandBase.func_175761_b(var10.yCoord, args[2], true);
        final double var13 = (float)CommandBase.func_175761_b(var10.zCoord, args[3], true);
        final double var14 = (float)CommandBase.parseDouble(args[4]);
        final double var15 = (float)CommandBase.parseDouble(args[5]);
        final double var16 = (float)CommandBase.parseDouble(args[6]);
        final double var17 = (float)CommandBase.parseDouble(args[7]);
        int var18 = 0;
        if (args.length > 8) {
            var18 = CommandBase.parseInt(args[8], 0);
        }
        boolean var19 = false;
        if (args.length > 9 && "force".equals(args[9])) {
            var19 = true;
        }
        final World var20 = sender.getEntityWorld();
        if (var20 instanceof WorldServer) {
            final WorldServer var21 = (WorldServer)var20;
            final int[] var22 = new int[var4.func_179345_d()];
            if (var4.func_179343_f()) {
                final String[] var23 = args[0].split("_", 3);
                for (int var24 = 1; var24 < var23.length; ++var24) {
                    try {
                        var22[var24 - 1] = Integer.parseInt(var23[var24]);
                    }
                    catch (NumberFormatException var25) {
                        throw new CommandException("commands.particle.notFound", new Object[] { args[0] });
                    }
                }
            }
            var21.func_180505_a(var4, var19, var11, var12, var13, var18, var14, var15, var16, var17, var22);
            CommandBase.notifyOperators(sender, this, "commands.particle.success", var9, Math.max(var18, 1));
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, EnumParticleTypes.func_179349_a()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : ((args.length == 9) ? CommandBase.getListOfStringsMatchingLastWord(args, "normal", "force") : null));
    }
}
