package Velo.impl.Modules.combat;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.api.Util.Other.MovementUtil;
import Velo.api.Util.Other.RotationUtils;
import Velo.api.Util.Other.other.RotationUtil;
import Velo.api.Util.Render.RenderUtil;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class TargetStrafe extends Module {
	
	public static double dir = 1;
	
	public static Killaura ka = new Killaura();
    public  BooleanSetting space = new BooleanSetting("Hold Space", true);
	public static NumberSetting range = new NumberSetting("Range", 4, 1, 15, 0.1);
	
	public static boolean isEnable = false;
	
	public static TargetStrafe targetstrafe = new TargetStrafe();
	public static Entity entity;
	
	public TargetStrafe() {
		super("TargetStrafe",  "TargetStrafe", Keyboard.KEY_NONE, Category.COMBAT);
		this.loadSettings(range, space);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		mc.thePlayer.speedInAir = 0.02F;
		isEnable = false;
	}
	 public static void drawLinesAroundPlayer(Entity entity, RenderManager renderManager, double radius, float partialTicks, int points, float width, int color) {
	      GL11.glPushMatrix();
	      GL11.glDisable(3553);
	      GL11.glEnable(2848);
	      GL11.glHint(3154, 4354);
	      GL11.glDisable(2929);
	      GL11.glLineWidth(width/3);
	      GL11.glEnable(3042);
	      GL11.glBlendFunc(770, 771);
	      GL11.glDisable(2929);
	      GL11.glBegin(3);
	      double x = RenderUtil.interpolate(entity.prevPosX, entity.posX, partialTicks) - renderManager.viewerPosX;
	      double y = RenderUtil.interpolate(entity.prevPosY, entity.posY - (entity.posY - entity.lastTickPosY), partialTicks) - renderManager.viewerPosY;
	      double z = RenderUtil.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - renderManager.viewerPosZ;
	      RenderUtil.glColor(color);

	      for(int i = 0; i <= points; ++i) {
	         GL11.glVertex3d(x + radius * Math.cos((double)i * 3.141592653589793D * 2.0D / (double)points), y, z + radius * Math.sin((double)i * 3.141592653589793D * 2.0D / (double)points));
	      }

	      GL11.glEnd();
	      GL11.glDepthMask(true);
	      GL11.glDisable(3042);
	      GL11.glEnable(2929);
	      GL11.glDisable(2848);
	      GL11.glEnable(2929);
	      GL11.glEnable(3553);
	      GL11.glPopMatrix();
	 }
	      
	      public void onRender3DUpdate(EventRender3D event) {
	      Killaura killAura= new Killaura();
	        final EntityLivingBase target = ka.target;
         if (entity != null && entity != mc.thePlayer && killAura.target == entity) {
         drawLinesAroundPlayer(entity, mc.getRenderManager(), (Double)range.getValue(), event.getPartialTicks(), 12, 3.0F, Color.white.getRGB());
         drawLinesAroundPlayer(entity, mc.getRenderManager(), (Double)range.getValue(), event.getPartialTicks(), 12, 2.0F, Color.white.getRGB());
         }
	 }



	
	
	
    public void strafe(EventMovement e, final double moveSpeed) {
    	Killaura ka= new Killaura();
        final EntityLivingBase target = ka.target;
        float[] rotations = ka.getKaRotations(target);
        if (mc.thePlayer.getDistanceToEntity(target) <= range.getValue()) {
            MovementUtil.setSpeed(e, moveSpeed, rotations[0], dir, 0);
        } else {
            MovementUtil.setSpeed(e, moveSpeed, rotations[0], dir, 1);
        }
        
        
     //   if (mc.thePlayer.getDistanceToEntity(ta.entity) <= ta.range.getValue() - 2.3) {
     //       moveForward = 0;
   //     } else {
   //         moveForward = 1;
    //    }
        
        
        
    }
	
	
	@Override
	public void onPreMotionUpdate(EventPreMotion event) {
		if(mc.thePlayer.isCollidedHorizontally) {
			invertStrafe();
		}
		if(canStrafe()) {
			mc.thePlayer.movementInput.moveForward = 0;
		}
		
		
		
		if(Killaura.target != null) {
			
			EntityLivingBase target = Killaura.target;
			
			isEnable = true;
			
			entity = target;
			
		} else {	
			
			isEnable = false;
			
			entity = null;
		}
		super.onPreMotionUpdate(event);
	}
	
	
	@Override
	public void onMovementUpdate(EventMovement e) {

           if (mc.thePlayer.isCollidedHorizontally) {
               invertStrafe();
           }

           if (mc.gameSettings.keyBindLeft.isKeyDown()) {
        	   dir = 1;
           }
           if (mc.gameSettings.keyBindRight.isKeyDown()) {
               dir = -1;
           }
                      
       	BlockPos posx = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ);
       	BlockPos posz = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 01);
       	
           if(!MovementUtil.isBlockUnderneath(posx)) {
        	   dir = -1;
           }
           if(!MovementUtil.isBlockUnderneath(posz)) {
        	   dir = 1;
           }
           
           if(canStrafe() && MovementUtil.isBlockUnderneath(mc.thePlayer.getPosition()) && ((MovementUtil.isBlockUnderneath(posx) || !MovementUtil.isBlockUnderneath(posz) || (!MovementUtil.isBlockUnderneath(posx) || MovementUtil.isBlockUnderneath(posz))))) {
        	     if(space.isEnabled() ) {
             // 	mc.gameSettings.keyBindJump.pressed = true;
                 }
        	     
          strafe(e, MovementUtil.getSpeed());
           }

       	
		super.onMovementUpdate(e);
	}
	

	
	public static void invertStrafe() {
		dir = -dir;
	}
	
	public static boolean canStrafe() {
		EntityLivingBase target = Killaura.target;
	
		return (target != null && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= range.getValue() && MovementUtil.isBlockUnderneath(target.getPosition()));
	}
	
	
	
	  
}

