package net.minecraft.client.particle;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

public class EntityCritFX
extends EntitySmokeFX {
    protected EntityCritFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1201_8_, double p_i1201_10_, double p_i1201_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1201_8_, p_i1201_10_, p_i1201_12_, 2.5f);
    }
}
