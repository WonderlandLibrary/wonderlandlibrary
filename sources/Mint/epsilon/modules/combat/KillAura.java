package epsilon.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.modules.movement.NoSlow;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.MathHelperEpsilon;
import epsilon.util.RotUtils;
import epsilon.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class KillAura extends Module{
	
	public Timer timer = new Timer();
	public ModeSetting targetType = new ModeSetting("Target", "Single", "Single","Multi");
	public ModeSetting rot = new ModeSetting("Rotations", "Snap", "Snap", "None", "Shake", "Verus");
	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1);
	public NumberSetting mincps = new NumberSetting("MinCPS", 8.5, 1, 20, 0.5);
	public NumberSetting maxcps = new NumberSetting("MaxCPS", 12, 1, 20, 0.5);	
	public BooleanSetting cpsBipas = new BooleanSetting("CPSBypass", true);
	public BooleanSetting noSwing = new BooleanSetting("No Swing", false);
	public ModeSetting autoBlock = new ModeSetting("AutoBlock", "Vanilla", "Vanilla", "Bypass", "Fake","None","NCP", "AAC", "Verus", "Watchdog");
	public BooleanSetting rangeim = new BooleanSetting("HitOnEnter", true);
	public BooleanSetting hurt = new BooleanSetting("AlwaysHurtTime", true);
	public BooleanSetting skyblock = new BooleanSetting("SkyblockMode", false);
	public BooleanSetting rt = new BooleanSetting("RayTrace", true);
	public static EntityLivingBase target = null;
	double cpsBipasValue;
	boolean blocking;
	boolean bipas = false;
	int moreValuesMin;
    int moreValuesMax;
    int resultCPS;
    public static float yaw, pitch, lastYaw, lastPitch;
	boolean dorangeim = true;
	public KillAura(){
		super("KillAura", Keyboard.KEY_K, Category.COMBAT, "Automatically hits entities who enter your range");
		this.addSettings(range, targetType,rot, rt,mincps, maxcps, cpsBipas,noSwing, autoBlock,rangeim, hurt,skyblock);
	}
	
	public void onEnable(){
		blocking = mc.gameSettings.keyBindUseItem.getIsKeyPressed();
		 bipas = true;
		boolean dorangeim = true;
		target = null;
	}
	
	public void onDisable(){
		if(autoBlock.getMode()=="Fake" && !NoSlow.enabled) 
			NoSlow.doNotSlow = false;
		blocking = false;
		 bipas = false;
		target = null;
		 moreValuesMin = 0;
	     moreValuesMax =0;
	     resultCPS =0;
	     
	    	if(!blocking)
	    	 mc.gameSettings.keyBindUseItem.pressed = false;
	         
	     
	}
	
	public void onEvent(Event e){
		if(autoBlock.getMode()=="Fake" && !NoSlow.enabled && target !=null) 
			NoSlow.doNotSlow = true;
		if(autoBlock.getMode()!="Fake" && !NoSlow.enabled)
			NoSlow.doNotSlow = false;
		
		
		if(mc.thePlayer != null) {
			
			
			if(target != null && rot.getMode() != "None" &&  mc.gameSettings.thirdPersonView!=0) {
				mc.thePlayer.rotationYawHead = getRotations(target, rot.getMode())[0];
				mc.thePlayer.renderYawOffset = getRotations(target, rot.getMode())[0];
				mc.thePlayer.rotationPitchHead = getRotations(target, rot.getMode())[1];
			}	
			if(e instanceof EventUpdate) {
				lastYaw = mc.thePlayer.rotationYawHead;
				lastPitch = mc.thePlayer.rotationPitchHead;
			}
			
			if(e instanceof EventMotion) {
				EventMotion event = (EventMotion)e;
				this.displayInfo = rot.getMode() + " | " + range.getValue() + " | " + autoBlock.getMode();
				if(target == null && blocking) {
					unblock();
				}
				if((rot.getMode() != "None" || rot.getMode() != "Goofy")&& target != null) {
					event.setYaw(getRotations(target, rot.getMode())[0]);
					event.setPitch(getRotations(target, rot.getMode())[1]);
				}
				if(e.isPre()) {
					
					if(target != null && (mc.thePlayer.getDistanceToEntity(target) > range.getValue() || target.isDead)) {
						target = null;
						unblock();
						mc.gameSettings.keyBindUseItem.pressed = false;
					}
					
					
					if (this.maxcps.getValue() < this.mincps.getValue()) {
			            maxcps.setValue(mincps.getValue());
			        }
					
					
					
					List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		
					switch(targetType.getMode()) {
					case "Single":
						
						targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue() && entity != mc.thePlayer && !entity.bot && !entity.isDead && entity.ticksExisted>5 && entity.getHealth() > 0 && !(entity instanceof EntityArmorStand) && ((!(entity instanceof EntityPlayer) && skyblock.isEnabled() || !skyblock.isEnabled()))).collect(Collectors.toList());
			
						targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
						if(!skyblock.isEnabled())
							targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
						if(skyblock.isEnabled()) {

							List<EntityLivingBase> valid = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase)
									.map(entity -> ((EntityLivingBase) entity))
									.filter(entity -> {
										
										if(entity instanceof EntityPlayer || entity instanceof EntityArmorStand) return false; 
										
										return true;
									})
									.collect(Collectors.toList());
				
							
						}
						break;
						
					case "Multi":
						
						for(EntityLivingBase t : targets) {
							if(!(t instanceof EntityPlayer))
								targets.remove(t);
							
							EntityPlayer p = (EntityPlayer) t;
							
							if(p.getDistanceToEntity(mc.thePlayer) > range.getValue() || p.isDead || p==mc.thePlayer || p.bot || p.getHealth()<=0)
								targets.remove(t);
						}
						
						break;
					}
					
					
					
					if(!targets.isEmpty()) {
						if(mc.thePlayer.getCurrentEquippedItem()!=null) {
						if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && autoBlock.getMode() != "None") 
							mc.gameSettings.keyBindUseItem.pressed = true;
						}

						switch(targetType.getMode()) {
							case "Single":
			                target = targets.get(0);
								
			                if(!(mc.thePlayer.canEntityBeSeen(target))) {
			                	return;
			                }
			                
			                
			                
			                if((rt.isEnabled() && RotUtils.canHitEntity(mc.thePlayer.rotationYawHead, mc.thePlayer.rotationPitch, (float) range.getValue(), target)) || !rt.isEnabled()) {

								if(rangeim.isEnabled() && dorangeim) {
									if(noSwing.isEnabled())
									mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
									else
										mc.thePlayer.swingItem();
									mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									
									dorangeim = false;
								}
								resultCPS = (int)Math.floor(Math.random()*(moreValuesMax-moreValuesMin+1)+moreValuesMin);
								if(cpsBipas.isEnabled()) {
									cpsBipasValue = ((Math.random() * Math.random() ) * (3 + Math.random()) * resultCPS);
								}else {
									cpsBipasValue = 0;
								}
								if(!blocking) {
									switch(autoBlock.getMode()) {
									
									case "Watchdog":
										
										mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
										blocking = true;
										
										break;
									
									}
								}
								
								if(timer.hasTimeElapsed((long) (1000 / MathHelperEpsilon.getRandInt((int) mincps.getValue(), (int) maxcps.getValue()) + cpsBipasValue), true)) {
									if(blocking) {
										unblock();
									}
									bipas = false;
									if(noSwing.isEnabled()) {
										mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
									}else {
										mc.thePlayer.swingItem();
									}
									

									switch(autoBlock.getMode()) {
									
									case "Watchdog":

										mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
										blocking = false;
										
										break;
									
									}
									
									mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
									

									switch(autoBlock.getMode()) {
									
									case "Watchdog":


										mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
										blocking = true;
										
										break;
									
									}
									
									if(autoBlock.getMode()=="AAC" && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
									mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
									blocking = true;
									}
									
								}else {

									bipas = true;
								}
								if(mc.thePlayer.getHeldItem()!=null) {
									if( mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
										switch(autoBlock.getMode()) {
										
										
										case "Vanilla":
											mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
											break;
											
										case "Bypass":
											if(bipas) {
												mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
												blocking = true;
											}
											break;
											
										case "NCP":
											mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
											blocking = true;
											break;
										
										
										}
									}
								}
			                }
			                
							break;
							
							case "Multi":
								
								break;
						}
					}else if(mc.thePlayer.getHeldItem()!=null){
						if(!mc.thePlayer.isBlocking() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
							mc.gameSettings.keyBindUseItem.pressed = false;
							dorangeim = true;
						}
					}
				}else if (e.isPost()) {
					if(autoBlock.getMode()=="AAC" && blocking && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
						blocking = false;
					}
				}
			}
		}	
			
	}
	
	public double randomBetween(final double min, final double max) {
        return min + (Math.random() * (max - min));
    }
	public float fovToEntity(Entity e) {
        double x = e.posX - mc.thePlayer.posX;
        double z = e.posZ - mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 57.2957795D;
        return (float)(yaw * -1.0D);
     }
	
	public double fovFromEntity(Entity e) {
        return ((double)(mc.thePlayer.rotationYaw - fovToEntity(e)) % 360.0D + 540.0D) % 360.0D - 180.0D;
     }
	
	public static float[] getRotations(final Entity e, final String mode) { 
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
				   deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				   deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				   distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
		final int fps = (int) (mc.func_175610_ah() / 20.0F);
		
		final double x = target.posX - (target.lastTickPosX - target.posX) + 0.01 - mc.thePlayer.posX;
        final double z = target.posZ - (target.lastTickPosZ - target.posZ)  - mc.thePlayer.posZ;

        double posFix = (mc.thePlayer.posY - target.posY);


        if (posFix < -1.4) posFix = -1.4;
        if (posFix > 0.1) posFix = 0.1;

        final double yy = (e.posY - (target.lastTickPosY - e.posY)) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + posFix;

        final double xzSqrt = MathHelper.sqrt_double(x * x + z * z);

        float yaw = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(z, x)) - 90.0F);

        float pitch = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(-Math.atan2(yy, xzSqrt)));

        double random = Math.random()/3;
        
		double shakeX = e.posX+random + (e.posX+random - e.lastTickPosX+random) - mc.thePlayer.posX+random,
				
		   shakeY = e.posY- random - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
		   
		   shakeZ = e.posZ+random + (e.posZ+random - e.lastTickPosZ+random) - mc.thePlayer.posZ+random;
		   
		switch (mode) {
			case "Snap":
				yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)); pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));
				
				if(deltaX < 0 && deltaZ < 0) {
					yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
				}else if(deltaX > 0 && deltaZ < 0) {
					yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
				}
			
		
				
				break;
				
			case "Shake":
				
				yaw = (float) Math.toDegrees(-Math.atan(shakeX / shakeZ));
				pitch = (float) -Math.toDegrees(Math.atan(shakeY / distance));
				
				if(deltaX < 0 && deltaZ < 0) {
					yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
				}else if(deltaX > 0 && deltaZ < 0) {
					yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
				}
				
				break;
				
			case "Verus":

				final float verusPitchFucker = (float) (pitch - (Math.random() * (Math.random()>0.5 ? 1 : -1))/10);
				
                final float yawDelta = (float) (((((yaw - lastYaw) + 540) % 360) - 180) / (fps / 5 * (1 + Math.random())));
                final float pitchDelta = (float) ((verusPitchFucker - lastPitch) / (fps / 3 * (1 + Math.random())));

                yaw = lastYaw + yawDelta;
                pitch = lastPitch + pitchDelta;
                
				break;
			
		}
		float[] rotations = new float[]{yaw, pitch};
        
        if (mode=="None") {
            yaw = mc.thePlayer.rotationYaw;
            pitch = mc.thePlayer.rotationPitch;
        }

        pitch = MathHelper.clamp_float(pitch, -90.0F, 90.0F);
        
		return new float[] {yaw, pitch};		
	}
	public void unblock() {
        if (blocking) {
            mc.gameSettings.keyBindUseItem.pressed = false;
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blocking = false;
        }
    }
	

}