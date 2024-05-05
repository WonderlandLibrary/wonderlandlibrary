package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import epsilon.util.S08;
import epsilon.util.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class LongJump extends Module{
	
	private boolean flagged, hasFallen, matrixFreeze, matrixBoost;
	private int matrixTicks;
	
	boolean repeatma;
	private int getPearlSlot() {
        for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemEnderPearl) {
                return i - 36;
            }
        }
        return -1;
    }
	public Timer timer = new Timer();
	
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Hypixel","BlocksMC","Verus","VerusDamage","Matrix", "Spartan","NCP", "NCPTickDash","Vulcan", "Zonecraft", "Mospixel");
	public BooleanSetting autodisable = new BooleanSetting ("AutoDisable", true);
	
	double y, lastX, lastY, lastZ;
	double matrixxstart = 0;
	boolean matrixxcontinue = false;
	boolean matrixxdoitagain = false;
	boolean holdm = false;
	boolean repeatspartandmg = false;
	boolean nextncp = false;
	boolean nextspartanstep = false;
	boolean disablenext = false;

	double speed = 0;
	double beforeSpoofY = 0;
	MoveUtil move = new MoveUtil();
	double stophere = 0;
	MoveUtil moveUtil = new MoveUtil();
	public LongJump(){
		super("LongJump", Keyboard.KEY_X, Category.MOVEMENT, "Long, jump");
		this.addSettings(mode,autodisable);
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1;
		mc.thePlayer.speedInAir = 0.02f;
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionZ = 0;
	}
	
	public void onEnable() {
		flagged = matrixFreeze = matrixBoost = false;
		mc.timer.timerSpeed = 1;
		nextncp = false;
		disablenext = false;
		
		if(mode.getMode()=="Verus") {
			speed = 0.1;
			move.place(new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
		}
		
		switch(mode.getMode()) {
		
			
		case "VerusDamage":
			
			move.verusDamage();
			
			break;
			
		case "Matrix":
			
			if(mc.thePlayer.onGround) {
				mc.thePlayer.jump();
				mc.thePlayer.motionY+=0.1;
			}
			
			break;
		}
	
		
		
		if(mode.getMode() == "Spartan" || mode.getMode() == "Vulcan" /*|| mode.getMode()=="Hypixel"*/) {
			matrixxstart = mc.thePlayer.posY;
			mc.thePlayer.jump();
			matrixxcontinue = true;
			matrixxdoitagain = false;
			holdm = true;
			
		}
		
		if(mode.getMode() == "SpartanDamage") {
			//mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ, false));
            //mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			move.damagePreservePacket();
			repeatspartandmg = true;
			nextspartanstep = false;
			stophere = mc.thePlayer.posY;
		}
		int enderPearlSlot = getPearlSlot();
		y = mc.thePlayer.posY;
		
			
		speed = 0;
	}
	
	public void onEvent(Event e){
		if(mc.thePlayer != null) {
			float yaw = (float) (mc.thePlayer.rotationYaw * (Math.PI / 180));
			
			if(e instanceof EventSendPacket) {

				Packet p = e.getPacket();
				switch(mode.getMode()) {
				
				case "Matrix":
					
					if(matrixFreeze && p instanceof C03PacketPlayer)
						e.setCancelled();
					
					break;
				}
			}
			
			if(e instanceof EventReceivePacket) {

				Packet p = e.getPacket();
				
				switch(mode.getMode()) {
				
				case "Matrix":
					
					
					
					break;
					
					
				}
				
			}
			
			if(e instanceof EventUpdate){

    			this.displayInfo = mode.getMode();
    			
				switch(mode.getMode()) {
				
				
				case "NCPTickDash":
					move.strafe();
					if(mc.thePlayer.onGround && !flagged) {
						move.strafe(move.getBaseMoveSpeed() + 0.6 - (Math.random()/10));
						mc.thePlayer.motionY = 0.42f;
						Epsilon.addChatMessage("Speed: " + move.getSpeed());
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
					}
					if(mc.thePlayer.motionY!=0.42f && !flagged) {
						if(move.getSpeed()>move.getBaseMoveSpeed()) {
							mc.getNetHandler().addToSendQueue(new C03PacketPlayer(false));
							flagged = true;
							mc.thePlayer.motionY = -0.42f;
							move.strafe(move.getBaseMoveSpeed() - 0.05 );
						}
						
						
					}
					
					if(flagged) {
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							move.strafe(move.getSpeed()+0.01);
                        	mc.thePlayer.motionY = move.getJumpMotion((float) (0.42 - Math.random() / 440));
						}else if (mc.thePlayer.fallDistance>0.2)
							mc.thePlayer.motionY += 0.05;
						
						if(mc.thePlayer.motionY<0.1 && mc.thePlayer.motionY <-0.1) {
							mc.timer.timerSpeed = 0.9f;
						}else mc.timer.timerSpeed = 1.3f;
						
						
					}
				
					
					break;
				
				case "Matrix":
					
					matrixTicks++;
					
					if(!matrixFreeze && matrixTicks>2) {
						
							lastX = mc.thePlayer.motionX;
							lastY = mc.thePlayer.motionY;
							lastZ = mc.thePlayer.motionZ;
					}
					
					if(matrixTicks>3)
						matrixFreeze = true;
					else
						matrixFreeze = false;
					
					if(matrixTicks>7) {
						matrixTicks = 0;
						matrixBoost = true;
					}else
						matrixBoost = false;
					
					break;
				
				}
				
				if(e.isPre()) {
					
					if(mode.getMode()=="Vanilla") {
						if(mc.thePlayer.motionY<0.1) {
							mc.thePlayer.motionY += 1;
						}
					}
					
					if(mode.getMode()=="Hypixel") {
						
						if(move.isMoving()){
							mc.timer.timerSpeed = 1.6f;
							if(mc.thePlayer.onGround) {
								
								if(disablenext && this.toggled && autodisable.isEnabled()) {
									mc.timer.timerSpeed = 1;
									this.toggle();
									mc.timer.timerSpeed = 1;
								}	
								
								if(!disablenext) {
									y = mc.thePlayer.posY;
									mc.thePlayer.jump();
									if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) {
				                    	move.setMoveSpeed(move.getBaseMoveSpeed() + 0.2);
				                    } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) {
				                    	move.strafe(move.getBaseMoveSpeed() + 0.2);
				                    } else {
				                       move.strafe(move.getBaseMoveSpeed() + 0.19);
				                    }
								}
								
							}else {
								
								if(mc.thePlayer.fallDistance<=0 && mc.thePlayer.motionY<0.1f) {
									
									mc.thePlayer.motionY += 0.024;
									
								}
								
								if(mc.thePlayer.fallDistance>0 && mc.thePlayer.fallDistance<1.24f) {
									mc.thePlayer.motionY += mc.thePlayer.fallDistance/30;
									if(autodisable.isEnabled())
										disablenext = true;
								}
								
								move.strafe(move.getSpeed()+0.01);
								
								
							}
							
							/*
							move.strafe();
							if(mc.thePlayer.onGround) {
								
								
								if(disablenext && this.toggled && autodisable.isEnabled()) {
									mc.timer.timerSpeed = 1;
									this.toggled = false;
								}	
								
								if(!disablenext) {
									y = mc.thePlayer.posY;
									mc.thePlayer.jump();
									if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) {
				                    	move.setMoveSpeed(move.getBaseMoveSpeed() + 0.34);
				                    } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) {
				                    	move.strafe(move.getBaseMoveSpeed() + 0.25);
				                    } else {
				                       move.strafe(move.getBaseMoveSpeed()+0.22);
				                    }
								}
							}else {
								if(mc.thePlayer.fallDistance<=0) {
									mc.thePlayer.motionY += 0.0066;
								}
								if(mc.thePlayer.fallDistance>0)
									mc.thePlayer.motionY += 0.018;
								move.strafe(move.getSpeed()+0.012);
								
								if(mc.thePlayer.posY-1.32>y && mc.thePlayer.fallDistance<=0) {
									mc.thePlayer.motionY += 0.04;
									if(autodisable.isEnabled())
										disablenext = true;
								}	
								
							}*/
							
						}
						else mc.timer.timerSpeed = 1;
					}	
					
					
					
					if(mode.getMode()=="Zonecraft") {
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.motionY = 0.7f;
							move.strafe(move.getBaseMoveSpeed()+0.13);
							
							
						}else {
							
							if(mc.thePlayer.motionY<0.3f && mc.thePlayer.fallDistance>0) 
								mc.thePlayer.motionY += 0.026f;
							
							if(mc.thePlayer.motionY<-0.3) {
								mc.thePlayer.motionY+=0.04;
							}
							
							if(mc.thePlayer.fallDistance>0) {
								mc.thePlayer.motionY += 0.05;
							}
							
							
						}
						
						
					}
					
					if(mode.getMode() == "NCP" || mode.getMode() == "BlocksMC") {
						if (mc.thePlayer.onGround) {
	                        mc.thePlayer.jump();
	                        if(mode.getMode()=="NCP") {
	                        	mc.thePlayer.motionY = move.getJumpMotion((float) (0.424 - Math.random() / 450));
	                        }else if (mode.getMode() == "BlocksMC") {
	                        	mc.thePlayer.motionY = move.getJumpMotion((float) 0.42);
	                        }
	                        if(mode.getMode() == "NCP")
	                        	move.strafe(0.49);
	                    }else {
	                    	
	                    	if(mc.thePlayer.ticksExisted % 2 == 0) {
	                    		nextncp = true;
	                    		if(autodisable.isEnabled())
	                    			disablenext = true;
	                    	}else {
	                    		nextncp = false;
	                    	}
	                    	if(nextncp && mc.thePlayer.fallDistance<=0 && mode.getMode() == "NCP") {
	                    		move.strafe(0.39);
	                    		nextncp = false;
	                    	}
	                    	if (mc.thePlayer.fallDistance > 0 && mc.thePlayer.fallDistance < 3 && mode.getMode() == "NCP") {
	                            mc.thePlayer.motionY += 0.0457;
	                    	}else if (mc.thePlayer.fallDistance > 0 && mc.thePlayer.fallDistance < 3 && mode.getMode() == "BlocksMC") {
	                            mc.thePlayer.motionY += 0.044;
	                    		
	                    	}
	                    }
						if(mode.getMode()=="NCP")
						move.strafe();
						if(mc.thePlayer.onGround && disablenext && autodisable.isEnabled())
							this.toggled = false;
						
					}
					if(mode.getMode() == "SpartanDamage") {
						/*if(mc.thePlayer.hurtTime>0) {
							mc.thePlayer.motionY += 0.1;
						}else {
							move.damage();
						}*/
						
					}
					if(mode.getMode() == "Spartan" || mode.getMode() == "Vulcan") {
	
						if(mode.getMode() == "Spartan" && !mc.thePlayer.onGround)moveUtil.setMoveSpeed(0.26);
						if(mc.thePlayer.fallDistance>0.2&& matrixxcontinue) {
							mc.thePlayer.motionY = 0.42;
							matrixxdoitagain = true;
							holdm = true;
							matrixxcontinue = false;
							mc.thePlayer.fallDistance = 0;
						}	
						if(mc.thePlayer.fallDistance>0.2 && matrixxdoitagain ) {
							if(mode.getMode() == "Vulcan") { mc.thePlayer.motionY = 0.19; holdm = false;}
							matrixxdoitagain = false;
							
							if(mode.getMode() == "Spartan") {mc.thePlayer.motionY = 0.16; 
							matrixxdoitagain = false; holdm = false;}
						
						}
						if(mode.getMode() == "Vulcan" && !matrixxdoitagain && !matrixxcontinue && !holdm && mc.thePlayer.fallDistance > 0.1) {
							if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)mc.thePlayer.motionY = -0.1;else mc.thePlayer.motionY = -0.25;
						}
						
					}
					
					
					
					
					
				}
			}
			if(e instanceof EventMotion) {
				
				if(mc.thePlayer.fallDistance>0)
					hasFallen = true;
				
				if(e.isPre()) {
					EventMotion event = (EventMotion)e;
					
					switch(mode.getMode()) {
					
					case "Mospixel":
						move.strafe();
						if(mc.thePlayer.fallDistance<1.25) {
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							move.strafe(move.getSpeed()+0.5);
							y = mc.thePlayer.posY;
						}else{
							if(mc.thePlayer.motionY<0.1 && mc.thePlayer.motionY>-0.1)  {
								mc.thePlayer.motionY+=0.01;
							}
							if(move.getSpeed()<move.getBaseMoveSpeed()+0.3) {
								move.strafe(move.getSpeed()+0.03+Math.random()/10);
							}
							if(mc.thePlayer.fallDistance>0 && timer.hasTimeElapsed(20, true) && move.getSpeed()<move.getBaseMoveSpeed()+0.5) {
								move.strafe(move.getSpeed()+0.01);
							}
							
						}
						}
						
						
						break;
					
					case "Matrix":
						
						if(matrixFreeze)
							mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
						else {
							move.strafe(move.getBaseMoveSpeed());
							mc.thePlayer.motionY = lastY;
							move.strafe(move.getBaseMoveSpeed());
						
							if(mc.thePlayer.onGround) mc.thePlayer.jump();
						}
						
						
						break;
					
					case "Verus":
						
						speed -= 0.01;
						
						mc.thePlayer.motionY = speed + 0.5;
						
							
						
						if(mc.thePlayer.onGround) {
							y = mc.thePlayer.posY;
							mc.thePlayer.jump();
							speed = 0;
						}	
						//if(move.getBlockRelativeToPlayer(0, 0, 0) instanceof BlockAir) {
							//mc.thePlayer.swingItem();
						if(!mc.thePlayer.onGround && !mc.gameSettings.keyBindBack.getIsKeyPressed()) {
							move.strafe(move.getBaseMoveSpeed()+0.08);
							move.place(new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY - y,mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
						}

						if(mc.thePlayer.ticksExisted%2==0 && !mc.thePlayer.onGround && !mc.gameSettings.keyBindBack.getIsKeyPressed()) {
							move.place(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
									new ItemStack(Blocks.stone.getItem
										(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
							event.setOnGround(true);
							event.setY(Math.round(mc.thePlayer.posY));
							move.strafe(move.getSpeed()+0.28);
						}
						break;
					}
					
					
				}
				if(e.isPost()) {
					
				}
				
			}
		}	
	}
}