package net.minecraft.tileentity;

import com.google.gson.JsonParseException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign.1;
import net.minecraft.tileentity.TileEntitySign.2;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class TileEntitySign extends TileEntity {
   public final IChatComponent[] signText = new IChatComponent[]{new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("")};
   public int lineBeingEdited = -1;
   private boolean isEditable = true;
   private EntityPlayer player;
   private final CommandResultStats stats = new CommandResultStats();

   // $FF: synthetic method
   static CommandResultStats access$000(TileEntitySign x0) {
      return x0.stats;
   }

   public void setEditable(boolean isEditableIn) {
      this.isEditable = isEditableIn;
      if(!isEditableIn) {
         this.player = null;
      }

   }

   public boolean func_183000_F() {
      return true;
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);

      for(int i = 0; i < 4; ++i) {
         String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
         compound.setString("Text" + (i + 1), s);
      }

      this.stats.writeStatsToNBT(compound);
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   public void readFromNBT(NBTTagCompound compound) {
      this.isEditable = false;
      super.readFromNBT(compound);
      ICommandSender icommandsender = new 1(this);

      for(int i = 0; i < 4; ++i) {
         String s = compound.getString("Text" + (i + 1));

         try {
            IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);

            try {
               this.signText[i] = ChatComponentProcessor.processComponent(icommandsender, ichatcomponent, (Entity)null);
            } catch (CommandException var7) {
               this.signText[i] = ichatcomponent;
            }
         } catch (JsonParseException var8) {
            this.signText[i] = new ChatComponentText(s);
         }
      }

      this.stats.readStatsFromNBT(compound);
   }

   public boolean getIsEditable() {
      return this.isEditable;
   }

   public void setPlayer(EntityPlayer playerIn) {
      this.player = playerIn;
   }

   public CommandResultStats getStats() {
      return this.stats;
   }

   public Packet getDescriptionPacket() {
      IChatComponent[] aichatcomponent = new IChatComponent[4];
      System.arraycopy(this.signText, 0, aichatcomponent, 0, 4);
      return new S33PacketUpdateSign(this.worldObj, this.pos, aichatcomponent);
   }

   public boolean executeCommand(EntityPlayer playerIn) {
      ICommandSender icommandsender = new 2(this, playerIn);

      for(int i = 0; i < this.signText.length; ++i) {
         ChatStyle chatstyle = this.signText[i] == null?null:this.signText[i].getChatStyle();
         if(chatstyle != null && chatstyle.getChatClickEvent() != null) {
            ClickEvent clickevent = chatstyle.getChatClickEvent();
            if(clickevent.getAction() == Action.RUN_COMMAND) {
               MinecraftServer.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue());
            }
         }
      }

      return true;
   }
}
