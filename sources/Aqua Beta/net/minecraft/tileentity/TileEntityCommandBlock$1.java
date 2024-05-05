package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

class TileEntityCommandBlock.1
extends CommandBlockLogic {
    TileEntityCommandBlock.1() {
    }

    public BlockPos getPosition() {
        return TileEntityCommandBlock.this.pos;
    }

    public Vec3 getPositionVector() {
        return new Vec3((double)TileEntityCommandBlock.this.pos.getX() + 0.5, (double)TileEntityCommandBlock.this.pos.getY() + 0.5, (double)TileEntityCommandBlock.this.pos.getZ() + 0.5);
    }

    public World getEntityWorld() {
        return TileEntityCommandBlock.this.getWorld();
    }

    public void setCommand(String command) {
        super.setCommand(command);
        TileEntityCommandBlock.this.markDirty();
    }

    public void updateCommand() {
        TileEntityCommandBlock.this.getWorld().markBlockForUpdate(TileEntityCommandBlock.this.pos);
    }

    public int func_145751_f() {
        return 0;
    }

    public void func_145757_a(ByteBuf p_145757_1_) {
        p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getX());
        p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getY());
        p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getZ());
    }

    public Entity getCommandSenderEntity() {
        return null;
    }
}
