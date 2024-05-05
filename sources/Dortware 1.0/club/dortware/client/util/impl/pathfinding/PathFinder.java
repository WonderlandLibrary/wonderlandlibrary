package club.dortware.client.util.impl.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTallGrass;

import java.util.List;

public class PathFinder {

    private List<Path> pathList;

    public PathFinder(List<Path> pathList) {
        this.pathList = pathList;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public PathFinderBlockInfo getBlockInfo(Block block) {
        if (block instanceof BlockAir || block instanceof BlockLiquid)
            return new PathFinderBlockInfo(-1, block);
        else if (block.isSolidFullCube())
            return new PathFinderBlockInfo(1, block);
        else if (block instanceof BlockTallGrass)
            return new PathFinderBlockInfo(-1, block);
        return new PathFinderBlockInfo(0.5F, block);
    }

}
