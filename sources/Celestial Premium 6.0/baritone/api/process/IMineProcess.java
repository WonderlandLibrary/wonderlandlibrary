/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.process.IBaritoneProcess;
import baritone.api.utils.BlockOptionalMeta;
import baritone.api.utils.BlockOptionalMetaLookup;
import java.util.stream.Stream;
import net.minecraft.block.Block;

public interface IMineProcess
extends IBaritoneProcess {
    public void mineByName(int var1, String ... var2);

    public void mine(int var1, BlockOptionalMetaLookup var2);

    default public void mine(BlockOptionalMetaLookup filter) {
        this.mine(0, filter);
    }

    default public void mineByName(String ... blocks) {
        this.mineByName(0, blocks);
    }

    default public void mine(int quantity, BlockOptionalMeta ... boms) {
        this.mine(quantity, new BlockOptionalMetaLookup(boms));
    }

    default public void mine(BlockOptionalMeta ... boms) {
        this.mine(0, boms);
    }

    default public void mine(int quantity, Block ... blocks) {
        this.mine(quantity, new BlockOptionalMetaLookup((BlockOptionalMeta[])Stream.of(blocks).map(BlockOptionalMeta::new).toArray(BlockOptionalMeta[]::new)));
    }

    default public void mine(Block ... blocks) {
        this.mine(0, blocks);
    }

    default public void cancel() {
        this.onLostControl();
    }
}

