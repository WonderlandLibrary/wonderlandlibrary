package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.util.MoveUtil;
import epsilon.util.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;


public class Speed extends Module{
	float camerapitch = 0;
	int py;
	
	int catboystage;
	
	public NumberSetting vspeed = new NumberSetting("VanillaSpeed", 1.0f, 0.15f, 20.0f, 0.04f);
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "WatchdogHop","WatchdogTest", "StrictNCP"
			,"NCPYPort","Test","Watchdog", "MatrixHop","MatrixTimer","Verus", "VerusGround"
			,"MineLatino","MineLatinoOther","Spartan","Vulcan", "Kauri", "KauriSmooth", "BlocksMC"
			, "AGC","Zonecraft", "Redesky","GroundEvent", "PacketRandom", "Mospixel");
	public ModeSetting matrixbalance = new ModeSetting ("MatrixTimermode", "Infinite", "Infinite", "Semi-inf", "Fast", "ZOOOOOOM");
	public BooleanSetting strafeh = new BooleanSetting("StrafeHypixel", false);
	public BooleanSetting vulcant = new BooleanSetting("VulcanTimer", true);
	public BooleanSetting hawkdmg = new BooleanSetting("HawkDmgBoost", true);
	public BooleanSetting bmcbws = new BooleanSetting("BMCStrafeBW", false);
	public BooleanSetting vd = new BooleanSetting("DMGBoostVerus", true);
	public BooleanSetting vsf = new BooleanSetting("VerusStrafeFix", true);
	public NumberSetting vdb = new NumberSetting ("VerusDmgSpeed", 1, 0.3, 5, 0.1);
	public BooleanSetting j = new BooleanSetting("AutoJump", true);
	public BooleanSetting dohop = new BooleanSetting("LowHop", false);
	public NumberSetting hopy = new NumberSetting ("HopMotion", 0.2, 0.0, 2.0, 0.02);
	public BooleanSetting fall = new BooleanSetting("FastFall", false);
	public NumberSetting fallm = new NumberSetting ("FastFallMotion", 0.2, 0.0, 2.0, 0.02);
	public NumberSetting timerupspeed = new NumberSetting("TimerUpSpeed", 1, 0.1, 20, 0.1);
	public NumberSetting timerdownspeed= new NumberSetting("TimerUpDown", 1, 0.1, 20, 0.1);
	public Timer timer = new Timer();
	public MoveUtil move = new MoveUtil();
	boolean switchAstro = false;
	double yyy;
	double damoty = 0.06;
	double expectedX;
    double expectedY;
    double expectedZ;
    
	public Speed(){
		super("Speed", Keyboard.KEY_J, Category.MOVEMENT, "Increases the speed at which your character moves");
		this.addSettings(mode, vspeed, strafeh,matrixbalance, vulcant,hawkdmg,vd, vsf,vdb,bmcbws,j, dohop, hopy, timerupspeed, timerdownspeed);
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1;
		mc.thePlayer.speedInAir =  0.02F;
		damoty = 0.06;
		if(mode.getMode()=="VerusGround")
			move.SetMomentum(0, 0, 0);
		
	}
	public void onEnable() {
		catboystage =0;
		if(mode.getMode()=="VerusGround")
			move.SetMomentum(0, 0, 0);
		 mc.thePlayer.speedInAir = 0.02F;
		 
		 switch(mode.getMode()) {
		 
		 case "WatchdogHop":

				mc.thePlayer.speedInAir+=0.001;
				
			 break;
		 
		 }
		 
		boolean switchAstro = false;
	}	
	
    public void packetComedy(double h, double v) {
    	
    	//I lied, there are no packets, take off your clothes.
    	
    	double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

        expectedX = x + (-Math.sin(yaw) * h);
        expectedY = y + v;
        expectedZ = z + (Math.cos(yaw) * h);
        mc.thePlayer.setPosition(expectedX, expectedY, expectedZ);
        
    }
    
	
	
	public void onEvent(Event e){
		if(move.isMoving()) {
			if(e instanceof EventMotion){
				EventMotion event = (EventMotion)e;
				if(e.isPre()) {
					this.displayInfo = mode.getMode();
					switch(mode.getMode()) {
					
					case"WatchdogHop":
						
						if(mc.thePlayer.onGround) {
							camerapitch = 1.2f;
							yyy = mc.thePlayer.posY;
							mc.thePlayer.jump();
							move.strafe(move.getBaseMoveSpeed()+Math.random()/10+0.12);
							
							

							
						}
						
						else {
							
							
							if(mc.thePlayer.posY-1.24f>yyy && mc.thePlayer.fallDistance<=0) {
								mc.thePlayer.motionY=-0.1;
								camerapitch = 1f;
							}
							
							
						}

						mc.timer.timerSpeed = camerapitch;
						camerapitch+=0.0085;
						break;
					
					case "Redesky":
						
						if(mc.thePlayer.onGround) {
							yyy = mc.thePlayer.posY;
							mc.thePlayer.jump();
						}else {
							if(mc.thePlayer.fallDistance<0.2) {
								mc.thePlayer.motionY -= mc.thePlayer.fallDistance/15;
							}
						}
						
						break;
					
					case "Zonecraft":
						
						if(mc.thePlayer.onGround) {
							catboystage = 0;
							mc.thePlayer.jump();
							move.strafe(move.getBaseMoveSpeed()+0.12);
							yyy = mc.thePlayer.posY;
	
						}else {
							catboystage++;
							
							
							switch(catboystage) {
							
							case 1:
								move.strafe(move.getBaseMoveSpeed()+0.12);
								
								break;
								
							case 2:
								
								mc.thePlayer.motionX*=1.01;
								mc.thePlayer.motionZ*=1.01;
								
								break;
								
							case 7:
							case 8:
								
								mc.thePlayer.motionX*=1.1;
								mc.thePlayer.motionZ*=1.1;
								
								break;
								
								
							}
							
							if(mc.thePlayer.posY-1<yyy) {
								mc.thePlayer.motionY-=0.02;
							}
							
						}
						
						break;
					
					case "MatrixHop":
						
						
						if(mc.thePlayer.onGround) {
							yyy = mc.thePlayer.posY;
							mc.thePlayer.jump();
							mc.thePlayer.motionY-= 0.002;
							if(move.getSpeed()<0.4844 && move.getSpeed()>0.4)
								move.SetMomentum(mc.thePlayer.motionX*1.005, mc.thePlayer.motionY, mc.thePlayer.motionZ*1.005);
						}else {
							
							if(mc.thePlayer.fallDistance>0 && mc.thePlayer.fallDistance<1.24)
								mc.thePlayer.motionY -= 0.0003;
							
							if(mc.thePlayer.motionY>0.2 && mc.thePlayer.posY-0.2>yyy) {
								move.strafe();
							}
							
							if(mc.thePlayer.fallDistance<1.24) {
								if(mc.thePlayer.motionY<0.1)
									mc.thePlayer.motionY -= 0.003;
								
								if(mc.thePlayer.motionY>0.13)  {
									//move.strafe();
									mc.timer.timerSpeed = 1;
								}else {
									mc.timer.timerSpeed = (float) ( 1 + Math.random()/20);
								}
								if(mc.thePlayer.motionY<-0.45)
									mc.thePlayer.motionY = -0.476;	//mc.thePlayer.motionY = -0.4734427408367397 - Math.random()/100;
							}	
							
						}
						
						break;
						
					case "Mospixel":
						
						if(mc.thePlayer.onGround) {
							catboystage = 0;
							mc.thePlayer.jump();
							move.strafe(move.getBaseMoveSpeed()+0.2-(Math.random()/10));
							yyy = mc.thePlayer.posY;
	
							mc.timer.timerSpeed = 1.8f;
						}else {
							catboystage++;
							
							double rand = Math.random()/10;
							
							if(mc.thePlayer.fallDistance>0.5f) {
								mc.thePlayer.motionY-=0.05f;
							}
							
							if(mc.thePlayer.posY-1.2>yyy && mc.thePlayer.fallDistance<=0) {
								mc.thePlayer.motionY= -0.1;
							}
							
							switch(catboystage) {
							
							case 1:
								mc.timer.timerSpeed = 1.6f;
								
								move.strafe(move.getBaseMoveSpeed()+0.18-rand);
								
								break;
							case 2:
								
	
								move.strafe(move.getBaseMoveSpeed()+0.18-rand);
								
								
								break;
								
							case 3:

								mc.timer.timerSpeed = (float) (1.3f-rand);
	
								move.strafe(move.getBaseMoveSpeed()+0.15-rand);
								
								break;
								
							case 4:
	
								mc.timer.timerSpeed = 1.5f;
								move.strafe(move.getBaseMoveSpeed()+0.15-rand);
								
								break;
							case 5:

								mc.timer.timerSpeed = 1.4f;
	
								move.strafe(move.getBaseMoveSpeed()+0.14);
								
								break;
								
							case 6:
								
								mc.timer.timerSpeed = (float) (1.4f-rand);
								
								break;
								
							case 7:
								
	
								mc.timer.timerSpeed = 1.3f;
								break;
								
							case 8:
								mc.timer.timerSpeed = 1.3f;

								move.strafe(move.getBaseMoveSpeed()+0.11-rand);
								
								break;
								
							case 9:
								
	
								move.strafe(move.getBaseMoveSpeed()+0.11-rand);
								mc.timer.timerSpeed = 1.2f;
								break;
								
							case 10:
							case 11:
								move.strafe(move.getBaseMoveSpeed()+0.01);
								break;
							}
							
							move.strafe();
							
						}
						
						break;
						
					case "WatchdogTest":
						
						if(mc.thePlayer.motionY<0) {
							mc.timer.timerSpeed = 1.3f;
						}else {
							mc.timer.timerSpeed = 1;
						}
	
						if(mc.thePlayer.motionY<0.42f && mc.thePlayer.motionY>-0.2) {
							mc.thePlayer.motionY -= 0.003;
							mc.timer.timerSpeed = 1.1f;
							
						}else
							mc.timer.timerSpeed = 1;
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.motionY = 0.4f;
							yyy = mc.thePlayer.posY;
							move.strafe(move.getBaseMoveSpeed()+0.15);
						}
						
						if(move.getSpeed()< 0.32)
							move.strafe(move.getSpeed()+0.0005);
						if(move.getSpeed()<0.3)
							move.strafe(move.getSpeed()+0.001);
							
						
						move.strafe();
						
						break;
						
	
					case "GroundEvent":
					case "NCPYPort":
						
						mc.thePlayer.jumpMovementFactor = 0;
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							if(move.getSpeed()<0.3)
							move.strafe(0.4089793270387926);
							if(move.getSpeed()>0.5 && move.getSpeed()<0.6)
								move.strafe(move.getSpeed()+0.08);
							
						}	else if(mc.thePlayer.fallDistance<1.5){
							mc.thePlayer.motionY -= 4;
						}else
							mc.thePlayer.motionY = -0.42;
						
						
						break;
						
					case "VerusGround":
						
						if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir) && !mc.gameSettings.keyBindJump.getIsKeyPressed()) {
							mc.thePlayer.motionY=0;
							if (mc.thePlayer.ticksExisted%10 ==0) {
								event.setOnGround(false);
								move.SetMomentum(0, 0, 0);
								mc.thePlayer.moveForward = 0;
								mc.thePlayer.jumpMovementFactor = 0;
							}else {
								if(mc.thePlayer.isUsingItem())
									move.strafe(move.getBaseMoveSpeed() + 0.23);
								else
									move.strafe(move.getBaseMoveSpeed() + 0.3);
								event.setOnGround(true);
								
							}
						}
						
						//Theres a way to do this thats not y = 0 using C08 but i cba to do that rn.
						
						break;
						
					case "AGC":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							mc.thePlayer.motionX *= 1.05;mc.thePlayer.motionY *= 1.05;
								
							
							
						}
						if(mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<15) {
							move.strafe(move.getBaseMoveSpeed() + 0.2);
						}
						move.strafe();
						
						break;
						
					case "MineLatinoOther":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.speedInAir =  0.02F;
							mc.thePlayer.jump();
							yyy = mc.thePlayer.posY;
							
						}else {
							mc.thePlayer.speedInAir =  0.022F;
							if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) 
								mc.thePlayer.speedInAir =  0.03F;
		                    else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) 
		                    	mc.thePlayer.speedInAir =  0.04F;
		                    
						}
						
						break;
						
					case "MineLatino":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							mc.thePlayer.motionY-=0.02f;
							move.strafe(move.getSpeed()+0.03);
						}
						
						break;
						
					case "Verus":
						
						if(mc.thePlayer.onGround) {
							yyy = mc.thePlayer.posY;
							mc.thePlayer.jump();
							if(move.getSpeed()<move.getBaseMoveSpeed()+0.20)
							move.strafe(move.getSpeed()+(Math.random()/10)+0.1);
						}else {
							move.strafe(move.getBaseMoveSpeed()+(0.08-Math.random()/20));
							if(mc.thePlayer.posY-0.8>yyy && mc.thePlayer.fallDistance<=0) {

								move.place(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
										new ItemStack(Blocks.stone.getItem
											(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
								event.setOnGround(true);

								event.setY(Math.round(mc.thePlayer.posY));
								move.strafe(move.getSpeed()+0.26);
								mc.thePlayer.motionY =-0.1;
								if(mc.thePlayer.hurtTime>0) {
									move.strafe(move.getSpeed()+mc.thePlayer.hurtTime/2);
								}
							}else
							
							if(mc.thePlayer.ticksExisted%2==0) {

								move.place(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
										new ItemStack(Blocks.stone.getItem
											(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
								event.setOnGround(true);
								event.setY(Math.round(mc.thePlayer.posY));
								move.strafe(move.getSpeed()+0.20);
							}
							
							if(mc.gameSettings.keyBindBack.getIsKeyPressed()) {
								mc.thePlayer.motionX*=0.5f;
								mc.thePlayer.motionZ*=0.5f;
							}else if (mc.gameSettings.keyBindLeft.getIsKeyPressed() || mc.gameSettings.keyBindRight.getIsKeyPressed()) {

								mc.thePlayer.motionX*=0.7f;
								mc.thePlayer.motionZ*=0.7f;
							}
							
						}
						move.strafe();
						
						break;
						
					case "OldVerus":
						
						//Old verus wasnt affected by c08 so you cant do lowhop
						
						break;
						
					case "Spartan":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							yyy = mc.thePlayer.posY;
							
							if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) {
		                    	move.setMoveSpeed(move.getBaseMoveSpeed() + 0.35);
		                    } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) {
		                    	move.strafe(move.getBaseMoveSpeed() + 0.3);
		                    } else {
		                       move.setMoveSpeed(0.48);
		                    }
						}else {
							if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) {
		                    	move.setMoveSpeed(move.getBaseMoveSpeed() + 0.11);
		                    } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) {
		                    	move.strafe(move.getBaseMoveSpeed() + 0.05);
		                    } else {
		                       move.strafe();
		                    }
							if(mc.thePlayer.posY-1.24>yyy) 
								mc.thePlayer.motionY = -0.2;
							
							if(yyy+0.2>mc.thePlayer.posY) {
								if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) {
			                    	move.setMoveSpeed(move.getBaseMoveSpeed() + 0.11);
			                    } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) {
			                    	move.strafe(move.getBaseMoveSpeed() + 0.05);
			                    } else {
			                    	move.setMoveSpeed(0.3);
			                    }
							}
						}
						if(mc.thePlayer.fallDistance>0 && mc.thePlayer.motionY< -0.2) {
							mc.thePlayer.motionY -= 0.05;
						}
						
	                    
						
						break;
						
					case "StrictNCP":
						
						move.strafe();
						
						if(mc.thePlayer.onGround) {
							mc.timer.timerSpeed = (float) 1.2;
							mc.thePlayer.jump();
							yyy = mc.thePlayer.posY;
							
							if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) {
		                    	move.setMoveSpeed(move.getBaseMoveSpeed() + 0.31);
		                    } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) {
		                    	move.strafe(move.getBaseMoveSpeed() + 0.25);
		                    } else {
		                       move.setMoveSpeed(0.48);
		                    }
						}else{
							if(mc.thePlayer.fallDistance<1.24) {
								if(mc.thePlayer.fallDistance>0) {
									mc.timer.timerSpeed=(float) 1;
									mc.thePlayer.motionY -= 0.0045;
								}else {
								}
								if(mc.thePlayer.posY-1.05>yyy) {
									mc.timer.timerSpeed = (float) 1.05;
									mc.thePlayer.motionY -= 0.18;
								}else if (mc.thePlayer.posY-0.9>yyy){
									mc.thePlayer.motionY -= 0.0009;
								}
								if(mc.thePlayer.posY-0.6<yyy) {
									mc.thePlayer.motionY -= 0.005;
								}
							}
						}
						
						break;
						
					case "Vanilla":
						
						if(mc.thePlayer.onGround) {
							if(dohop.isEnabled())
								mc.thePlayer.motionY = hopy.getValue();
							else
								mc.thePlayer.jump();
						}
						
						if(mc.thePlayer.fallDistance > 0) {
							mc.timer.timerSpeed = (float) timerdownspeed.getValue();
							if(fall.isEnabled())
								mc.thePlayer.motionY = -fallm.getValue();
						
						}else 
							mc.timer.timerSpeed = (float) timerupspeed.getValue();
						
						move.strafe(vspeed.getValue());
						
						
						break;
						
					case "BlocksMC":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							move.strafe(move.getBaseMoveSpeed()+0.1);
						}else if(bmcbws.isEnabled()){
							move.strafe();
						}
						if(mc.thePlayer.fallDistance>0) {
							
						}else {
							mc.timer.timerSpeed =1;
						}
						
						break;
						
					case "KauriSmooth":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							mc.thePlayer.motionY = 0.3;
							mc.thePlayer.cameraPitch = camerapitch;
						}
						if(mc.thePlayer.fallDistance>0 && mc.thePlayer.motionY < -0.3) {
							mc.thePlayer.motionY = -0.9;
							move.setMoveSpeed(0.283);
						}
						if(!mc.thePlayer.onGround) {
							if(mc.gameSettings.keyBindRight.getIsKeyPressed() || mc.gameSettings.keyBindLeft.getIsKeyPressed() || camerapitch != mc.thePlayer.cameraPitch) {
								move.setMoveSpeed(0.28);
								camerapitch = mc.thePlayer.cameraPitch;
							}
						}
						
						break;
						
					case "Kauri":
						
						if(mc.thePlayer.onGround) {
							yyy = mc.thePlayer.posY;
							mc.thePlayer.jump();
							move.setMoveSpeed(0.8);
						}else {
							//move.setMoveSpeed(0.28);
							if(mc.thePlayer.fallDistance>0) {
								mc.thePlayer.motionY = -1000;
							}
							if(mc.thePlayer.posY-0.01>yyy && mc.thePlayer.fallDistance<=0) {
								mc.thePlayer.motionY = -0.3;
								
							}
						}
						
						break;
						
					case "Vulcan":
						
						if(mc.thePlayer.onGround) {
							
							yyy = mc.thePlayer.posY;
							mc.thePlayer.jump();
							move.setMoveSpeed(move.getBaseMoveSpeed() + 0.1917);
							
						}else {
							if(mc.thePlayer.posY-1<yyy && mc.thePlayer.fallDistance<0)
								move.strafe();
							
							if(mc.thePlayer.posY-0.33<yyy && mc.thePlayer.fallDistance<0) {
								move.setMoveSpeed(0.48);
							}
							
							if(mc.thePlayer.posY-1.21>yyy) {
								mc.thePlayer.motionY -=0.1;
							}
							if(mc.thePlayer.fallDistance>0 &&mc.thePlayer.fallDistance<1.3 && !(mc.thePlayer.posY-1.24>yyy)) {
								mc.timer.timerSpeed=1;
								mc.thePlayer.motionY =-0.41;
							}else if (mc.thePlayer.fallDistance>1.3) {
								mc.thePlayer.motionY =-0.7;
							}
						}
						if(mc.thePlayer.fallDistance>0 && mc.thePlayer.fallDistance<0.88) 
							mc.timer.timerSpeed=(float) 1.1;
						else
							mc.timer.timerSpeed=1;
						if(mc.thePlayer.fallDistance<0) 
							move.strafe();
						
						break;
						
					case "MatrixTimer":
						
						if(mc.thePlayer.fallDistance>0) {
							mc.timer.timerSpeed = 10000;
						}else if(mc.thePlayer.onGround){
							if(matrixbalance.getMode() == "Infinite") {
								mc.timer.timerSpeed = (float) 0.12;
							}else if(matrixbalance.getMode() == "Semi-inf"){
								mc.timer.timerSpeed = (float) 0.2;
							}else if(matrixbalance.getMode() == "Fast") {
								mc.timer.timerSpeed = (float) 0.5;
							}else if (matrixbalance.getMode() == "ZOOOOOOM") {
								mc.timer.timerSpeed = (float) 0.85;
							}
							if(timer.hasTimeElapsed(275, true)) {
								mc.thePlayer.jump();
							}
						}
						
						break;
						
						
					case "Watchdog":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							yyy = mc.thePlayer.posY;
						}
						
						if(mc.thePlayer.fallDistance>0)
							mc.thePlayer.motionY -= 0.001;
						
						else if(mc.thePlayer.posY-1.1>yyy) {
							mc.thePlayer.motionY = -0.06f;
						}
						
						if(mc.thePlayer.ticksExisted%4==0) {
							mc.timer.timerSpeed = 1.3f;
						}else 
							mc.timer.timerSpeed = 1;
	
						move.strafe(move.getSpeed() + 0.007);
						
						break;
						
					}
					
					
					
					
					
				}	
			}
		}
	}
	

   
}