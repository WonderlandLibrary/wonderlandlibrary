package wtf.evolution.module.impl.Combat;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.MathHelper;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "BowAimBot", type = Category.Combat)
public class BowAimBot extends Module {

    public Entity target;

    @EventTarget
    public void onUpdate(EventMotion e) {
        resolvePlayers();
        target = mc.world.getEntities(EntityLivingBase.class, this::isValid).get(0);
        if (target != null) {
            if (mc.player.getActiveItemStack().getItem() instanceof ItemBow) {
                rotate(e);

            }
        }
        releaseResolver();
    }

    public boolean isValid(Entity e) {
        if (e == mc.player) return false;
        if (e instanceof EntityPlayer) return true;
        if (mc.player.getDistance(e) < 50) return true;
        return false;
    }

    public void resolvePlayers() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player instanceof EntityOtherPlayerMP) {
                ((EntityOtherPlayerMP) player).resolve();
            }
        }
    }

    public void releaseResolver() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player instanceof EntityOtherPlayerMP) {
                ((EntityOtherPlayerMP) player).releaseResolver();
            }
        }
    }

    public double resolveX, resolveZ;

    public void rotate(EventMotion e) {
        resolveX = wtf.evolution.helpers.math.MathHelper.interpolate((target.posX - target.prevPosX) * 3, resolveX, 0.1);
        resolveZ = wtf.evolution.helpers.math.MathHelper.interpolate((target.posZ - target.prevPosZ) * 3, resolveZ, 0.1);
        double x = target.posX - mc.player.posX + ((target.posX - target.prevPosX) == 0 ? 0 : resolveX);
        double y = target.posY - mc.player.posY + mc.player.getDistance(target) / (25) + Math.abs((target.posY - target.prevPosY)) ;
        double z = target.posZ - mc.player.posZ + ((target.posZ - target.prevPosZ) == 0 ? 0 : resolveZ);
        double yaw = Math.atan2(z, x);
        double pitch = MathHelper.clamp(Math.atan2(y, Math.sqrt(x * x + z * z)), -90, 90);
        e.setYaw((float) (Math.toDegrees(yaw) - 90));
        e.setPitch(-(float) (Math.toDegrees(pitch)));
    }

}
