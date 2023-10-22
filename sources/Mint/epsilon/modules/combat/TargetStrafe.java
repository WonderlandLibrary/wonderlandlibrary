package epsilon.modules.combat;


import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.modules.Module.Category;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.MoveUtil;
import epsilon.modules.combat.KillAura;
import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TargetStrafe extends Module {

	private final MoveUtil move = new MoveUtil();
	
	private float distance;
	
	private boolean direction;
	
	private Entity target;

	
	public ModeSetting mode = new ModeSetting ("Mode", "Vanilla", "Vanilla", "AGC", "Verus", "Position");
    public NumberSetting radius = new NumberSetting("Radius", 2, 0, 5, 0.1);
    public BooleanSetting jumpOnly = new BooleanSetting("JumpOnly", false);
    public BooleanSetting nospeed = new BooleanSetting("NotSpeed", false);
    public BooleanSetting dmgonly = new BooleanSetting("VanillaDMGonly", false);
    public NumberSetting speed = new NumberSetting("Speed", 0.1, 0.02, 5, 0.1);
    public static double rad;

    public TargetStrafe() {
        super("TargetStrafe", Keyboard.KEY_NONE, Category.COMBAT, "Spinyfish coded this retardedlyTM");
        this.addSettings(mode,radius, nospeed, dmgonly,speed);
    }
    public void onDisable() {
    	target = null;
    }
    
    public void onEvent(Event e){
    	target = KillAura.target;
		if(e instanceof EventMotion){
			EventMotion event = (EventMotion)e;
			this.displayInfo = mode.getMode();
			if(target != null && !target.isDead) {
				rad = radius.getValue();
				
				switch(mode.getMode()) {
				
				case "Vanilla":
					if(mc.thePlayer.onGround)
						mc.thePlayer.jump();
					if(dmgonly.isEnabled()) {
						if( mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<15) {
							if(nospeed.isEnabled())
							move.strafeT(move.getSpeed());
							else
								move.strafeT(move.getBaseMoveSpeed() + speed.getValue());
						}	
					}else {
						if(nospeed.isEnabled())
							move.strafeT(move.getSpeed());
						else
							move.strafeT(move.getBaseMoveSpeed() + speed.getValue());
					}
					break;
					
				case "AGC":
					if(mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<15) {
						if(mc.thePlayer.onGround) 
							mc.thePlayer.jump();
						move.strafeT(move.getBaseMoveSpeed() + 0.2);
					}
					break;
					
				case "Verus":
					if(mc.thePlayer.onGround) 
						mc.thePlayer.jump();
					
					move.strafeT(move.getBaseMoveSpeed()-(Math.random())/25);
					if(mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<15) {
						move.strafeT(move.getBaseMoveSpeed() + 0.3-(Math.random())/25);
						
					}
					
					
					
					break;
					
				case "Position":

					if(mc.thePlayer.getDistanceToEntity(KillAura.target)<6)
						mc.thePlayer.setPosition(Math.round(KillAura.target.posX), mc.thePlayer.posY, Math.round(KillAura.target.posZ));
					break;
					
				
				}	
 			}	
		}
    }	
}
