package Velo.api.Util.Other.other;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class CombatUtil {
	private static Minecraft mc = Minecraft.getMinecraft();
	public static List<Entity> getTargets(){
		List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < 4 && entity != mc.thePlayer && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0).collect(Collectors.toList());
		
		targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
			return targets;

		
	}
}
