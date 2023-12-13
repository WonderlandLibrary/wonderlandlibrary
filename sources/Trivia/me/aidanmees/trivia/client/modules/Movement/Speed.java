package me.aidanmees.trivia.client.modules.Movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.openal.SoundStore;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.BlockUtils;
import me.aidanmees.trivia.client.tools.LiquidUtils;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Speed extends Module {

	private double moveSpeedVanilla;
	private double speed;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private WaitTimer timer = new WaitTimer();
	private int motionDelay;
	boolean xd = true;
	boolean Meme = false;
	 private boolean nextTick;
	  private boolean hasJumped;
	  
	  
	  private int cooldown; 
	  private double x;
	  private double z;
	  
	 
	  private int groundTimer;
	  public boolean canStep;
	  private float ground = 0.0F;
	  private boolean boostCollided;
	  private double boostSpeed;
	  private int ticks;
		private int jumps = 0;
	  
	  private double moveSpeed2;
	  public static int settingUpTicks;
	  private double lastDist2;
	  public static int stage2;
	  
	@Override
	public ModSetting[] getModSettings() {
		// airAgilitySpeedSlider

//		Slider airAgilitySpeedSlider = new BasicSlider("Vanilla Speed Factor", ClientSettings.VanillaspeedFactor, 10,
//				150, 0.0, ValueDisplay.DECIMAL);
//
//		airAgilitySpeedSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.VanillaspeedFactor = slider.getValue();
//
//			}
//		});
		SliderSetting<Number> airAgilitySpeedSlider = new SliderSetting<Number>("Vanilla Speed", ClientSettings.VanillaspeedFactor, 10, 150, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { airAgilitySpeedSlider }; 
	}

	public Speed() {
		super("Speed", Keyboard.KEY_M, Category.MOVEMENT, "WROOOOOM");
		
	}
	@Override
	public void onDisable(){
		mc.timer.timerSpeed = 1.0F;
		
		
		 mc.timer.timerSpeed = 1.0F;
		 mc.thePlayer.speedInAir = 0.02F;
	}
	@Override
	public void onEnable(){
		this.lastDist2 = 0.0D;
	      stage2 = 2;
	      settingUpTicks = 2;
	      
	}

	@Override
	public void onToggle() {
		timer.reset();
		this.moveSpeed = getBaseMoveSpeed();
		speed = 1;
		this.stage = 2;
		super.onToggle();
	}
	private boolean shouldSpeedUp()
	  {
	    boolean moving = (MovementInput.moveForward != 0.0F) || (MovementInput.moveStrafe != 0.0F);
	    
	    return (!Minecraft.thePlayer.isInWater()) && (!LiquidUtils.isInLiquid()) && (!mc.thePlayer.isOnLadder()) && (!Minecraft.thePlayer.isSneaking()) && (!LiquidUtils.isOnLiquid()) && (moving);
	  }
	public static double roundToPlace(double value, int places)
	  {
	    if (places < 0) {
	      throw new IllegalArgumentException();
	    }
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	  }

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if (trivia.getModuleByName("Flight").isToggled()) {
			jumps = -1;
			this.stage = 2;
			return;
		}
		 if (currentMode.equals("Hypixel")){
			 MovementInput movementInput = mc.thePlayer.movementInput;
				float forward = movementInput.moveForward;
				float strafe = movementInput.moveStrafe;
				float yaw = mc.thePlayer.rotationYaw;
				if ((forward == 0.0F) && (strafe == 0.0F)) {
					event.x = 0;
					event.z = 0;
				} else if (forward != 0.0F) {
					if (strafe >= 1.0F) {
						yaw += (forward > 0.0F ? -45 : 45);
						strafe = 0.0F;
					} else if (strafe <= -1.0F) {
						yaw += (forward > 0.0F ? 45 : -45);
						strafe = 0.0F;
					}
					if (forward > 0.0F)
						forward = 1.0F;
					else if (forward < 0.0F)
						forward = -1.0F;
				}
				double mx = Math.cos(Math.toRadians(yaw + 90.0F));
				double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		if((movementInput.moveForward == 0) && (movementInput.moveStrafe == 0)){
			
						moveSpeed = ((float)getBaseMoveSpeed()*1.05F);
					}
					if((stage == 1) && mc.thePlayer.isCollidedVertically && ((movementInput.moveForward != 0) || (movementInput.moveStrafe != 0))){
						moveSpeed = ((float)(3.66D * getBaseMoveSpeed() - 0.01D));
					}
					if((stage != 2) || !mc.thePlayer.isCollidedVertically || ((movementInput.moveForward == 0) && (movementInput.moveStrafe == 0))){
						if(stage == 3){
							float diff = (float)(0.33000001311302185D * (getBaseMoveSpeed() - lastDist));
							moveSpeed = ((float)(getBaseMoveSpeed() - diff));
							mc.timer.timerSpeed = 1.47F;
						}else{
							//entity or render
							List<AxisAlignedBB> collisions = mc.thePlayer.getEntityWorld().getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer
									.boundingBox.expand(0.0D, 
											mc.thePlayer.motionY, 0.0D));
							if (((collisions.size() > 0) || (mc.thePlayer.isCollidedVertically)) && (stage > 0)) {
								stage = ((movementInput.moveForward == 0.0F) && (movementInput.moveStrafe == 0.0F) ? 0 : 1);
							}
							moveSpeed = (lastDist - lastDist / 210.0F);
						}
					}else{
						if (mc.thePlayer.ticksExisted % 2 == 0) {
							event.y = mc.thePlayer.motionY = 0.38D;
						} else {
							event.y = mc.thePlayer.motionY = 0.38D;
						}
						moveSpeed = ((float)(moveSpeed * 1.55D));
						mc.timer.timerSpeed = 1.47f;
					}
					if(stage > 0){
						if (!((forward == 0.0F) && (strafe == 0.0F))) {
							moveSpeed = ((float)Math.max(moveSpeed, getBaseMoveSpeed()));
							event.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
							event.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
						}
					}
					if((movementInput.moveForward != 0) || (movementInput.moveStrafe != 0))
						stage++;
		 }
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		
		 
		if (currentMode.equals("YPort")){
			if(mc.thePlayer.onGround || (stage == 3))
	    {
	      if (roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == roundToPlace(0.138D, 3)) {
	        event.y = -0.3D;
	      }
	      if (((mc.thePlayer.isCollidedHorizontally) || (mc.thePlayer.moveForward == 0.0F)) && (mc.thePlayer.moveStrafing == 0.0F))
	      {
	    
	        mc.timer.timerSpeed = 1F;
	      }
	      else if (stage == 2)
	      {
	        this.moveSpeed *= 2.1D;
	        stage = 3;
	      }
	      else if (stage == 3)
	      {
	        stage = 2;
	        double timer = 0.66D * (this.lastDist - getBaseMoveSpeed());
	        this.moveSpeed = (this.lastDist - timer);
	      }
	      else if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || (mc.thePlayer.isCollidedVertically))
	      {
	        stage = 1;
	      }
	      
	      double lol = this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
			
			
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
			event.x = (forward * lol * mx + strafe * lol * mz);
			event.z = (forward * lol * mz - strafe * lol * mx);
	     
	    }
		
		}
		if (currentMode.equals("SlowHop") || currentMode.equals("NormalHop")) {
		Minecraft.getMinecraft().thePlayer.aps = 0;
		double round = round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3);
	
		if(this.currentMode.equals("SlowHop") && forward != 0 || strafe != 0) {
			if (round == 0.869 || round == 0.15) {
				Entity thePlayer = mc.thePlayer;
				thePlayer.motionY -= 0.1D;
			}
		}
		
		// trivia.chatMessage(round);
		
		if ((this.stage == 1) && (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed))
        {
			this.stage = 2;

			this.moveSpeed = (2.012D * getBaseMoveSpeed());
			if (this.currentMode.equals("NormalHop")) {
				this.moveSpeed = (1.0D * getBaseMoveSpeed() - 0.01D);
			}
			if(this.currentMode.equals("SlowHop")) {
				this.moveSpeed = getBaseMoveSpeed();
			}
		} else if (this.stage == 2) {
			this.stage = 3;
		
				mc.thePlayer.motionY = 0.399399995803833D;
			
			event.y = 0.399399995803833D;
			this.moveSpeed *= 2.149D;
			if(this.currentMode.equals("SlowHop")) {
				this.moveSpeed = getBaseMoveSpeed() * 1.88;
			}
			jumps++;

		} else if (this.stage == 3) {
			this.stage = 4;

			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference);
		} else {
			boolean ground = false;
			
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true).size() > 0)) {
					ground = true;
				} else {
					ground = false;
				
			}
			// trivia.chatMessage(ground);
			if (ground || (mc.thePlayer.isCollidedVertically) || mc.thePlayer.onGround) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
			jumps = 0;
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
		
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
		event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
		super.onPreMotion(event);
	}
		}
	
		if (this.currentMode.equals("Vanilla")) {
			
			this.moveSpeedVanilla = ClientSettings.VanillaspeedFactor / 20;
			
			
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
			event.x = (forward * this.moveSpeedVanilla * mx + strafe * this.moveSpeedVanilla * mz);
			event.z = (forward * this.moveSpeedVanilla * mz - strafe * this.moveSpeedVanilla * mx);
			super.onPreMotion(event);
		}
		
		
	
	
	
		 
	}
	
   
             
      
	
 
	
	 private boolean canSpeed(boolean groundCheck)
	  {
	  
	    List collidingBoundingBoxes = Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().expand(0.25D, 0.0D, 0.25D));
	    boolean blockCheck =  collidingBoundingBoxes.isEmpty();
	    
	    boolean moving = MovementInput.moveForward != 0.0F;
	    boolean strafing = MovementInput.moveStrafe != 0.0F;
	    
	    moving = (moving) || (strafing);
	    
	    boolean sneaking = Minecraft.thePlayer.isSneaking();
	    boolean collided = Minecraft.thePlayer.isCollidedHorizontally;
	    
	    boolean inLiquid = BlockUtils.isInLiquid(Minecraft.thePlayer);
	    boolean onLiquid = BlockUtils.isOnLiquid(Minecraft.thePlayer);
	    boolean onIce = BlockUtils.isOnIce(Minecraft.thePlayer);
	    
	    return (moving) && (!sneaking) && (!collided) && (!inLiquid) && (!onLiquid) && (!onIce) && (groundCheck) && (blockCheck);
	  }
	  
	  private double applySpeedModifier(double speed)
	  {
	    if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed))
	    {
	      PotionEffect effect = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed);
	      switch (effect.getAmplifier())
	      {
	      case 0: 
	        speed -= 0.2975D;
	        break;
	      case 1: 
	        speed -= 0.5575D;
	        break;
	      case 2: 
	        speed -= 0.7858D;
	        break;
	      case 3: 
	        speed -= 0.9075D;
	      }
	    }
	    return speed;
	  }
	  public static void setSpeed(double speed)
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

	  public float getSpeed()
	  {
	    return (float)Math.sqrt(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + 
	      Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
	  }
	  
	  public static double[] moveLooking( float yawOffset) {
	        float dir = mc.thePlayer.rotationYaw + yawOffset;
	        //dir += (mc.thePlayer.moveForward < 0.0F ? 180.0F : (mc.thePlayer.moveStrafing > 0.0F ? -(90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F)) : ( mc.thePlayer.moveStrafing < 0.0F ? (90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F)) : 0)));
	        //import is a faggot
	                        if (mc.thePlayer.moveForward < 0.0F) {
	            dir += 180.0F;
	        }
	        if (mc.thePlayer.moveStrafing > 0.0F) {
	            dir -= 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F);
	        }
	        if (mc.thePlayer.moveStrafing < 0.0F) {
	            dir += 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F);
	        }

	        float xD = (float)Math.cos((dir + 90.0F) * Math.PI / 180.0D);
	        float zD = (float)Math.sin((dir + 90.0F) * Math.PI / 180.0D); // sin(x + 90) = cos(x) ? Explain?
	        return new double[] { xD, zD };
	    }
	  private void setSpeed2(double speed)
	  {
	    mc.thePlayer.motionX = (-MathHelper.sin(getDirection()) * speed);
	    mc.thePlayer.motionZ = (MathHelper.cos(getDirection()) * speed);
	  }
	
	  @Override
	  public void onUpdate(UpdateEvent event)
	  {
		  
		 
		  if (trivia.getModuleByName("Flight").isToggled()) {
				return;
			}
		  if (currentMode.equals("Hypixel")) {
			  
			  if ((mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) && !mc.getMinecraft().gameSettings.keyBindJump.pressed)
		        {
				 
			  float dx = (float)(mc.thePlayer.posX - mc.thePlayer.prevPosX);
				float dz = (float)(mc.thePlayer.posZ - mc.thePlayer.prevPosZ);
				lastDist = ((float)Math.sqrt(dx * dx + dz * dz));
				if (mc.thePlayer.onGround) {
					mc.timer.timerSpeed = 3F;
				} else {
					mc.timer.timerSpeed = 1.0F;
					
				}
		  }
		  }
		  if(currentMode.equals("Mineplex")) {
			  if ((mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) && !mc.getMinecraft().gameSettings.keyBindJump.pressed)
		        {
				 
				 if (mc.thePlayer.onGround) {
					
			
			               
			            	
					 mc.thePlayer.motionY = 0.40f;
					
				 }
				 else {
					
					
				 }
		        }
			  else {
				  mc.thePlayer.motionX = 0;
				  mc.thePlayer.motionZ = 0;
			  }
		  }
          
		  if (currentMode.equals("Guardian-YPort")) {
			  if ((mc.thePlayer.movementInput.moveForward == 0.0F)
						&& (mc.thePlayer.movementInput.moveStrafe == 0.0F)) {
					EntityPlayerSP thePlayer = mc.thePlayer;
					EntityPlayerSP thePlayer2 = mc.thePlayer;
					double n = 0.0D;
					thePlayer2.motionZ = 0.0D;
					thePlayer.motionX = 0.0D;
					return;
				}
				if (mc.thePlayer.onGround) {
					setSpeed(0.5899999713897705D);
					mc.thePlayer.motionY = 0.2D;
					mc.timer.timerSpeed = mc.thePlayer.ticksExisted % 2 == 0 ? 2.05F : 1.35F;
				}
				setSpeed(0.3499999940395355D);
				mc.thePlayer.motionY = -1.0D;
						
	
		  }
		  if (currentMode.equals("Guardian-Hop")){
			  if ((mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) && !mc.getMinecraft().gameSettings.keyBindJump.pressed)
		        {
				  if (mc.thePlayer.fallDistance >= 3.0F) {
					}
					if ((mc.thePlayer.moveStrafing == 0.0F) && (mc.thePlayer.moveForward == 0.0F)) {
						mc.thePlayer.motionX = 0.0D;
						mc.thePlayer.motionZ = 0.0D;
					}
					if ((mc.thePlayer.onGround) && (mc.thePlayer.isMoving())
							&& (!LiquidUtils.isInLiquid()) && (!LiquidUtils.isOnLiquid())) {
						setSpeed(1.2000001276837158D);
						mc.thePlayer.motionY = 0.4255D;
					}
					setSpeed((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX
							+ mc.thePlayer.motionZ * mc.thePlayer.motionZ));
				}

				
		  }

		  
		  if (currentMode.equals("AAC-Port")){
			  if ((mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) && !mc.getMinecraft().gameSettings.keyBindJump.pressed)
		        {
		            if (mc.thePlayer.onGround && !mc.getMinecraft().gameSettings.keyBindJump.pressed)
		            {
		                mc.thePlayer.jump();
		            } 
		            else if (!mc.getMinecraft().gameSettings.keyBindJump.pressed)
		            {
		                mc.timer.renderPartialTicks = 0.0835F;
		                mc.thePlayer.motionY = -0.215; 
		                mc.thePlayer.jumpMovementFactor *= 1.03;
		            }
		  }
		  }
		
		  if (currentMode.equals("YPort")){
			  
			  if (stage == 3) {
			        event.y = (event.y + 0.424D);
			      }
			      double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			      double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			      this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
			      mc.thePlayer.motionY = -0.424D;
			    }
		  
	
	  
	  }
	
	@Override
	public String[] getModes() {
		return new String[] { "Vanilla", "NormalHop", "SlowHop",  "Hypixel",  "YPort" , "AAC-Port" , "Guardian-Hop", "Guardian-YPort" , "Mineplex"};
	}

	public void onLateUpdate() {
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		super.onLateUpdate();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}
	public static void setMoveSpeed(PreMotionEvent event, double speed)
	  {
	    double forward = mc.thePlayer.moveForward;
	    double strafe = mc.thePlayer.moveStrafing;
	    float yaw =  mc.thePlayer.rotationYaw;
	    if ((forward == 0.0D) && (strafe == 0.0D))
	    {
	      event.x = (0.0D);
	      event.z = (0.0D);
	    }
	    else
	    {
	      if (forward != 0.0D)
	      {
	        if (strafe > 0.0D) {
	          yaw += (forward > 0.0D ? -45 : 45);
	        } else if (strafe < 0.0D) {
	          yaw += (forward > 0.0D ? 45 : -45);
	        }
	        strafe = 0.0D;
	        if (forward > 0.0D) {
	          forward = 1.0D;
	        } else if (forward < 0.0D) {
	          forward = -1.0D;
	        }
	      }
	      event.x = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
	      event.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
	    }
	  }
	public static void setMoveSpeed(UpdateEvent event, double speed)
	  {
	    double forward = mc.thePlayer.moveForward;
	    double strafe = mc.thePlayer.moveStrafing;
	    float yaw =  mc.thePlayer.rotationYaw;
	    if ((forward == 0.0D) && (strafe == 0.0D))
	    {
	      event.y = (0.0D);
	      event.z = (0.0D);
	    }
	    else
	    {
	      if (forward != 0.0D)
	      {
	        if (strafe > 0.0D) {
	          yaw += (forward > 0.0D ? -45 : 45);
	        } else if (strafe < 0.0D) {
	          yaw += (forward > 0.0D ? 45 : -45);
	        }
	        strafe = 0.0D;
	        if (forward > 0.0D) {
	          forward = 1.0D;
	        } else if (forward < 0.0D) {
	          forward = -1.0D;
	        }
	      }
	      event.x = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
	      event.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
	    }
	  }

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	  public boolean willCollide(final double x, final double z) {
	        for (int i = 1; i < 4; ++i) {
	            if (!Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(x * i, 0.0, z * i)).isEmpty()) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    public double getHighestOffset(final double max) {
	        for (double i = 0.0; i < max; i += 0.01) {
	            for (final int offset : new int[] { -2, -1, 0, 1, 2 }) {
	                if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(this.mc.thePlayer.motionX * offset, i, this.mc.thePlayer.motionZ * offset)).size() > 0) {
	                    return i - 0.01;
	                }
	            }
	        }
	        return max;
	    }
}
