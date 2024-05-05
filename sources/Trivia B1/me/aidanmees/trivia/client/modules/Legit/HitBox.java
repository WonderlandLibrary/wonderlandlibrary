package me.aidanmees.trivia.client.modules.Legit;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.friends.FriendsMananger;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.src.MathUtils;
import net.minecraft.util.MathHelper;

public class HitBox extends Module {
	
	public HitBox() {
		super("HitBox", Keyboard.KEY_NONE, Category.LEGIT, "Extends the player hitbox.");
	}

	@Override
	public void onToggle() {
		
		super.onToggle();
	}

	
	@Override
	public ModSetting[] getModSettings() {
		
		SliderSetting<Number> HitBox = new SliderSetting<Number>("Size", ClientSettings.HitBoxSize, 0.2, 5.0, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { HitBox };
	}
	@Override
	public void onUpdate() {
	}
}
