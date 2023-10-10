/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock {
    private int blockId = -1;
    private int[] metadatas = null;

    public MatchBlock(int p_i40_1_) {
        this.blockId = p_i40_1_;
    }

    public MatchBlock(int p_i41_1_, int[] p_i41_2_) {
        this.blockId = p_i41_1_;
        this.metadatas = p_i41_2_;
    }

    public int getBlockId() {
        return this.blockId;
    }

    public int[] getMetadatas() {
        return this.metadatas;
    }

    public boolean matches(BlockStateBase p_matches_1_) {
        if (p_matches_1_.getBlockId() != this.blockId) {
            return false;
        }
        if (this.metadatas != null) {
            boolean flag = false;
            int i2 = p_matches_1_.getMetadata();
            for (int j2 = 0; j2 < this.metadatas.length; ++j2) {
                int k2 = this.metadatas[j2];
                if (k2 != i2) continue;
                flag = true;
                break;
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
}

