package me.aidanmees.trivia.client.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Flight extends Module {

	int wait = 6;
	double MACvelY = 0.02;
	double startingHeight;
	double fallSpeed = 0.05;
	private boolean air = true;
	int counter = 0;
	public static WaitTimer timer3 = new WaitTimer();
	 double pos;

	double maxY;
	Double startX;
	boolean meme;
	Double startY;
	Double startZ;
	boolean damaging = false;
	private WaitTimer timer = new WaitTimer();
	public double flyHeight;
	private Double moveSpeed;
	private boolean aac;
	Double posY;
	private double aad;
	boolean Up = false;
	boolean Start = false;
	private WaitTimer cubeTimer = new WaitTimer();
	private boolean cubecraftEnabled = false;
	public static boolean cubecraftOverrideTpaurathingidkwhattonameit = false;
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
	 double memepos;
	
	private double sX;
	private double sY;
	private double sZ;
	

	public Flight() {
		super("Flight", Keyboard.KEY_G, Category.MOVEMENT, "Allows you to fly.");
	}

	@Override
	public void onDisable() {
		counter = 1;
		counter = 0;
		 this.mc.timer.timerSpeed = 1.0F;
	      this.mc.thePlayer.capabilities.isFlying = false;
	      this.mc.thePlayer.motionX = 0.0D;
	      this.mc.thePlayer.motionY = 0.0D;
	      this.mc.thePlayer.motionZ = 0.0D;
		 mc.thePlayer.capabilities.isFlying = false;
	        mc.thePlayer.speedInAir = 0.02F;
	        if (currentMode.equals("Mineplex")) {
	        	for (int i = 1; i < 100; i++) {
	                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY + 11, mc.thePlayer.posZ + mc.thePlayer.motionZ, mc.thePlayer.rotationYaw , mc.thePlayer.rotationPitch, true));
	            	}
	        }
	        mc.timer.timerSpeed = 1.0F;
		super.onDisable();
	}

	public void onPreMotion(PreMotionEvent event) {
if(currentMode.equals("Cubecraft")) {
	
		
	
			mc.timer.timerSpeed = 0.30f;
            mc.thePlayer.motionY = 0.00;
            mc.thePlayer.setPosition(mc.thePlayer.posX ,mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ);
            
            event.y = 0;
            MovementInput movementInput = mc.thePlayer.movementInput;
            double lol = 1;
            event.y = 0;
  			float forward = movementInput.moveForward;
  			float strafe = movementInput.moveStrafe;
  			boolean up = movementInput.jump;
  			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
  			if ((forward == 0.0F) && (strafe == 0.0F)) {
  				event.x = 0.0D;
  				event.y = 0;
  				event.z = 0.0D;
  			} else if (forward != 0.0F) {
  				if (strafe >= 1.0F) {
  					yaw += (forward > 0.0F ? -45 : 45);
  					strafe = 0.0F;
  				} else if (strafe <= -1.0F) {
  					yaw += (forward > 0.0F ? 45 : -45);
  					strafe = 0.0F;
  					event.y = 0;
  				}
  				if (forward > 0.0F) {
  					forward = 1.0F;       
  				} else if (forward < 0.0F) {
  					forward = -1.0F;
  				}
  			}
//  			if(mc.gameSettings.keyBindJump.isKeyDown()) {
//  				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2, mc.thePlayer.posZ);
//  			}
//  			if(mc.gameSettings.keyBindSneak.isKeyDown()) {
//  				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.2, mc.thePlayer.posZ);
//  			}
  			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
  			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
  			event.x = (forward * lol * mx + strafe * lol * mz);
  			event.z = (forward * lol * mz - strafe * lol * mx);
  			event.y = 0;
  			if (mc.gameSettings.keyBindJump.isKeyDown()) {
  				event.y = (0.5 * lol);
//  				System.out.println(event.y);
  			}
  			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
  				event.y = (0.5 * -lol);
			
  			
  			}
  			
  	     
  	    }
  		
  		
		
	}

	public void gijabgioagbpwigbpihbpisbsrlkgbaoighbaig(float speed) {
		mc.thePlayer.motionX = (-(Math.sin(aan()) * speed));
		mc.thePlayer.motionZ = (Math.cos(aan()) * speed);
	}

	public float aan() {
		float var1 = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.thePlayer.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.thePlayer.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;

		return var1;
	}

	@Override
	public ModSetting[] getModSettings() {
//		BasicSlider slider1 = new BasicSlider("Flight Speed", ClientSettings.FlightdefaultSpeed, 0, 10, 0,
//				ValueDisplay.DECIMAL);
//		SliderListener listener1 = new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//				ClientSettings.FlightdefaultSpeed = slider.getValue();
//			}
//		};
//		slider1.addSliderListener(listener1);
//		final BasicCheckButton box1 = new BasicCheckButton("Default Smooth Flight");
//		box1.setSelected(ClientSettings.Flightsmooth);
//		ButtonListener listener2 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.Flightsmooth = box1.isSelected();
//			}
//		};
//		box1.addButtonListener(listener2);
//		final BasicCheckButton box2 = new BasicCheckButton("Flight Kick Bypass");
//		box2.setSelected(ClientSettings.flightkick);
//		ButtonListener listener3 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.flightkick = box2.isSelected();
//			}
//		};
//		box2.addButtonListener(listener3);
//		
//		final BasicCheckButton box3 = new BasicCheckButton("Glide Damage");
//		box3.setSelected(ClientSettings.glideDmg);
//		ButtonListener listener4 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.glideDmg = box3.isSelected();
//			}
//		};
//		box3.addButtonListener(listener4);
//		
//		final BasicCheckButton box4 = new BasicCheckButton("onGround Spoof");
//		box4.setSelected(ClientSettings.onGroundSpoofFlight);
//		ButtonListener listener5 = new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.onGroundSpoofFlight = box4.isSelected();
//			}
//		};
//		box3.addButtonListener(listener5);
		SliderSetting<Number> slider1 = new SliderSetting<Number>("Flight Speed", ClientSettings.FlightdefaultSpeed, 0.1, 9, 0.0, ValueFormat.DECIMAL);
		CheckBtnSetting box2 = new CheckBtnSetting("Smooth Flight", "Flightsmooth");
		CheckBtnSetting box1 = new CheckBtnSetting("Vanilla Kick Bypass", "flightkick");
		
		return new ModSetting[] { slider1, box2, box1};
	}
	

	@Override
	public void onEnable() {
		  memepos = mc.thePlayer.posY;
		  counter = 1;
		  if (currentMode.equals("Hypixel")) {
			  mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0000000001, mc.thePlayer.posZ);
	             
		  }
		if (currentMode.equals("Mineplex")) {
			mc.thePlayer.motionY =-100;
			timer.reset();
			
			
		}
		
		
		super.onEnable();
	}

	public void updateFlyHeight() {
		double h = 1;
		AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625);
		for (flyHeight = 0; flyHeight < mc.thePlayer.posY; flyHeight += h) {
			AxisAlignedBB nextBox = box.offset(0, -flyHeight, 0);

			if (mc.theWorld.checkBlockCollision(nextBox)) {
				if (h < 0.0625)
					break;

				flyHeight -= h;
				h /= 2;
			}
		}
	}

	public void goToGround() {
		if (flyHeight > 320)
			return;

		double minY = mc.thePlayer.posY - flyHeight;

		if (minY <= 0)
			return;

		for (double y = mc.thePlayer.posY; y > minY;) {
			y -= 9.9;
			if (y < minY)
				y = minY;

			C04PacketPlayerPosition packet = new C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}

		for (double y = minY; y < mc.thePlayer.posY;) {
			y += 9.9;
			if (y > mc.thePlayer.posY)
				y = mc.thePlayer.posY;

			C04PacketPlayerPosition packet = new C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
			mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}
	
	
	
	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		timer3.reset();
		if (this.currentMode.equals("NCP")) {
			if (packetIn instanceof S12PacketEntityVelocity) {
				S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packetIn;
				Entity entity = mc.getNetHandler().clientWorldController.getEntityByID(velocityPacket.getEntityID());
				if (entity instanceof EntityPlayerSP) {
					entity.setVelocity(-(double) velocityPacket.getMotionX() / -5000.0D,
							-(double) velocityPacket.getMotionY() / -7000.0D,
							-(double) velocityPacket.getMotionZ() / -5000.0D);
					((S12PacketEntityVelocity) packetIn).cancel();
				}
			}
		}

		super.onPacketRecieved(packetIn);
	}
	public static void setMoveSpeed(PreMotionEvent event, double speed)
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
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);	
		if (currentMode.equals("Guardian-Fast")) {
			 
		       
			 if (mc.gameSettings.keyBindJump.pressed) {
	                this.setSpeed(0);
	                memepos = mc.thePlayer.posY;
	                mc.thePlayer.motionY = 1;
	                this.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
	         
			 }
			 if (mc.thePlayer.fallDistance > 4 && !mc.thePlayer.isSneaking()) {
				 sendPacket(new C03PacketPlayer(true));
				 mc.thePlayer.motionY = 0.8;
				 mc.thePlayer.fallDistance = 0;
				 this.setSpeed((float)3);
			 }
	            else {
	                this.setSpeed((float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
	            }
		        
		}
		if(currentMode.equals("NCP")) {
			mc.timer.timerSpeed = 0.3f;
			mc.thePlayer.speedInAir = 0.03f;
		}
		
		
        if(currentMode.equals("Spartan")) {
        	 BlockPos bp1 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ);
             Block b1 = this.mc.theWorld.getBlockState(bp1).getBlock();
             if (b1 == Blocks.air)
             {
               this.mc.thePlayer.motionY += 0.11D;
               this.mc.thePlayer.motionY = 0.2D;
             }
		}
		
		if(this.currentMode.equals("Guardian")) {
			mc.thePlayer.motionY = -0;
//			mc.thePlayer.motionY = -1.0E-2D - 0.066;
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
                for (double i = 0; i < 1.0E+0D; i += 0.5) {
                    mc.thePlayer.moveEntity(0, i, 0);
                }
            }
			if (mc.thePlayer.isSneaking()) {
                for (double i = 0; i < 1.0E+0D; i += 0.5) {
                    mc.thePlayer.moveEntity(0, -i, 0);
                }
            }
//            for (int i = 0; i < 20; i++) {
//                for (int i2 = 0; i2 < 20; i2++) {
//                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-4D,
//                            mc.thePlayer.posZ);
//                }
//                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-4D, mc.thePlayer.posZ);
//            }
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-4D, mc.thePlayer.posZ);
		}
		
		
		if(currentMode.equals("Hypixel")) {
if(mc.thePlayer.hurtResistantTime == 19){
	mc.thePlayer.motionY = 0;
	mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
}
			mc.thePlayer.motionY = 0;
			mc.thePlayer.onGround = true;
			
			
			
            
            
	
         
			 counter++;
			if (counter  == 1) {
				 mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0000000001, mc.thePlayer.posZ);
	                
	            } else if (counter == 2) {
	            	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000000001, mc.thePlayer.posZ);
	               
	                counter = 0;
	               
	                
	            }
	            mc.thePlayer.motionY = 0;
	            
		}
	
		
		
		
		
		if (currentMode.equalsIgnoreCase("Default")) {
			
			if (!ClientSettings.Flightsmooth) {
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionY = 0;
			if (ClientSettings.Flightsmooth) {
				mc.thePlayer.jumpMovementFactor = (float) (ClientSettings.FlightdefaultSpeed / 10);
			} else {
				mc.thePlayer.jumpMovementFactor = (float) (ClientSettings.FlightdefaultSpeed);
			}

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.thePlayer.motionY += ClientSettings.FlightdefaultSpeed / 2;
			}
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY += -ClientSettings.FlightdefaultSpeed / 2;
			}
		
			
			if (ClientSettings.flightkick) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				updateFlyHeight();
				if (flyHeight <= 290 && timer.hasTimeElapsed(500, true)
						|| flyHeight > 290 && timer.hasTimeElapsed(100, true)) {
					goToGround();
				}
			}
		}
		
		
		if(currentMode.equals("Mineplex")) {
			
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
			
			
            
            
	
            double[] dir = this.moveLooking(0);
            double xDir = dir[0];
            double zDir = dir[1];
            if(mc.thePlayer.isMoving()) {
            	double sped = 3;
            	
                mc.thePlayer.motionX = xDir * sped;
                mc.thePlayer.motionZ = zDir * sped;
            }
            if(mc.gameSettings.keyBindJump.pressed){
             mc.thePlayer.motionY += 0.5f;
             pos = mc.thePlayer.posY;
           }
            if(mc.gameSettings.keyBindSneak.pressed){
                mc.thePlayer.motionY -= 0.5f;
                mc.thePlayer.posY = pos;
            }
           
           
if(timer.hasTimeElapsed(2000, true)){
	
	mc.thePlayer.posY = pos;
	mc.thePlayer.motionY =-4000;
	mc.thePlayer.posY = pos;
                timer.reset();
            }


            
            
		
			
		}
}
		
		
    

	
	
	
	@Override
	public void onBasicUpdates() {
		if (damaging) {
			damaging = false;
		}

		if (currentMode.equals("AirWalk")) {
			if (mc.thePlayer.posY <= maxY) {
				mc.thePlayer.onGround = true;
				// mc.thePlayer.motionY = 0;
			} else {
				mc.thePlayer.onGround = false;
			}
		}
		super.onBasicUpdates();
	}
	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}
	@Override
	public String[] getModes() {
		return new String[] { "Default","Hypixel", "NCP", "Mineplex", "Cubecraft" , "Spartan", "Guardian", "Guardian-Fast"};
	}

	@Override
	public String getAddonText() {
		return currentMode;
	}

}