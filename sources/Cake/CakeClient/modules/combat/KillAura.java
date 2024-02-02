package CakeClient.modules.combat;

import java.util.Iterator;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;
import CakeClient.modules.Module;

public class KillAura extends Module
{
    public Float activationRange;
    public Float attackRange;
    public Boolean attackStorm = false;
    
    public KillAura() {
        super("KillAura");
        this.activationRange = 208.0f;
        this.attackRange = 4.0f;
    }
    
    public void onLeftConfig() {attackStorm = false; }
    public void onRightConfig() {attackStorm = true; }
    public String getConfigStatus()
    {
    	if (attackStorm) return "attack storm ON";
    	else return "attack storm OFF";
    }
    
    public void attack(Entity target)
    {
    	if (target != null)
    	{
    		this.mc.thePlayer.swingItem();
    		this.mc.thePlayer.onCriticalHit(target);
            this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    	}
    }
    
    public Float distanceTo(Entity target)
    {
    	return target.getDistanceToEntity(this.mc.thePlayer);
    }
    
    @Override
    public void onUpdate() {
    	Float closestDistance = this.activationRange;
        EntityLivingBase closestTarget = null;
    	List<Entity> targets = (List<Entity>)this.mc.theWorld.getLoadedEntityList();
        for (final Entity target : targets) {
            if (target instanceof EntityLivingBase && !(target instanceof EntityArmorStand) && target != this.mc.thePlayer && distanceTo(target) <= attackRange) {
            	if (attackStorm)
            	{
            		attack(target);
            	}
            	
            	else if (distanceTo(target) < closestDistance)
            	{
            		closestTarget = (EntityLivingBase) target;
            		closestDistance = distanceTo(target);
            	}
            }
        }
        if (!attackStorm)
        {
        	attack(closestTarget);
        }
    	/*
        Float closestDistance = this.activationRange;
        EntityLivingBase closestTarget = null;
        final List<Entity> targets = (List<Entity>)this.mc.theWorld.getLoadedEntityList();
        for (final Entity target : targets) {
            if (target instanceof EntityLivingBase && !(target instanceof EntityArmorStand) && target != this.mc.thePlayer && target.getDistanceToEntity((Entity)this.mc.thePlayer) < closestDistance) {
                
            	closestDistance = target.getDistanceToEntity((Entity)this.mc.thePlayer);
                closestTarget = (EntityLivingBase)target;
            }
        }
        if (closestTarget != null && closestTarget.getDistanceToEntity((Entity)this.mc.thePlayer) <= this.attackRange) {
            this.mc.thePlayer.swingItem();
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity((Entity)closestTarget, C02PacketUseEntity.Action.ATTACK));
        }
        */
    }
}