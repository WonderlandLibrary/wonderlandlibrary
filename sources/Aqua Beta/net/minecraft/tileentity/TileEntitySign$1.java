package net.minecraft.tileentity;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

class TileEntitySign.1
implements ICommandSender {
    TileEntitySign.1() {
    }

    public String getName() {
        return "Sign";
    }

    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public void addChatMessage(IChatComponent component) {
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true;
    }

    public BlockPos getPosition() {
        return TileEntitySign.this.pos;
    }

    public Vec3 getPositionVector() {
        return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5, (double)TileEntitySign.this.pos.getY() + 0.5, (double)TileEntitySign.this.pos.getZ() + 0.5);
    }

    public World getEntityWorld() {
        return TileEntitySign.this.worldObj;
    }

    public Entity getCommandSenderEntity() {
        return null;
    }

    public boolean sendCommandFeedback() {
        return false;
    }

    public void setCommandStat(CommandResultStats.Type type, int amount) {
    }
}
