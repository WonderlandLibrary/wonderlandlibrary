package net.ccbluex.liquidbounce.features.module.modules.render;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockESP;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\b\n\u0000\n\n\u0000\u00000H\n¢\b"}, d2={"<anonymous>", "", "run"})
final class BlockESP$onUpdate$1
implements Runnable {
    final BlockESP this$0;
    final int $radius;
    final IBlock $selectedBlock;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Override
    public final void run() {
        int n;
        List blockList = new ArrayList();
        int n2 = -this.$radius;
        int n3 = this.$radius;
        while (n2 < n3) {
            void x;
            n = this.$radius;
            int n4 = -this.$radius + 1;
            if (n >= n4) {
                while (true) {
                    void y;
                    int n5 = -this.$radius;
                    int n6 = this.$radius;
                    while (n5 < n6) {
                        IBlock block;
                        void z;
                        IEntityPlayerSP thePlayer;
                        if (MinecraftInstance.mc.getThePlayer() == null) {
                            Intrinsics.throwNpe();
                        }
                        int xPos = (int)thePlayer.getPosX() + x;
                        int yPos = (int)thePlayer.getPosY() + y;
                        int zPos = (int)thePlayer.getPosZ() + z;
                        WBlockPos blockPos = new WBlockPos(xPos, yPos, zPos);
                        boolean $i$f$getBlock = false;
                        Object object = MinecraftInstance.mc.getTheWorld();
                        IBlock iBlock = object != null && (object = object.getBlockState(blockPos)) != null ? object.getBlock() : (block = null);
                        if (Intrinsics.areEqual(block, this.$selectedBlock) && blockList.size() < ((Number)this.this$0.blockLimitValue.get()).intValue()) {
                            blockList.add(blockPos);
                        }
                        ++z;
                    }
                    if (y == n4) break;
                    --y;
                }
            }
            ++x;
        }
        this.this$0.searchTimer.reset();
        List list = this.this$0.posList;
        n3 = 0;
        n = 0;
        synchronized (list) {
            boolean bl = false;
            this.this$0.posList.clear();
            n = this.this$0.posList.addAll(blockList) ? 1 : 0;
        }
    }

    BlockESP$onUpdate$1(BlockESP blockESP, int n, IBlock iBlock) {
        this.this$0 = blockESP;
        this.$radius = n;
        this.$selectedBlock = iBlock;
    }
}
