package me.robbanrobbin.jigsaw.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class AntiBot extends Module {

	public AntiBot() {
		super("AntiBot", Keyboard.KEY_NONE, Category.TARGET);
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if(currentMode.equals("Hypixel")) {
			if(mc.player.ticksExisted < 60) {
				Utils.blackList.clear();
			}
//			System.out.println(Utils.isEntityOnGround(mc.player));
			for(EntityPlayer player : mc.world.playerEntities) {
				if(!mc.player.isEntityEqual(player)) {
					if(player.onGround && !Utils.isEntityOnGround(player)
							&& mc.player.ticksExisted > 60 && Utils.getXZDist(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), 
									new Vec3d(player.posX, player.posY, player.posZ)) < 2
							&& player.posY - mc.player.posY > 5) {
//						System.out.println(player.getName());
						if(!Utils.isBlacklisted(player)) {
							Utils.blackList.add(player);
						}
					}
					if(player.isInvisible() && player.onGround && !Utils.isEntityOnGround(player)
							&& mc.player.ticksExisted > 60 && Utils.getXZDist(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), 
									new Vec3d(player.posX, player.posY, player.posZ)) > 6 &&
							Utils.getXZDist(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), 
									new Vec3d(player.posX, player.posY, player.posZ)) < 30
							&& mc.player.ticksExisted < 500) {
						if(!Utils.isBlacklisted(player)) {
							Utils.blackList.add(player);
						}
					}
				}
			}
		}
		super.onUpdate(event);
	}
	
	@Override
	public void onToggle() {
		Utils.blackList.clear();
		super.onToggle();
	}

	@Override
	public String[] getModes() {
		return new String[]{"Hypixel", "Mineplex", "Ground", "TicksExisted"};
	}
	
}
