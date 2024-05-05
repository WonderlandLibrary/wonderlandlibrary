package me.aidanmees.trivia.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandHologram extends Command {
	String nbt = null;
	

	@Override
	public void run(String[] args) {
		String Text = "";
		for (int i = 1; i < args.length; i++){
		
			Text = Text + " " + args[i];  
			
			
		}
		
		
		ItemStack stack = new ItemStack(Items.armor_stand);
	    NBTTagCompound effect = new NBTTagCompound();
	    NBTTagList effects = new NBTTagList();
	    
	    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posX));
	    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posY));
	    Minecraft.getMinecraft();effects.appendTag(new NBTTagDouble(mc.thePlayer.posZ));
	    effect.setBoolean("Invisible", true);
	    effect.setBoolean("NoGravity", true);
	    effect.setBoolean("CustomNameVisible", true);
	    
	    
	    effect.setString("CustomName", Text.replaceAll("&", "§"));
	    effect.setTag("Pos", effects);
	    stack.setTagInfo("EntityTag", effect);
	    stack.setStackDisplayName("Place me to create the hologram!");

	    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
	  }
	

	@Override
	public String getActivator() {
		return ".hologram";
	}

	@Override
	public String getSyntax() {
		return ".hologram, .hologram <text>";
	}

	@Override
	public String getDesc() {
		return "Gives you a hologram.";
	}
}
