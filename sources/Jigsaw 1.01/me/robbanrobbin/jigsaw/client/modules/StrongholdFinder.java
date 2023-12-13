package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.util.math.Vec3d;
import strongholdfinder.Intersection;
import strongholdfinder.NotEnoughPresicionException;
import strongholdfinder.Vector;

public class StrongholdFinder extends Module {
	
	private EntityEnderEye eye1;
	private boolean eye1Done = false;
	
	private EntityEnderEye eye2;
	private boolean eye2Done = false;
	
	Vector v1s;
	Vector v1e;
	
	Vector v2s;
	Vector v2e;
	
	Vector intersection;
	
	private WaitTimer eyeTimer = new WaitTimer();

	public StrongholdFinder() {
		super("StrongholdFinder", Keyboard.KEY_NONE, Category.EXPLOITS, "Triangulates the position of the stronghold after throwing two eye of enders.");
	}
	
	@Override
	public void onToggle() {
		super.onToggle();
		eye1 = null;
		eye2 = null;
		eye1Done = false;
		eye2Done = false;
		intersection = null;
	}

	@Override
	public void onUpdate() {
		
		if(eyeTimer.hasTimeElapsed(5000, false) && eye2Done) {
			eye1Done = false;
			eye2Done = false;
			eye1 = null;
			eye2 = null;
		}
		
		for(Entity entity : mc.world.loadedEntityList) {
			if(entity instanceof EntityEnderEye) {
				EntityEnderEye enderEye = (EntityEnderEye)entity;
				
				if(eye1 == null && !enderEye.isEntityEqual(eye2)) {
					eye1 = enderEye;
					Jigsaw.chatMessage("First eye found, please throw the next one a lot further away!");
				}
				else if(eye2 == null && !enderEye.isEntityEqual(eye1)) {
					eye2 = enderEye;
					Jigsaw.chatMessage("Second eye found, calculating intersection...");
				}
			}
		}
		
		if(eye1 != null && !eye1Done) {
			
			if(eye1.ticksExisted < 10 || eye1.ticksExisted > 20) {
				return;
			}
			
			v1s = new Vector(eye1.lastTickPosX, eye1.lastTickPosZ);
			v1e = new Vector(eye1.posX, eye1.posZ);
			
			eye1Done = true;
			
		}
		
		if(eye2 != null && !eye2Done) {
			
			if(eye2.ticksExisted < 10  || eye2.ticksExisted > 20) {
				return;
			}
			
			v2s = new Vector(eye2.lastTickPosX, eye2.lastTickPosZ);
			v2e = new Vector(eye2.posX, eye2.posZ);
			
			eye2Done = true;
			
		}
		
		if(eye1Done && eye2Done && eyeTimer.hasTimeElapsed(6000, false)) {
			eyeTimer.reset();
			
			try {
				intersection = Intersection.GetIntersection(
						v1s, 
						v1e, 
						v2s, 
						v2e);
			}
			catch(NotEnoughPresicionException e) {
				Jigsaw.chatMessage("§cThe two ender pearls flew the same path, please throw them a lot further away!");
				Jigsaw.getNotificationManager().addNotification(new Notification(Level.ERROR, "The two ender pearls flew the same path, please throw them a lot further away!"));
				return;
			}
			
			
			intersection.x -= 3;
			intersection.y -= 3;
			
			Jigsaw.chatMessage("Done! " + "x: " + intersection.x + ", z:" + intersection.y);
			
		}
		
		super.onUpdate();
	}
	
	@Override
	public void onRender() {
		super.onRender();
		
		if(intersection != null) {
			
			Vec3d pos = RenderTools.getRenderPos(intersection.x, 9, intersection.y);
			
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			RenderTools.draw3DLine(pos.x, pos.y, pos.z, pos.x, 256, pos.z, 1f, 0.2f, 0.2f, 0.7f, 2f);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			RenderTools.drawTracerLine(pos.x, pos.y + 30, pos.z, 1f, 0.2f, 1f, 1f, 1f);
			
		}
		
		
		
	}

}
