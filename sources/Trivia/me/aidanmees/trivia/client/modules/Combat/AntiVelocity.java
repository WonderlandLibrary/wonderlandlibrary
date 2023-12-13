package me.aidanmees.trivia.client.modules.Combat;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class AntiVelocity extends Module {
	
	
	public AntiVelocity() {
		super("AntiVelocity", Keyboard.KEY_NONE, Category.COMBAT,
				"Modifies your knockback.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}
	
	@Override
	public ModSetting[] getModSettings() {

		SliderSetting<Number> slider1 = new SliderSetting<Number>("Horizontal Knockback", ClientSettings.KBHorizontal, 0, 100, 0.0, ValueFormat.PERCENT);
		SliderSetting<Number> slider2 = new SliderSetting<Number>("Vertical Knockback", ClientSettings.KBVertical, 0, 100, 0.0, ValueFormat.PERCENT);
		
		return new ModSetting[] { slider1, slider2};
	}

	
	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		if (packetIn instanceof S12PacketEntityVelocity) {

			  S12PacketEntityVelocity packet = (S12PacketEntityVelocity)packetIn;
		      if (packet.getEntityID() == mc.thePlayer.getEntityId())
		      {
		    	  
		        int vertical = ClientSettings.KBHorizontal;
		        int horizontal = ClientSettings.KBHorizontal;
		        trivia.chatMessage("Vertical"+vertical + "horizontal" + horizontal);
		        if ((vertical != 0) || (horizontal != 0))
		        {
		          packet.motionZ = (horizontal * packet.getMotionX() / 100);
		          packet.motionY = (vertical * packet.getMotionY() / 100);
		          packet.motionZ = (horizontal * packet.getMotionZ() / 100);
		        }
		        else
		        {
		        	packetIn.cancel();
		        }
		      }
		}

			super.onPacketRecieved(packetIn);
		
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}
	
	
	

}
