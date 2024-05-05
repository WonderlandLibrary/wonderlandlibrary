package me.aidanmees.trivia.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandArmorStandRain extends Command {
	String nbt = null;
	

	@Override
	public void run(String[] args) {
	
		
		 ItemStack stack = new ItemStack(Items.armor_stand);
	    NBTTagCompound effect = new NBTTagCompound();
	    NBTTagList effects = new NBTTagList();
	    NBTTagList list = new NBTTagList();
	     

	    
	   
	    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posX));
	    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posY + 200));
	    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posZ));
	    effect.setBoolean("Invisible", false);
	    effect.setBoolean("NoGravity", false);
	    effect.setBoolean("CustomNameVisible", false);
	    
	    
	   
	    effect.setTag("Pos", effects);
	    stack.setTagInfo("EntityTag", effect);
	    stack.setStackDisplayName("Place me to create the hologram!");
	    
	    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
	  }
	

	@Override
	public String getActivator() {
		return ".armorstandrain";
	}

	@Override
	public String getSyntax() {
		return ".armorstandrain";
	}

	@Override
	public String getDesc() {
		return "Gives you a armorstand.";
	}
}
