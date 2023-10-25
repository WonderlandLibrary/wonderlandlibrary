/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandSummon
extends CommandBase {
    @Override
    public String getCommandName() {
        return "summon";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandSummon.getListOfStringsMatchingLastWord(stringArray, EntityList.getEntityNameList()) : (stringArray.length > 1 && stringArray.length <= 4 ? CommandSummon.func_175771_a(stringArray, 1, blockPos) : null);
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.summon.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        World world;
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        }
        String string = stringArray[0];
        BlockPos blockPos = iCommandSender.getPosition();
        Vec3 vec3 = iCommandSender.getPositionVector();
        double d = vec3.xCoord;
        double d2 = vec3.yCoord;
        double d3 = vec3.zCoord;
        if (stringArray.length >= 4) {
            d = CommandSummon.parseDouble(d, stringArray[1], true);
            d2 = CommandSummon.parseDouble(d2, stringArray[2], false);
            d3 = CommandSummon.parseDouble(d3, stringArray[3], true);
            blockPos = new BlockPos(d, d2, d3);
        }
        if (!(world = iCommandSender.getEntityWorld()).isBlockLoaded(blockPos)) {
            throw new CommandException("commands.summon.outOfWorld", new Object[0]);
        }
        if ("LightningBolt".equals(string)) {
            world.addWeatherEffect(new EntityLightningBolt(world, d, d2, d3));
            CommandSummon.notifyOperators(iCommandSender, (ICommand)this, "commands.summon.success", new Object[0]);
        } else {
            Object object;
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            boolean bl = false;
            if (stringArray.length >= 5) {
                object = CommandSummon.getChatComponentFromNthArg(iCommandSender, stringArray, 4);
                try {
                    nBTTagCompound = JsonToNBT.getTagFromJson(object.getUnformattedText());
                    bl = true;
                }
                catch (NBTException nBTException) {
                    throw new CommandException("commands.summon.tagError", nBTException.getMessage());
                }
            }
            nBTTagCompound.setString("id", string);
            try {
                object = EntityList.createEntityFromNBT(nBTTagCompound, world);
            }
            catch (RuntimeException runtimeException) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            if (object == null) {
                throw new CommandException("commands.summon.failed", new Object[0]);
            }
            ((Entity)object).setLocationAndAngles(d, d2, d3, ((Entity)object).rotationYaw, ((Entity)object).rotationPitch);
            if (!bl && object instanceof EntityLiving) {
                ((EntityLiving)object).onInitialSpawn(world.getDifficultyForLocation(new BlockPos((Entity)object)), null);
            }
            world.spawnEntityInWorld((Entity)object);
            Object object2 = object;
            NBTTagCompound nBTTagCompound2 = nBTTagCompound;
            while (object2 != null && nBTTagCompound2.hasKey("Riding", 10)) {
                Entity entity = EntityList.createEntityFromNBT(nBTTagCompound2.getCompoundTag("Riding"), world);
                if (entity != null) {
                    entity.setLocationAndAngles(d, d2, d3, entity.rotationYaw, entity.rotationPitch);
                    world.spawnEntityInWorld(entity);
                    ((Entity)object2).mountEntity(entity);
                }
                object2 = entity;
                nBTTagCompound2 = nBTTagCompound2.getCompoundTag("Riding");
            }
            CommandSummon.notifyOperators(iCommandSender, (ICommand)this, "commands.summon.success", new Object[0]);
        }
    }
}

