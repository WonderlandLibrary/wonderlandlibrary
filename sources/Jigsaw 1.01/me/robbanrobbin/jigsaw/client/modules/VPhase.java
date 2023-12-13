package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

public class VPhase extends Module {
	
	public VPhase() {
		super("VPhase", Keyboard.KEY_NONE, Category.EXPLOITS,
				"Note: You have to sneak! Enables you to go through 6 blocks of floor in vanilla");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if(mc.gameSettings.keyBindSneak.pressed
				&& !Jigsaw.getModuleByName("Freecam").isToggled()
				&& mc.player.onGround) {
			//mc.player.setPosition(((int)event.x) - 0.5, event.y, ((int)event.z) - 0.5);
			for(int i = 1; i < 9; i++) {
				BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY - i, mc.player.posZ);
				IBlockState blockState = Utils.getBlockState(pos);
				if(blockState.getMaterial() == Material.AIR && i < 8
						&& Utils.getBlockState(pos.down()).getMaterial() == Material.AIR) {
					sendPacket(new CPacketPlayer.Position(pos.getX() + 0.5, event.y, pos.getZ() + 0.5, mc.player.onGround));
					mc.player.setPosition(pos.getX() + 0.5, event.y - (i + 1), pos.getZ() + 0.5);
					break;
				}
			}
		}
		super.onUpdate();
	}

}
