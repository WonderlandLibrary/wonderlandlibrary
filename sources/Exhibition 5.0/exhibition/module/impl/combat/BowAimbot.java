// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.Iterator;
import java.util.List;
import exhibition.management.friend.FriendManager;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import exhibition.event.impl.EventPacket;
import exhibition.event.RegisterEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import exhibition.util.RotationUtils;
import net.minecraft.item.ItemBow;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class BowAimbot extends Module
{
    boolean send;
    boolean isFiring;
    
    public BowAimbot(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMotion.class, EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                final EntityLivingBase target = this.getTarg();
                if (BowAimbot.mc.thePlayer.isUsingItem() && BowAimbot.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && target != BowAimbot.mc.thePlayer && target != null) {
                    final float[] rotations = RotationUtils.getBowAngles(target);
                    em.setYaw(rotations[0]);
                    em.setPitch(rotations[1]);
                }
            }
        }
    }
    
    private EntityLivingBase getTarg() {
        final List<EntityLivingBase> loaded = new ArrayList<EntityLivingBase>();
        for (final Object o3 : BowAimbot.mc.theWorld.getLoadedEntityList()) {
            if (o3 instanceof EntityLivingBase) {
                final EntityLivingBase ent = (EntityLivingBase)o3;
                if (!(ent instanceof EntityPlayer) || !BowAimbot.mc.thePlayer.canEntityBeSeen(ent) || BowAimbot.mc.thePlayer.getDistanceToEntity(ent) >= 15.0f || FriendManager.isFriend(ent.getName())) {
                    continue;
                }
                if (ent == Killaura.vip) {
                    return ent;
                }
                loaded.add(ent);
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        final float[] rot1;
        final float[] rot2;
        loaded.sort((o1, o2) -> {
            rot1 = RotationUtils.getRotations(o1);
            rot2 = RotationUtils.getRotations(o2);
            return (int)(RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationYaw, rot1[0]) + RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationPitch, rot1[1]) - (RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationYaw, rot2[0]) + RotationUtils.getDistanceBetweenAngles(BowAimbot.mc.thePlayer.rotationPitch, rot2[1])));
        });
        final EntityLivingBase target = loaded.get(0);
        return target;
    }
}
