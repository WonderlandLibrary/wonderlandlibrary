package me.aidanmees.trivia.client.modules.Misc;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.src.MathUtils;

public class HeadChanger extends Module {


	  private Long last = null;
	
	
	public HeadChanger() {
		super("HeadChanger", Keyboard.KEY_NONE, Category.MISC,
				"Puts random items in ur head slot.");
	}

	

	@Override
	public void onUpdate() {
		  if (mc.thePlayer.capabilities.isCreativeMode)
		    {
			 
		      new Item();
		      Item item = Item.getItemById(MathUtils.customRandInt(1, Item.itemRegistry.getKeys().size()));
		      ItemStack itemStack = new ItemStack(item, MathUtils.customRandInt(1, 64));
		      
		      itemStack.setStackDisplayName(generateFormattedString(1024));
		      if (item != null) {
		        mc.thePlayer.sendQueue.addToSendQueue(
		          new C10PacketCreativeInventoryAction(5, itemStack));
		       
		      
		  }
		    }
	}
		  public static Long getSysTime()
		  {
		    return Long.valueOf(System.nanoTime() / 1000000L);
		  }
		  
		  public static String generateFormattedString(int length)
		  {
		    String[] colors = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", 
		      "m", "n", "o", "r" };
		    
		    String[] chars = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", 
		      "s", "t", "u", "w", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", 
		      "P", "Q", "R", "S", "T", "U", "W", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		    
		    String string = "";
		    for (int l = 0; l < length; l++) {
		      string = 
		        string + "" + colors[MathUtils.customRandInt(0, colors.length - 1)] + chars[MathUtils.customRandInt(0, colors.length - 1)];
		    }
		    return string;
		  }
		}
