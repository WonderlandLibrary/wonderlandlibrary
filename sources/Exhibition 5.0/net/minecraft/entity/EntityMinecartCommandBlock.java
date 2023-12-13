// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.item.EntityMinecart;

public class EntityMinecartCommandBlock extends EntityMinecart
{
    private final CommandBlockLogic field_145824_a;
    private int field_145823_b;
    private static final String __OBFID = "CL_00001672";
    
    public EntityMinecartCommandBlock(final World worldIn) {
        super(worldIn);
        this.field_145824_a = new CommandBlockLogic() {
            private static final String __OBFID = "CL_00001673";
            
            @Override
            public void func_145756_e() {
                EntityMinecartCommandBlock.this.getDataWatcher().updateObject(23, this.getCustomName());
                EntityMinecartCommandBlock.this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(this.getLastOutput()));
            }
            
            @Override
            public int func_145751_f() {
                return 1;
            }
            
            @Override
            public void func_145757_a(final ByteBuf p_145757_1_) {
                p_145757_1_.writeInt(EntityMinecartCommandBlock.this.getEntityId());
            }
            
            @Override
            public BlockPos getPosition() {
                return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public Vec3 getPositionVector() {
                return new Vec3(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public World getEntityWorld() {
                return EntityMinecartCommandBlock.this.worldObj;
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return EntityMinecartCommandBlock.this;
            }
        };
        this.field_145823_b = 0;
    }
    
    public EntityMinecartCommandBlock(final World worldIn, final double p_i45322_2_, final double p_i45322_4_, final double p_i45322_6_) {
        super(worldIn, p_i45322_2_, p_i45322_4_, p_i45322_6_);
        this.field_145824_a = new CommandBlockLogic() {
            private static final String __OBFID = "CL_00001673";
            
            @Override
            public void func_145756_e() {
                EntityMinecartCommandBlock.this.getDataWatcher().updateObject(23, this.getCustomName());
                EntityMinecartCommandBlock.this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(this.getLastOutput()));
            }
            
            @Override
            public int func_145751_f() {
                return 1;
            }
            
            @Override
            public void func_145757_a(final ByteBuf p_145757_1_) {
                p_145757_1_.writeInt(EntityMinecartCommandBlock.this.getEntityId());
            }
            
            @Override
            public BlockPos getPosition() {
                return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public Vec3 getPositionVector() {
                return new Vec3(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public World getEntityWorld() {
                return EntityMinecartCommandBlock.this.worldObj;
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return EntityMinecartCommandBlock.this;
            }
        };
        this.field_145823_b = 0;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(23, "");
        this.getDataWatcher().addObject(24, "");
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.field_145824_a.readDataFromNBT(tagCompund);
        this.getDataWatcher().updateObject(23, this.func_145822_e().getCustomName());
        this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(this.func_145822_e().getLastOutput()));
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        this.field_145824_a.writeDataToNBT(tagCompound);
    }
    
    @Override
    public EnumMinecartType func_180456_s() {
        return EnumMinecartType.COMMAND_BLOCK;
    }
    
    @Override
    public IBlockState func_180457_u() {
        return Blocks.command_block.getDefaultState();
    }
    
    public CommandBlockLogic func_145822_e() {
        return this.field_145824_a;
    }
    
    @Override
    public void onActivatorRailPass(final int p_96095_1_, final int p_96095_2_, final int p_96095_3_, final boolean p_96095_4_) {
        if (p_96095_4_ && this.ticksExisted - this.field_145823_b >= 4) {
            this.func_145822_e().trigger(this.worldObj);
            this.field_145823_b = this.ticksExisted;
        }
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        this.field_145824_a.func_175574_a(playerIn);
        return false;
    }
    
    @Override
    public void func_145781_i(final int p_145781_1_) {
        super.func_145781_i(p_145781_1_);
        if (p_145781_1_ == 24) {
            try {
                this.field_145824_a.func_145750_b(IChatComponent.Serializer.jsonToComponent(this.getDataWatcher().getWatchableObjectString(24)));
            }
            catch (Throwable t) {}
        }
        else if (p_145781_1_ == 23) {
            this.field_145824_a.setCommand(this.getDataWatcher().getWatchableObjectString(23));
        }
    }
}
