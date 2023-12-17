// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.command.CommandResultStats;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;

public class TileEntityCommandBlock extends TileEntity
{
    private boolean powered;
    private boolean auto;
    private boolean conditionMet;
    private boolean sendToClient;
    private final CommandBlockBaseLogic commandBlockLogic;
    
    public TileEntityCommandBlock() {
        this.commandBlockLogic = new CommandBlockBaseLogic() {
            @Override
            public BlockPos getPosition() {
                return TileEntityCommandBlock.this.pos;
            }
            
            @Override
            public Vec3d getPositionVector() {
                return new Vec3d(TileEntityCommandBlock.this.pos.getX() + 0.5, TileEntityCommandBlock.this.pos.getY() + 0.5, TileEntityCommandBlock.this.pos.getZ() + 0.5);
            }
            
            @Override
            public World getEntityWorld() {
                return TileEntityCommandBlock.this.getWorld();
            }
            
            @Override
            public void setCommand(final String command) {
                super.setCommand(command);
                TileEntityCommandBlock.this.markDirty();
            }
            
            @Override
            public void updateCommand() {
                final IBlockState iblockstate = TileEntityCommandBlock.this.world.getBlockState(TileEntityCommandBlock.this.pos);
                TileEntityCommandBlock.this.getWorld().notifyBlockUpdate(TileEntityCommandBlock.this.pos, iblockstate, iblockstate, 3);
            }
            
            @Override
            public int getCommandBlockType() {
                return 0;
            }
            
            @Override
            public void fillInInfo(final ByteBuf buf) {
                buf.writeInt(TileEntityCommandBlock.this.pos.getX());
                buf.writeInt(TileEntityCommandBlock.this.pos.getY());
                buf.writeInt(TileEntityCommandBlock.this.pos.getZ());
            }
            
            @Override
            public MinecraftServer getServer() {
                return TileEntityCommandBlock.this.world.getMinecraftServer();
            }
        };
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.commandBlockLogic.writeToNBT(compound);
        compound.setBoolean("powered", this.isPowered());
        compound.setBoolean("conditionMet", this.isConditionMet());
        compound.setBoolean("auto", this.isAuto());
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.commandBlockLogic.readDataFromNBT(compound);
        this.powered = compound.getBoolean("powered");
        this.conditionMet = compound.getBoolean("conditionMet");
        this.setAuto(compound.getBoolean("auto"));
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        if (this.isSendToClient()) {
            this.setSendToClient(false);
            final NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
            return new SPacketUpdateTileEntity(this.pos, 2, nbttagcompound);
        }
        return null;
    }
    
    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }
    
    public CommandBlockBaseLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }
    
    public CommandResultStats getCommandResultStats() {
        return this.commandBlockLogic.getCommandResultStats();
    }
    
    public void setPowered(final boolean poweredIn) {
        this.powered = poweredIn;
    }
    
    public boolean isPowered() {
        return this.powered;
    }
    
    public boolean isAuto() {
        return this.auto;
    }
    
    public void setAuto(final boolean autoIn) {
        final boolean flag = this.auto;
        this.auto = autoIn;
        if (!flag && autoIn && !this.powered && this.world != null && this.getMode() != Mode.SEQUENCE) {
            final Block block = this.getBlockType();
            if (block instanceof BlockCommandBlock) {
                this.setConditionMet();
                this.world.scheduleUpdate(this.pos, block, block.tickRate(this.world));
            }
        }
    }
    
    public boolean isConditionMet() {
        return this.conditionMet;
    }
    
    public boolean setConditionMet() {
        this.conditionMet = true;
        if (this.isConditional()) {
            final BlockPos blockpos = this.pos.offset(this.world.getBlockState(this.pos).getValue((IProperty<EnumFacing>)BlockCommandBlock.FACING).getOpposite());
            if (this.world.getBlockState(blockpos).getBlock() instanceof BlockCommandBlock) {
                final TileEntity tileentity = this.world.getTileEntity(blockpos);
                this.conditionMet = (tileentity instanceof TileEntityCommandBlock && ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() > 0);
            }
            else {
                this.conditionMet = false;
            }
        }
        return this.conditionMet;
    }
    
    public boolean isSendToClient() {
        return this.sendToClient;
    }
    
    public void setSendToClient(final boolean p_184252_1_) {
        this.sendToClient = p_184252_1_;
    }
    
    public Mode getMode() {
        final Block block = this.getBlockType();
        if (block == Blocks.COMMAND_BLOCK) {
            return Mode.REDSTONE;
        }
        if (block == Blocks.REPEATING_COMMAND_BLOCK) {
            return Mode.AUTO;
        }
        return (block == Blocks.CHAIN_COMMAND_BLOCK) ? Mode.SEQUENCE : Mode.REDSTONE;
    }
    
    public boolean isConditional() {
        final IBlockState iblockstate = this.world.getBlockState(this.getPos());
        return iblockstate.getBlock() instanceof BlockCommandBlock && iblockstate.getValue((IProperty<Boolean>)BlockCommandBlock.CONDITIONAL);
    }
    
    @Override
    public void validate() {
        this.blockType = null;
        super.validate();
    }
    
    public enum Mode
    {
        SEQUENCE, 
        AUTO, 
        REDSTONE;
    }
}
