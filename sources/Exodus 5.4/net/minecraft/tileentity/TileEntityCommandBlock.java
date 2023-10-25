/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileEntityCommandBlock
extends TileEntity {
    private final CommandBlockLogic commandBlockLogic = new CommandBlockLogic(){

        @Override
        public int func_145751_f() {
            return 0;
        }

        @Override
        public void updateCommand() {
            TileEntityCommandBlock.this.getWorld().markBlockForUpdate(TileEntityCommandBlock.this.pos);
        }

        @Override
        public BlockPos getPosition() {
            return TileEntityCommandBlock.this.pos;
        }

        @Override
        public void setCommand(String string) {
            super.setCommand(string);
            TileEntityCommandBlock.this.markDirty();
        }

        @Override
        public Vec3 getPositionVector() {
            return new Vec3((double)TileEntityCommandBlock.this.pos.getX() + 0.5, (double)TileEntityCommandBlock.this.pos.getY() + 0.5, (double)TileEntityCommandBlock.this.pos.getZ() + 0.5);
        }

        @Override
        public World getEntityWorld() {
            return TileEntityCommandBlock.this.getWorld();
        }

        @Override
        public Entity getCommandSenderEntity() {
            return null;
        }

        @Override
        public void func_145757_a(ByteBuf byteBuf) {
            byteBuf.writeInt(TileEntityCommandBlock.this.pos.getX());
            byteBuf.writeInt(TileEntityCommandBlock.this.pos.getY());
            byteBuf.writeInt(TileEntityCommandBlock.this.pos.getZ());
        }
    };

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        this.writeToNBT(nBTTagCompound);
        return new S35PacketUpdateTileEntity(this.pos, 2, nBTTagCompound);
    }

    public CommandResultStats getCommandResultStats() {
        return this.commandBlockLogic.getCommandResultStats();
    }

    public CommandBlockLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.commandBlockLogic.readDataFromNBT(nBTTagCompound);
    }

    @Override
    public boolean func_183000_F() {
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        this.commandBlockLogic.writeDataToNBT(nBTTagCompound);
    }
}

