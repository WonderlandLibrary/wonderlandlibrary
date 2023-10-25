package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.world.World;

public static class EntityCrit2FX.MagicFactory
implements IParticleFactory {
    public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
        EntityCrit2FX entityfx = new EntityCrit2FX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        entityfx.setRBGColorF(entityfx.getRedColorF() * 0.3f, entityfx.getGreenColorF() * 0.8f, entityfx.getBlueColorF());
        entityfx.nextTextureIndexX();
        return entityfx;
    }
}
