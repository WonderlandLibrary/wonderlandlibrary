package me.aidanmees.trivia.client.commands;

import org.apache.commons.lang3.StringUtils;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.IChatComponent;

public class CommandFirework extends Command {
	String nbt = null;
	

	@Override
	public void run(String[] args) {
		  
		ItemStack itm = new ItemStack(Items.fireworks, 64);
		
		NBTTagCompound base = new NBTTagCompound();
	     NBTTagList list = new NBTTagList();
	     nbt = "{Fireworks:{Flight:0,Explosions:[{Type:1,Flicker:1,Trail:1,Colors:[16711680],FadeColors:[16744448]},{Type:1,Trail:1,Colors:[16768256],FadeColors:[3538691]},{Type:1,Flicker:1,Trail:1,Colors:[244991],FadeColors:[2303]}";
	     
	     String Add = ",{Type:1,Trail:1,Colors:[16768256],FadeColors:[3538691]},{Type:1,Flicker:1,Trail:1,Colors:[244991],FadeColors:[2303]},{Type:1,Flicker:1,Trail:1,Colors:[12255487],FadeColors:[16715215]},{Type:1,Flicker:1,Trail:1,Colors:[16713098],FadeColors:[16713843]}";
		for(int i = 1; i < 2000; i ++){
			nbt = nbt+ Add;
		}
		nbt = nbt + "]}}";
	     
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
		return ".firework";
	}

	@Override
	public String getSyntax() {
		return ".firework";
	}

	@Override
	public String getDesc() {
		return "Gives you a firework.";
	}
}
