package net.smoothboot.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EntityUtil {
    ;


    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public static Stream<Entity> getAttackableEntities()
    {
        return StreamSupport.stream(mc.world.getEntities().spliterator(), true)
                .filter(IS_ATTACKABLE);
    }

    public static Predicate<Entity> IS_ATTACKABLE = e -> e != null
            && !e.isRemoved()
            && (e instanceof LivingEntity && ((LivingEntity)e).getHealth() > 0
            || e instanceof EndCrystalEntity
            || e instanceof ShulkerBulletEntity);


    public static <T extends Entity> T findClosestPlayer(Class<T> entityClass, float range) {
        for (Entity entity : mc.world.getPlayers()) {
            if (entityClass.isAssignableFrom(entity.getClass()) && !entity.equals(mc.player) && entity.distanceTo(mc.player) <= range) {
                return (T) entity;
            }
        }
        return null;
    }

    public static <T extends Entity> T findPlayer(Class<T> entityClass) {
        for (Entity entity : mc.world.getEntities()) {
            if (entityClass.isAssignableFrom(entity.getClass()) && !entity.equals(mc.player)) {
                return (T) entity;
            }
        }
        return null;
    }

    public static Vec3d getLerpedPos(Entity e, float partialTicks)
    {
        double x = MathHelper.lerp(partialTicks, e.lastRenderX, e.getX());
        double y = MathHelper.lerp(partialTicks, e.lastRenderY, e.getY());
        double z = MathHelper.lerp(partialTicks, e.lastRenderZ, e.getZ());
        return new Vec3d(x, y, z);
    }
}