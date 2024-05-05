package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public static class EntitySpellParticleFX.WitchFactory
implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
        EntitySpellParticleFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        entityfx.setBaseSpellTextureIndex(144);
        float f = worldIn.rand.nextFloat() * 0.5f + 0.35f;
        entityfx.setRBGColorF(1.0f * f, 0.0f * f, 1.0f * f);
        return entityfx;
    }
}
