package me.aidanmees.trivia.client.modules.Player;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockQuartz;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", Keyboard.KEY_NONE, Category.PLAYER, "Disables fall damage.");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		
		
		
		
		if( mc.thePlayer.fallDistance > 2f && !mc.thePlayer.isSneaking() && !ClientSettings.HypixelNofall) {
			
			sendPacket(new C03PacketPlayer(true));
			
			
		}
		if (mc.thePlayer.fallDistance > 2f && ClientSettings.HypixelNofall && mc.thePlayer.fallDistance < 20) {
			sendPacket(new C03PacketPlayer(true));
		}
		super.onUpdate();
	}
	@Override
	public ModSetting[] getModSettings() {

		
		CheckBtnSetting box1 = new CheckBtnSetting("Hypixel (20 blocks)", "HypixelNofall");
		
		return new ModSetting[] {box1};
	}
	
}
