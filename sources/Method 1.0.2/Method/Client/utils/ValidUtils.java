package Method.Client.utils;


import Method.Client.managers.FriendManager;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.combat.AntiBot;
import Method.Client.utils.system.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ValidUtils {

    public static boolean isInAttackFOV(EntityLivingBase entity, int fov) {
        return Utils.fovDistanceFromMouse(entity) <= fov;
    }

    public static boolean isClosest(EntityLivingBase entity, EntityLivingBase entityPriority) {
        return entityPriority == null || Wrapper.INSTANCE.player().getDistance(entity) < Wrapper.INSTANCE.player().getDistance(entityPriority);
    }

    public static boolean isLowHealth(EntityLivingBase entity, EntityLivingBase entityPriority) {
        return entityPriority == null || entity.getHealth() < entityPriority.getHealth();
    }

    public static boolean isInAttackRange(EntityLivingBase entity, float range) {
        return entity.getDistance(Wrapper.INSTANCE.player()) <= range;
    }

    public static boolean isBot(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            Module module = ModuleManager.getModuleByName("AntiBot");
            return module.isToggled() && AntiBot.isBot(player);
        }
        return false;
    }

    public static boolean isFriendEnemy(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            String ID = Utils.getPlayerName(player);
            return FriendManager.friendsList.contains(ID);
        }
        return false;
    }


}
