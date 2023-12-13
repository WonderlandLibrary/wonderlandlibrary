package vestige.impl.module.combat;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.JumpEvent;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.PostMotionEvent;
import vestige.api.event.impl.StrafeEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.impl.module.movement.Fly;
import vestige.impl.module.movement.Speed;
import vestige.impl.module.world.Scaffold;
import vestige.util.entity.RotationsUtil;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;

import org.lwjgl.input.Keyboard;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

@ModuleInfo(name = "Killaura", category = Category.COMBAT, key = Keyboard.KEY_R)
public class Killaura extends Module {
	
	@Getter
    private EntityLivingBase target;
	
    private TimerUtil timerUtil = new TimerUtil();
    
    private float yaw, pitch;
    
    private final ModeSetting rotations = new ModeSetting("Rotations", this, "Normal", "Normal", "Randomised", "Delayed", "Delayed2", "Down", "Derp", "None");
    private final ModeSetting filtrer = new ModeSetting("Filtrer by", this, "Range", "Range", "Health");
    private final NumberSetting range = new NumberSetting("Range", this, 4.2, 2.8, 6, 0.1, false);
    private final NumberSetting startingRange = new NumberSetting("Starting Range", this, 4.2, 2.8, 6, 0.1, false);
    
    private final ModeSetting attackDelayMode = new ModeSetting("Attack Delay", this, "Randomised APS", "Randomised APS", "Every x ticks");
    
    private final NumberSetting rotationsUpdateFrequency = new NumberSetting("Rots Update Frequency", this, 10, 2, 50, 1, false) {
    	@Override
    	public boolean isShown() {
    		return rotations.is("Delayed");
    	}
    };
    
    private final NumberSetting minCps = new NumberSetting("Min CPS", this, 10, 1, 20, 1, false) {
    	@Override
    	public boolean isShown() {
    		return attackDelayMode.is("Randomised APS");
    	}
    	
    	@Override
		public void setCurrentValue(double value) {
	        super.setCurrentValue(value);
	    }
	};
	private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 10, 1, 20, 1, false) {
		@Override
    	public boolean isShown() {
    		return attackDelayMode.is("Randomised APS");
    	}
		
		@Override
		public void setCurrentValue(double value) {
	        super.setCurrentValue(value);
	    }
	};
    
    private final NumberSetting attackDelayInTicks = new NumberSetting("Attack delay in ticks", this, 2, 1, 20, 1, false) {
    	@Override
    	public boolean isShown() {
    		return attackDelayMode.is("Every x ticks");
    	}
    };
    
    private final BooleanSetting moveFix = new BooleanSetting("Movement Fix", this, false);
    private final BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", this, true);
    private final BooleanSetting silent = new BooleanSetting("Silent", this, true);
    private final ModeSetting autoblock = new ModeSetting("Autoblock", this, "Fake", "Vanilla", "NCP", "Hypixel", "Redesky", "Fake", "None");
    
    private final BooleanSetting hypixelRelease = new BooleanSetting("Release every 3 ticks", this, false) {
    	@Override
    	public boolean isShown() {
    		return autoblock.is("Hypixel");
    	} 
    };
    
    private final BooleanSetting whileInvOpen = new BooleanSetting("While Inventory Open", this, false);
    private final BooleanSetting whileScaffoldEnabled = new BooleanSetting("While Scaffold Enabled", this, false);
    private final BooleanSetting whileMovingFast = new BooleanSetting("While Moving Fast", this, false);
    
    private final NumberSetting hurtTime = new NumberSetting("Hurt time", this, 10, 0, 10, 1, false);
    private final BooleanSetting swingWhileTargetHurt = new BooleanSetting("Swing while target hurt", this, true);
    
    private final BooleanSetting onlyPlayers = new BooleanSetting("Only players", this, true);
    private final BooleanSetting invisibles = new BooleanSetting("Hit invisibles", this, false);
    
    private final BooleanSetting test = new BooleanSetting("Test", this, false) {
    	@Override
    	public boolean isShown() {
    		return Vestige.getInstance().getUsername().equals("YesCheatPlus");
    	}
    };
    
    private boolean blocking;
    
    private int rotsTicks;
    
    public Killaura() {
        this.registerSettings(rotations, rotationsUpdateFrequency, filtrer, range, startingRange, attackDelayMode, minCps, maxCps, attackDelayInTicks, hurtTime, swingWhileTargetHurt, moveFix, keepSprint, silent, autoblock, hypixelRelease, whileInvOpen, whileScaffoldEnabled, whileMovingFast, onlyPlayers, invisibles, test);
    }
    
    public void onEnable() {
    	target = null;
    	updateRotations();
    	
    	rotsTicks = 0;
    }
    
    public void onDisable() {
    	this.releaseBlocking();
    }
    
    @Listener
    public void onUpdate(UpdateEvent event) {
    	if(mc.thePlayer == null || mc.thePlayer.ticksExisted < 5) {
    		this.setEnabled(false);
    	}
    	
        target = findTarget();
        
        if (target == null) {
        	this.releaseBlocking();
        	return;
        }
        
        boolean shouldAttack = false;
        
        switch (attackDelayMode.getMode()) {
        	case "Randomised APS":
        		double minCpsValue = Math.min(minCps.getCurrentValue(), maxCps.getCurrentValue());
        		double maxCpsValue = Math.max(minCps.getCurrentValue(), maxCps.getCurrentValue());
        		
        		/*
        		int min = (int) (1000 / minCpsValue);
        		int max = (int) (1000 / maxCpsValue);
        		
        		int diff = min - max;
        		
        		int delay = (int) (min - Math.random() * diff);
        		*/
        		
        		long delay;
        		
        		if(minCpsValue == maxCpsValue) {
        			delay = (long) (1200 / maxCpsValue);
        		} else {
        			delay = (long) (1200 / ThreadLocalRandom.current().nextDouble(minCpsValue, maxCpsValue));
        		}
        		
        		shouldAttack = timerUtil.getTimeElapsed() >= delay;
        		//shouldAttack = timerUtil.getTimeElapsed() >= delay && mc.thePlayer.ticksExisted % 13 != 0 && mc.thePlayer.ticksExisted % 19 != 0;
        		break;
        	case "Every x ticks":
        		shouldAttack = timerUtil.getTimeElapsed() >= (int) (attackDelayInTicks.getCurrentValue() * 50);
        		break;
        }
        
        if (mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest) {
        	if (!whileInvOpen.isEnabled()) {
        		shouldAttack = false;
        		this.releaseBlocking();
        		return;
        	}
        }
        
        if (Vestige.getInstance().getModuleManager().getModule(Scaffold.class).isEnabled()) {
        	if (!whileScaffoldEnabled.isEnabled()) {
        		shouldAttack = false;
        		this.releaseBlocking();
        		return;
        	}
        }
        
        if (!whileMovingFast.isEnabled()) {
        	if (Math.abs(mc.thePlayer.motionY) > 2 || MovementUtils.getHorizontalMotion() > 2) {
        		shouldAttack = false;
        		this.releaseBlocking();
        		return;
        	}
        }
        
        if (shouldAttack) {
            if(target.hurtTime <= hurtTime.getCurrentValue() || swingWhileTargetHurt.isEnabled()) {
            	mc.thePlayer.swingItem();
            } else {
            	mc.thePlayer.swingItemNoPacket();
            }
            
            if(target.hurtTime <= hurtTime.getCurrentValue()) {
            	if(target.hurtTime <= 2) {
            		if(test.isEnabled() && mc.thePlayer.onGround) {
                		//PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.08, mc.thePlayer.posZ, false));
                		PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(target.posX, target.posY, target.posZ, target.onGround));
                		//PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.08, mc.thePlayer.posZ, false));
                	}
            	}
            	
            	if (keepSprint.isEnabled() || 
                		Vestige.getInstance().getModuleManager().getModule(Speed.class).isEnabled() || 
                		Vestige.getInstance().getModuleManager().getModule(Fly.class).isEnabled()) {
                	PacketUtil.sendPacketNoEvent(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));	
                } else {
                	mc.playerController.attackEntity(mc.thePlayer, target);
                }	
            }
            timerUtil.reset();
        }
        
        if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
        	this.preAutoblock();
        }
    }
    
    @Listener
    public void onStrafe(StrafeEvent event) {
    	if(moveFix.isEnabled()) {
    		updateRotations();
    	}
    	
    	if(target != null) {
    		if (!silent.isEnabled()) {
        		mc.thePlayer.rotationYaw = yaw;
        		mc.thePlayer.rotationPitch = pitch;
        	} else if (moveFix.isEnabled()) {
        		event.setYaw(yaw);
        	}
    	}
    }
    
    @Listener
    public void onJump(JumpEvent event) {
    	if (moveFix.isEnabled() && target != null) {
    		event.setYaw(yaw);
    	}
    }
    
    @Listener
    public void onMotion(MotionEvent event) {
    	if(!moveFix.isEnabled()) {
    		updateRotations();
    	}
    	
    	if (target == null) return;
    	
    	if (!rotations.is("None")) {
    		float finalYaw = yaw;
    		float finalPitch = pitch;
    		
    		event.setYaw(finalYaw);
    		event.setPitch(finalPitch);
    		mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw;
    		mc.thePlayer.rotationPitchHead = pitch;
    	}
    }
    
    @Listener
    public void onPostMotion(PostMotionEvent event) {
    	if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
    		this.postAutoblock();
        }
    }
    
    private void preAutoblock() {
    	switch (autoblock.getMode()) {
    		case "Vanilla":
    			if(!blocking) {
    				PacketUtil.sendBlocking(false, false);
    				blocking = true;
    			}
    			break;
    		case "NCP":
    			if(blocking) {
    				PacketUtil.releaseUseItem(false);
    				blocking = false;
    			}
    			break;
    		case "Hypixel":
    			/*
    			if(hypixelRelease.isEnabled()) {
    				if(blocking && mc.thePlayer.ticksExisted % 3 == 1) {
        				PacketUtil.releaseUseItem(false);
        				blocking = false;
        			}
    			}
    			*/
    			
    			if(target instanceof EntityPlayer) {
    				EntityPlayer player = (EntityPlayer) target;
    				Speed speed = (Speed) Vestige.getInstance().getModuleManager().getModule(Speed.class);
        			if(player.hurtTime < 5 && mc.thePlayer.ticksExisted % 3 == 1) {
        				PacketUtil.sendBlocking(false, true);
        				blocking = true;
        			}
    			}
    			break;
    		case "Redesky":
    			blocking = true;
    			PacketUtil.sendBlocking(false, false);
    			break;
    	}
    }
    
    private void postAutoblock() {
    	switch (autoblock.getMode()) {
    		case "NCP":
    			if(!blocking) {
    				PacketUtil.sendBlocking(false, false);
    				blocking = true;
    			}
    			break;
    		case "Hypixel":
    			if(hypixelRelease.isEnabled()) {
    				if(blocking && mc.thePlayer.ticksExisted % 3 == 0) {
        				PacketUtil.releaseUseItem(false);
        				blocking = false;
        			}
    			}
    			break;
    	}
    }
    
    private void releaseBlocking() {
    	if(blocking) {
    		switch (autoblock.getMode()) {
    			case "Vanilla":
    			case "NCP":
    			case "Hypixel":
    			case "Redesky":
    				PacketUtil.releaseUseItem(false);
    				break;
    		}
    	}
    	blocking = false;
    }
    
    private void updateRotations() {
    	if (target == null) {
    		switch (rotations.getMode()) {
    			case "Normal":
    			case "Randomised":
    			case "Delayed":
    			case "Delayed2":
    			case "Down":
    				yaw = mc.thePlayer.rotationYaw;
    				pitch = mc.thePlayer.rotationPitch;
    				break;
    			case "Derp":
    				yaw = mc.thePlayer.rotationYaw - 180;
    				break;
    		}
    		rotsTicks = 0;
    	} else {
    		float rots[] = RotationsUtil.getRotations(target);
    		
    		float randomisedYaw = rots[0];
			float randomisedPitch = rots[1];
    		
    		switch (rotations.getMode()) {
	    		case "Normal":
	    			yaw = rots[0];
	    			pitch = rots[1];
	    			
	    			yaw += Math.random() * 2 - 1;  			
	    			pitch += Math.random() * 2 - 1;
	    			
	    			pitch = Math.min(pitch, 90);
	    			pitch = Math.max(pitch, -90);
	    			break;
	    		case "Delayed":
	    			if(rotsTicks++ % (int) rotationsUpdateFrequency.getCurrentValue() != 0) break;
	    		case "Randomised":
	    			randomisedYaw += Math.random() * 8 - 4;  			
	    			randomisedPitch += Math.random() * 4 - 2;
	    			
	    			randomisedPitch = Math.min(randomisedPitch, 90);
	    			randomisedPitch = Math.max(randomisedPitch, -90);
	    			
	    			if(mc.thePlayer.ticksExisted % 5 < 4) {
	    				yaw = randomisedYaw;
		    			pitch = randomisedPitch;
	    			}
	    			break;
	    		case "Down":
	    			randomisedYaw += Math.random() * 8 - 4;
	    			
	    			if(mc.thePlayer.ticksExisted % 5 < 4) {
	    				yaw = randomisedYaw;
	    			}
	    			pitch = 90;
	    			break;
	    		case "Delayed2":
	    			double diff = Math.abs(rots[0] - yaw);
	    			
	    			if(rotsTicks > 15) {
	    				yaw = (float) (rots[0] + Math.random() * 2 - 1);
	    				rotsTicks = 0;
	    			} else if(diff > 50 && diff < 310) {
	    				yaw = (float) (rots[0] + Math.random() * 2 - 1);
	    				rotsTicks = 0;
	    			}
	    			
	    			if(mc.thePlayer.getDistanceToEntity(target) < 1) {
	    				pitch = 90;
	    			} else {
	    				if(mc.thePlayer.ticksExisted % 5 < 2 || mc.thePlayer.ticksExisted % 9 == 0) {
	    					pitch = (float) (rots[1] + Math.random() * 2 - 1);
 	    				}
	    			}
	    			
	    			pitch = Math.min(pitch, 90);
	    			pitch = Math.max(pitch, -90);
	    			
	    			rotsTicks++;
	    			break;
    			case "Derp":
    				yaw += 60;
    				pitch = 90;
    				
    				float finalYaw = (float) (yaw + Math.random() * 0.5 - 0.25);
    	    		float finalPitch = (float) (pitch + Math.random() * 0.5 - 0.25);
    	    		finalPitch = Math.min(Math.max(finalPitch, -90), 90);
    	    		
    	    		yaw = finalYaw;
    				pitch = finalPitch;
    				break;
    		}
    	}
    }
    
    private EntityLivingBase findTarget() {
        ArrayList<EntityLivingBase> entities = new ArrayList<>();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                if (canAttackEntity((EntityLivingBase) entity)) {
                    entities.add((EntityLivingBase) entity);
                }
            }
        }

        if (entities != null && entities.size() > 0) {
            switch(filtrer.getMode()) {
                case "Range":
                    entities.sort(Comparator.comparingDouble(entity -> (entity).getDistanceToEntity(mc.thePlayer)));
                    break;
                case "Health":
                    entities.sort(Comparator.comparingDouble(entity -> (entity).getHealth()));
                    break;
            }
            return entities.get(0);
        }
        return null;
    }

    private boolean canAttackEntity(EntityLivingBase entity) {
        if (entity == mc.thePlayer) return false;
        
        if (entity instanceof EntityPlayer || ((entity instanceof EntityAnimal || entity instanceof EntityMob) && !onlyPlayers.isEnabled())) {
            if (!entity.isDead) {
                if (mc.thePlayer.getDistanceToEntity(entity) < (target != null ? range.getCurrentValue() : startingRange.getCurrentValue())) {
                    if ((!entity.isInvisible() && !entity.isInvisibleToPlayer(mc.thePlayer)) || invisibles.isEnabled()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    public boolean shouldRenderBlocking() {
    	return this.isEnabled() && target != null && !autoblock.is("None");
    }

}
