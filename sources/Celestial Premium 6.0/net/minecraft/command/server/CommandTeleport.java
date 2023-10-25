/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CommandTeleport
extends CommandBase {
    @Override
    public String getCommandName() {
        return "teleport";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.teleport.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.teleport.usage", new Object[0]);
        }
        Entity entity = CommandTeleport.getEntity(server, sender, args[0]);
        if (entity.world != null) {
            int i = 4096;
            Vec3d vec3d = sender.getPositionVector();
            int j = 1;
            CommandBase.CoordinateArg commandbase$coordinatearg = CommandTeleport.parseCoordinate(vec3d.x, args[j++], true);
            CommandBase.CoordinateArg commandbase$coordinatearg1 = CommandTeleport.parseCoordinate(vec3d.y, args[j++], -4096, 4096, false);
            CommandBase.CoordinateArg commandbase$coordinatearg2 = CommandTeleport.parseCoordinate(vec3d.z, args[j++], true);
            Entity entity1 = sender.getCommandSenderEntity() == null ? entity : sender.getCommandSenderEntity();
            CommandBase.CoordinateArg commandbase$coordinatearg3 = CommandTeleport.parseCoordinate(args.length > j ? (double)entity1.rotationYaw : (double)entity.rotationYaw, args.length > j ? args[j] : "~", false);
            CommandBase.CoordinateArg commandbase$coordinatearg4 = CommandTeleport.parseCoordinate(args.length > ++j ? (double)entity1.rotationPitch : (double)entity.rotationPitch, args.length > j ? args[j] : "~", false);
            CommandTeleport.doTeleport(entity, commandbase$coordinatearg, commandbase$coordinatearg1, commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4);
            CommandTeleport.notifyCommandListener(sender, (ICommand)this, "commands.teleport.success.coordinates", entity.getName(), commandbase$coordinatearg.getResult(), commandbase$coordinatearg1.getResult(), commandbase$coordinatearg2.getResult());
        }
    }

    private static void doTeleport(Entity p_189862_0_, CommandBase.CoordinateArg p_189862_1_, CommandBase.CoordinateArg p_189862_2_, CommandBase.CoordinateArg p_189862_3_, CommandBase.CoordinateArg p_189862_4_, CommandBase.CoordinateArg p_189862_5_) {
        if (p_189862_0_ instanceof EntityPlayerMP) {
            EnumSet<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
            float f = (float)p_189862_4_.getAmount();
            if (p_189862_4_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            } else {
                f = MathHelper.wrapDegrees(f);
            }
            float f1 = (float)p_189862_5_.getAmount();
            if (p_189862_5_.isRelative()) {
                set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            } else {
                f1 = MathHelper.wrapDegrees(f1);
            }
            p_189862_0_.dismountRidingEntity();
            ((EntityPlayerMP)p_189862_0_).connection.setPlayerLocation(p_189862_1_.getResult(), p_189862_2_.getResult(), p_189862_3_.getResult(), f, f1, set);
            p_189862_0_.setRotationYawHead(f);
        } else {
            float f2 = (float)MathHelper.wrapDegrees(p_189862_4_.getResult());
            float f3 = (float)MathHelper.wrapDegrees(p_189862_5_.getResult());
            f3 = MathHelper.clamp(f3, -90.0f, 90.0f);
            p_189862_0_.setLocationAndAngles(p_189862_1_.getResult(), p_189862_2_.getResult(), p_189862_3_.getResult(), f2, f3);
            p_189862_0_.setRotationYawHead(f2);
        }
        if (!(p_189862_0_ instanceof EntityLivingBase) || !((EntityLivingBase)p_189862_0_).isElytraFlying()) {
            p_189862_0_.motionY = 0.0;
            p_189862_0_.onGround = true;
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandTeleport.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return args.length > 1 && args.length <= 4 ? CommandTeleport.getTabCompletionCoordinate(args, 1, pos) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

