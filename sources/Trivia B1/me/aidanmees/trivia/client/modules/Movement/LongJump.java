package me.aidanmees.trivia.client.modules.Movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class LongJump extends Module {

	private double speed;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private double Meme1;
	  private int Meme2;
	  private int airTicks;
	  private int groundTicks;
		int jumps = 0;
	  private float Meme3;
	  private int Meme4;

	public LongJump() {
		super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes your jumps longer.");
	}
	@Override
	public void onEnable(){
		if (currentMode.equals("New")){
			  this.groundTicks = -5;
		}
	      super.onEnable();
		
	}
	@Override 
	public void onDisable(){
		if (currentMode.equals("New"))
		{
			
		}
		
		
		
		super.onDisable();
	}
	

	@Override
	public void onToggle() {
	if (currentMode.equals("NCP"))
		{	
		this.moveSpeed = getBaseMoveSpeed();
		speed = 1;
		this.stage = 0;
		super.onToggle();
	}
	}
	@Override
	public void onUpdate(UpdateEvent e){
		if (currentMode.equals("Guardian")){
			
			 if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
		            if (mc.thePlayer.onGround) {
		                this.setSpeed(8.5);
		                mc.thePlayer.motionY = 0.4255;
		               
		            }
		            else {
		                this.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
		            }
		        }
		        else {
		            mc.thePlayer.motionX = 0.0;
		            mc.thePlayer.motionZ = 0.0;
		        }
		}
	if (currentMode.equals("New")){
		 boolean moving = (this.mc.gameSettings.keyBindForward.isKeyDown()) || (this.mc.gameSettings.keyBindBack.isKeyDown());
		    if (!moving) {
		      return;
		    }
		    double forward = MovementInput.moveForward;
		    float yaw = Minecraft.thePlayer.rotationYaw;
		    if (forward != 0.0D)
		    {
		      if (forward > 0.0D) {
		        forward = 1.0D;
		      } else if (forward < 0.0D) {
		        forward = -1.0D;
		      }
		    }
		    else {
		      forward = 0.0D;
		    }
		    float[] motion = { 0.4206065F, 0.4179245F, 0.41525924F, 0.41261F, 0.409978F, 0.407361F, 0.404761F, 0.402178F, 0.399611F, 0.39706F, 0.394525F, 0.392F, 0.3894F, 0.38644F, 0.383655F, 0.381105F, 0.37867F, 0.37625F, 0.37384F, 0.37145F, 0.369F, 0.3666F, 0.3642F, 0.3618F, 0.35945F, 0.357F, 0.354F, 0.351F, 0.348F, 0.345F, 0.342F, 0.339F, 0.336F, 0.333F, 0.33F, 0.327F, 0.324F, 0.321F, 0.318F, 0.315F, 0.312F, 0.309F, 0.307F, 0.305F, 0.303F, 0.3F, 0.297F, 0.295F, 0.293F, 0.291F, 0.289F, 0.287F, 0.285F, 0.283F, 0.281F, 0.279F, 0.277F, 0.275F, 0.273F, 0.271F, 0.269F, 0.267F, 0.265F, 0.263F, 0.261F, 0.259F, 0.257F, 0.255F, 0.253F, 0.251F, 0.249F, 0.247F, 0.245F, 0.243F, 0.241F, 0.239F, 0.237F };
		    float[] glide = { 0.3425F, 0.5445F, 0.65425F, 0.685F, 0.675F, 0.2F, 0.895F, 0.719F, 0.76F };
		    if ((!Minecraft.thePlayer.isCollidedVertically) && (!Minecraft.thePlayer.onGround))
		    {
		      this.airTicks += 1;
		      this.groundTicks = -5;
		      if (!Minecraft.thePlayer.isCollidedVertically)
		      {
		        if ((this.airTicks - 6 >= 0) && (this.airTicks - 6 < glide.length)) {
		        	
		          Minecraft.thePlayer.motionY *= glide[(this.airTicks - 6)];
		        }
		        if ((Minecraft.thePlayer.motionY < -0.2D) && (Minecraft.thePlayer.motionY > -0.24D)) {
		          Minecraft.thePlayer.motionY *= 0.7D;
		        } else if ((Minecraft.thePlayer.motionY < -0.25D) && (Minecraft.thePlayer.motionY > -0.32D)) {
		          Minecraft.thePlayer.motionY *= 0.8D;
		        } else if ((Minecraft.thePlayer.motionY < -0.35D) && (Minecraft.thePlayer.motionY > -0.8D)) {
		          Minecraft.thePlayer.motionY *= 0.98D;
		        }
		      }
		      if ((this.airTicks - 1 >= 0) && (this.airTicks - 1 < motion.length))
		      {
		        Minecraft.thePlayer.motionX = (forward * motion[(this.airTicks - 1)] * 3.0D * Math.cos(Math.toRadians(yaw + 90.0F)));
		        Minecraft.thePlayer.motionZ = (forward * motion[(this.airTicks - 1)] * 3.0D * Math.sin(Math.toRadians(yaw + 90.0F)));
		      }
		      else
		      {
		        Minecraft.thePlayer.motionX = 0.0D;
		        Minecraft.thePlayer.motionZ = 0.0D;
		      }
		    }
		    else
		    {
		      this.airTicks = 0;
		      this.groundTicks += 1;
		      if (this.groundTicks <= 2)
		      {
		        Minecraft.thePlayer.motionX = (forward * 0.009999999776482582D * Math.cos(Math.toRadians(yaw + 90.0F)));
		        Minecraft.thePlayer.motionZ = (forward * 0.009999999776482582D * Math.sin(Math.toRadians(yaw + 90.0F)));
		      }
		      else if (this.groundTicks > 2)
		      {
		        Minecraft.thePlayer.motionX = (forward * 0.30000001192092896D * Math.cos(Math.toRadians(yaw + 90.0F)));
		        Minecraft.thePlayer.motionZ = (forward * 0.30000001192092896D * Math.sin(Math.toRadians(yaw + 90.0F)));
		        Minecraft.thePlayer.motionY = 0.42399999499320984D;
		      }
		    }
		  }
		}

	  public void updatePosition(double paramDouble1, double paramDouble2, double paramDouble3) {}
	  
	  private double getDistance(EntityPlayer paramEntityPlayer, double paramDouble)
	  {
	    List localList = paramEntityPlayer.worldObj.getCollidingBoundingBoxes(paramEntityPlayer, paramEntityPlayer.getEntityBoundingBox().addCoord(0.0D, -paramDouble, 0.0D));
	    if (localList.isEmpty()) {
	      return 0.0D;
	    }
	    double d = 0.0D;
	    Iterator localIterator = localList.iterator();
	    while (localIterator.hasNext())
	    {
	      AxisAlignedBB localAxisAlignedBB = (AxisAlignedBB)localIterator.next();
	      if (localAxisAlignedBB.maxY > d) {
	        d = localAxisAlignedBB.maxY;
	      }
	    }
	    return paramEntityPlayer.posY - d;
	  }
	
	  private void setSpeed(double speed)
	  {
	    mc.thePlayer.motionX = (-MathHelper.sin(getDirection()) * speed);
	    mc.thePlayer.motionZ = (MathHelper.cos(getDirection()) * speed);
	  }
	
	
public static float getDirection()
{
  float yaw = mc.thePlayer.rotationYawHead;
  float forward = mc.thePlayer.moveForward;
  float strafe = mc.thePlayer.moveStrafing;
  yaw += (forward < 0.0F ? 180 : 0);
  if (strafe < 0.0F) {
    yaw += (forward == 0.0F ? 90 : forward < 0.0F ? -45 : 45);
  }
  if (strafe > 0.0F) {
    yaw -= (forward == 0.0F ? 90 : forward < 0.0F ? -45 : 45);
  }
  return yaw * 0.017453292F;
}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		
		
	if (currentMode.equals("NCP"))
		{
		if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
			this.stage = 1;
			return;
		}
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		double round = round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3);
		if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
			this.stage = 2;
			this.moveSpeed = (1.38D * getBaseMoveSpeed() - 0.01D);
		} else if (this.stage == 2) {
			this.stage = 3;
			mc.thePlayer.motionY = 0.399399995803833D;
			event.y = 0.399399995803833D;
			this.moveSpeed *= 2.149D;
		} else if (this.stage == 3) {
			this.stage = 4;
			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference) * 2.5;
		} else {
			if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
					mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true).size() > 0)
					|| (mc.thePlayer.isCollidedVertically)) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
			if (mc.thePlayer.motionY < -0.1) {
				// mc.thePlayer.motionY += 0.05;
			}

		}
		
		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
		} else if (forward != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (forward > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (forward > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}
			if (forward > 0.0F) {
				forward = 1.0F;
			} else if (forward < 0.0F) {
				forward = -1.0F;
			}
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
		event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
		super.onPreMotion(event);
	}
	}

	public void onLateUpdate() {
	if (currentMode.equals("NCP"))
		{
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		super.onLateUpdate();
	}
	}
	
	private double getBaseMoveSpeed() {
	
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}
	
	public static double round(double value, int places) {
	
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	@Override
	public String[] getModes() {
		return new String[] { "NCP","New" , "Guardian"};
	}
}
