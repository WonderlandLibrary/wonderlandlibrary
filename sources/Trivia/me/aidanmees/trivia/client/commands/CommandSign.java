package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.IChatComponent;

public class CommandSign extends Command {
	
	
	  public static trivia instance = null;
	 
	 String nbt;

	@Override
	public void run(String[] args) {
		  
		        ItemStack itm = new ItemStack(Items.sign);
		        NBTTagCompound base = new NBTTagCompound();
			     NBTTagList list = new NBTTagList();
			     nbt = "{BlockEntityTag:{Text1:\"{\"text\":\"Minecraft Tools\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"Hai\"}}\"},display:{Name:Helo}}";
			     
			     
				
				try {
					itm.setTagCompound(JsonToNBT.getTagFromJson(nbt));
				} catch (NBTException e) {
					
					e.printStackTrace();
				}

		        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itm));
		        trivia.chatMessage(" Check your inventory!");
		      }

		
	
	

	@Override
	public String getActivator() {
		return ".sign";
	}

	@Override
	public String getSyntax() {
		return ".sign";
	}

	@Override
	public String getDesc() {
		return "Gives you a Exploited sign.";
	}
}
