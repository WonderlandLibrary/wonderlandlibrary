package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IChatComponent;

public class CommandKillerpotion extends Command {
	
	
	 

	@Override
	public void run(String[] args) {
		 ItemStack stackkp = new ItemStack(Items.potionitem);
		    stackkp.stackSize = 64;
		   
		    stackkp.setItemDamage(Integer.MAX_VALUE);
		    NBTTagList effectskp = new NBTTagList();
		    NBTTagCompound effectkp = new NBTTagCompound();
		    effectkp.setInteger("Amplifier", 124);
		    effectkp.setInteger("Duration", Integer.MAX_VALUE);
		    effectkp.setInteger("Id", 6);
		    effectskp.appendTag(effectkp);
		    stackkp.setTagInfo("CustomPotionEffects", effectskp);
		    stackkp.setStackDisplayName("Potions r cool!");
		    
		    mc.thePlayer.sendQueue
		      .addToSendQueue(new C10PacketCreativeInventoryAction(36, stackkp));
	
	}

	
	

	@Override
	public String getActivator() {
		return ".killerpotion";
	}

	@Override
	public String getSyntax() {
		return ".killerpotion";
	}

	@Override
	public String getDesc() {
		return "Gives you a Killerpotion.";
	}
}
