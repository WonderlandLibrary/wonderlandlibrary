package net.minecraft.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class EntityMinecartCommandBlock
extends EntityMinecart {
    private final CommandBlockLogic commandBlockLogic = new /* Unavailable Anonymous Inner Class!! */;
    private int activatorRailCooldown = 0;

    public EntityMinecartCommandBlock(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartCommandBlock(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(23, (Object)"");
        this.getDataWatcher().addObject(24, (Object)"");
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.commandBlockLogic.readDataFromNBT(tagCompund);
        this.getDataWatcher().updateObject(23, (Object)this.getCommandBlockLogic().getCommand());
        this.getDataWatcher().updateObject(24, (Object)IChatComponent.Serializer.componentToJson((IChatComponent)this.getCommandBlockLogic().getLastOutput()));
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        this.commandBlockLogic.writeDataToNBT(tagCompound);
    }

    public EntityMinecart.EnumMinecartType getMinecartType() {
        return EntityMinecart.EnumMinecartType.COMMAND_BLOCK;
    }

    public IBlockState getDefaultDisplayTile() {
        return Blocks.command_block.getDefaultState();
    }

    public CommandBlockLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }

    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
        if (receivingPower && this.ticksExisted - this.activatorRailCooldown >= 4) {
            this.getCommandBlockLogic().trigger(this.worldObj);
            this.activatorRailCooldown = this.ticksExisted;
        }
    }

    public boolean interactFirst(EntityPlayer playerIn) {
        this.commandBlockLogic.tryOpenEditCommandBlock(playerIn);
        return false;
    }

    public void onDataWatcherUpdate(int dataID) {
        super.onDataWatcherUpdate(dataID);
        if (dataID == 24) {
            try {
                this.commandBlockLogic.setLastOutput(IChatComponent.Serializer.jsonToComponent((String)this.getDataWatcher().getWatchableObjectString(24)));
            }
            catch (Throwable throwable) {}
        } else if (dataID == 23) {
            this.commandBlockLogic.setCommand(this.getDataWatcher().getWatchableObjectString(23));
        }
    }
}
