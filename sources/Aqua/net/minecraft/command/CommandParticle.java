package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle
extends CommandBase {
    public String getCommandName() {
        return "particle";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.particle.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        World world;
        if (args.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        }
        boolean flag = false;
        EnumParticleTypes enumparticletypes = null;
        for (EnumParticleTypes enumparticletypes1 : EnumParticleTypes.values()) {
            if (enumparticletypes1.hasArguments()) {
                if (!args[0].startsWith(enumparticletypes1.getParticleName())) continue;
                flag = true;
                enumparticletypes = enumparticletypes1;
                break;
            }
            if (!args[0].equals((Object)enumparticletypes1.getParticleName())) continue;
            flag = true;
            enumparticletypes = enumparticletypes1;
            break;
        }
        if (!flag) {
            throw new CommandException("commands.particle.notFound", new Object[]{args[0]});
        }
        String s = args[0];
        Vec3 vec3 = sender.getPositionVector();
        double d6 = (float)CommandParticle.parseDouble((double)vec3.xCoord, (String)args[1], (boolean)true);
        double d0 = (float)CommandParticle.parseDouble((double)vec3.yCoord, (String)args[2], (boolean)true);
        double d1 = (float)CommandParticle.parseDouble((double)vec3.zCoord, (String)args[3], (boolean)true);
        double d2 = (float)CommandParticle.parseDouble((String)args[4]);
        double d3 = (float)CommandParticle.parseDouble((String)args[5]);
        double d4 = (float)CommandParticle.parseDouble((String)args[6]);
        double d5 = (float)CommandParticle.parseDouble((String)args[7]);
        int i = 0;
        if (args.length > 8) {
            i = CommandParticle.parseInt((String)args[8], (int)0);
        }
        boolean flag1 = false;
        if (args.length > 9 && "force".equals((Object)args[9])) {
            flag1 = true;
        }
        if ((world = sender.getEntityWorld()) instanceof WorldServer) {
            WorldServer worldserver = (WorldServer)world;
            int[] aint = new int[enumparticletypes.getArgumentCount()];
            if (enumparticletypes.hasArguments()) {
                String[] astring = args[0].split("_", 3);
                for (int j = 1; j < astring.length; ++j) {
                    try {
                        aint[j - 1] = Integer.parseInt((String)astring[j]);
                        continue;
                    }
                    catch (NumberFormatException var29) {
                        throw new CommandException("commands.particle.notFound", new Object[]{args[0]});
                    }
                }
            }
            worldserver.spawnParticle(enumparticletypes, flag1, d6, d0, d1, i, d2, d3, d4, d5, aint);
            CommandParticle.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.particle.success", (Object[])new Object[]{s, Math.max((int)i, (int)1)});
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandParticle.getListOfStringsMatchingLastWord((String[])args, (String[])EnumParticleTypes.getParticleNames()) : (args.length > 1 && args.length <= 4 ? CommandParticle.func_175771_a((String[])args, (int)1, (BlockPos)pos) : (args.length == 10 ? CommandParticle.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"normal", "force"}) : null));
    }
}
