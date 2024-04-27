/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="BlockESP", description="Allows you to see a selected block through walls.", category=ModuleCategory.RENDER)
public final class BlockESP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "2D"}, "Box");
    private final BlockValue blockValue = new BlockValue("Block", 168);
    private final IntegerValue radiusValue = new IntegerValue("Radius", 40, 5, 120);
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 179, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 72, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final MSTimer searchTimer = new MSTimer();
    private final List<WBlockPos> posList = new ArrayList();
    private Thread thread;

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        block6: {
            block7: {
                if (!this.searchTimer.hasTimePassed(1000L)) break block6;
                if (this.thread == null) break block7;
                Thread thread2 = this.thread;
                if (thread2 == null) {
                    Intrinsics.throwNpe();
                }
                if (thread2.isAlive()) break block6;
            }
            int radius = ((Number)this.radiusValue.get()).intValue();
            IBlock selectedBlock = MinecraftInstance.functions.getBlockById(((Number)this.blockValue.get()).intValue());
            if (selectedBlock == null || selectedBlock.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR))) {
                return;
            }
            Thread thread3 = this.thread = new Thread(new Runnable(this, radius, selectedBlock){
                final /* synthetic */ BlockESP this$0;
                final /* synthetic */ int $radius;
                final /* synthetic */ IBlock $selectedBlock;

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 * WARNING - void declaration
                 */
                public final void run() {
                    int n;
                    List blockList = new ArrayList<E>();
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
                                    void z;
                                    int zPos;
                                    int yPos;
                                    IEntityPlayerSP thePlayer;
                                    int xPos;
                                    WBlockPos blockPos;
                                    IBlock block;
                                    if (MinecraftInstance.mc.getThePlayer() == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if ((block = BlockUtils.getBlock(blockPos = new WBlockPos(xPos = (int)thePlayer.getPosX() + x, yPos = (int)thePlayer.getPosY() + y, zPos = (int)thePlayer.getPosZ() + z))).equals(this.$selectedBlock)) {
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
                    BlockESP.access$getSearchTimer$p(this.this$0).reset();
                    List list = BlockESP.access$getPosList$p(this.this$0);
                    n3 = 0;
                    n = 0;
                    synchronized (list) {
                        boolean bl = false;
                        BlockESP.access$getPosList$p(this.this$0).clear();
                        n = (int)(BlockESP.access$getPosList$p(this.this$0).addAll(blockList) ? 1 : 0);
                    }
                }
                {
                    this.this$0 = blockESP;
                    this.$radius = n;
                    this.$selectedBlock = iBlock;
                }
            }, "BlockESP-BlockFinder");
            if (thread3 == null) {
                Intrinsics.throwNpe();
            }
            thread3.start();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        var2_2 = this.posList;
        var3_3 = false;
        var4_4 = false;
        synchronized (var2_2) {
            $i$a$-synchronized-BlockESP$onRender3D$1 = false;
            color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
            for (WBlockPos blockPos : this.posList) {
                var9_11 = (String)this.modeValue.get();
                var10_12 = false;
                v0 = var9_11;
                if (v0 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var9_11 = v0.toLowerCase();
                switch (var9_11.hashCode()) {
                    case 1650: {
                        if (!var9_11.equals("2d")) ** break;
                        break;
                    }
                    case 97739: {
                        if (!var9_11.equals("box")) ** break;
                        RenderUtils.drawBlockBox(blockPos, color, true);
                        ** break;
                    }
                }
                RenderUtils.draw2D(blockPos, color.getRGB(), Color.BLACK.getRGB());
lbl25:
                // 5 sources

            }
            var4_5 = Unit.INSTANCE;
        }
    }

    @Override
    public String getTag() {
        return ((Number)this.blockValue.get()).intValue() == 26 ? "Bed" : BlockUtils.getBlockName(((Number)this.blockValue.get()).intValue());
    }

    public static final /* synthetic */ MSTimer access$getSearchTimer$p(BlockESP $this) {
        return $this.searchTimer;
    }

    public static final /* synthetic */ List access$getPosList$p(BlockESP $this) {
        return $this.posList;
    }
}

