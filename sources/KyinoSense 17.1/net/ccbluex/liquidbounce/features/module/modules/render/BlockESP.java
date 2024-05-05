/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="BlockESP", description="Allows you to see a selected block through walls.", category=ModuleCategory.RENDER)
public class BlockESP
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Box", "2D"}, "Box");
    private final BlockValue blockValue = new BlockValue("Block", 168);
    private final IntegerValue radiusValue = new IntegerValue("Radius", 40, 5, 120);
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 179, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 72, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final MSTimer searchTimer = new MSTimer();
    private final List<BlockPos> posList = new ArrayList<BlockPos>();
    private Thread thread;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.searchTimer.hasTimePassed(1000L) && (this.thread == null || !this.thread.isAlive())) {
            int radius = (Integer)this.radiusValue.get();
            Block selectedBlock = Block.func_149729_e((int)((Integer)this.blockValue.get()));
            if (selectedBlock == null || selectedBlock == Blocks.field_150350_a) {
                return;
            }
            this.thread = new Thread(() -> {
                ArrayList<BlockPos> blockList = new ArrayList<BlockPos>();
                for (int x = -radius; x < radius; ++x) {
                    for (int y = radius; y > -radius; --y) {
                        for (int z = -radius; z < radius; ++z) {
                            int xPos = (int)BlockESP.mc.field_71439_g.field_70165_t + x;
                            int yPos = (int)BlockESP.mc.field_71439_g.field_70163_u + y;
                            int zPos = (int)BlockESP.mc.field_71439_g.field_70161_v + z;
                            BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                            Block block = BlockUtils.getBlock(blockPos);
                            if (block != selectedBlock) continue;
                            blockList.add(blockPos);
                        }
                    }
                }
                this.searchTimer.reset();
                List<BlockPos> list = this.posList;
                synchronized (list) {
                    this.posList.clear();
                    this.posList.addAll(blockList);
                }
            }, "BlockESP-BlockFinder");
            this.thread.start();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        List<BlockPos> list = this.posList;
        synchronized (list) {
            Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
            for (BlockPos blockPos : this.posList) {
                switch (((String)this.modeValue.get()).toLowerCase()) {
                    case "box": {
                        RenderUtils.drawBlockBox(blockPos, color, true);
                        break;
                    }
                    case "2d": {
                        RenderUtils.draw2D(blockPos, color.getRGB(), Color.BLACK.getRGB());
                    }
                }
            }
        }
    }

    @Override
    public String getTag() {
        return BlockUtils.getBlockName((Integer)this.blockValue.get());
    }
}

