/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class CommandSummon
extends CommandBase {
    @Override
    public String getCommandName() {
        return "summon";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.summon.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world;
        if (args.length < 1) {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        }
        String s = args[0];
        BlockPos blockpos = sender.getPosition();
        Vec3d vec3d = sender.getPositionVector();
        double d0 = vec3d.x;
        double d1 = vec3d.y;
        double d2 = vec3d.z;
        if (args.length >= 4) {
            d0 = CommandSummon.parseDouble(d0, args[1], true);
            d1 = CommandSummon.parseDouble(d1, args[2], false);
            d2 = CommandSummon.parseDouble(d2, args[3], true);
            blockpos = new BlockPos(d0, d1, d2);
        }
        if (!(world = sender.getEntityWorld()).isBlockLoaded(blockpos)) {
            throw new CommandException("commands.summon.outOfWorld", new Object[0]);
        }
        if (EntityList.field_191307_a.equals(new ResourceLocation(s))) {
            world.addWeatherEffect(new EntityLightningBolt(world, d0, d1, d2, false));
            CommandSummon.notifyCommandListener(sender, (ICommand)this, "commands.summon.success", new Object[0]);
        } else {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            boolean flag = false;
            if (args.length >= 5) {
                String s1 = CommandSummon.buildString(args, 4);
                try {
                    nbttagcompound = JsonToNBT.getTagFromJson(s1);
                    flag = true;
                }
                catch (NBTException nbtexception) {
                    throw new CommandException("commands.summon.tagError", nbtexception.getMessage());
                }
            }
            nbttagcompound.setString("id", s);
            Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, true);
            if (entity == null) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
            if (!flag && entity instanceof EntityLiving) {
                ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
            }
            CommandSummon.notifyCommandListener(sender, (ICommand)this, "commands.summon.success", new Object[0]);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandSummon.getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        }
        return args.length > 1 && args.length <= 4 ? CommandSummon.getTabCompletionCoordinate(args, 1, pos) : Collections.emptyList();
    }
}

