package me.aidanmees.trivia.client.modules.World;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.BoundingBoxEvent;
import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.LiquidUtils;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Jesus extends Module {

	private double moveSpeed;
	private WaitTimer timer = new WaitTimer();

	public Jesus() {
		super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to walk on liquids");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		timer.reset();
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if ((LiquidUtils.isOnLiquid()) && 
			      (!LiquidUtils.isInLiquid()) && (!Minecraft.thePlayer.isCollidedHorizontally)) {
			      this.mc.gameSettings.keyBindJump.pressed = false;
			    } else {
			      this.mc.gameSettings.keyBindJump.pressed = ((Keyboard.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode())) && (this.mc.inGameHasFocus));
			    }
			    if ((LiquidUtils.isInLiquid()) && 
			      (Minecraft.thePlayer.isInsideOfMaterial(Material.air)) && 
			      (!Minecraft.thePlayer.isSneaking()) && (
			      (!this.mc.gameSettings.keyBindJump.isKeyDown()) || (Minecraft.thePlayer.isInWater()))) {
			      Minecraft.thePlayer.motionY = 0.05D;
			    }
			    
			  
	super.onUpdate();
}
			  
			  public boolean isSafe()
			  {
			    return (Minecraft.thePlayer.isInWater()) || (Minecraft.thePlayer.isInsideOfMaterial(Material.lava)) || (Minecraft.thePlayer.isOnLadder()) || (Minecraft.thePlayer.getActivePotionEffects().contains(Potion.blindness)) || (Minecraft.thePlayer.ridingEntity != null);
			  }

	

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {

		super.onPacketRecieved(packetIn);
	}
	
	@Override
	public void onBoundingBox(BoundingBoxEvent event) {
	

	    if (((event.block instanceof BlockLiquid)) && 
	      (!LiquidUtils.isInLiquid()) && 
	      (!Minecraft.thePlayer.isSneaking()) && (mc.thePlayer.posY < Minecraft.thePlayer.getEntityBoundingBox().minY) && 
	      (event.block.getMetaFromState(Minecraft.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ))) < 4)) {
	      event.boundingBox = AxisAlignedBB.fromBounds(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX + 1.0D, mc.thePlayer.posY + (Minecraft.thePlayer.fallDistance < 3.0F ? 1.0D : 0.4D), mc.thePlayer.posZ + 1.0D);
	    }
	  }
		

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C03PacketPlayer) {
			 C03PacketPlayer player = (C03PacketPlayer)packet;
		      if ((LiquidUtils.isOnLiquid()) && (!LiquidUtils.isInLiquid()) && 
		        (Minecraft.thePlayer.ticksExisted % 2 == 0)) {
		        player.y = player.getPositionY() - 0.019999999552965164D;
		      }
		    

		}
		super.onPacketSent(packet);
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {

		super.onPreMotion(event);
	}

	@Override
	public String[] getModes() {
		return super.getModes();
	}

	@Override
	public String getAddonText() {
		return super.getAddonText();
	}

	

}
