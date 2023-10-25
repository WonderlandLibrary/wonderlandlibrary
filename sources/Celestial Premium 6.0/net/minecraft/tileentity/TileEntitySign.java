/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class TileEntitySign
extends TileEntity {
    public final ITextComponent[] signText = new ITextComponent[]{new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("")};
    public int lineBeingEdited = -1;
    private boolean isEditable = true;
    private EntityPlayer player;
    private final CommandResultStats stats = new CommandResultStats();

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        for (int i = 0; i < 4; ++i) {
            String s = ITextComponent.Serializer.componentToJson(this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }
        this.stats.writeStatsToNBT(compound);
        return compound;
    }

    @Override
    protected void setWorldCreate(World worldIn) {
        this.setWorldObj(worldIn);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.isEditable = false;
        super.readFromNBT(compound);
        ICommandSender icommandsender = new ICommandSender(){

            @Override
            public String getName() {
                return "Sign";
            }

            @Override
            public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
                return true;
            }

            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }

            @Override
            public Vec3d getPositionVector() {
                return new Vec3d((double)TileEntitySign.this.pos.getX() + 0.5, (double)TileEntitySign.this.pos.getY() + 0.5, (double)TileEntitySign.this.pos.getZ() + 0.5);
            }

            @Override
            public World getEntityWorld() {
                return TileEntitySign.this.world;
            }

            @Override
            public MinecraftServer getServer() {
                return TileEntitySign.this.world.getInstanceServer();
            }
        };
        for (int i = 0; i < 4; ++i) {
            String s = compound.getString("Text" + (i + 1));
            ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
            try {
                this.signText[i] = TextComponentUtils.processComponent(icommandsender, itextcomponent, null);
                continue;
            }
            catch (CommandException var7) {
                this.signText[i] = itextcomponent;
            }
        }
        this.stats.readStatsFromNBT(compound);
    }

    @Override
    @Nullable
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

    public void setEditable(boolean isEditableIn) {
        this.isEditable = isEditableIn;
        if (!isEditableIn) {
            this.player = null;
        }
    }

    public void setPlayer(EntityPlayer playerIn) {
        this.player = playerIn;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public boolean executeCommand(final EntityPlayer playerIn) {
        ICommandSender icommandsender = new ICommandSender(){

            @Override
            public String getName() {
                return playerIn.getName();
            }

            @Override
            public ITextComponent getDisplayName() {
                return playerIn.getDisplayName();
            }

            @Override
            public void addChatMessage(ITextComponent component) {
            }

            @Override
            public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
                return permLevel <= 2;
            }

            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }

            @Override
            public Vec3d getPositionVector() {
                return new Vec3d((double)TileEntitySign.this.pos.getX() + 0.5, (double)TileEntitySign.this.pos.getY() + 0.5, (double)TileEntitySign.this.pos.getZ() + 0.5);
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
            public void setCommandStat(CommandResultStats.Type type, int amount) {
                if (TileEntitySign.this.world != null && !TileEntitySign.this.world.isRemote) {
                    TileEntitySign.this.stats.setCommandStatForSender(TileEntitySign.this.world.getInstanceServer(), this, type, amount);
                }
            }

            @Override
            public MinecraftServer getServer() {
                return playerIn.getServer();
            }
        };
        for (ITextComponent itextcomponent : this.signText) {
            ClickEvent clickevent;
            Style style;
            Style style2 = style = itextcomponent == null ? null : itextcomponent.getStyle();
            if (style == null || style.getClickEvent() == null || (clickevent = style.getClickEvent()).getAction() != ClickEvent.Action.RUN_COMMAND) continue;
            playerIn.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue());
        }
        return true;
    }

    public CommandResultStats getStats() {
        return this.stats;
    }
}

