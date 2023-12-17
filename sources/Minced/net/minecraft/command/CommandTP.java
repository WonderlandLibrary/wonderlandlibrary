// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import java.util.EnumSet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandTP extends CommandBase
{
    @Override
    public String getName() {
        return "tp";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.tp.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        int i = 0;
        Entity entity;
        if (args.length != 2 && args.length != 4 && args.length != 6) {
            entity = CommandBase.getCommandSenderAsPlayer(sender);
        }
        else {
            entity = CommandBase.getEntity(server, sender, args[0]);
            i = 1;
        }
        if (args.length != 1 && args.length != 2) {
            if (args.length < i + 3) {
                throw new WrongUsageException("commands.tp.usage", new Object[0]);
            }
            if (entity.world != null) {
                final int j = 4096;
                int k = i + 1;
                final CoordinateArg commandbase$coordinatearg = CommandBase.parseCoordinate(entity.posX, args[i], true);
                final CoordinateArg commandbase$coordinatearg2 = CommandBase.parseCoordinate(entity.posY, args[k++], -4096, 4096, false);
                final CoordinateArg commandbase$coordinatearg3 = CommandBase.parseCoordinate(entity.posZ, args[k++], true);
                final CoordinateArg commandbase$coordinatearg4 = CommandBase.parseCoordinate(entity.rotationYaw, (args.length > k) ? args[k++] : "~", false);
                final CoordinateArg commandbase$coordinatearg5 = CommandBase.parseCoordinate(entity.rotationPitch, (args.length > k) ? args[k] : "~", false);
                teleportEntityToCoordinates(entity, commandbase$coordinatearg, commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4, commandbase$coordinatearg5);
                CommandBase.notifyCommandListener(sender, this, "commands.tp.success.coordinates", entity.getName(), commandbase$coordinatearg.getResult(), commandbase$coordinatearg2.getResult(), commandbase$coordinatearg3.getResult());
            }
        }
        else {
            final Entity entity2 = CommandBase.getEntity(server, sender, args[args.length - 1]);
            if (entity2.world != entity.world) {
                throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            }
            entity.dismountRidingEntity();
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP)entity).connection.setPlayerLocation(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            }
            else {
                entity.setLocationAndAngles(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            }
            CommandBase.notifyCommandListener(sender, this, "commands.tp.success", entity.getName(), entity2.getName());
        }
    }
    
    private static void teleportEntityToCoordinates(final Entity teleportingEntity, final CoordinateArg argX, final CoordinateArg argY, final CoordinateArg argZ, final CoordinateArg argYaw, final CoordinateArg argPitch) {
        if (teleportingEntity instanceof EntityPlayerMP) {
            final Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
            if (argX.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.X);
            }
            if (argY.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Y);
            }
            if (argZ.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Z);
            }
            if (argPitch.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }
            if (argYaw.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }
            float f = (float)argYaw.getAmount();
            if (!argYaw.isRelative()) {
                f = MathHelper.wrapDegrees(f);
            }
            float f2 = (float)argPitch.getAmount();
            if (!argPitch.isRelative()) {
                f2 = MathHelper.wrapDegrees(f2);
            }
            teleportingEntity.dismountRidingEntity();
            ((EntityPlayerMP)teleportingEntity).connection.setPlayerLocation(argX.getAmount(), argY.getAmount(), argZ.getAmount(), f, f2, set);
            teleportingEntity.setRotationYawHead(f);
        }
        else {
            final float f3 = (float)MathHelper.wrapDegrees(argYaw.getResult());
            float f4 = (float)MathHelper.wrapDegrees(argPitch.getResult());
            f4 = MathHelper.clamp(f4, -90.0f, 90.0f);
            teleportingEntity.setLocationAndAngles(argX.getResult(), argY.getResult(), argZ.getResult(), f3, f4);
            teleportingEntity.setRotationYawHead(f3);
        }
        if (!(teleportingEntity instanceof EntityLivingBase) || !((EntityLivingBase)teleportingEntity).isElytraFlying()) {
            teleportingEntity.motionY = 0.0;
            teleportingEntity.onGround = true;
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length != 1 && args.length != 2) ? Collections.emptyList() : CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
