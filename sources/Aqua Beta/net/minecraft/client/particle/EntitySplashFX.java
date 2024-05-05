package net.minecraft.client.particle;

import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.world.World;

public class EntitySplashFX
extends EntityRainFX {
    protected EntitySplashFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleGravity = 0.04f;
        this.nextTextureIndexX();
        if (ySpeedIn == 0.0 && (xSpeedIn != 0.0 || zSpeedIn != 0.0)) {
            this.motionX = xSpeedIn;
            this.motionY = ySpeedIn + 0.1;
            this.motionZ = zSpeedIn;
        }
    }
}
