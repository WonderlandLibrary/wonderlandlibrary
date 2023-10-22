package epsilon.modules.combat;

import java.util.ArrayList;
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
import epsilon.util.BlockUtils;
import epsilon.util.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class TpAura extends Module {

	public ModeSetting target = new ModeSetting ("Target", "Single", "Single", "Multi");
	public static NumberSetting range = new NumberSetting ("Range", 64, 1, 160, 2);
	public NumberSetting cps = new NumberSetting ("CPS", 10, 1, 20, 2);
	public NumberSetting mt = new NumberSetting ("MaxMultiTargets", 2, 2, 20, 1);
	public BooleanSetting nofall = new BooleanSetting ("AntiFallDamage", true);
	
	private static double x;
	private double y;
	private double z;
	
	private List<EntityLivingBase> targetsInRange = new ArrayList<EntityLivingBase>();
	
    private ArrayList<Vec3> pathList;
	
	public TpAura() {
		super("InfiniteAura", Keyboard.KEY_NONE, Category.COMBAT, "Allows you to hit targets from essentially anywhere");
		this.addSettings(target, range, cps, mt, nofall);
	}
	
	public void onEvent(Event e) {
		
		
		if(e instanceof EventMotion) {
			if(e.isPre()) {
				refreshTargetList();
				if(targetsInRange == null) return;

				EntityPlayer sTarget = (EntityPlayer) targetsInRange.get(0);
				
				x = mc.thePlayer.posX; y = mc.thePlayer.posY; z = mc.thePlayer.posZ;
				
				targetsInRange.subList((int) mt.getValue(), targetsInRange.size()).clear();
					
			}
		}
		
	}
	
	private void refreshTargetList() {
		
		final double dist = range.getValue();
		
		targetsInRange = targetsInRange.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < dist && entity != mc.thePlayer && !entity.bot && !entity.isDead  && entity.getHealth() > 0).collect(Collectors.toList());
		
		targetsInRange.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
		
		targetsInRange = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		
		
	}


}
