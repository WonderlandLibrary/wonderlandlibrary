package org.luaj.vm2.customs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityHook {

    private Entity entity;

    public EntityHook(Entity entity) {
        this.entity = entity;
    }

    public float getX() {
        return (float) entity.getPosX();
    }

    public float getY() {
        return (float) entity.getPosY();
    }

    public float getZ() {
        return (float) entity.getPosZ();
    }

    public double lastTickPosX() {
        return entity.lastTickPosX;
    }

    public double lastTickPosY() {
        return entity.lastTickPosY;
    }

    public double lastTickPosZ() {
        return entity.lastTickPosZ;
    }

    public double prevPosX() {
        return entity.prevPosX;
    }

    public double prevPosY() {
        return entity.prevPosY;
    }

    public double prevPosZ() {
        return entity.prevPosZ;
    }

    public double getWidth() {
        return entity.getWidth();
    }

    public double getHeight() {
        return entity.getHeight();
    }

    public double getDistanceFromMe() {
        return entity.getDistance(Minecraft.getInstance().player);
    }

    public double getEyeHeight() {
        return entity.getEyeHeight();
    }

    public String getDisplayName() {
        return entity.getDisplayName().getString();
    }

    public double getHealth() {
        return ((LivingEntity) entity).getHealth();
    }

    public double getMaxHealth() {
        return ((LivingEntity) entity).getMaxHealth();
    }

    public Entity getEntity() {
        return entity;
    }

    public String getName() {
        return entity.getName().getString();
    }

    public void attack() {
        Minecraft.getInstance().playerController.attackEntity(Minecraft.getInstance().player, entity);
    }

    public boolean is(String s) {
        if (s.equalsIgnoreCase("player"))
            return entity instanceof PlayerEntity;
        if (s.equalsIgnoreCase("item"))
            return entity instanceof ItemEntity;
        if (s.equalsIgnoreCase("pearl"))
            return entity instanceof EnderPearlEntity;
        if (s.equalsIgnoreCase("animals"))
            return entity instanceof AnimalEntity;
        if (s.equalsIgnoreCase("mobs"))
            return entity instanceof MobEntity;
        if (s.equalsIgnoreCase("me"))
            return entity instanceof ClientPlayerEntity;
        return true;
    }


}
