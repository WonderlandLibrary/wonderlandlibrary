package xyz.northclient.util.killaura;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import xyz.northclient.InstanceAccess;
import xyz.northclient.NorthSingleton;
import xyz.northclient.features.modules.KillAura;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TargetingUtil implements InstanceAccess {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static List<Entity> collectTargets() {
        List<Entity> ents = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        KillAura killaura = ((KillAura) NorthSingleton.INSTANCE.getModules().get("KillAura"));

        switch (killaura.sortMode.get().getName()) {
            case "Hurt":
                ents.sort(Comparator.comparingDouble(ent -> ((EntityLivingBase) ent).hurtTime).reversed());
            case "Health":
                ents.sort(Comparator.comparingDouble(ent -> ((EntityLivingBase) ent).getHealth()).reversed());
            case "Distance":
                ents.sort(Comparator.comparingDouble(ent -> ((EntityLivingBase) ent).getDistanceToEntity(mc.thePlayer)).reversed());
        }
        return ents;
    }

    public static EntityLivingBase getCloset(List<Entity> ents, double reach) {
        double dist = reach;
        EntityLivingBase target = null;
        for (Entity ent : ents) {
            if (ent.getDistanceToEntity(mc.thePlayer) < dist && ent != mc.thePlayer && isValidEntity((EntityLivingBase) ent)) {
                target = (EntityLivingBase) ent;
            }
        }
        return target;
    }

    public static boolean isValidEntity(EntityLivingBase ent) {
        if (ent instanceof EntityPlayer || ent instanceof EntityMob || !(ent instanceof EntityArmorStand)) {
            if (ent instanceof EntitySlime) return false;
            if (ent.isOnSameTeam(Minecraft.getMinecraft().thePlayer)) return false;
            if (ent.getName().equalsIgnoreCase("VILLAGER")) return false;
            if (ent.getName().equalsIgnoreCase("SHOP")) return false;
            if (ent.getName().equalsIgnoreCase("UPGRADES")) return false;
            if (ent.isInvisible()) return false;
            if (ent instanceof EntityArmorStand) return false;
            if (ent.isDead) return false;
            return true;
        }
        return false;
    }
}
