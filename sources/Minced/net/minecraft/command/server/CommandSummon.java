// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityList;
import net.minecraft.command.CommandException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSummon extends CommandBase
{
    @Override
    public String getName() {
        return "summon";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.summon.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        }
        final String s = args[0];
        BlockPos blockpos = sender.getPosition();
        final Vec3d vec3d = sender.getPositionVector();
        double d0 = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        if (args.length >= 4) {
            d0 = CommandBase.parseDouble(d0, args[1], true);
            d2 = CommandBase.parseDouble(d2, args[2], false);
            d3 = CommandBase.parseDouble(d3, args[3], true);
            blockpos = new BlockPos(d0, d2, d3);
        }
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.summon.outOfWorld", new Object[0]);
        }
        if (EntityList.LIGHTNING_BOLT.equals(new ResourceLocation(s))) {
            world.addWeatherEffect(new EntityLightningBolt(world, d0, d2, d3, false));
            CommandBase.notifyCommandListener(sender, this, "commands.summon.success", new Object[0]);
        }
        else {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            boolean flag = false;
            if (args.length >= 5) {
                final String s2 = CommandBase.buildString(args, 4);
                try {
                    nbttagcompound = JsonToNBT.getTagFromJson(s2);
                    flag = true;
                }
                catch (NBTException nbtexception) {
                    throw new CommandException("commands.summon.tagError", new Object[] { nbtexception.getMessage() });
                }
            }
            nbttagcompound.setString("id", s);
            final Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d2, d3, true);
            if (entity == null) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            entity.setLocationAndAngles(d0, d2, d3, entity.rotationYaw, entity.rotationPitch);
            if (!flag && entity instanceof EntityLiving) {
                ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
            }
            CommandBase.notifyCommandListener(sender, this, "commands.summon.success", new Object[0]);
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, EntityList.getEntityNameList());
        }
        return (args.length > 1 && args.length <= 4) ? CommandBase.getTabCompletionCoordinate(args, 1, targetPos) : Collections.emptyList();
    }
}
