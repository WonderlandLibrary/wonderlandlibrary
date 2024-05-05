package me.aidanmees.trivia.client.modules.Render;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class Hotbar extends Module {

	EntityArrow arrow;
	WaitTimer timer = new WaitTimer();

	public Hotbar() {
		super("Hotbar", Keyboard.KEY_NONE, Category.RENDER,
				"Just a hot hotbar");
	}

	@Override
	public void onDisable() {
		
		super.onDisable();
	}

	@Override
	public void onUpdate() {
	
		super.onUpdate();
	}

	@Override
	public void onEnable() {
	
		super.onEnable();
	}

}