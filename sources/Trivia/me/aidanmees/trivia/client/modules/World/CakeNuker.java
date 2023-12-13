package me.aidanmees.trivia.client.modules.World;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.RenderTools;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class CakeNuker extends Module {

	int timer;

	RenderManager renderManager = mc.getRenderManager();

	@Override
	public ModSetting[] getModSettings() {
		// nukerSpeedSlider

//		Slider nukerSpeedSlider = new BasicSlider("Nuker Radius", ClientSettings.Nukerradius, 1.0, 6.3, 0.0,
//				ValueDisplay.DECIMAL);
//
//		nukerSpeedSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.Nukerradius = (int) Math.round(slider.getValue());
//
//			}
//		});
		SliderSetting<Integer> nukerSpeedSlider = new SliderSetting<Integer>("Radius", ClientSettings.Nukerradius, 1, 6, ValueFormat.INT);
		return new ModSetting[] { nukerSpeedSlider };
	}

	public CakeNuker() {
		super("Fucker", Keyboard.KEY_N, Category.WORLD,
				"Destroys every bed/cake/egg near the hood.");
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
	public void onUpdate() {

		for (int x = -ClientSettings.Nukerradius; x < ClientSettings.Nukerradius; x++) {
			for (int y = ClientSettings.Nukerradius + 1; y > -ClientSettings.Nukerradius + 1; y--) {
				for (int z = -ClientSettings.Nukerradius; z < ClientSettings.Nukerradius; z++) {

					double xBlock = (mc.thePlayer.posX + x);
					double yBlock = (mc.thePlayer.posY + y);
					double zBlock = (mc.thePlayer.posZ + z);

					BlockPos blockPos = new BlockPos(xBlock, yBlock, zBlock);
					Block block = mc.theWorld.getBlockState(blockPos).getBlock();

					if (block.getMaterial() == Material.cake || block == Blocks.bed || block == Blocks.dragon_egg) {
					
						 mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
					mc.thePlayer.sendQueue.addToSendQueue(
							new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
					mc.thePlayer.sendQueue.addToSendQueue(
							new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
				
			
					}
				}
			}
		}
		super.onUpdate();
	}
}

	


