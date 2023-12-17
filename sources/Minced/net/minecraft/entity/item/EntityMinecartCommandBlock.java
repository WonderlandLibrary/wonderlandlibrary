// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.datasync.DataParameter;

public class EntityMinecartCommandBlock extends EntityMinecart
{
    private static final DataParameter<String> COMMAND;
    private static final DataParameter<ITextComponent> LAST_OUTPUT;
    private final CommandBlockBaseLogic commandBlockLogic;
    private int activatorRailCooldown;
    
    public EntityMinecartCommandBlock(final World worldIn) {
        super(worldIn);
        this.commandBlockLogic = new CommandBlockBaseLogic() {
            @Override
            public void updateCommand() {
                EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.COMMAND, this.getCommand());
                EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.LAST_OUTPUT, this.getLastOutput());
            }
            
            @Override
            public int getCommandBlockType() {
                return 1;
            }
            
            @Override
            public void fillInInfo(final ByteBuf buf) {
                buf.writeInt(EntityMinecartCommandBlock.this.getEntityId());
            }
            
            @Override
            public BlockPos getPosition() {
                return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public Vec3d getPositionVector() {
                return new Vec3d(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public World getEntityWorld() {
                return EntityMinecartCommandBlock.this.world;
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return EntityMinecartCommandBlock.this;
            }
            
            @Override
            public MinecraftServer getServer() {
                return EntityMinecartCommandBlock.this.world.getMinecraftServer();
            }
        };
    }
    
    public EntityMinecartCommandBlock(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
        this.commandBlockLogic = new CommandBlockBaseLogic() {
            @Override
            public void updateCommand() {
                EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.COMMAND, this.getCommand());
                EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.LAST_OUTPUT, this.getLastOutput());
            }
            
            @Override
            public int getCommandBlockType() {
                return 1;
            }
            
            @Override
            public void fillInInfo(final ByteBuf buf) {
                buf.writeInt(EntityMinecartCommandBlock.this.getEntityId());
            }
            
            @Override
            public BlockPos getPosition() {
                return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public Vec3d getPositionVector() {
                return new Vec3d(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
            }
            
            @Override
            public World getEntityWorld() {
                return EntityMinecartCommandBlock.this.world;
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return EntityMinecartCommandBlock.this;
            }
            
            @Override
            public MinecraftServer getServer() {
                return EntityMinecartCommandBlock.this.world.getMinecraftServer();
            }
        };
    }
    
    public static void registerFixesMinecartCommand(final DataFixer fixer) {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartCommandBlock.class);
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                if (TileEntity.getKey(TileEntityCommandBlock.class).equals(new ResourceLocation(compound.getString("id")))) {
                    compound.setString("id", "Control");
                    fixer.process(FixTypes.BLOCK_ENTITY, compound, versionIn);
                    compound.setString("id", "MinecartCommandBlock");
                }
                return compound;
            }
        });
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EntityMinecartCommandBlock.COMMAND, "");
        this.getDataManager().register((DataParameter<TextComponentString>)EntityMinecartCommandBlock.LAST_OUTPUT, new TextComponentString(""));
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.commandBlockLogic.readDataFromNBT(compound);
        this.getDataManager().set(EntityMinecartCommandBlock.COMMAND, this.getCommandBlockLogic().getCommand());
        this.getDataManager().set(EntityMinecartCommandBlock.LAST_OUTPUT, this.getCommandBlockLogic().getLastOutput());
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        this.commandBlockLogic.writeToNBT(compound);
    }
    
    @Override
    public Type getType() {
        return Type.COMMAND_BLOCK;
    }
    
    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.COMMAND_BLOCK.getDefaultState();
    }
    
    public CommandBlockBaseLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }
    
    @Override
    public void onActivatorRailPass(final int x, final int y, final int z, final boolean receivingPower) {
        if (receivingPower && this.ticksExisted - this.activatorRailCooldown >= 4) {
            this.getCommandBlockLogic().trigger(this.world);
            this.activatorRailCooldown = this.ticksExisted;
        }
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        this.commandBlockLogic.tryOpenEditCommandBlock(player);
        return false;
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (EntityMinecartCommandBlock.LAST_OUTPUT.equals(key)) {
            try {
                this.commandBlockLogic.setLastOutput(this.getDataManager().get(EntityMinecartCommandBlock.LAST_OUTPUT));
            }
            catch (Throwable t) {}
        }
        else if (EntityMinecartCommandBlock.COMMAND.equals(key)) {
            this.commandBlockLogic.setCommand(this.getDataManager().get(EntityMinecartCommandBlock.COMMAND));
        }
    }
    
    @Override
    public boolean ignoreItemEntityData() {
        return true;
    }
    
    static {
        COMMAND = EntityDataManager.createKey(EntityMinecartCommandBlock.class, DataSerializers.STRING);
        LAST_OUTPUT = EntityDataManager.createKey(EntityMinecartCommandBlock.class, DataSerializers.TEXT_COMPONENT);
    }
}
