package com.initial.modules.impl;

import net.minecraft.client.*;
import net.minecraft.entity.*;
import java.util.function.*;
import java.util.stream.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import java.util.*;
import com.initial.modules.impl.combat.*;
import com.initial.*;

public class Targets
{
    static Minecraft mc;
    
    public static List<Entity> getTargets(final double range, final boolean hitPlayers, final boolean hitVillagers, final boolean hitMobs, final boolean hitInvis) {
        List<Entity> targets = Targets.mc.theWorld.loadedEntityList.stream().filter(Objects::nonNull).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
        targets = targets.stream().filter(entity -> entity.getDistanceToEntity(Targets.mc.thePlayer) < range && entity != Targets.mc.thePlayer).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
        targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(Targets.mc.thePlayer)));
        final List<Entity> targetArray = new ArrayList<Entity>();
        for (final Entity entity2 : targets) {
            if (entity2 instanceof EntityAnimal) {
                targetArray.add(entity2);
            }
            if (hitPlayers && entity2 instanceof EntityPlayer) {
                targetArray.add(entity2);
            }
            if (hitVillagers && entity2 instanceof EntityVillager) {
                targetArray.add(entity2);
            }
            if (hitMobs && entity2 instanceof EntityMob) {
                targetArray.add(entity2);
            }
            if (hitInvis && entity2.isInvisible()) {
                targetArray.add(entity2);
            }
        }
        targets = targetArray;
        return targets;
    }
    
    public static List<Entity> getKillAuraTargets(final double range, final boolean hitPlayers, final boolean hitVillagers, final boolean hitMobs, final boolean hitInvis, final boolean hitAnimals, final boolean hitFriends, final boolean hitTargets, final boolean teamsEnabled) {
        List<Entity> targets = Targets.mc.theWorld.loadedEntityList.stream().filter(Objects::nonNull).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
        targets = targets.stream().filter(entity -> entity.getDistanceToEntity(Targets.mc.thePlayer) < range && entity != Targets.mc.thePlayer).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
        targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(Targets.mc.thePlayer)));
        final List<Entity> targetArray = new ArrayList<Entity>();
        for (final Entity entity2 : targets) {
            if (hitAnimals && entity2 instanceof EntityAnimal) {
                targetArray.add(entity2);
            }
            if (hitPlayers && entity2 instanceof EntityPlayer) {
                targetArray.add(entity2);
            }
            else if (!hitTargets || entity2 instanceof EntityPlayer) {}
            if (teamsEnabled && entity2 instanceof EntityPlayer && entity2.getDisplayName().getUnformattedText().length() > 2) {
                final char c = entity2.getDisplayName().getUnformattedText().charAt(1);
                final char playerC = Targets.mc.thePlayer.getDisplayName().getUnformattedText().charAt(1);
                if (c == playerC) {
                    targetArray.remove(entity2);
                }
            }
            if (hitVillagers && entity2 instanceof EntityVillager) {
                targetArray.add(entity2);
            }
            if (hitMobs && entity2 instanceof EntityMob) {
                targetArray.add(entity2);
            }
            if (hitInvis && entity2.isInvisible()) {
                targetArray.add(entity2);
            }
            if (((KillAura)Astomero.instance.moduleManager.getMod("KillAura")).botCheck.isEnabled() && entity2.getDisplayName().getUnformattedText().toLowerCase().contains("[npc]")) {
                targetArray.remove(entity2);
            }
        }
        targets = targetArray;
        return targets;
    }
    
    static {
        Targets.mc = Minecraft.getMinecraft();
    }
}
