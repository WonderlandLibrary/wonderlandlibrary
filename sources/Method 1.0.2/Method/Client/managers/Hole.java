package Method.Client.managers;

import net.minecraft.util.math.BlockPos;

public class Hole extends BlockPos {
    private final boolean isTall;
    private final HoleType holeType;

    public enum HoleType {
        OBSIDIAN,
        BEDROCK,
        ENCASED,
        VOID
    }

    public Hole(BlockPos pos, HoleType holeType, boolean isTall) {
        super(pos);
        this.holeType = holeType;
        this.isTall = isTall;
    }

    public boolean isTall() {
        return isTall;
    }

    public HoleType getHoleType() {
        return holeType;
    }
}
