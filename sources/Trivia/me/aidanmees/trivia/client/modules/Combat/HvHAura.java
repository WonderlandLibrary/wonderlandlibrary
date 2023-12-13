
package me.aidanmees.trivia.client.modules.Combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.modules.Movement.NoSlowdown;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.client.tools.Helper;
import me.aidanmees.trivia.client.tools.RotationUtil;
import me.aidanmees.trivia.client.tools.Timer1;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.friends.FriendsMananger;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class HvHAura extends Module {
    public boolean unblock = false;

    
    private Timer1 attackTimer = new Timer1();
    public static ArrayList<Entity> attackList = new ArrayList<>();
   
    public float yaw, pitch;
    public int ticks;
    public int delay;
    private  boolean potted, nextHit;
    public boolean override;
    public int currentTarget;
    public static Timer1 potTimer = new Timer1();
    public static Timer1 duraTimer = new Timer1();
    public static Timer1 critTimer = new Timer1();
    EntityLivingBase lol3;
    EntityLivingBase lol;
	  EntityLivingBase lol1;
    
	public HvHAura() {
		super("HvHAura", Keyboard.KEY_NONE, Category.COMBAT, "Automatically attacks entities in range.");
	}

	 public void doAttack2(Entity target) {
	        
	        if (ClientSettings.armorbreakern) {
	            Helper.mc().thePlayer.swingItem(); if(ClientSettings.noswingn) mc.thePlayer.swingProgressInt = 10;
	            Helper.inventoryUtils().swap(9, mc.thePlayer.inventory.currentItem);
	            doCritAttack(target, false);
	            doCritAttack(target, false);
	            doCritAttack(target, true);
	            Helper.inventoryUtils().swap(9, mc.thePlayer.inventory.currentItem);
	            doCritAttack(target, false);
	            doCritAttack(target, true);
	        } else {
	            if (Helper.player().isBlocking())
	                Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
	            Criticals.crit();
	            Helper.player().swingItem();
	            Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
	        }
	    }
	

	 public boolean canAttack() {
	        if (!attackList.isEmpty() && !trivia.getModuleByName("Freecam").isToggled())
	            return true;
	        return false;
	    }
	    @Override
	    public void onEnable(){
	    	
	        super.onEnable();
	        if (currentMode.equals("Switch")){
	        	
	        	    }
	        	    
	        if(ClientSettings.teamn)
	            trivia.chatMessage("Teaming enabled, warning its buggy! It might make aura not attack some players");

	    }
	    @Override
	    public void onUpdate() {
	    	super.onUpdate();
	    	if (currentMode.equals("Tick")){
	    		if(Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null) {
		    		 
		    		 lol3 = Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn);
		    		 if (isValidEntity(lol3)){
	    		
	    		if (canBlock() && Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null &&  isValidEntity(lol3))
	                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
	        }
		    	
	    		 int delay = 493;

	                if (attackTimer.hasReached(delay)) {
		    	 if(Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null) {
		    		 
		    		 lol3 = Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn);
		    		 if (isValidEntity(lol3)){
	    		
	    		
	           
	                	
	                    
	                    doAttack2(getTarget());
	                    changeTargets();
	                    override = potted = false;
	                    attackTimer.reset();
	                }
	            
		    		 
	            }
	            if (canBlock() && Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null &&  isValidEntity(lol3))
	                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
	        }
	    	}
	    	}
	       
	     if (currentMode.equals("Switch")){
	    	
	    	
	    	 
	    	 if(Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null) {
	    		
	    		 lol = Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn);
	    		 if (isValidEntity(lol) && IsntMineplexBot(lol)){
	    	
	            boolean useRandom = ClientSettings.rndn > 0;
	            int cd = 490;
	            double attackDelay = useRandom ? (ClientSettings.APSn - Helper.mathUtils().getRandom((int) ClientSettings.rndn)) : ClientSettings.APSn;
	            if (ticks >= (20 / attackDelay)) {
	                delay++;
	                if (critTimer.hasTimeElapsed((long) cd, true)) {
	                    if (!ClientSettings.armorbreakern)
	                        Criticals.crit();
	                }
	                
	                if (potTimer.hasTimeElapsed(10, false))
	                	
	                	if (!mc.thePlayer.isSprinting()){
	            			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            					C0BPacketEntityAction.Action.START_SPRINTING));
	            			mc.thePlayer.setSprinting(true);
	            		
	            			}
	            			if (!mc.thePlayer.isSprinting()){
	            				mc.thePlayer.setSprinting(false);
	            				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            						C0BPacketEntityAction.Action.STOP_SPRINTING));
	            			
	            			}
	            			if (!mc.thePlayer.isSprinting()){
	            				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            						C0BPacketEntityAction.Action.START_SPRINTING));
	            				mc.thePlayer.setSprinting(true);
	            				
	            				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            						C0BPacketEntityAction.Action.STOP_SPRINTING));
	            				mc.thePlayer.setSprinting(false);
	            			
	            			}
	            			
	                    doAttack(getTarget());
	                    if (!mc.thePlayer.isSprinting()){
	            			mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            					C0BPacketEntityAction.Action.START_SPRINTING));
	            			mc.thePlayer.setSprinting(true);
	            		
	            			}
	            			if (!mc.thePlayer.isSprinting()){
	            				mc.thePlayer.setSprinting(false);
	            				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            						C0BPacketEntityAction.Action.STOP_SPRINTING));
	            			
	            			}
	            			if (!mc.thePlayer.isSprinting()){
	            				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            						C0BPacketEntityAction.Action.START_SPRINTING));
	            				mc.thePlayer.setSprinting(true);
	            				
	            				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer,
	            						C0BPacketEntityAction.Action.STOP_SPRINTING));
	            				mc.thePlayer.setSprinting(false);
	            			
	            			}
	                    
	                    
	                ticks = 0;
	                changeTargets();
	            }
	        }
	        if (canBlock() && Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null &&  isValidEntity(lol))
	            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
	    		 }
	     }
	    	 }
	     
	    
	    public void doAttack(Entity target) {
	        if(mc.thePlayer.isBlocking() && !NoSlowdown.noslowing)
	            Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));

	        if (duraTimer.hasTimeElapsed(370L, true) && target instanceof EntityPlayer && ClientSettings.armorbreakern) {
	            
	            doCritAttack(target, false);
	            doCritAttack(target, true);
	            return;
	        }
	        Helper.player().swingItem();
	        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
	        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT));
	    }
	    private void changeTargets(){
	        if(attackList.size() == 1)
	            return;
	        if(ClientSettings.sdelayn == 0)
	            currentTarget += 1;
	        if(delay >= ClientSettings.sdelayn){
	            currentTarget += 1;
	            delay = 0;
	        }
	        
	    }
	  
	   
	    public boolean MovementInput(){
	    	
			return (Helper.mc().gameSettings.keyBindForward.isPressed() || Helper.mc().gameSettings.keyBindBack.isPressed() ||
			Helper.mc().gameSettings.keyBindLeft.isPressed() || Helper.mc().gameSettings.keyBindRight.isPressed() || ((Helper.mc().gameSettings.keyBindSneak.isPressed() && !Helper.player().isCollidedVertically) || Helper.mc().gameSettings.keyBindJump.isPressed()));
		}
	    


	    public EntityLivingBase getTarget() {
	        
	        return Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn);
	    }

	  

	    @Override
	    public void onUpdate(UpdateEvent e) {
	    	super.onUpdate();
	    	  if (currentMode.equals("Tick")){
	    	
	    		  if(Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null) {
			    		 lol1 = Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn);
			    		 if (isValidEntity(lol1)){
		    		 float[] prot2 = RotationUtil.faceEntity(getTarget(), 5000.0f, 5000.0f);
			            if (prot2 != null) {
			            	 if (ClientSettings.lockviewn) {
				 		            mc.thePlayer.rotationYaw = prot2[0];
				 		            mc.thePlayer.rotationPitch = prot2[1];
				 		            
				 		           
				 		        
				 	    }
				            	 else{
				                    e.yaw = prot2[0];
				                    e.pitch = prot2[1];
				                 
				                   
				                
				    }
			            }
	    	        
	    	    }
			    		 
	    	        if (currentTarget >= attackList.size())
	    	            currentTarget = 0;
	    	        if (!canAttack()) {
	    	            
	    	            
	    	            override = true;
	    	            return;
	    	        }

	    	      
	    	            potted = false;
	    	           
	    	        
	    	       
	    	        sortTargets();
	    	        
	    	       
			    	
			    	 
			    	
			            
			    		 
			    	}
	    	  }
	    	    int d = 467, i = 0;
	    	    
	    	if (currentMode.equals("Switch")){
	    		
		    	 if(Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn) != null) {
		    		 lol = Utils.getClosestEntitySkipValidCheck((float)ClientSettings.reachn);
		    		 if (isValidEntity(lol)){
		    			 
	    		 float[] prot = RotationUtil.faceEntity(getTarget(), 5000.0f, 5000.0f);
		            if (prot != null) {
		            	 if (ClientSettings.lockviewn) {
		            		
		        
		 		            mc.thePlayer.rotationYaw = prot[0];
		 		            mc.thePlayer.rotationPitch = prot[1];
		 		          
		 		           
		 		        
		 	    }
		            	 else{
		            		
		                    e.yaw = prot[0];
		                    e.pitch = prot[1];
		                   
		 		           e.yaw = prot[0];
		                    e.pitch = prot[1];
		                    
		                   
		                
		    }
		            }
	    	 }
		        if (currentTarget >= attackList.size())
		            currentTarget = 0;
		        ticks++;
		        if (ticks > 20) ticks = 20;
		        if (!canAttack()) {
		            
		            
		            override = true;
		            return;
		        }
		        
		        sortTargets();
		        

		        
		       
                
		       
		       
	            
		    	}
	    }
			    		 
			    	 
	    	  
	    
	    }
	    @Override
	    public void onDisable(){
	        super.onDisable();
	        if (currentMode.equals("Tick")){
	            currentTarget = ticks = 0;
	            
	            override = true;
	            super.onDisable();
	        }
	        if (currentMode.equals("Switch")){
		    	super.onDisable();
		        currentTarget = ticks = 0;
		        
		        override = true;
		        if(!MovementInput())
		            Helper.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
		    }
	        attackList.clear();
	        
	        Helper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
	   
	    }
	    
	    @Override
		public ModSetting[] getModSettings() {
	    	///public Switch switchMode = new Switch(this);
	        ///public Tick tickMode = new Tick(this);
	        ///public Multi multiMode = new Multi(this);

	      /* 
	      
	        public DoubleProperty ticks = new DoubleProperty(this, "Exist Ticks", 30, 0, 100);
	        public DoubleProperty reach = new DoubleProperty(this, "Reach", 4.4, 1, 10, 1);
	        public DoubleProperty FOV = new DoubleProperty(this, "FOV", 360, 15, 360);

	        public DoubleProperty APS = new DoubleProperty(this, "APS", 12, 1, 20);
	        public DoubleProperty rnd = new DoubleProperty(this, "Randomization", 4, 0 , 20);
	        public DoubleProperty sdelay = new DoubleProperty(this, "Switch Delay", 10, 0, 20);

	        public DoubleProperty multiDelay = new DoubleProperty(this, "Multi Delay", 500, 100, 800);
	        public DoubleProperty targets = new DoubleProperty(this, "Multi Targets", 4, 1, 50);

	        public Property<Sorting> sorting = new Property<>(this, "Sorting", Sorting.Crosshair);
	        public Property<AuraMode> mode = new Property<>(this, "Mode", switchMode);
*/
	    	SliderSetting<Number> Slider1 = new SliderSetting<Number>("Exist Ticks", ClientSettings.ticksn, 0, 100, 0.0, ValueFormat.DECIMAL);
	    	SliderSetting<Number> Slider2 = new SliderSetting<Number>("Reach", ClientSettings.reachn, 1.0, 6.0, 0.0, ValueFormat.DECIMAL);
	    	SliderSetting<Number> Slider3 = new SliderSetting<Number>("FOV", ClientSettings.FOVn, 15, 360, 1.0, ValueFormat.DECIMAL);
	    	SliderSetting<Number> Slider4 = new SliderSetting<Number>("APS", ClientSettings.APSn, 1.0, 20.0, 0.0, ValueFormat.DECIMAL);
	    	SliderSetting<Number> Slider5 = new SliderSetting<Number>("Randomization", ClientSettings.rndn, 0, 20.0, 0.0, ValueFormat.DECIMAL);
	    	SliderSetting<Number> Slider6 = new SliderSetting<Number>("Switch Delay", ClientSettings.sdelayn, 0.1, 20, 0.0, ValueFormat.DECIMAL);
	    	
	    	CheckBtnSetting Box1 = new CheckBtnSetting("AutoBlock", "autoblockn");
	    	CheckBtnSetting Box2 = new CheckBtnSetting("ArmorBreaker", "armorbreakern");
	    	CheckBtnSetting Box3 = new CheckBtnSetting("Players", "playersn");
	    	CheckBtnSetting Box4 = new CheckBtnSetting("Animals", "animalsn");
	    	CheckBtnSetting Box5 = new CheckBtnSetting("Monsters", "monstersn");
	    	CheckBtnSetting Box6 = new CheckBtnSetting("Invisibles", "invisiblesn");
	    	CheckBtnSetting Box7 = new CheckBtnSetting("Friends", "friendsn");
	    	CheckBtnSetting Box8 = new CheckBtnSetting("NoSwing", "noswingn");
	    	CheckBtnSetting Box9 = new CheckBtnSetting("LockView", "lockviewn");
	    	CheckBtnSetting Box10 = new CheckBtnSetting("Death", "deathn");
	    	CheckBtnSetting Box11 = new CheckBtnSetting("Team", "teamn");
	    	CheckBtnSetting GWEN = new CheckBtnSetting("Anti GWEN", "GWEN");
		    
	    	
	    	
			
			return new ModSetting[] { Slider1, Slider2 , Slider3 , Slider4 , Slider5 , Slider6 , Box1, Box2, Box3, Box4, Box5, Box6, Box7, Box8, Box9, Box10 , Box11, GWEN};
		}
	    
	    public void doCritAttack(Entity target, boolean crit) {
	    	
	        if(ClientSettings.noswingn) Helper.sendPacket(new C0APacketAnimation());
	        else Helper.player().swingItem();
	        if (crit) Criticals.crit();
	        else Helper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, true));
	        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
	    }
	    public void doCritAttack(Entity target, boolean crit, boolean swing) {
	        if(swing)
	            Helper.player().swingItem();
	        if (crit)
	            Criticals.crit();
	        else Helper.sendPacket(new C03PacketPlayer(true));
	        Helper.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
	    }
	    public boolean canBlock() {
	        if (Helper.player().getHeldItem() == null)
	            return false;
	        if(Helper.player().isBlocking() || (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))
	            return true;
	        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isPressed())
	            return true;
	        if ((Helper.player().getHeldItem().getItem() instanceof ItemSword  && ClientSettings.autoblockn))
	            return true;
	        return false;
	    }
	    public void setupTargets() {
	        if(Minecraft.theWorld == null)
	            return;
	        Minecraft.theWorld.loadedEntityList.forEach(o -> {
	            Entity e = (Entity)o;
	            if(attackList.size() >= 100) return;
	            if(isValidTarget(e) && !attackList.contains(e))
	                attackList.add(e);
	            if(!isValidTarget(e) && attackList.contains(e))
	                attackList.remove(e);
	            if(mc.thePlayer.getDistanceToEntity(e) > 15 && attackList.contains(e))
	                attackList.remove(e);

	        });
	    }
	    private boolean IsntMineplexBot(Entity e){
	    	if (e instanceof EntityPlayer) {
	    	
	    	
				if (!((EntityPlayer) e).didSwingItem) {
					if (e.onGround) {
						if (e.isSprinting()) {
							return true;
						}
					} else {
						if (e.hurtResistantTime == 0) {
							return false;
						}
					}
				}
				return true;
			
	    }
	    	else {
	    		return true;
	    	}
	    }
	    private boolean isValidEntity(Entity e){
	    	if (e instanceof EntityVillager) {
	    		return false;
	    	}
	    	if (!IsntMineplexBot(e)) {
	    		return false;
	    	}
	        if (ClientSettings.animalsn && e instanceof EntityAnimal)
	            return true;

	        if (ClientSettings.TEAMMM && Helper.isOnSameTeam((EntityLivingBase)e, mc.thePlayer, true)) {
	        	return false;
	        }
	        if (ClientSettings.playersn && e instanceof EntityPlayer)
	            return true;

	        if (ClientSettings.monstersn && (e instanceof EntityMob || e instanceof EntitySlime || e instanceof EntityWither || e instanceof EntityDragon))
	            return true;
	        return false;
	    }
	    
	    public void sortTargets() {
	       
	    	attackList.sort((o1, o2) -> {
                double rot1 = Helper.entityUtils().getRotationToEntity(o1)[0];
                double rot2 = Helper.entityUtils().getRotationToEntity(o2)[0];
                return (rot1 < rot2) ? 1 : (rot1 == rot2) ? 0 : -1;
            });
	    }
	             /*   break;
	            case Range:
	                attackList.sort((o1, o2) -> {
	                    double range1 = Helper.player().getDistanceToEntity(o1);
	                    double range2 = Helper.player().getDistanceToEntity(o2);
	                    return (range1 < range2) ? -1 : (range1 == range2) ? 0 : 1;
	                });
	                break;
	            case Health:
	                attackList.sort((o1, o2) -> {
	                    double h1 = ((EntityLivingBase) o1).getHealth();
	                    double h2 = ((EntityLivingBase) o2).getHealth();
	                    return (h1 < h2) ? -1 : (h1 == h2) ? 0 : 1;
	                });
	                break;
	            case Crosshair:
	                attackList.sort((o1, o2) -> {
	                    double rot1 = Helper.entityUtils().getRotationToEntity(o1)[0];
	                    double rot2 = Helper.entityUtils().getRotationToEntity(o2)[0];
	                    double h1 = (mc.thePlayer.rotationYaw - rot1) ;
	                    double h2 = (mc.thePlayer.rotationYaw - rot2) ;
	                    return (h1 < h2) ? -1 : (h1 == h2) ? 0 : 1;
	                });
	                break;
	        }*/
	    

	    public static float[] getRotations(Entity entity){
	        if(entity == null)
	            return null;

	        double diffX = entity.posX - mc.thePlayer.posX;
	        double diffZ = entity.posZ - mc.thePlayer.posZ;
	        double diffY;
	        if(entity instanceof EntityLivingBase){
	            EntityLivingBase elb = (EntityLivingBase) entity;
	            diffY = elb.posY
	                    + (elb.getEyeHeight() - 0.4)
	                    - (mc.thePlayer.posY + mc.thePlayer
	                    .getEyeHeight());
	        }else
	            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY)
	                    / 2.0D
	                    - (mc.thePlayer.posY + mc.thePlayer
	                    .getEyeHeight());
	        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
	        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

	        return new float[] { yaw, pitch };
	    }

	    private boolean isValidTarget(Entity en){

	        return Helper.player().getDistanceToEntity(en) <= ClientSettings.reachn && (en.isInvisible() ? ClientSettings.invisiblesn : true) && en.isEntityAlive() &&
	                isValidEntity(en) && (en != Helper.player()) && en.ticksExisted >= ClientSettings.ticksn && Helper.entityUtils().isWithingFOV(en, (int)ClientSettings.FOVn) && (ClientSettings.TEAMMM && !Helper.isOnSameTeam((EntityLivingBase)en, mc.thePlayer, true) &&
	                (FriendsMananger.isFriend(en.getName()) ? ClientSettings.friendsn : true));
	    }

	    

	    private boolean isOnSameTeam(Entity entity) {
	        boolean team = false;

	        if(ClientSettings.teamn && entity instanceof EntityPlayer) {
	            String n = entity.getDisplayName().getFormattedText();
	            if(n.startsWith('\u00a7' + "f") && !n.equalsIgnoreCase(entity.getName()))
	                team = (n.substring(0, 6).equalsIgnoreCase(mc.thePlayer.getDisplayName().getFormattedText().substring(0, 6)));
	            else team = (n.substring(0,2).equalsIgnoreCase(mc.thePlayer.getDisplayName().getFormattedText().substring(0,2)));
	        }

	        return team;
	    }

	   

	    private enum Sorting {
	        Range, Health, Angle, Crosshair;
	    }
	    public boolean hasPlayerNear(){
	        return mc.theWorld.loadedEntityList.stream().anyMatch(e -> (e instanceof EntityPlayer) &&
	                !(e instanceof EntityArmorStand) && mc.thePlayer.getDistanceToEntity((EntityLivingBase) e) < 8
	                && !(e instanceof EntityPlayerSP));
	    }
	   

	   
	    @Override
		public String[] getModes() {

			return new String[] { "Tick", "Switch"};
		}





	    

	    
	}
