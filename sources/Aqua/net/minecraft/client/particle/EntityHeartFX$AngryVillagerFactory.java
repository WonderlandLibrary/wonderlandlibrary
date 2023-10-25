package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public static class EntityHeartFX.AngryVillagerFactory
implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
        EntityHeartFX entityfx = new EntityHeartFX(worldIn, xCoordIn, yCoordIn + 0.5, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        entityfx.setParticleTextureIndex(81);
        entityfx.setRBGColorF(1.0f, 1.0f, 1.0f);
        return entityfx;
    }
}
