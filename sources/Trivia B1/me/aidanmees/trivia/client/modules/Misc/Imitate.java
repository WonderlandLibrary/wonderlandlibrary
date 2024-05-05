package me.aidanmees.trivia.client.modules.Misc;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Imitate extends Module {

	public Imitate() {
		super("Imitate", Keyboard.KEY_NONE, Category.MOVEMENT, "Imitates the closest entity.");
	}

	@Override
	public void onUpdate() {
		EntityLivingBase newEn = null;
		newEn = Utils.getClosestEntity((float) 10);
		if (newEn == null) {							
			
			return;
		}
		if (mc.thePlayer.getDistanceToEntity(newEn) < 10) {
			
			
			mc.thePlayer.rotationPitch = newEn.rotationPitch;
			mc.thePlayer.rotationYaw = newEn.rotationYaw;
			mc.thePlayer.rotationYawHead = newEn.rotationYawHead;
		
			
			
			if (newEn.isSneaking()){
				mc.thePlayer.isSneaking();
				
			}
			if (newEn.isSwingInProgress){
				
				trivia.click();
				
			}
			if (newEn.isSprinting()){
				mc.thePlayer.setSprinting(true);
				
			}
			
			if (newEn.motionX < 0) {
				mc.thePlayer.motionX = newEn.motionX;
				
			}
			if (newEn.motionY < 0) {
				mc.thePlayer.motionY = newEn.motionY;
				
			}
			if (newEn.motionZ < 0) {
				mc.thePlayer.motionZ = newEn.motionZ;
				
			}
			
			
			
			
		}
		}
}