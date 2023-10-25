package net.minecraft.tileentity;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
class TileEntitySign.2
implements ICommandSender {
    final /* synthetic */ EntityPlayer val$playerIn;

    TileEntitySign.2(EntityPlayer entityPlayer) {
        this.val$playerIn = entityPlayer;
    }

    public String getName() {
        return this.val$playerIn.getName();
    }

    public IChatComponent getDisplayName() {
        return this.val$playerIn.getDisplayName();
    }

    public void addChatMessage(IChatComponent component) {
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return permLevel <= 2;
    }

    public BlockPos getPosition() {
        return TileEntitySign.this.pos;
    }

    public Vec3 getPositionVector() {
        return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5, (double)TileEntitySign.this.pos.getY() + 0.5, (double)TileEntitySign.this.pos.getZ() + 0.5);
    }

    public World getEntityWorld() {
        return this.val$playerIn.getEntityWorld();
    }

    public Entity getCommandSenderEntity() {
        return this.val$playerIn;
    }

    public boolean sendCommandFeedback() {
        return false;
    }

    public void setCommandStat(CommandResultStats.Type type, int amount) {
        TileEntitySign.access$000((TileEntitySign)TileEntitySign.this).setCommandStatScore((ICommandSender)this, type, amount);
    }
}
