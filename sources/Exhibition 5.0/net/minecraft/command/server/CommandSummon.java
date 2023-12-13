// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import java.util.Collection;
import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.command.CommandException;
import net.minecraft.util.BlockPos;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSummon extends CommandBase
{
    private static final String __OBFID = "CL_00001158";
    
    @Override
    public String getCommandName() {
        return "summon";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.summon.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        }
        final String var3 = args[0];
        BlockPos var4 = sender.getPosition();
        final Vec3 var5 = sender.getPositionVector();
        double var6 = var5.xCoord;
        double var7 = var5.yCoord;
        double var8 = var5.zCoord;
        if (args.length >= 4) {
            var6 = CommandBase.func_175761_b(var6, args[1], true);
            var7 = CommandBase.func_175761_b(var7, args[2], false);
            var8 = CommandBase.func_175761_b(var8, args[3], true);
            var4 = new BlockPos(var6, var7, var8);
        }
        final World var9 = sender.getEntityWorld();
        if (!var9.isBlockLoaded(var4)) {
            throw new CommandException("commands.summon.outOfWorld", new Object[0]);
        }
        if ("LightningBolt".equals(var3)) {
            var9.addWeatherEffect(new EntityLightningBolt(var9, var6, var7, var8));
            CommandBase.notifyOperators(sender, this, "commands.summon.success", new Object[0]);
        }
        else {
            NBTTagCompound var10 = new NBTTagCompound();
            boolean var11 = false;
            if (args.length >= 5) {
                final IChatComponent var12 = CommandBase.getChatComponentFromNthArg(sender, args, 4);
                try {
                    var10 = JsonToNBT.func_180713_a(var12.getUnformattedText());
                    var11 = true;
                }
                catch (NBTException var13) {
                    throw new CommandException("commands.summon.tagError", new Object[] { var13.getMessage() });
                }
            }
            var10.setString("id", var3);
            Entity var14;
            try {
                var14 = EntityList.createEntityFromNBT(var10, var9);
            }
            catch (RuntimeException var18) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            if (var14 == null) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            var14.setLocationAndAngles(var6, var7, var8, var14.rotationYaw, var14.rotationPitch);
            if (!var11 && var14 instanceof EntityLiving) {
                ((EntityLiving)var14).func_180482_a(var9.getDifficultyForLocation(new BlockPos(var14)), null);
            }
            var9.spawnEntityInWorld(var14);
            Entity var15 = var14;
            Entity var17;
            for (NBTTagCompound var16 = var10; var15 != null && var16.hasKey("Riding", 10); var15 = var17, var16 = var16.getCompoundTag("Riding")) {
                var17 = EntityList.createEntityFromNBT(var16.getCompoundTag("Riding"), var9);
                if (var17 != null) {
                    var17.setLocationAndAngles(var6, var7, var8, var17.rotationYaw, var17.rotationPitch);
                    var9.spawnEntityInWorld(var17);
                    var15.mountEntity(var17);
                }
            }
            CommandBase.notifyOperators(sender, this, "commands.summon.success", new Object[0]);
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.func_175762_a(args, EntityList.func_180124_b()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : null);
    }
}
