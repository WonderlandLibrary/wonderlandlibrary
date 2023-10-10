package summer.cheat.cheats.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.RotationUtils;
import summer.base.utilities.TimerUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventMotion;

public class BetterAura extends Cheats {

	public BetterAura() {
		super("BetterKillAura", "Sheeeeesh", Selection.COMBAT);
	}
	
	
	TimerUtils t = new TimerUtils();
	
	public static EntityLivingBase target;
	
	@EventTarget
	public void onMotion(EventMotion e) {
		
		ArrayList<EntityLivingBase> targets = new ArrayList<>();
		
		for(Entity ent : mc.theWorld.loadedEntityList) {
		
			if(ent instanceof EntityLivingBase) {
				targets.add((EntityLivingBase) ent);
			}
			
		}
		
		targets = (ArrayList<EntityLivingBase>) targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= 5 && entity !=mc.thePlayer && !entity.isDead && entity.getHealth() > 0).collect(Collectors.toList());

        targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));
		
		
        if(!targets.isEmpty()) {
        	
        	EntityLivingBase target = targets.get(0);
        	
        	this.target = target;
        
        	
        	if(target != null) {
        		
        		mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 13);
        		
        		if(t.hasReached(50)) {
        			
        			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
        			mc.thePlayer.swingItem();
        			
        		}
        		
        	}
        	
        	
        }
		
	}

}
