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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle
extends CommandBase {
    @Override
    public String getCommandName() {
        return "particle";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.particle.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        World world;
        Object object;
        if (stringArray.length < 8) {
            throw new WrongUsageException("commands.particle.usage", new Object[0]);
        }
        boolean bl = false;
        Object object2 = null;
        EnumParticleTypes[] enumParticleTypesArray = EnumParticleTypes.values();
        int n = enumParticleTypesArray.length;
        int n2 = 0;
        while (n2 < n) {
            object = enumParticleTypesArray[n2];
            if (object.hasArguments()) {
                if (stringArray[0].startsWith(object.getParticleName())) {
                    bl = true;
                    object2 = object;
                    break;
                }
            } else if (stringArray[0].equals(object.getParticleName())) {
                bl = true;
                object2 = object;
                break;
            }
            ++n2;
        }
        if (!bl) {
            throw new CommandException("commands.particle.notFound", stringArray[0]);
        }
        object = stringArray[0];
        Vec3 vec3 = iCommandSender.getPositionVector();
        double d = (float)CommandParticle.parseDouble(vec3.xCoord, stringArray[1], true);
        double d2 = (float)CommandParticle.parseDouble(vec3.yCoord, stringArray[2], true);
        double d3 = (float)CommandParticle.parseDouble(vec3.zCoord, stringArray[3], true);
        double d4 = (float)CommandParticle.parseDouble(stringArray[4]);
        double d5 = (float)CommandParticle.parseDouble(stringArray[5]);
        double d6 = (float)CommandParticle.parseDouble(stringArray[6]);
        double d7 = (float)CommandParticle.parseDouble(stringArray[7]);
        int n3 = 0;
        if (stringArray.length > 8) {
            n3 = CommandParticle.parseInt(stringArray[8], 0);
        }
        boolean bl2 = false;
        if (stringArray.length > 9 && "force".equals(stringArray[9])) {
            bl2 = true;
        }
        if ((world = iCommandSender.getEntityWorld()) instanceof WorldServer) {
            WorldServer worldServer = (WorldServer)world;
            int[] nArray = new int[((EnumParticleTypes)((Object)object2)).getArgumentCount()];
            if (((EnumParticleTypes)((Object)object2)).hasArguments()) {
                String[] stringArray2 = stringArray[0].split("_", 3);
                int n4 = 1;
                while (n4 < stringArray2.length) {
                    try {
                        nArray[n4 - 1] = Integer.parseInt(stringArray2[n4]);
                    }
                    catch (NumberFormatException numberFormatException) {
                        throw new CommandException("commands.particle.notFound", stringArray[0]);
                    }
                    ++n4;
                }
            }
            worldServer.spawnParticle((EnumParticleTypes)((Object)object2), bl2, d, d2, d3, n3, d4, d5, d6, d7, nArray);
            CommandParticle.notifyOperators(iCommandSender, (ICommand)this, "commands.particle.success", object, Math.max(n3, 1));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandParticle.getListOfStringsMatchingLastWord(stringArray, EnumParticleTypes.getParticleNames()) : (stringArray.length > 1 && stringArray.length <= 4 ? CommandParticle.func_175771_a(stringArray, 1, blockPos) : (stringArray.length == 10 ? CommandParticle.getListOfStringsMatchingLastWord(stringArray, "normal", "force") : null));
    }
}

