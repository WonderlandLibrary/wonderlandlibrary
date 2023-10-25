/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.cache.IWorldData;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.IPlayerController;
import baritone.api.utils.Rotation;
import java.util.Optional;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IPlayerContext {
    public EntityPlayerSP player();

    public IPlayerController playerController();

    public World world();

    public IWorldData worldData();

    public RayTraceResult objectMouseOver();

    default public BetterBlockPos playerFeet() {
        BetterBlockPos feet = new BetterBlockPos(this.player().posX, this.player().posY + 0.1251, this.player().posZ);
        try {
            if (this.world().getBlockState(feet).getBlock() instanceof BlockSlab) {
                return feet.up();
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return feet;
    }

    default public Vec3d playerFeetAsVec() {
        return new Vec3d(this.player().posX, this.player().posY, this.player().posZ);
    }

    default public Vec3d playerHead() {
        return new Vec3d(this.player().posX, this.player().posY + (double)this.player().getEyeHeight(), this.player().posZ);
    }

    default public Rotation playerRotations() {
        return new Rotation(this.player().rotationYaw, this.player().rotationPitch);
    }

    public static double eyeHeight(boolean ifSneaking) {
        return ifSneaking ? 1.54 : 1.62;
    }

    default public Optional<BlockPos> getSelectedBlock() {
        RayTraceResult result = this.objectMouseOver();
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            return Optional.of(result.getBlockPos());
        }
        return Optional.empty();
    }

    default public boolean isLookingAt(BlockPos pos) {
        return this.getSelectedBlock().equals(Optional.of(pos));
    }
}

