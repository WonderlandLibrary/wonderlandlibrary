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
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class TileEntitySign
extends TileEntity {
    public final IChatComponent[] signText = new IChatComponent[]{new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("")};
    public int lineBeingEdited = -1;
    private boolean isEditable = true;
    private EntityPlayer player;
    private final CommandResultStats stats = new CommandResultStats();

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        for (int i = 0; i < 4; ++i) {
            String s = IChatComponent.Serializer.componentToJson((IChatComponent)this.signText[i]);
            compound.setString("Text" + (i + 1), s);
        }
        this.stats.writeStatsToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.isEditable = false;
        super.readFromNBT(compound);
        1 icommandsender = new /* Unavailable Anonymous Inner Class!! */;
        for (int i = 0; i < 4; ++i) {
            String s = compound.getString("Text" + (i + 1));
            try {
                IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent((String)s);
                try {
                    this.signText[i] = ChatComponentProcessor.processComponent((ICommandSender)icommandsender, (IChatComponent)ichatcomponent, (Entity)null);
                }
                catch (CommandException var7) {
                    this.signText[i] = ichatcomponent;
                }
                continue;
            }
            catch (JsonParseException var8) {
                this.signText[i] = new ChatComponentText(s);
            }
        }
        this.stats.readStatsFromNBT(compound);
    }

    public Packet getDescriptionPacket() {
        IChatComponent[] aichatcomponent = new IChatComponent[4];
        System.arraycopy((Object)this.signText, (int)0, (Object)aichatcomponent, (int)0, (int)4);
        return new S33PacketUpdateSign(this.worldObj, this.pos, aichatcomponent);
    }

    public boolean func_183000_F() {
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

    public boolean executeCommand(EntityPlayer playerIn) {
        2 icommandsender = new /* Unavailable Anonymous Inner Class!! */;
        for (int i = 0; i < this.signText.length; ++i) {
            ClickEvent clickevent;
            ChatStyle chatstyle;
            ChatStyle chatStyle = chatstyle = this.signText[i] == null ? null : this.signText[i].getChatStyle();
            if (chatstyle == null || chatstyle.getChatClickEvent() == null || (clickevent = chatstyle.getChatClickEvent()).getAction() != ClickEvent.Action.RUN_COMMAND) continue;
            MinecraftServer.getServer().getCommandManager().executeCommand((ICommandSender)icommandsender, clickevent.getValue());
        }
        return true;
    }

    public CommandResultStats getStats() {
        return this.stats;
    }

    static /* synthetic */ CommandResultStats access$000(TileEntitySign x0) {
        return x0.stats;
    }
}
