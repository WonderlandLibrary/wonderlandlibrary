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

public class CommandCrashStick extends Command {
	String nbt = null;
	

	@Override
	public void run(String[] args) {
		  
		ItemStack itm = new ItemStack(Items.iron_sword, 64);
		NBTTagCompound base = new NBTTagCompound();
	     NBTTagList list = new NBTTagList();
	     nbt = "{AttributeModifiers:[{AttributeName:generic.movementSpeed,Name:generic.movementSpeed, Amount:2147483647,Operation:0, UUIDMost:246216, UUIDLeast:24636}], display:{Name:Hold Me}, Unbreakable:1,HideFlags:63}";
	     
	     
		
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
		return ".crashsword";
	}

	@Override
	public String getSyntax() {
		return ".crashsword";
	}

	@Override
	public String getDesc() {
		return "Gives you a crashsword.";
	}
}
