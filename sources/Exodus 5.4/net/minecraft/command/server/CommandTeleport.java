/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.EnumSet;
import java.util.List;
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
    @Override
    public String getCommandName() {
        return "tp";
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.tp.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length != 1 && stringArray.length != 2 ? null : CommandTeleport.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        Entity entity;
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        int n = 0;
        if (stringArray.length != 2 && stringArray.length != 4 && stringArray.length != 6) {
            entity = CommandTeleport.getCommandSenderAsPlayer(iCommandSender);
        } else {
            entity = CommandTeleport.func_175768_b(iCommandSender, stringArray[0]);
            n = 1;
        }
        if (stringArray.length != 1 && stringArray.length != 2) {
            if (stringArray.length < n + 3) {
                throw new WrongUsageException("commands.tp.usage", new Object[0]);
            }
            if (entity.worldObj != null) {
                int n2 = n + 1;
                CommandBase.CoordinateArg coordinateArg = CommandTeleport.parseCoordinate(entity.posX, stringArray[n], true);
                CommandBase.CoordinateArg coordinateArg2 = CommandTeleport.parseCoordinate(entity.posY, stringArray[n2++], 0, 0, false);
                CommandBase.CoordinateArg coordinateArg3 = CommandTeleport.parseCoordinate(entity.posZ, stringArray[n2++], true);
                CommandBase.CoordinateArg coordinateArg4 = CommandTeleport.parseCoordinate(entity.rotationYaw, stringArray.length > n2 ? stringArray[n2++] : "~", false);
                CommandBase.CoordinateArg coordinateArg5 = CommandTeleport.parseCoordinate(entity.rotationPitch, stringArray.length > n2 ? stringArray[n2] : "~", false);
                if (entity instanceof EntityPlayerMP) {
                    EnumSet<S08PacketPlayerPosLook.EnumFlags> enumSet = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
                    if (coordinateArg.func_179630_c()) {
                        enumSet.add(S08PacketPlayerPosLook.EnumFlags.X);
                    }
                    if (coordinateArg2.func_179630_c()) {
                        enumSet.add(S08PacketPlayerPosLook.EnumFlags.Y);
                    }
                    if (coordinateArg3.func_179630_c()) {
                        enumSet.add(S08PacketPlayerPosLook.EnumFlags.Z);
                    }
                    if (coordinateArg5.func_179630_c()) {
                        enumSet.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
                    }
                    if (coordinateArg4.func_179630_c()) {
                        enumSet.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
                    }
                    float f = (float)coordinateArg4.func_179629_b();
                    if (!coordinateArg4.func_179630_c()) {
                        f = MathHelper.wrapAngleTo180_float(f);
                    }
                    float f2 = (float)coordinateArg5.func_179629_b();
                    if (!coordinateArg5.func_179630_c()) {
                        f2 = MathHelper.wrapAngleTo180_float(f2);
                    }
                    if (f2 > 90.0f || f2 < -90.0f) {
                        f2 = MathHelper.wrapAngleTo180_float(180.0f - f2);
                        f = MathHelper.wrapAngleTo180_float(f + 180.0f);
                    }
                    entity.mountEntity(null);
                    ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(coordinateArg.func_179629_b(), coordinateArg2.func_179629_b(), coordinateArg3.func_179629_b(), f, f2, enumSet);
                    entity.setRotationYawHead(f);
                } else {
                    float f = (float)MathHelper.wrapAngleTo180_double(coordinateArg4.func_179628_a());
                    float f3 = (float)MathHelper.wrapAngleTo180_double(coordinateArg5.func_179628_a());
                    if (f3 > 90.0f || f3 < -90.0f) {
                        f3 = MathHelper.wrapAngleTo180_float(180.0f - f3);
                        f = MathHelper.wrapAngleTo180_float(f + 180.0f);
                    }
                    entity.setLocationAndAngles(coordinateArg.func_179628_a(), coordinateArg2.func_179628_a(), coordinateArg3.func_179628_a(), f, f3);
                    entity.setRotationYawHead(f);
                }
                CommandTeleport.notifyOperators(iCommandSender, (ICommand)this, "commands.tp.success.coordinates", entity.getName(), coordinateArg.func_179628_a(), coordinateArg2.func_179628_a(), coordinateArg3.func_179628_a());
            }
        } else {
            Entity entity2 = CommandTeleport.func_175768_b(iCommandSender, stringArray[stringArray.length - 1]);
            if (entity2.worldObj != entity.worldObj) {
                throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            }
            entity.mountEntity(null);
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            } else {
                entity.setLocationAndAngles(entity2.posX, entity2.posY, entity2.posZ, entity2.rotationYaw, entity2.rotationPitch);
            }
            CommandTeleport.notifyOperators(iCommandSender, (ICommand)this, "commands.tp.success", entity.getName(), entity2.getName());
        }
    }
}

