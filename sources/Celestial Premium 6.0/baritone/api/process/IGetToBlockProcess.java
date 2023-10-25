/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.process.IBaritoneProcess;
import baritone.api.utils.BlockOptionalMeta;
import net.minecraft.block.Block;

public interface IGetToBlockProcess
extends IBaritoneProcess {
    public void getToBlock(BlockOptionalMeta var1);

    default public void getToBlock(Block block) {
        this.getToBlock(new BlockOptionalMeta(block));
    }

    public boolean blacklistClosest();
}

