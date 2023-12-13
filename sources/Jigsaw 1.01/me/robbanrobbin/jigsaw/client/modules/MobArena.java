package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class MobArena extends Module {
	private static EntityLivingBase e;
	private int timer;

	public MobArena() {
		super("MobArena", Keyboard.KEY_NONE, Category.WORLD, "Automatically runs around and kills mobs.");
	}

	@Override
	public void onDisable() {

		e = null;
		mc.gameSettings.keyBindForward.pressed = false;
		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		float mindistance = 1000000;

		try {
			for (Object o : mc.world.getLoadedEntityList()) {
				if (!(o instanceof EntityLivingBase)) {
					continue;
				}
				if (o instanceof EntityPlayer) {
					continue;
				}
				EntityLivingBase en = (EntityLivingBase) o;
				if (mc.player.getDistanceToEntity(en) < mindistance) {
					mindistance = mc.player.getDistanceToEntity(en);
					e = en;
				}
			}
			if (e == null) {
				setToggled(false, true);
			}
			if (e.isDead) {
				e = null;
				setToggled(false, true);
			}
			if (mc.player.isDead) {
				e = null;
				setToggled(false, true);
			}

			Utils.faceEntity(e);
			float distance = mc.player.getDistanceToEntity(e);
			if (distance > 1.5f) {
				mc.gameSettings.keyBindForward.pressed = true;
			} else {
				mc.gameSettings.keyBindForward.pressed = false;
			}
			if (mc.player.isCollidedHorizontally && mc.player.onGround) {
				mc.player.jump();
			}
			if (mc.player.isInWater()) {
				mc.gameSettings.keyBindJump.pressed = true;
			} else {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.gameSettings.keyBindJump.pressed = false;
				}
			}
			if (mc.player.getDistanceToEntity(e) <= 4.25f) {
				if (timer < 5) {
					timer++;
					return;
				}
				AutoBlock.stopBlock();
				Criticals.crit(mc.player.posX, mc.player.posY, mc.player.posZ);;
				timer = 0;
				
				mc.playerController.attackEntity(mc.player, e);
				mc.player.swingArm(EnumHand.MAIN_HAND);

				AutoBlock.startBlock();
			}
		} catch (Exception e) {
			System.out.println(getName() + "Enountered an exception: " + e.getMessage());
		}

		super.onUpdate();
	}

}
