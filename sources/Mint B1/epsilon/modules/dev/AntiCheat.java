package epsilon.modules.dev;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.util.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class AntiCheat extends Module {

	public BooleanSetting verbose = new BooleanSetting("AlertChat", false);
	private final Timer timer = new Timer();
	
	public AntiCheat() {
		super("Anticheat", Keyboard.KEY_NONE, Category.DEV, "Detects hackars");
		this.addSettings(verbose);
	}
	
	public void onEnable() {
	}
	
	public void onDisable() {}
	
	public void onEvent(Event e) {
		if(e instanceof EventReceivePacket) {
			
			if(e.getPacket() instanceof S14PacketEntity ) {
				S14PacketEntity p = (S14PacketEntity ) e.getPacket();
				
				
				
			}
			
		}
		
		if(e instanceof EventUpdate) {
			//Basic checks, advanced checks are handled elsewhere
			

			if(mc.theWorld!=null) {
				
				for (Object t: mc.theWorld.loadedEntityList) {
					
					if(t instanceof EntityPlayer) {
						final EntityPlayer p = (EntityPlayer) t;
						
						final String name = p.getName();
						
						if(!p.isRiding() && mc.thePlayer!=t && p.ticksExisted>10) {
							
							final double x = p.posX, y = p.posY, z = p.posZ;
							final double lx = p.lastTickPosX, ly = p.lastTickPosY, lz = p.lastTickPosZ;
							final double yDiff = y-ly;
							
				            final boolean groundReal = (!(mc.theWorld.getBlock(new BlockPos(x - 0.5, y - 0.5, z - 0.5)) instanceof BlockAir)
				                    || !(mc.theWorld.getBlock(new BlockPos(x + 0.5, y - 0.5, z - 0.5)) instanceof BlockAir)
				                    || !(mc.theWorld.getBlock(new BlockPos(x + 0.5, y - 0.5, z + 0.5)) instanceof BlockAir)
				                    || !(mc.theWorld.getBlock(new BlockPos(x - 0.5, y - 0.5, z + 0.5)) instanceof BlockAir)
				                    		
				                    && (!(mc.theWorld.getBlock(new BlockPos(lx - 0.5, ly - 0.52, lz - 0.5)) instanceof BlockAir)
						                    || !(mc.theWorld.getBlock(new BlockPos(lx + 0.5, ly - 0.5, z - 0.5)) instanceof BlockAir)
						                    || !(mc.theWorld.getBlock(new BlockPos(lx + 0.5, ly - 0.5, lz + 0.5)) instanceof BlockAir)
						                    || !(mc.theWorld.getBlock(new BlockPos(lx - 0.5, ly - 0.5, lz + 0.5)) instanceof BlockAir))
				                    		
				                    		);
				            
				            final float deltaYaw = Math.abs(p.rotationYaw-p.prevRotationYaw);
				            
				            if(deltaYaw>1.5f) {
				            	
				            }
				            
				            if(getYMotionOfEntity(p)>0.53) {
				            	

				            	final String flag = name + " flagged impossible Y value: " + getYMotionOfEntity(p);
				            	
								if(verbose.isEnabled()) {
									if(timer.hasTimeElapsed(1500, true))
									
									mc.thePlayer.sendChatMessage(flag);
									
								}else Epsilon.addChatMessage(flag);
				            	
				            }

							
							
							
				            if(yDiff>1.5) {


								if(verbose.isEnabled()) {
									if(timer.hasTimeElapsed(1500, true))
									
									mc.thePlayer.sendChatMessage(name + " flagged basic fly! : " + Math.random());
									
								}else Epsilon.addChatMessage(name + " flagged basic fly!");
				            }else if (yDiff<-3.5) {

					            	
									/*if(verbose.isEnabled()) {
										if(timer.hasTimeElapsed(1500, true))
										mc.thePlayer.sendChatMessage(name + " flagged basic fastfall : " + Math.random());
										
									}else Epsilon.addChatMessage(name + " flagged basic fastfall");*/
				            	
				            }


				            if(getSpeedOfEntity(p)>1) {

								final String flag = name + " speed is too high! Speed of  " + getSpeedOfEntity(p);
								
								if(verbose.isEnabled()) {
									if(timer.hasTimeElapsed(1500, true))
									
									mc.thePlayer.sendChatMessage(flag);
									
								}else Epsilon.addChatMessage(flag);
				            }
				            
							
							if(!p.onGround && !groundReal && !(mc.theWorld.getBlock(new BlockPos(p.posX, p.posY-0.5f, p.posZ)) instanceof BlockLiquid)&& p.lastTickPosX!= p.posX && p.lastTickPosZ!=p.posZ) {
								
								final String flag = name + " is not abiding by gravity! : " + Math.random();
								final String flag1 = name + " is not abiding by gravity!";
								
								if(p.lastTickPosY==p.posY) {
									

									if(verbose.isEnabled()) {
										if(timer.hasTimeElapsed(1500, true))
										
										mc.thePlayer.sendChatMessage(flag);
										
									}else Epsilon.addChatMessage(flag1);
									
								}
								
								
							}
							
							if(veloCheckXZ(p)) {
								
								final String flag = name + " flagged Velocity on the XZ axis! : " + Math.random();
								final String flag1 = name + " flagged Velocity on the XZ axis!";
								
								

								if(verbose.isEnabled()) {
									if(timer.hasTimeElapsed(1500, true))
									
									mc.thePlayer.sendChatMessage(flag);
									
								}else Epsilon.addChatMessage(flag1);
								
							
								
							}
							if(veloCheckY(p)) {

								final String flag = name + " flagged Velocity on the Y axis! : " + Math.random();
								final String flag1 = name + " flagged Velocity on the Y axis!";
								
								

								if(verbose.isEnabled()) {
									if(timer.hasTimeElapsed(1500, true))
									
									mc.thePlayer.sendChatMessage(flag);
									
								}else Epsilon.addChatMessage(flag1);
							}
							
						}
						
						
						
					}
				}
			}
			
			
		}
	}
	
	private boolean veloCheckXZ(final EntityPlayer e) {
		
		if(e.hurtTime<10 && e.hurtTime>0) {
			final double diffX = e.posX - e.lastTickPosX;
			
			final double diffZ = e.posX - e.lastTickPosX;
			
			
			return diffX>0.9 || diffX<-0.9 || diffZ>0.9 || diffZ<-0.9;
			
		}
		return false;
		
	}
	
	private boolean veloCheckY(final EntityPlayer e) {
		
		if(e.hurtTime<10 && e.hurtTime>0) {
			final boolean flagY = e.onGround && e.lastTickPosY == e.posY;
			
			return flagY;
			
		}
		return false;
		
	}
	
	public static double getSpeedOfEntity(final EntityPlayer e) {
		
		final double x1 = e.posX, x2 = e.lastTickPosX, z1 = e.posZ, z2 = e.lastTickPosZ;
		
		final double xMot = (x1-x2);
		final double zMot = (z1 - z2);
		
		return Math.hypot(xMot, zMot);
	}
	
	public static double getYMotionOfEntity(final EntityPlayer e) {
		
		final double mot = e.posY-e.lastTickPosY;
		final double calculateOffset = mot-0.0784000015258789;
		
		return calculateOffset;
		
	}
	
	public static double getYMotion(final double y1, final double y2) {
		
		final double mot = y1-y2;
		final double off = mot-0.0784000015258789;
		return off;
		
	}
	

}
