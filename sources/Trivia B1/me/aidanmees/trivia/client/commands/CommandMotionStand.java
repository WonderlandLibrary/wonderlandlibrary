package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.tools.ItemStackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Rotations;

public class CommandMotionStand extends Command {
	String nbt = null;

	@Override
	public void run(String[] args) {

		ItemStack stack = new ItemStack(Items.armor_stand);
		NBTTagCompound effect = new NBTTagCompound();
		NBTTagList effects = new NBTTagList();
		NBTTagList motion = new NBTTagList();
		NBTTagList list = new NBTTagList();
		NBTTagCompound Cmd = new NBTTagCompound();
		NBTTagList equipment = new NBTTagList();
		ItemStack Hand = new ItemStack(Items.diamond_sword);
		ItemStack Chest = new ItemStack(Items.diamond_chestplate);
		ItemStack Legs = new ItemStack(Items.diamond_boots);
		ItemStack Feets = new ItemStack(Items.diamond_leggings);
		ItemStack Head = new ItemStack(Items.diamond_helmet);
		ItemStack CommandBlock = ItemStackUtil.stringtostack("command_block 1 0").setStackDisplayLore("");
		

        Enchantment[] arrenchantment = Enchantment.enchantmentsBookList;
        int var5 = arrenchantment.length;
        int var4 = 0;
        while (var4 < var5) {
            Enchantment e2 = arrenchantment[var4];
            Hand.addEnchantment(e2, 127);
	  		Chest.addEnchantment(e2, 127);
	  		Feets.addEnchantment(e2, 127);
	  		Legs.addEnchantment(e2, 127);
	  		Head.addEnchantment(e2, 127);
            ++var4;
        }

	         
	        
		
		
		
		motion.appendTag(new NBTTagDouble(Double.parseDouble(args[1])));
		motion.appendTag(new NBTTagDouble(Double.parseDouble(args[2])));
		motion.appendTag(new NBTTagDouble(Double.parseDouble(args[3])));

		equipment.appendTag(Hand.writeToNBT(new NBTTagCompound()));
		equipment.appendTag(Legs.writeToNBT(new NBTTagCompound()));
		equipment.appendTag(Feets.writeToNBT(new NBTTagCompound()));
		equipment.appendTag(Chest.writeToNBT(new NBTTagCompound()));
		
		equipment.appendTag(CommandBlock.writeToNBT(new NBTTagCompound()));

		
		String Text = "Herobrine";
		effect.setBoolean("OnGround", false);
		
		effect.setBoolean("Invisible", false);
		effect.setBoolean("NoGravity", false);
		effect.setFloat("FallDistance", 255);
		effect.setBoolean("Invulnerable", true);
	
		
		effect.setBoolean("NoBasePlate", true);
		effect.setBoolean("ShowArms", true);
	

		effect.setTag("Equipment", equipment);
		effect.setTag("Motion", motion);
		stack.setTagInfo("EntityTag", effect);
		stack.setStackDisplayName("Place me to create a MotionStand!");
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
	}

	@Override
	public String getActivator() {
		return ".motionstand";
	}

	@Override
	public String getSyntax() {
		return ".motionstand, .motionstand <MotionX> <MotionY> <MotionZ>";
	}

	@Override
	public String getDesc() {
		return "Gives you a Motion-Armorstand.";
	}
}
