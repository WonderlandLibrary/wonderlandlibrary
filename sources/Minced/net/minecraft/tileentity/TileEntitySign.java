// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

public class TileEntitySign extends TileEntity
{
    public final ITextComponent[] signText;
    public int lineBeingEdited;
    private boolean isEditable;
    private EntityPlayer player;
    private final CommandResultStats stats;
    
    public TileEntitySign() {
        this.signText = new ITextComponent[] { new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("") };
        this.lineBeingEdited = -1;
        this.isEditable = true;
        this.stats = new CommandResultStats();
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        for (int i = 0; i < 4; ++i) {
            final String s = ITextComponent.Serializer.componentToJson(this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }
        this.stats.writeStatsToNBT(compound);
        return compound;
    }
    
    @Override
    protected void setWorldCreate(final World worldIn) {
        this.setWorld(worldIn);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        this.isEditable = false;
        super.readFromNBT(compound);
        final ICommandSender icommandsender = new ICommandSender() {
            @Override
            public String getName() {
                return "Sign";
            }
            
            @Override
            public boolean canUseCommand(final int permLevel, final String commandName) {
                return true;
            }
            
            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }
            
            @Override
            public Vec3d getPositionVector() {
                return new Vec3d(TileEntitySign.this.pos.getX() + 0.5, TileEntitySign.this.pos.getY() + 0.5, TileEntitySign.this.pos.getZ() + 0.5);
            }
            
            @Override
            public World getEntityWorld() {
                return TileEntitySign.this.world;
            }
            
            @Override
            public MinecraftServer getServer() {
                return TileEntitySign.this.world.getMinecraftServer();
            }
        };
        for (int i = 0; i < 4; ++i) {
            final String s = compound.getString("Text" + (i + 1));
            final ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
            try {
                this.signText[i] = TextComponentUtils.processComponent(icommandsender, itextcomponent, null);
            }
            catch (CommandException var7) {
                this.signText[i] = itextcomponent;
            }
        }
        this.stats.readStatsFromNBT(compound);
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 9, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }
    
    public boolean getIsEditable() {
        return this.isEditable;
    }
    
    public void setEditable(final boolean isEditableIn) {
        if (!(this.isEditable = isEditableIn)) {
            this.player = null;
        }
    }
    
    public void setPlayer(final EntityPlayer playerIn) {
        this.player = playerIn;
    }
    
    public EntityPlayer getPlayer() {
        return this.player;
    }
    
    public boolean executeCommand(final EntityPlayer playerIn) {
        final ICommandSender icommandsender = new ICommandSender() {
            @Override
            public String getName() {
                return playerIn.getName();
            }
            
            @Override
            public ITextComponent getDisplayName() {
                return playerIn.getDisplayName();
            }
            
            @Override
            public void sendMessage(final ITextComponent component) {
            }
            
            @Override
            public boolean canUseCommand(final int permLevel, final String commandName) {
                return permLevel <= 2;
            }
            
            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }
            
            @Override
            public Vec3d getPositionVector() {
                return new Vec3d(TileEntitySign.this.pos.getX() + 0.5, TileEntitySign.this.pos.getY() + 0.5, TileEntitySign.this.pos.getZ() + 0.5);
            }
            
            @Override
            public World getEntityWorld() {
                return playerIn.getEntityWorld();
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return playerIn;
            }
            
            @Override
            public boolean sendCommandFeedback() {
                return false;
            }
            
            @Override
            public void setCommandStat(final CommandResultStats.Type type, final int amount) {
                if (TileEntitySign.this.world != null && !TileEntitySign.this.world.isRemote) {
                    TileEntitySign.this.stats.setCommandStatForSender(TileEntitySign.this.world.getMinecraftServer(), this, type, amount);
                }
            }
            
            @Override
            public MinecraftServer getServer() {
                return playerIn.getServer();
            }
        };
        for (final ITextComponent itextcomponent : this.signText) {
            final Style style = (itextcomponent == null) ? null : itextcomponent.getStyle();
            if (style != null && style.getClickEvent() != null) {
                final ClickEvent clickevent = style.getClickEvent();
                if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    playerIn.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue());
                }
            }
        }
        return true;
    }
    
    public CommandResultStats getStats() {
        return this.stats;
    }
}
