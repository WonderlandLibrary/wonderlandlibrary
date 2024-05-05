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

public class CommandCleanRam extends Command {
	
	

	@Override
	public void run(String[] args) {
	
		  trivia.chatMessage("Please wait...");
	        Runtime r2 = Runtime.getRuntime();
	        long Lused2 = (r2.totalMemory() - r2.freeMemory()) / 1024 / 1024;
	        System.gc();
	        long Lused3 = (r2.totalMemory() - r2.freeMemory()) / 1024 / 1024;
	        trivia.chatMessage("Cleared " + Long.toString(Lused2 - Lused3) + "M RAM");
	        trivia.chatMessage("Done!");
	}
	
	

	@Override
	public String getActivator() {
		return ".cleanram";
	}

	@Override
	public String getSyntax() {
		return ".cleanram";
	}

	@Override
	public String getDesc() {
		return "Cleans ur ram, Duh!";
	}
}
