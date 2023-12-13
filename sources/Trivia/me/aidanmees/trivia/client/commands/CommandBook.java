package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.IChatComponent;

public class CommandBook extends Command {
	
	
	  public static trivia instance = null;
	  private final String json = "{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"%COMMAND%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"%HOVERTEXT%\"}},\"text\":\"%TEXT%\"}";
	  
	 

	@Override
	public void run(String[] args) {
		  if (args[0].equalsIgnoreCase(".")) {
			  trivia.chatMessage(" Usage: .book <command|message>");
		    }
		    if (args[0].equalsIgnoreCase(".book")) {
		      try
		      {
		        String command = "";
		        for (int i = 1; i < args.length; i++) {
		          command = command + args[i] + " ";
		        }
		        ItemStack WritItm = new ItemStack(Items.written_book);
		        NBTTagCompound WritBase = new NBTTagCompound();
		        NBTTagList WritList = new NBTTagList();
		        String spaces = "";
		        for (int i = 0; i < 500; i++) {
		          spaces = spaces + " ";
		        }
		        WritList.appendTag(new NBTTagString("{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"%COMMAND%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"%HOVERTEXT%\"}},\"text\":\"%TEXT%\"}".replace("%COMMAND%", command).replace("%HOVERTEXT%", "§F§L[§4§lClick§f§l] Click here!").replace("%TEXT%", "§F§L§8[§4§lClick§f§l§8] Click here!" +spaces)));
		        
		        
		        WritBase.setTag("pages", WritList);
		        WritBase.setString("author", "§7§l[§4§lAdmin§7§l]");
		        WritBase.setByte("resolved", (byte)1);
		        WritBase.setString("title", "§8§kiii§f§7Minecraft§8§kiii");
		        WritItm.setTagCompound(WritBase);
		        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, WritItm));
		        trivia.chatMessage(" Check your inventory!");
		      }
		      catch (Exception e)
		      {
		    	  trivia.chatMessage(" Usage: .book <command|message>");
		      }
		    }
		  }
		
	
	

	@Override
	public String getActivator() {
		return ".book";
	}

	@Override
	public String getSyntax() {
		return ".book";
	}

	@Override
	public String getDesc() {
		return "Gives you a CommandBook.";
	}
}
