// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import java.util.EnumSet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.CommandException;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommand;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTeleport extends CommandBase
{
    @Override
    public String getName() {
        return "teleport";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.teleport.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.teleport.usage", new Object[0]);
        }
        final Entity entity = CommandBase.getEntity(server, sender, args[0]);
        if (entity.world != null) {
            final int i = 4096;
            final Vec3d vec3d = sender.getPositionVector();
            int j = 1;
            final CoordinateArg commandbase$coordinatearg = CommandBase.parseCoordinate(vec3d.x, args[j++], true);
            final CoordinateArg commandbase$coordinatearg2 = CommandBase.parseCoordinate(vec3d.y, args[j++], -4096, 4096, false);
            final CoordinateArg commandbase$coordinatearg3 = CommandBase.parseCoordinate(vec3d.z, args[j++], true);
            final Entity entity2 = (sender.getCommandSenderEntity() == null) ? entity : sender.getCommandSenderEntity();
            final CoordinateArg commandbase$coordinatearg4 = CommandBase.parseCoordinate((args.length > j) ? ((double)entity2.rotationYaw) : ((double)entity.rotationYaw), (args.length > j) ? args[j] : "~", false);
            ++j;
            final CoordinateArg commandbase$coordinatearg5 = CommandBase.parseCoordinate((args.length > j) ? ((double)entity2.rotationPitch) : ((double)entity.rotationPitch), (args.length > j) ? args[j] : "~", false);
            doTeleport(entity, commandbase$coordinatearg, commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4, commandbase$coordinatearg5);
            CommandBase.notifyCommandListener(sender, this, "commands.teleport.success.coordinates", entity.getName(), commandbase$coordinatearg.getResult(), commandbase$coordinatearg2.getResult(), commandbase$coordinatearg3.getResult());
        }
    }
    
    private static void doTeleport(final Entity teleportingEntity, final CoordinateArg argX, final CoordinateArg argY, final CoordinateArg argZ, final CoordinateArg argYaw, final CoordinateArg argPitch) {
        if (teleportingEntity instanceof EntityPlayerMP) {
            final Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
            float f = (float)argYaw.getAmount();
            if (argYaw.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }
            else {
                f = MathHelper.wrapDegrees(f);
            }
            float f2 = (float)argPitch.getAmount();
            if (argPitch.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }
            else {
                f2 = MathHelper.wrapDegrees(f2);
            }
            teleportingEntity.dismountRidingEntity();
            ((EntityPlayerMP)teleportingEntity).connection.setPlayerLocation(argX.getResult(), argY.getResult(), argZ.getResult(), f, f2, set);
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
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return (args.length > 1 && args.length <= 4) ? CommandBase.getTabCompletionCoordinate(args, 1, targetPos) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
