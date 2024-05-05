package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.WaitTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandDeathStick extends Command {
	String nbt = null;
	

	@Override
	public void run(String[] args) {
		  
		ItemStack itm = new ItemStack(Items.iron_sword, 64);
		
		NBTTagCompound base = new NBTTagCompound();
	     NBTTagList list = new NBTTagList();
	 	NBTTagList nbttaglist = new NBTTagList();
		nbttaglist.appendTag(new NBTTagString("+1000% Speed"));
	
		base.setTag("Lore", nbttaglist);
		itm.setTagInfo("display", base);
	     nbt = "{AttributeModifiers:[{AttributeName:generic.maxHealth,Name:generic.maxHealth, Amount:-20,Operation:0, UUIDMost:246216, UUIDLeast:24636}], display:{Name:Hold Me}, Unbreakable:1,HideFlags:63}";
	     
	     
		
		try {
			
			itm.setTagCompound(JsonToNBT.getTagFromJson(nbt));
		} catch (NBTException e) {
			
			e.printStackTrace();
		}

		       
		
		
		        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itm));
		   
		        WaitTimer timer = new WaitTimer();
		        if (timer.hasTimeElapsed(1000, true)){
		        	 mc.thePlayer.dropOneItem(true);
		        }
	}
	
	

	@Override
	public String getActivator() {
		return ".deathsword";
	}

	@Override
	public String getSyntax() {
		return ".deathsword";
	}

	@Override
	public String getDesc() {
		return "Gives you a Deathsword.";
	}
}
