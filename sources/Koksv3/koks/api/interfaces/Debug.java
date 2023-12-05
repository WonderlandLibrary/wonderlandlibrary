package koks.api.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;

/**
 * @author Kroko, Phantom, Deleteboys, Dirt
 * @created on 06.12.2020 : 18:37
 */
public interface Debug {

    default void debugEntity(Entity finalEntity) {
        System.out.println("----DEBUG----");
        System.out.println("Name: " + finalEntity.getName());
        System.out.println("ExistTime: " + finalEntity.ticksExisted);
        System.out.println("Eye Height: " + finalEntity.getEyeHeight());
        System.out.println("DistanceToPlayer: " + finalEntity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer));
        System.out.println("CanBePushed: " + finalEntity.canBePushed());
        System.out.println("canAttackWithItem: " + finalEntity.canAttackWithItem());
        System.out.println("EntityID: " + finalEntity.getEntityId());
        System.out.println("LookVec: " + finalEntity.getLookVec());
        System.out.println("UUID: " + finalEntity.getUniqueID());
        if (finalEntity.getInventory() != null)
            System.out.println("Inventory Length: " + finalEntity.getInventory().length);
        System.out.println("Position: " + finalEntity.getPosition());
        System.out.println("onGround: " + finalEntity.onGround);
        System.out.println("hurtResistantTime: " + finalEntity.hurtResistantTime);
        System.out.println("MotionX: " + finalEntity.motionX);
        System.out.println("MotionY: " + finalEntity.motionY);
        System.out.println("MotionZ: " + finalEntity.motionZ);
        System.out.println("isDead: " + finalEntity.isDead);
        if (finalEntity instanceof EntityPlayer) {
            System.out.println("Health: " + ((EntityPlayer) finalEntity).getHealth());
            System.out.println("MaxHealth: " + ((EntityPlayer) finalEntity).getMaxHealth());
            System.out.println("Team: " + ((EntityPlayer) finalEntity).getTeam());
            System.out.println("AIMoveSpeed: " + ((EntityPlayer) finalEntity).getAIMoveSpeed());
            System.out.println("BedLocation: " + ((EntityPlayer) finalEntity).getBedLocation());
        }
        System.out.println("MaxFallHeight: " + finalEntity.getMaxFallHeight());
    }

    default void debugCapabilities(EntityPlayer entity) {
        PlayerCapabilities capabilities = entity.capabilities;
        System.out.println("Allow Edit: " + capabilities.allowEdit);
        System.out.println("Allow Flight: " + capabilities.allowFlying);
        System.out.println("Disable Damage: " + capabilities.disableDamage);
        System.out.println("Is Flying: " + capabilities.isFlying);
        System.out.println("Is Creative Mode: " + capabilities.isCreativeMode);
        System.out.println("Fly Speed: " + capabilities.getFlySpeed());
        System.out.println("Walk Speed: " + capabilities.getWalkSpeed());

    }
}
