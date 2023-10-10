package Reality.Realii.mods.modules.player;

import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.mods.modules.movement.NoSlow;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.mods.modules.world.SpeedMine;
import Reality.Realii.utils.render.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3i;
import net.minecraft.block.BlockBed;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.World;

import java.awt.Color;

import org.lwjgl.opengl.GL11;


public class BedNuker
extends Module {

	 private Option Swing = new Option("Swing", false);
    public BedNuker(){
        super("BedNuker", ModuleType.Player);
       
       this.addValues(Swing);
      
       
    

     // Get the player's position
    
    }
   
    
    int detectionRange = 3;
    
    
    public static float[] getRotationsToBlock(double posX, double posY, double posZ, EnumFacing facing) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        double x = posX - player.posX - 0.5;
        double y = (posY - player.posY - 0.5) + 1.82;
        double z = posZ - player.posZ - 0.5;
        
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);
        
        switch(facing) {
            case NORTH:
                yaw += 180.0F;
                break;
            case SOUTH:
                yaw += 0.0F;
                break;
            case WEST:
                yaw += 90.0F;
                break;
            case EAST:
                yaw -= 90.0F;
                break;
            default:
                break;
        }
        
        return new float[] { yaw, pitch };
    }
    
    
    
    
   
    
 


    
    @EventHandler
    public void onPre(EventPreUpdate e) {
    	
    	
    	
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix("Normal");
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	
    	 
            
    	 if (Client.instance.getModuleManager().getModuleByClass(Killaura.class).isEnabled()) {
    		 return;
    	 }
    		for (int x = (int) mc.thePlayer.posX  - detectionRange; x <= mc.thePlayer.posX  + detectionRange; x++) {
         	    for (int y = (int) mc.thePlayer.posY - detectionRange; y <= mc.thePlayer.posY + detectionRange; y++) {
         	        for (int z = (int) mc.thePlayer.posZ - detectionRange; z <= mc.thePlayer.posZ + detectionRange; z++) {
         	           
         	            Block block = mc.theWorld.getBlock(x, y, z);
         	          
         	          if (block == Blocks.bed) {

         	        	 
         	     
         	       	  //mc.theWorld.destroyBlock(null, enabledOnStartup
         	        
         	            	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(  C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(x, y, z), EnumFacing.UP));
         	            			
         	                      
         	            	 if ((boolean) Swing.getValue()) {
         	            		mc.thePlayer.swingItem();
 	            			 }
         	            	
         	            	
         	            	if (mc.thePlayer.ticksExisted % 5 == 0) {
         	            		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(x, y, z), EnumFacing.UP));
             	                    
         	            	}
         	        	 
         	        	 
         	        	
         	            	
         	            	
         	                }
         	    }
         	}
         	    
         	    
         	    
         	   //breakBedsInRange(null, 5);
        	
        	
        	
         	}
        }
    
    
   
    
    
    public void breakBedsInRange(BlockPos centerPos, int range) {
        int rangeSq = range * range;
        for (int x = centerPos.getX() - range; x <= centerPos.getX() + range; x++) {
            for (int y = centerPos.getY() - range; y <= centerPos.getY() + range; y++) {
                for (int z = centerPos.getZ() - range; z <= centerPos.getZ() + range; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (centerPos.distanceSq(pos) <= rangeSq) {
                        if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed) {
                            EnumFacing facing =  mc.theWorld.getBlockState(pos).getValue(BlockBed.FACING);
                            Vec3i vec = facing.getDirectionVec();
                            BlockPos otherPos = pos.add(vec.getX(), vec.getY(), vec.getZ());
                            mc.theWorld.destroyBlock(pos, true);
                            mc.theWorld.destroyBlock(otherPos, false);
                        }
                    }
                }
            }
        }
    }
}
    

    
        
    



