package today.flux.addon.api.utils;

import com.soterdev.SoterObfuscator;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.BlockPos;

public class BlockPosition {
    @Getter @Setter
    public int x, y, z = 0;

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockPosition getBlockPosition(BlockPos blockPos) {
        return new BlockPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    
    public BlockPos getNativeBlockPos() {
        return new BlockPos(x, y, z);
    }
}
