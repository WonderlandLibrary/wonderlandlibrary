package rip.helium.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import rip.helium.cheat.FriendManager;

public class MultiUtils {

    static Minecraft mc;

    public static Entity getTarget(final boolean mobs, final boolean players, final double range, final boolean invis) {
        for (final Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityLivingBase) {
                final EntityLivingBase o = (EntityLivingBase) obj;
                if (o.getDistanceToEntity(mc.thePlayer) > range) {
                    continue;
                }
                if (o.isInvisible() && !invis) {
                    continue;
                }
                if (o == mc.thePlayer) {
                    continue;
                }
                if (FriendManager.friends.contains(o.getName())) {
                    continue;
                }
                if ((o instanceof EntityMob || o instanceof EntityLiving || o instanceof EntityCreature || o instanceof EntityAnimal) && !mobs) {
                    continue;
                }
                if (o instanceof EntityPlayer && !players) {
                    continue;
                }
                return o;
            }
        }
        return null;
    }
}
