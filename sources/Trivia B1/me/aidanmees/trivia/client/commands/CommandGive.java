package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.ItemStackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.IChatComponent;

public class CommandGive extends Command {
	
	
	
	 

	@Override
	public void run(String[] args) {
		  
		    if (args[0].equalsIgnoreCase(".give")) {
		      try
		      {
		        String command = "";
		        for (int i = 1; i < args.length ; i++) {
		          command = command + args[i] + " ";
		        }
String meme = "bow 1 0 {display:{Name:\"God Bow\",Lore:[\"The bow that no one knew...\",\"Don't give it back to the Gods..\"]},AttributeModifiers:[{AttributeName:\"generic.maxHealth\",Name:\"generic.maxHealth\",Amount:100,Operation:0,UUIDMost:78712,UUIDLeast:130183},{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1000,Operation:0,UUIDMost:86575,UUIDLeast:944034}],ench:[{id:16,lvl:50},{id:34,lvl:1000},{id:48,lvl:10000},{id:49,lvl:1000},{id:50,lvl:1000},{id:51,lvl:1000}]}";
		        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, ItemStackUtil.stringtostack(command)));
		        trivia.chatMessage(meme.length());
		      }
		      catch (Exception e)
		      {
		    	  trivia.chatMessage(" Usage: .give <item 1 0 ITEMMETA>");
		      }
		    }
		  }
		
	
	

	@Override
	public String getActivator() {
		return ".give";
	}

	@Override
	public String getSyntax() {
		return ".give";
	}

	@Override
	public String getDesc() {
		return "Gives you a item of choice.";
	}
}
