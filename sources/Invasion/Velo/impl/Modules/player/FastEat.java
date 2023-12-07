package Velo.impl.Modules.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.NumberSetting;

public class FastEat extends Module {
	
	public static NumberSetting delay = new NumberSetting("Delay", 1, 0, 21, 1);
	public static NumberSetting repeatvalue = new NumberSetting("Repeat", 5, 1, 50, 1);
	
	public FastEat() {
		super("FastUse", "FastUse", Keyboard.KEY_NONE, Category.PLAYER);
		this.loadSettings(delay, repeatvalue);
	}
	
	public void onUpdate(EventUpdate event) {
		if(mc.thePlayer != null && mc.theWorld != null) {
		EntityPlayerSP player = mc.thePlayer;
		GameSettings gameSettings = mc.gameSettings;
		
		
		try {
			
			Item item = player.getHeldItem().getItem();
		     if (item != null && (item instanceof ItemFood || item instanceof ItemPotion) && gameSettings.keyBindDrop.isKeyDown() && (player.onGround) && player.ticksExisted % 4 == 0) {
		            for (int i = 0; i < 2; ++i) {
		                player.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(player.posX, player.posY + 1.0E-9, player.posZ, player.rotationYaw, player.rotationPitch, true));
		            }
		}
		} catch(NullPointerException e) {
			
		}
		}
	}
}
