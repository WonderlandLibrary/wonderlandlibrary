package tech.atani.client.utility.world.entities;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.VecUtil;

public class EntitiesUtil implements Methods {

    public static double getDistanceToEntityBox(Entity entity1, Entity entity2) {
        Vec3 eyes = entity1.getPositionEyes(1.0F);
        Vec3 pos = VecUtil.getNearestPointBB(eyes, entity2.getEntityBoundingBox());
        double xDist = Math.abs(pos.xCoord - eyes.xCoord);
        double yDist = Math.abs(pos.yCoord - eyes.yCoord);
        double zDist = Math.abs(pos.zCoord - eyes.zCoord);
        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2) + Math.pow(zDist, 2));
    }

    public static int getPing(EntityPlayer player) {
        NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(player.getUniqueID());
        return playerInfo != null ? playerInfo.getResponseTime() : 0;
    }

}
