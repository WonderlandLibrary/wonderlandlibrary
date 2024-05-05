package me.aidanmees.trivia.client.tools;

import java.beans.EventHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class AntibotHelper
{
    private static Map<Integer, Double> distanceMap;
    private static Set<Integer> swingSet;
   
    public static boolean isEntityBot(final Entity entity) {
        if (!AntibotHelper.distanceMap.containsKey(entity.getEntityId())) {
            return false;
        }
        final double distance = AntibotHelper.distanceMap.get(entity.getEntityId());
        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            return false;
        }
      
            return (entity.posY != 0.0 && distance > 6.0 && distance < 10.0 && !AntibotHelper.swingSet.contains(entity.getEntityId())) || entity.getName().contains("§");
        

    }
}
