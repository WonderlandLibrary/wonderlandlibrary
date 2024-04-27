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
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
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
    private Thread thread;
    private final List posList;
    private final BoolValue colorRainbow;
    private final IntegerValue colorGreenValue;
    private final IntegerValue radiusValue;
    private final BlockValue blockValue;
    private final IntegerValue colorBlueValue;
    private final MSTimer searchTimer;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "2D"}, "Box");
    private final IntegerValue colorRedValue;

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public static final List access$getPosList$p(BlockESP blockESP) {
        return blockESP.posList;
    }

    @Override
    public String getTag() {
        return ((Number)this.blockValue.get()).intValue() == 26 ? "Bed" : BlockUtils.getBlockName(((Number)this.blockValue.get()).intValue());
    }

    public static final MSTimer access$getSearchTimer$p(BlockESP blockESP) {
        return blockESP.searchTimer;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        block6: {
            block7: {
                if (!this.searchTimer.hasTimePassed(1000L)) break block6;
                if (this.thread == null) break block7;
                Thread thread = this.thread;
                if (thread == null) {
                    Intrinsics.throwNpe();
                }
                if (thread.isAlive()) break block6;
            }
            int n = ((Number)this.radiusValue.get()).intValue();
            IBlock iBlock = BlockESP.access$getFunctions$p$s1046033730().getBlockById(((Number)this.blockValue.get()).intValue());
            if (iBlock == null || iBlock.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR))) {
                return;
            }
            Thread thread = this.thread = new Thread(new Runnable(this, n, iBlock){
                final BlockESP this$0;
                final IBlock $selectedBlock;
                final int $radius;
                {
                    this.this$0 = blockESP;
                    this.$radius = n;
                    this.$selectedBlock = iBlock;
                }

                static {
                }

                public final void run() {
                    int n;
                    int n2;
                    List list = new ArrayList<E>();
                    int n3 = this.$radius;
                    block1: for (int i = -this.$radius; i < n3; ++i) {
                        n2 = this.$radius;
                        n = -this.$radius + 1;
                        if (n2 < n) continue;
                        while (true) {
                            int n4 = this.$radius;
                            for (int j = -this.$radius; j < n4; ++j) {
                                int n5;
                                int n6;
                                IEntityPlayerSP iEntityPlayerSP;
                                int n7;
                                WBlockPos wBlockPos;
                                IBlock iBlock;
                                if (MinecraftInstance.mc.getThePlayer() == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (!(iBlock = BlockUtils.getBlock(wBlockPos = new WBlockPos(n7 = (int)iEntityPlayerSP.getPosX() + i, n6 = (int)iEntityPlayerSP.getPosY() + n2, n5 = (int)iEntityPlayerSP.getPosZ() + j))).equals(this.$selectedBlock)) continue;
                                list.add(wBlockPos);
                            }
                            if (n2 == n) continue block1;
                            --n2;
                        }
                    }
                    BlockESP.access$getSearchTimer$p(this.this$0).reset();
                    List list2 = BlockESP.access$getPosList$p(this.this$0);
                    n3 = 0;
                    n2 = 0;
                    synchronized (list2) {
                        n = 0;
                        BlockESP.access$getPosList$p(this.this$0).clear();
                        n2 = (int)(BlockESP.access$getPosList$p(this.this$0).addAll(list) ? 1 : 0);
                    }
                }
            }, "BlockESP-BlockFinder");
            if (thread == null) {
                Intrinsics.throwNpe();
            }
            thread.start();
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent var1_1) {
        var2_2 = this.posList;
        var3_3 = false;
        var4_4 = false;
        synchronized (var2_2) {
            var5_6 = false;
            var6_7 = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
            for (WBlockPos var8_9 : this.posList) {
                var9_10 = (String)this.modeValue.get();
                var10_11 = false;
                v0 = var9_10;
                if (v0 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var9_10 = v0.toLowerCase();
                switch (var9_10.hashCode()) {
                    case 1650: {
                        if (!var9_10.equals("2d")) ** break;
                        break;
                    }
                    case 97739: {
                        if (!var9_10.equals("box")) ** break;
                        RenderUtils.drawBlockBox(var8_9, var6_7, true);
                        ** break;
                    }
                }
                RenderUtils.draw2D(var8_9, var6_7.getRGB(), Color.BLACK.getRGB());
lbl24:
                // 5 sources

            }
            var4_5 = Unit.INSTANCE;
        }
    }

    public BlockESP() {
        this.blockValue = new BlockValue("Block", 168);
        this.radiusValue = new IntegerValue("Radius", 40, 5, 120);
        this.colorRedValue = new IntegerValue("R", 255, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 179, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 72, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.searchTimer = new MSTimer();
        this.posList = new ArrayList();
    }
}

