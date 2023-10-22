package epsilon.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TestAura extends Module {

	public NumberSetting reach = new NumberSetting ("Reach", 4, 0.1, 6, 0.1);
	
	public NumberSetting mincps = new NumberSetting("MinCPS", 8.5, 1, 20, 0.5);
	
	public NumberSetting maxcps = new NumberSetting("MaxCPS", 12, 1, 20, 0.5);	
	
	
	public ModeSetting aura = new ModeSetting ("Aura", "Single", "Single", "Multi");
	
	public ModeSetting singleswitch = new ModeSetting ("SingleSort", "Dist", "Dist", "Health", "Hurttime");
	
	public ModeSetting mode = new ModeSetting ("AutoBlock", "Vanilla", "Vanilla", "Watchdog", "NCP", "Bypass","Verus", "Vulcan", "Spartan", "Pre", "Post");
	
	public ModeSetting rot = new ModeSetting("Rotations", "Snap", "Snap", "None", "Shake", "Verus");
	

	public BooleanSetting invis = new BooleanSetting ("HitInvis", false);
	
	public BooleanSetting hitCheck = new BooleanSetting ("HitCheck", false);
	
	public BooleanSetting walls = new BooleanSetting ("ThruWalls", false);

	public BooleanSetting guiI = new BooleanSetting ("AttackInInv", false);
	
	public BooleanSetting guiC = new BooleanSetting ("AttackInChest", false);
	
	
	private boolean attacking, blocking;
	
	
	public TestAura() {
		super("Aura", Keyboard.KEY_NONE, Category.COMBAT, "Test aura");
		this.addSettings(reach, mincps, maxcps, mode, aura, singleswitch, rot, invis, hitCheck, walls, guiI, guiC);
	}
	

	public void onEnable() {
		
	}
	
	public void onDisable() {
		attacking = false;
	}
	
	public void onEvent(Event e) {
		if (e instanceof EventMotion) {
			
			List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			
			targets = targets.stream().filter(entity -> 
			entity.getDistanceToEntity(mc.thePlayer) < reach.getValue()
			
			&& entity != mc.thePlayer
			
			&& !entity.isDead
			
			&& entity.getHealth() > 0
			
			&& (mc.thePlayer.canEntityBeSeen(entity) || !walls.isEnabled())
			
			&& (entity.isInvisible() || !invis.isEnabled())
			
			//&& (!entity.isFriend() || hitFrends.isEnabled() add after friends manager is made
			
			&& !(entity instanceof EntityArmorStand)
			
					).collect(Collectors.toList());

			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
			
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
			
			if(e.isPre()) {
				
				switch(mode.getMode()) {
				
				case "Vanilla":
					
					block(true);
					
					break;
				}
			}
			
			if(e.isPost()) {
				
				switch(mode.getMode()) {
				
				
				}
				
				
			}
		}
		
		if (e instanceof EventUpdate) {
			
		}
	}

	private void block(final boolean sync) {
		
		if(sync) 
			mc.playerController.syncCurrentPlayItem();
		
		sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()), false);
		
	}
	
	private void unblock(final boolean sync) {
		
		if(sync) 
			mc.playerController.syncCurrentPlayItem();
		
		sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN), false);
	}
	
	private float[] getRot(final Entity t) {
		return KillAura.getRotations(t, rot.getMode());
	}
	
}
