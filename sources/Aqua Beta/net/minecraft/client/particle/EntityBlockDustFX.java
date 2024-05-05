package net.minecraft.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.world.World;

public class EntityBlockDustFX
extends EntityDiggingFX {
    protected EntityBlockDustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
    }
}
