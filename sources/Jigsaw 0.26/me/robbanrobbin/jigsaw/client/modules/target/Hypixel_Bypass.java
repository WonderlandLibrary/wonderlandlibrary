package me.robbanrobbin.jigsaw.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class Hypixel_Bypass extends Module {

	public Hypixel_Bypass() {
		super("AntiBot(Watchdog)", Keyboard.KEY_NONE, Category.TARGET, "Tries to not hit the fake players on hypixel");
	}

	@Override
	public boolean isCheckbox() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		if(mc.thePlayer.ticksExisted < 60) {
			Utils.blackList.clear();
		}
//		System.out.println(Utils.isEntityOnGround(mc.thePlayer));
		for(EntityPlayer player : mc.theWorld.playerEntities) {
			if(!mc.thePlayer.isEntityEqual(player)) {
				if(player.onGround && !Utils.isEntityOnGround(player)
						&& mc.thePlayer.ticksExisted > 60 && Utils.getXZDist(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 
								new Vec3(player.posX, player.posY, player.posZ)) < 3
						&& player.posY - mc.thePlayer.posY > 5) {
//					System.out.println(player.getName());
					if(!Utils.isBlacklisted(player)) {
						Utils.blackList.add(player);
					}
				}
				if(player.isInvisible() && player.onGround && !Utils.isEntityOnGround(player)
						&& mc.thePlayer.ticksExisted > 60 && Utils.getXZDist(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 
								new Vec3(player.posX, player.posY, player.posZ)) > 6 &&
						Utils.getXZDist(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 
								new Vec3(player.posX, player.posY, player.posZ)) < 30
						&& mc.thePlayer.ticksExisted < 300) {
					if(!Utils.isBlacklisted(player)) {
						Utils.blackList.add(player);
					}
				}
			}
		}
//		Utils.blackList.clear();
		super.onUpdate();
	}

}
