package artemis.mods.Combat;

import java.util.Random;

import artemis.Category;
import artemis.module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;

public class TPaura extends module{

	
	Killaura Killaura;
	private Random r = new Random();
	
	public TPaura() {
		super("TPaura", "Teleports to players and attacks them", 0, 687, Category.COMBAT);
	}
	
	   public void onDisable() {
		   //Killaura.setToggled(false);
	        super.onDisable();
	      }
	    
	    public void onUpdate() {
	        if (isToggled()) {
	        	for(Object e : mc.theWorld.loadedEntityList) {
	        		if(e instanceof EntityPlayer) {
	        			if(e != mc.thePlayer) {
	        				
	        				EntityPlayer p = (EntityPlayer) e;
	        				
	        				double var1 = p.posX;
	        				double var2 = p.posY + 2;
	        				double var3 = p.posZ;
	        				
	        				
	        				if(!p.isInvisible()) {
	        					if(mc.thePlayer.getDistanceToEntity(p) <= 8) {
	        						
	        						
	        						mc.thePlayer.posX = var1;
	        						mc.thePlayer.posY = var2;
	        						mc.thePlayer.posZ = var3;
	        						
	        						mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(var1, var2, var3, mc.thePlayer.onGround));
	        						mc.thePlayer.setPositionAndUpdate(var1, var2, var3);
	        						if(mc.thePlayer.getDistanceToEntity(p) <= 6.2173613F) {
	        			                  if(p.isEntityAlive()) {
	        			                	  if(p.hurtTime < 10) {
	        			                		  mc.playerController.attackEntity(mc.thePlayer, p);
	        			                          mc.thePlayer.swingItem();
	    
	        					            }
	        			                }
	        						}
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
	     }
	    
	    
	    
	    
	    
      }
 