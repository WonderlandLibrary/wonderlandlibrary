/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 */
package net.minecraft.tileentity;

import com.google.gson.JsonParseException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileEntitySign
extends TileEntity {
    private final CommandResultStats stats;
    public final IChatComponent[] signText = new IChatComponent[]{new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("")};
    private boolean isEditable = true;
    public int lineBeingEdited = -1;
    private EntityPlayer player;

    public void setEditable(boolean bl) {
        this.isEditable = bl;
        if (!bl) {
            this.player = null;
        }
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public TileEntitySign() {
        this.stats = new CommandResultStats();
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        int n = 0;
        while (n < 4) {
            String string = IChatComponent.Serializer.componentToJson(this.signText[n]);
            nBTTagCompound.setString("Text" + (n + 1), string);
            ++n;
        }
        this.stats.writeStatsToNBT(nBTTagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        this.isEditable = false;
        super.readFromNBT(nBTTagCompound);
        ICommandSender iCommandSender = new ICommandSender(){

            @Override
            public IChatComponent getDisplayName() {
                return new ChatComponentText(this.getName());
            }

            @Override
            public void addChatMessage(IChatComponent iChatComponent) {
            }

            @Override
            public boolean canCommandSenderUseCommand(int n, String string) {
                return true;
            }

            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }

            @Override
            public boolean sendCommandFeedback() {
                return false;
            }

            @Override
            public void setCommandStat(CommandResultStats.Type type, int n) {
            }

            @Override
            public String getName() {
                return "Sign";
            }

            @Override
            public Entity getCommandSenderEntity() {
                return null;
            }

            @Override
            public World getEntityWorld() {
                return TileEntitySign.this.worldObj;
            }

            @Override
            public Vec3 getPositionVector() {
                return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5, (double)TileEntitySign.this.pos.getY() + 0.5, (double)TileEntitySign.this.pos.getZ() + 0.5);
            }
        };
        int n = 0;
        while (n < 4) {
            String string = nBTTagCompound.getString("Text" + (n + 1));
            try {
                IChatComponent iChatComponent = IChatComponent.Serializer.jsonToComponent(string);
                try {
                    this.signText[n] = ChatComponentProcessor.processComponent(iCommandSender, iChatComponent, null);
                }
                catch (CommandException commandException) {
                    this.signText[n] = iChatComponent;
                }
            }
            catch (JsonParseException jsonParseException) {
                this.signText[n] = new ChatComponentText(string);
            }
            ++n;
        }
        this.stats.readStatsFromNBT(nBTTagCompound);
    }

    public CommandResultStats getStats() {
        return this.stats;
    }

    public boolean getIsEditable() {
        return this.isEditable;
    }

    @Override
    public boolean func_183000_F() {
        return true;
    }

    public void setPlayer(EntityPlayer entityPlayer) {
        this.player = entityPlayer;
    }

    @Override
    public Packet getDescriptionPacket() {
        IChatComponent[] iChatComponentArray = new IChatComponent[4];
        System.arraycopy(this.signText, 0, iChatComponentArray, 0, 4);
        return new S33PacketUpdateSign(this.worldObj, this.pos, iChatComponentArray);
    }

    public boolean executeCommand(final EntityPlayer entityPlayer) {
        ICommandSender iCommandSender = new ICommandSender(){

            @Override
            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }

            @Override
            public IChatComponent getDisplayName() {
                return entityPlayer.getDisplayName();
            }

            @Override
            public World getEntityWorld() {
                return entityPlayer.getEntityWorld();
            }

            @Override
            public boolean sendCommandFeedback() {
                return false;
            }

            @Override
            public Vec3 getPositionVector() {
                return new Vec3((double)TileEntitySign.this.pos.getX() + 0.5, (double)TileEntitySign.this.pos.getY() + 0.5, (double)TileEntitySign.this.pos.getZ() + 0.5);
            }

            @Override
            public String getName() {
                return entityPlayer.getName();
            }

            @Override
            public boolean canCommandSenderUseCommand(int n, String string) {
                return n <= 2;
            }

            @Override
            public Entity getCommandSenderEntity() {
                return entityPlayer;
            }

            @Override
            public void addChatMessage(IChatComponent iChatComponent) {
            }

            @Override
            public void setCommandStat(CommandResultStats.Type type, int n) {
                TileEntitySign.this.stats.func_179672_a(this, type, n);
            }
        };
        int n = 0;
        while (n < this.signText.length) {
            ClickEvent clickEvent;
            ChatStyle chatStyle;
            ChatStyle chatStyle2 = chatStyle = this.signText[n] == null ? null : this.signText[n].getChatStyle();
            if (chatStyle != null && chatStyle.getChatClickEvent() != null && (clickEvent = chatStyle.getChatClickEvent()).getAction() == ClickEvent.Action.RUN_COMMAND) {
                MinecraftServer.getServer().getCommandManager().executeCommand(iCommandSender, clickEvent.getValue());
            }
            ++n;
        }
        return true;
    }
}

