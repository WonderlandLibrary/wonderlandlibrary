package net.minecraft.tileentity;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCommandBlock
extends TileEntity {
    private final CommandBlockLogic commandBlockLogic = new /* Unavailable Anonymous Inner Class!! */;

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.commandBlockLogic.writeDataToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.commandBlockLogic.readDataFromNBT(compound);
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 2, nbttagcompound);
    }

    public boolean func_183000_F() {
        return true;
    }

    public CommandBlockLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }

    public CommandResultStats getCommandResultStats() {
        return this.commandBlockLogic.getCommandResultStats();
    }
}
