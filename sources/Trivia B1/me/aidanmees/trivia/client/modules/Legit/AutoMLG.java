package me.aidanmees.trivia.client.modules.Legit;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Time;
import me.aidanmees.trivia.client.tools.TimeHelper;
import me.aidanmees.trivia.client.tools.Wrapper;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoMLG extends Module {
	Time t = new Time();
	TimeHelper time = new TimeHelper();
	  private int oldSlot;

	public AutoMLG() {
		super("AutoMLG", Keyboard.KEY_NONE, Category.PLAYER, "Puts water underneath you when u fall.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	
		

	@Override
	public void onUpdate(UpdateEvent e) {
		
		
		
		 boolean check = Minecraft.thePlayer.isBurning();
		 if ((check) && 
		      (Wrapper.findHotbarItem(326, 1) != -1))
		    {
		     
		      this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
		      Minecraft.thePlayer.rotationPitch = 90.0F;
		      Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Wrapper.findHotbarItem(326, 1)));
		      mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
		      Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.oldSlot));
		    }
		 if ((Minecraft.thePlayer.fallDistance > 4F) && 
			      (Wrapper.findHotbarItem(326, 1) != -1) && ((Minecraft.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockAir)))
			    {
			 Minecraft.thePlayer.setSpeed(0);
			      mc.gameSettings.keyBindUseItem.pressed = false;
			      this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
			      e.pitch = 90.0F;
			      
			      Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Wrapper.findHotbarItem(326, 1)));
			      mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
			      Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.oldSlot));
			    }
			  
			

		super.onUpdate();
}
	

}