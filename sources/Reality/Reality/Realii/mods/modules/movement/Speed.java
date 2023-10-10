
package Reality.Realii.mods.modules.movement;

import Reality.Realii.Client;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import  Reality.Realii.mods.modules.movement.LagBackChecker;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.player.Motion;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;

public class Speed
        extends Module {
	private Mode Watchdog = new Mode("WatchdogMode", "WatchdogMode", new String[]{"Hvh","Normal","Smart","Safe"}, "Normal");
    private Mode mode = new Mode("Mode", "mode", new String[]{"Bhop","BMC","InvadedFast","Hypixel","Invaded","OldNcp","VulcanLow","NoRule","HypixelNew","Hylex", "WatchdogBhop" ,"Lowhop", "Ground" ,"Vulcan","VulcanNew","SlowLowhop" ,"SlowBhop", "New", "VulcanYport", "VerusBhop","HycraftBhop", "Intave","Ncp", "BlocksMc", "VerusLowHop","BanHop","MushMc","VeruFast"}, "WatchdogBhop");
    public static Numbers<Number>  speed2 = new Numbers<>("MotionsSpeed", 0.1f, 0.1f, 30f, 10f);
    private double movementSpeed;
    private double distance;
    private TimerUtil timer = new TimerUtil();
	private float setMoveSpeednonstrafe;
	 private boolean resetMotion;
	  private double speed;

    public Speed() {
        super("Speed", ModuleType.Movement);
        this.addValues(this.mode, speed2,Watchdog);
    }
    
    
    @EventHandler
    public void onPacket(EventPacketRecieve e) {
    
		if (e.getPacket() instanceof S08PacketPlayerPosLook && Client.instance.getModuleManager().getModuleByClass(LagBackChecker.class).isEnabled()) {
        	  this.setEnabled(false);
        		 NotificationsManager.addNotification(new Notification("You just got laggedbacked disabled some stuff", Notification.Type.Alert,3));
        }
        
   
    }
    
    @Override
    public void onDisable() {
    	if (this.mode.getValue().equals("Hypixel")) {
    	  mc.gameSettings.keyBindJump.pressed = false;
    	}
    	
    	if (this.mode.getValue().equals("Hylex")) {
    		  mc.timer.timerSpeed = 1.0f;
      	  mc.gameSettings.keyBindJump.pressed = false;
      	}
    	if (this.mode.getValue().equals("VulcanNew")) {
  		  mc.timer.timerSpeed = 1.0f;
    	  mc.gameSettings.keyBindJump.pressed = false;
    	}
    	
    	if (this.mode.getValue().equals("HypixelNew")) {
      	  mc.gameSettings.keyBindJump.pressed = false;
      	}
      mc.timer.timerSpeed = 1.0f;
    }
    @Override
    public void onEnable() {
    	if (this.mode.getValue().equals("VeruFast")) {
    		
    			mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    			
    			Helper.sendMessage("Bypass");
    			 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
    		     mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
    		     mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    			
    	}
    }
    

    private boolean canZoom() {
        if (this.mc.thePlayer.moving() && mc.thePlayer.onGround) {
            return true;
        }
        return false;
    }
    @EventHandler
    private void onPacketSend(EventPacketSend e) {
    	if (this.mode.getValue().equals("BMC")) {
    		 if (e.getPacket() instanceof C0BPacketEntityAction) {
                 e.setCancelled(true);
               
   	       }
    	}
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    	
    	if (this.mode.getValue().equals("BMC")) {
    		if (!PlayerUtils.isMoving()) {
                e.setX(e.getPitch() + (Math.random() - 0.5) / 100);
                e.setZ(e.getPitch() + (Math.random() - 0.5) / 100);
			}

			mc.thePlayer.sendQueue.addToSendQueue(
					new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
		}

		if (ArrayList2.Sufix.getValue().equals("On")) {

			this.setSuffix(this.mode.getModeAsString());
		}

		if (ArrayList2.Sufix.getValue().equals("Off")) {

			this.setSuffix("");
		}
	}

	@EventHandler
	private void onMove(EventMove e) {
		if (this.mode.getValue().equals("OldNcp")) {
			movementSpeed = RandomUtils.nextDouble(0.30, 0.40);
			if(mc.thePlayer.onGround) {
				movementSpeed = 0.50f;
			}
			
			if (this.canZoom()) {
				
				
				e.setY(mc.thePlayer.motionY = 0.41999998688697815F);

			}
			
			if(mc.thePlayer.motionY > 0.40f) {
				
				//mc.thePlayer.motionY = -2;
			}
			 
		}
		if (this.mode.getValue().equals("VulcanLow")) {
			if (mc.thePlayer.onGround) {
				double speed3 = Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
				this.movementSpeed = 0.42;
				mc.thePlayer.onGround = true;
				e.setY(e.getY() + movementSpeed);

			}
			
		}
		if (this.mode.getValue().equals("BMC")) {

			if (mc.thePlayer.ticksExisted % 2 == 0) {
				mc.timer.timerSpeed = (float)MathHelper.getRandomDoubleInRange(new Random(),1.1,1.2);
			}
			if (mc.thePlayer.ticksExisted % 4 == 0) {
				mc.timer.timerSpeed = 0.95f;
			}



			double threshold = 0.1;

			float min = 0.30f; // minimum value
			// 0.30
			float max = 0.33f; // maximum value

			float randomMotion = min + new Random().nextFloat() * (max - min);

			if (mc.thePlayer.onGround) {
				this.movementSpeed = 0.60;
			} else{
				this.movementSpeed = Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);

				this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed() + 0.02);
			}
			if (this.canZoom()) {

				 e.setY(mc.thePlayer.motionY = 0.40);
			//	e.setY(mc.thePlayer.motionY = 0.41999998688697815F);

			}




			
			// this.movementSpeed = 0.30;

			// 029
			// 0.30
			//if (mc.thePlayer.isSwingInProgress && Killaura.target != null) {
				//this.movementSpeed = randomMotion;
			//}
			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

			// Math.max(speed, base), Math.random() / 2000

		}
		if (this.mode.getValue().equals("NoRule")) {

			// if(!mc.thePlayer.onGround) {
			// if (mc.thePlayer.fallDistance > 2) {

			// mc.thePlayer.fallDistance = 0;
			// }
			// if (mc.thePlayer.ticksExisted % 3 != 0) {

			// mc.thePlayer.motionY = -0.0972;

			// }
			// else {
			// mc.thePlayer.motionY += 0.016;
//    	            / mc.thePlayer.motionY += 0.026;
			// }
			// }

			this.movementSpeed = 0.45f;
			// mc.timer.timerSpeed = 1.3f;
			// mc.timer.timerSpeed = 1.314f;
			// e.setY(mc.thePlayer.motionY = 0.41999998688697815F);

			if (this.canZoom()) {

				e.setY(mc.thePlayer.motionY = 0.30F);

				e.setY(mc.thePlayer.motionY = 0.41999998688697815F);

				this.movementSpeed = 0.70f;

			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
		}
		

		if (this.mode.getValue().equals("Hylex")) {
		
		
			if(mc.thePlayer.onGround && PlayerUtils.isMoving()) {
			 mc.thePlayer.setSpeed(0.52);

			 }

			if (mc.thePlayer.hurtTime > 1) {
				// mc.timer.timerSpeed = RandomUtils.nextFloat(1.1f, 1.18f);
				// mc.thePlayer.setSpeed(0.16f);

				// maybe use timer boost idk

			} // else {
				// mc.timer.timerSpeed = 1.0f;
				// }

			if (mc.thePlayer.ticksExisted % 5 == 0) {
				mc.timer.timerSpeed = 1.0f;
			}

			if (!PlayerUtils.isMoving() && mc.thePlayer.onGround) {

				mc.gameSettings.keyBindJump.pressed = false;
			}
			if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
				mc.gameSettings.keyBindJump.pressed = true;
			}
		}
		if (this.mode.getValue().equals("Hypixel")) {

			if (mc.thePlayer.hurtTime > 1) {
				this.movementSpeed = 0.35f;
				// 39
				// or higher
			}
			if (!PlayerUtils.isMoving() && mc.thePlayer.onGround) {

				mc.gameSettings.keyBindJump.pressed = false;
			}
			if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
				mc.gameSettings.keyBindJump.pressed = true;
			}
		}

		if (this.mode.getValue().equals("HypixelNew")) {

			if (mc.thePlayer.hurtTime > 1) {
				this.movementSpeed = 0.43f;

				// 0.43
				mc.gameSettings.keyBindJump.pressed = true;
				// or mayber meove this
			}

			// or use = null

			if (mc.thePlayer.onGround && Killaura.target != null
					&& Client.instance.getModuleManager().getModuleByClass(Killaura.class).isEnabled()
					&& Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()) {
				this.movementSpeed = 0.28f;
				// or
				// 27
				// OR higher
			}

			if (Killaura.target != null
					&& Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()) {
				mc.gameSettings.keyBindJump.pressed = false;
			}
			// OR if
			// (!Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()){
			// Maybe deltete this idk
			if (!Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()) {
				if (!PlayerUtils.isMoving() && mc.thePlayer.onGround) {

					mc.gameSettings.keyBindJump.pressed = false;
				}
				if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
					mc.gameSettings.keyBindJump.pressed = true;
				}
			}

			if (Killaura.target == null) {
				if (!PlayerUtils.isMoving() && mc.thePlayer.onGround) {

					mc.gameSettings.keyBindJump.pressed = false;
				}
				if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
					mc.gameSettings.keyBindJump.pressed = true;
				}
			}
		}
		if (this.mode.getValue().equals("VulcanNew")) {

			// e.setY(mc.thePlayer.motionY = 0.41999998688697815F);

			if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
				if (mc.thePlayer.onGround) {
					// 0.35
					mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.60) : 0);
				}

			}

			if (!PlayerUtils.isMoving() && mc.thePlayer.onGround) {

				mc.gameSettings.keyBindJump.pressed = false;
			}
			if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
				mc.gameSettings.keyBindJump.pressed = true;
			}

		}

		if (this.mode.getValue().equals("SlowLowHop")) {

		}

		if (this.mode.getValue().equals("New")) {
			if (mc.thePlayer.onGround) {
				mc.thePlayer.jump();
			}

		}
		if (this.mode.getValue().equals("WatchdogBhop")) {
			if (this.Watchdog.getValue().equals("Smart")) {
				if (this.canZoom()) {
					mc.thePlayer.moveStrafing = 0.f;
					e.setY(mc.thePlayer.motionY = 0.42F);

					this.movementSpeed = 0.30f;

					if (mc.thePlayer.hurtTime > 2) {
						this.movementSpeed = 0.55f;

					}

					if (mc.thePlayer.hurtTime > 4) {
						this.movementSpeed = 0.80f;

					}

				}
			}

			if (this.Watchdog.getValue().equals("Safe")) {
				if (this.canZoom()) {
					mc.thePlayer.moveStrafing = 0.f;
					e.setY(mc.thePlayer.motionY = 0.42F);

					this.movementSpeed = 0.30f;

					if (mc.thePlayer.hurtTime > 2) {
						this.movementSpeed = 0.55f;

					}

				}
			}

			if (this.Watchdog.getValue().equals("Normal")) {
				if (this.canZoom()) {
					mc.thePlayer.moveStrafing = 0.f;
					e.setY(mc.thePlayer.motionY = 0.42F);

					this.movementSpeed = 0.30f;

				}
			}

			if (this.Watchdog.getValue().equals("Hvh")) {
				if (this.canZoom()) {
					mc.thePlayer.moveStrafing = 0.f;
					e.setY(mc.thePlayer.motionY = 0.42F);

					this.movementSpeed = 0.30f;

					if (mc.thePlayer.hurtTime > 2) {
						this.movementSpeed = 0.80f;

					}

				}
			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		} else if (this.mode.getValue().equals("VulcanYport")) {
			this.movementSpeed = 0.28f;

			if (mc.thePlayer.motionY < 0.4) {

				mc.timer.timerSpeed = 1.0f;
			}

			if (mc.thePlayer.motionY < 0.0) {

				mc.timer.timerSpeed = 1.0f;

			}
			if (mc.thePlayer.motionY < -0.3) {
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();

				}

				mc.timer.timerSpeed = 1.55f;
			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
		} else if (this.mode.getValue().equals("Bhop")) {
			if (this.canZoom()) {
				e.setY(mc.thePlayer.motionY = 0.42F);

				this.movementSpeed = speed2.getValue().doubleValue();
			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		} else if (this.mode.getValue().equals("Lowhop")) {
			if (this.canZoom()) {
				e.setY(mc.thePlayer.motionY = 0.25F);
				this.movementSpeed = speed2.getValue().doubleValue();
			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		} else if (this.mode.getValue().equals("Ground")) {
			if (this.canZoom()) {

				this.movementSpeed = speed2.getValue().doubleValue();
			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		} else if (this.mode.getValue().equals("VerusBhop")) {
			if (this.canZoom()) {
				e.setY(mc.thePlayer.motionY = 0.40F);
				this.movementSpeed = 0.36f;
			}
			// if (mc.thePlayer.hurtTime > 3) {
			// this.movementSpeed = 0.58f;

			// }

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		}else if (this.mode.getValue().equals("Invaded")) {
			if (this.canZoom()) {
				
				
				e.setY(mc.thePlayer.motionY = 0.41999998688697815F);
				 
				
		   		
				
			}
			
		
			 if (mc.thePlayer.hurtTime > 1) {
				 this.movementSpeed = 0.80f;

			 } else {
					this.movementSpeed = 0.33f;
			
			 }
			
			
			

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
		
		
		} else if (this.mode.getValue().equals("InvadedFast")) {


			if (mc.thePlayer.ticksExisted % 10 == 0) {
				this.movementSpeed = 1f;

			}
			if (mc.thePlayer.ticksExisted % 17 == 0) {
				this.movementSpeed = 0.10f;
			}









			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);


		} else if (this.mode.getValue().equals("HycraftBhop")) {
			if (this.canZoom()) {
				mc.thePlayer.moveStrafing = 0.5f;

				if (mc.timer.timerSpeed == 10)
					;
				mc.thePlayer.motionY = -0.3;
				System.out.println("FALLING " + mc.thePlayer.motionY);
				System.out.println("RESETTING TIMER " + mc.thePlayer.motionY);
				mc.timer.timerSpeed = 1.0f;
				this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

			}

		} else if (this.mode.getValue().equals("Intave")) {
			if (this.canZoom()) {
				//mc.thePlayer.moveStrafing = 0.5f;
			//	e.setY(mc.thePlayer.motionY = 0.41F);

			}

			if (!PlayerUtils.isMoving() && mc.thePlayer.onGround) {

				mc.gameSettings.keyBindJump.pressed = false;
			}
			if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
				mc.gameSettings.keyBindJump.pressed = true;
			}
			//mc.thePlayer.moveStrafing = 1.0f;
			//if(!mc.thePlayer.onGround) {
			//0.4 or 0.2
				if (mc.thePlayer.fallDistance > 0.4) {
			//	if(mc.thePlayer.motionY < 0.40) {
					//this.movementSpeed = 0.20f;

					this.mc.thePlayer.setSpeed(0.20);
				}

		//	if (mc.thePlayer.fallDistance > 0.1) {
				//this.mc.thePlayer.setSpeed(0.19);
		//	}
			//this.mc.thePlayer.setSpeed(0.20);
		//	if(mc.thePlayer.motionY < 0.35) {
				//this.mc.thePlayer.setSpeed
				//this.mc.thePlayer.setSpeed(0.18);
		//	}

				if(mc.thePlayer.onGround) {
					//this.mc.thePlayer.setSpeed(0.15);
				}

			//}



		} else if (this.mode.getValue().equals("BlocksMc")) {

			if (this.canZoom()) {

				e.setY(mc.thePlayer.motionY = 0.41F);
				this.movementSpeed = 0.32f;

			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		} else if (this.mode.getValue().equals("BanHop")) {
			if (this.canZoom()) {
				e.setY(mc.thePlayer.motionY = 0.40F);
				// OR 30-35

				this.movementSpeed = 0.38;
				// 36

			}
			// if(mc.thePlayer.motionY < 0.18F) {
			// e.setY(mc.thePlayer.motionY = -0.9F);
			// mc.timer.timerSpeed = 1.1f;
			// }
			// if(mc.thePlayer.isCollidedvERTICALY) {
			if (mc.thePlayer.isCollidedHorizontally) {

			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
		} else if (this.mode.getValue().equals("MushMc")) {
			if (!mc.thePlayer.isCollidedHorizontally) {
				if (this.canZoom()) {
					e.setY(mc.thePlayer.motionY = 0.40F);
					// OR 30-35
					this.movementSpeed = 0.45f;

				}
				if (mc.thePlayer.motionY < 0.35F) {
					e.setY(mc.thePlayer.motionY = -0.15F);
					// mc.timer.timerSpeed = 1.1f;
				}
			}
			// if(mc.thePlayer.isCollidedvERTICALY) {
			if (mc.thePlayer.isCollidedHorizontally) {
				e.setY(mc.thePlayer.motionY = 0.40F);

			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
		} else if (this.mode.getValue().equals("Ncp")) {
			if (this.canZoom()) {

				e.setY(mc.thePlayer.motionY = 0.41999998688697815F);
				this.movementSpeed = 0.57f;
			} else {
				this.movementSpeed = Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
			}
			this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		} else if (this.mode.getValue().equals("VeruFast")) {
			if (this.canZoom()) {

				e.setY(mc.thePlayer.motionY = 0.42F);
				this.movementSpeed = 0.70f;

			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		}

		else if (this.mode.getValue().equals("Vulcan")) {
			
			if (PlayerUtils.isMoving() && mc.thePlayer.onGround) {
				if (mc.thePlayer.onGround) {
					// 0.35
					mc.thePlayer.setSpeed(PlayerUtils.isMoving() ? (0.60) : 0);
				}

			}
			//if (mc.thePlayer.ticksExisted % 5 == 0) {
			//if(mc.thePlayer.motionY > 0.30) {
			//	mc.thePlayer.motionY = -0.05f;
			//}
			//}
			if (this.canZoom()) {
				
				
					e.setY(mc.thePlayer.motionY = 0.41999998688697815F);
					

			} else {
				this.movementSpeed = Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
			}
			this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
		}

		else if (this.mode.getValue().equals("VerusLowHop")) {
			if (this.canZoom()) {
				e.setY(mc.thePlayer.motionY = 0.30F);
				this.movementSpeed = 0.34f;
			}

			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);

		}

		if (this.mode.getValue().equals("Hypixel")) {
			if (mc.thePlayer.hurtTime > 1) {
				this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
				((TargetStrafe) ModuleManager.getModuleByClass(TargetStrafe.class)).strafe(e, movementSpeed);
			}
		} else if (this.mode.getValue().equals("HypixelNew")) {
			if (mc.thePlayer.hurtTime > 1) {
				this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
				((TargetStrafe) ModuleManager.getModuleByClass(TargetStrafe.class)).strafe(e, movementSpeed);
			}
			if (mc.thePlayer.onGround && Killaura.target != null
					&& Client.instance.getModuleManager().getModuleByClass(Killaura.class).isEnabled()
					&& Client.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()) {
				this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
				((TargetStrafe) ModuleManager.getModuleByClass(TargetStrafe.class)).strafe(e, movementSpeed);
			}
		} else if (this.mode.getValue().equals("Hylex")) {

		} else if (this.mode.getValue().equals("VulcanNew")) {

		} else if (this.mode.getValue().equals("Intave")) {

		}else {
			this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
			((TargetStrafe) ModuleManager.getModuleByClass(TargetStrafe.class)).strafe(e, movementSpeed);
		}
	}
}
