package net.minecraft.command.server;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class CommandTeleport
extends CommandBase {
    public String getCommandName() {
        return "tp";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.tp.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Entity entity;
        if (args.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        int i = 0;
        if (args.length != 2 && args.length != 4 && args.length != 6) {
            entity = CommandTeleport.getCommandSenderAsPlayer((ICommandSender)sender);
        } else {
            entity = CommandTeleport.getEntity((ICommandSender)sender, (String)args[0]);
            i = 1;
        }
        if (args.length != 1 && args.length != 2) {
            if (args.length < i + 3) {
                throw new WrongUsageException("commands.tp.usage", new Object[0]);
            }
            if (entity.worldObj != null) {
                int lvt_5_2_ = i + 1;
                CommandBase.CoordinateArg commandbase$coordinatearg = CommandTeleport.parseCoordinate((double)entity.posX, (String)args[i], (boolean)true);
                CommandBase.CoordinateArg commandbase$coordinatearg1 = CommandTeleport.parseCoordinate((double)entity.posY, (String)args[lvt_5_2_++], (int)0, (int)0, (boolean)false);
                CommandBase.CoordinateArg commandbase$coordinatearg2 = CommandTeleport.parseCoordinate((double)entity.posZ, (String)args[lvt_5_2_++], (boolean)true);
                CommandBase.CoordinateArg commandbase$coordinatearg3 = CommandTeleport.parseCoordinate((double)entity.rotationYaw, (String)(args.length > lvt_5_2_ ? args[lvt_5_2_++] : "~"), (boolean)false);
                CommandBase.CoordinateArg commandbase$coordinatearg4 = CommandTeleport.parseCoordinate((double)entity.rotationPitch, (String)(args.length > lvt_5_2_ ? args[lvt_5_2_] : "~"), (boolean)false);
                if (entity instanceof EntityPlayerMP) {
                    EnumSet set = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
                    if (commandbase$coordinatearg.func_179630_c()) {
                        set.add((Object)S08PacketPlayerPosLook.EnumFlags.X);
                    }
                    if (commandbase$coordinatearg1.func_179630_c()) {
                        set.add((Object)S08PacketPlayerPosLook.EnumFlags.Y);
                    }
                    if (commandbase$coordinatearg2.func_179630_c()) {
                        set.add((Object)S08PacketPlayerPosLook.EnumFlags.Z);
                    }
                    if (commandbase$coordinatearg4.func_179630_c()) {
                        set.add((Object)S08PacketPlayerPosLook.EnumFlags.X_ROT);
                    }
                    if (commandbase$coordinatearg3.func_179630_c()) {
                        set.add((Object)S08PacketPlayerPosLook.EnumFlags.Y_ROT);
                    }
                    float f = (float)commandbase$coordinatearg3.func_179629_b();
                    if (!commandbase$coordinatearg3.func_179630_c()) {
                        f = MathHelper.wrapAngleTo180_float((float)f);
                    }
                    float f1 = (float)commandbase$coordinatearg4.func_179629_b();
                    if (!commandbase$coordinatearg4.func_179630_c()) {
                        f1 = MathHelper.wrapAngleTo180_float((float)f1);
                    }
                    if (f1 > 90.0f || f1 < -90.0f) {
                        f1 = MathHelper.wrapAngleTo180_float((float)(180.0f - f1));
                        f = MathHelper.wrapAngleTo180_float((float)(f + 180.0f));
                    }
                    entity.mountEntity((Entity)null);
                    ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(commandbase$coordinatearg.func_179629_b(), commandbase$coordinatearg1.func_179629_b(), commandbase$coordinatearg2.func_179629_b(), f, f1, (Set)set);
                    entity.setRotationYawHead(f);
                } else {
                    float f2 = (float)MathHelper.wrapAngleTo180_double((double)commandbase$coordinatearg3.func_179628_a());
                    float f3 = (float)MathHelper.wrapAngleTo180_double((double)commandbase$coordinatearg4.func_179628_a());
                    if (f3 > 90.0f || f3 < -90.0f) {
                        f3 = MathHelper.wrapAngleTo180_float((float)(180.0f - f3));
                        f2 = MathHelper.wrapAngleTo180_float((float)(f2 + 180.0f));
                    }
                    entity.setLocationAndAngles(commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg1.func_179628_a(), commandbase$coordinatearg2.func_179628_a(), f2, f3);
                    entity.setRotationYawHead(f2);
                }
                CommandTeleport.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.tp.success.coordinates", (Object[])new Object[]{entity.getName(), commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg1.func_179628_a(), commandbase$coordinatearg2.func_179628_a()});
            }
        } else {
            Entity entity1 = CommandTeleport.getEntity((ICommandSender)sender, (String)args[args.length - 1]);
            if (entity1.worldObj != entity.worldObj) {
                throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            }
            entity.mountEntity((Entity)null);
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
            } else {
                entity.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
            }
            CommandTeleport.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.tp.success", (Object[])new Object[]{entity.getName(), entity1.getName()});
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length != 1 && args.length != 2 ? null : CommandTeleport.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames());
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
