package me.aidanmees.trivia.client.modules.Hidden;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MiddleClickFriends extends Module {

	int wait;

	public MiddleClickFriends() {
		super("MiddleClickFriends", Keyboard.KEY_NONE, Category.HIDDEN,
				"Enables you to middle click your mouse to add the player you are looking at as a friend.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		wait = 10;
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (wait < 10) {
			wait++;
			return;
		}
		if (mc.objectMouseOver == null) {
			return;
		}
		if (mc.objectMouseOver.entityHit == null) {
			return;
		}
		Entity hit = mc.objectMouseOver.entityHit;
		if (!(hit instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) hit;
		if (mc.gameSettings.keyBindPickBlock.isKeyDown()) {
			if (trivia.getFriendsMananger().isFriend(player)) {
				trivia.getFriendsMananger().removeFriend(player);
			} else {
				trivia.getFriendsMananger().getFriends().add(player.getName());
			}
			wait = 0;
		}

		super.onUpdate();
	}

}
