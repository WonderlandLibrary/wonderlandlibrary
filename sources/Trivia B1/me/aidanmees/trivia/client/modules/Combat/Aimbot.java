package me.aidanmees.trivia.client.modules.Combat;

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

public class Aimbot extends Module {
	
	public Aimbot() {
		super("Aimbot", Keyboard.KEY_NONE, Category.LEGIT, "Aims for the closest entity. Tick the \"Smooth Aim\" box to make it a smooth aimbot.");
	}

	@Override
	public void onToggle() {
		
		super.onToggle();
	}

	
	@Override
	public ModSetting[] getModSettings() {
		SliderSetting<Number> slider1 = new SliderSetting<Number>("Range", ClientSettings.AimBotRange, 1.0, 6, 0.0, ValueFormat.DECIMAL);
		SliderSetting<Number> slider2 = new SliderSetting<Number>("Max Angle", ClientSettings.AimBotMax, 90, 360, 1.0, ValueFormat.DECIMAL);
		
		SliderSetting<Number> slider4 = new SliderSetting<Number>("Aim Speed", ClientSettings.AimBotSpeed, 0.1, 10.0, 0.0, ValueFormat.DECIMAL);


		
		CheckBtnSetting box1 = new CheckBtnSetting("Vertical Aim", "aimbot_Pitch");
		CheckBtnSetting box2 = new CheckBtnSetting("Ignore Teams", "ignoreTeams");
		CheckBtnSetting box3 = new CheckBtnSetting("Hold Weapon", "WeaponOnly");
		CheckBtnSetting box4 = new CheckBtnSetting("Holding Mouse", "MouseHold");
		
	
		
		return new ModSetting[] { slider1, slider2, slider4 , box1 ,box2 ,box3 ,box4 };
	}

	
	@Override
	public void onUpdate() {
		if(getClosestPlayerToMouse() == null || mc.currentScreen != null){
			return;
		}
		float[] meme = getRots(getClosestPlayerToMouse(), (float)ClientSettings.AimBotSpeed, (float)ClientSettings.AimBotSpeed);
		if(getDistanceFromMouse(getClosestPlayerToMouse()) > ClientSettings.AimBotMin && isHoldingWeapon() && isClicking()){
		mc.thePlayer.rotationYaw = meme[0];
		if(ClientSettings.aimbot_Pitch)
		mc.thePlayer.rotationPitch = meme[1];
		}
	}
	public boolean isClicking(){
		if(!ClientSettings.MouseHold){
			return true;
		}
		return Mouse.isButtonDown(0);
	}
	public boolean isHoldingWeapon(){
		if(!ClientSettings.WeaponOnly){
			return true;
		}
		return mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword || mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe);
	}
	 public float[] getRots(Entity p_70625_1_, float p_70625_2_, float p_70625_3_)
	    {
	    	Random r = new Random();
	    	boolean yup = r.nextBoolean();
	    	boolean rup = r.nextBoolean();
	    	double var4 = p_70625_1_.posX - mc.thePlayer.posX;
	        double var8 = p_70625_1_.posZ - mc.thePlayer.posZ;
	        double var6;

	        if (p_70625_1_ instanceof EntityLivingBase)
	        {
	            EntityLivingBase var14 = (EntityLivingBase)p_70625_1_;
	            var6 = (var14.posY - 0.5f + (double)var14.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()));
	        }
	        else
	        {
	            var6 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0D - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
	        }

	        double var141 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
	        float var12 = (float)(Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
	        float var13 = (float)(-(Math.atan2(var6, var141) * 180.0D / Math.PI));
	        float pitch = this.updateRotation(mc.thePlayer.rotationPitch, var13, p_70625_3_);
	        float yaw  = this.updateRotation(mc.thePlayer.rotationYaw, var12, p_70625_2_);
	        return new float[]{yaw, pitch};
	    }

	    /**
	     * Arguments: current rotation, intended rotation, max increment.
	     */
	    private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_)
	    {
	        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

	        if (var4 > p_70663_3_)
	        {
	            var4 = p_70663_3_;
	        }

	        if (var4 < -p_70663_3_)
	        {
	            var4 = -p_70663_3_;
	        }

	        return p_70663_1_ + var4;
	    }

	public EntityPlayer getClosestPlayerToMouse(){
		EntityPlayer closest = null;
		for(EntityPlayer player : mc.theWorld.getLoadedPlayers()){
			if(isValid(player)){
				if(closest == null || getDistanceFromMouse(closest) > getDistanceFromMouse(player)){
					closest = player;
				}
			}
		}
		return closest;
	}
	public boolean isValid(EntityPlayer player){
		if(FriendsMananger.isFriend(player.getName())){
			return false;
		}
		if(player == mc.thePlayer){
			return false;
		}
		if(player.isInvisible()){
			return false;
		}
		if(player.getDistanceToEntity(mc.thePlayer) > ClientSettings.AimBotRange){
			return false;
		}
		if(!ClientSettings.ignoreTeams){
			if(MathUtils.isOnSameTeam(player, mc.thePlayer)){
				return false;
			}
		}
		if(!player.canEntityBeSeen(mc.thePlayer)){
			return false;
		}
		if(getDistanceFromMouse(player) > ClientSettings.AimBotMax){
			return false;
		}
		return true;
	}
	public static int getDistanceFromMouse(Entity entity)
	{
		float[] neededRotations = getRotationsNeeded(entity);
		if(neededRotations != null)
		{
			float neededYaw = mc.thePlayer.rotationYaw- neededRotations[0];
			float neededPitch = mc.thePlayer.rotationPitch - neededRotations[1];
			float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
			return (int)distanceFromMouse;
		}
		return -1;
	}
  public static float[] getRotationsNeeded(Entity entity)
	{
		if(entity == null)
			return null;
		double diffX = entity.posX - mc.thePlayer.posX;
		double diffY;
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
		}else
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
		double diffZ = entity.posZ - mc.thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw =
			(float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
		
	}
}
