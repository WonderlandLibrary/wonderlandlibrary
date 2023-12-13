// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import java.awt.Color;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.util.math.Vec3i;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.Blocks;
import com.elementars.eclient.util.BlockInteractionHelper;
import com.elementars.eclient.module.combat.AutoCrystal;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class VoidESP extends Module
{
    private final Value<Integer> range;
    private final Value<Integer> activateAtY;
    private final Value<String> holeMode;
    private final Value<String> renderMode;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> alpha;
    private ConcurrentSet<BlockPos> voidHoles;
    
    public VoidESP() {
        super("VoidESP", "Highlights possible void holes", 0, Category.RENDER, true);
        this.range = this.register(new Value<Integer>("Range", this, 8, 1, 32));
        this.activateAtY = this.register(new Value<Integer>("ActivateAtY", this, 32, 1, 512));
        this.holeMode = this.register(new Value<String>("HoleMode", this, "Sides", new ArrayList<String>(Arrays.asList("Sides", "Above"))));
        this.renderMode = this.register(new Value<String>("RenderMode", this, "Down", new ArrayList<String>(Arrays.asList("Down", "Block"))));
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 0, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 128, 0, 255));
    }
    
    @Override
    public void onUpdate() {
        if (VoidESP.mc.player.getPosition().y > this.activateAtY.getValue()) {
            return;
        }
        if (this.voidHoles == null) {
            this.voidHoles = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        }
        else {
            this.voidHoles.clear();
        }
        final List<BlockPos> blockPosList = BlockInteractionHelper.getCircle(AutoCrystal.getPlayerPos(), 0, this.range.getValue(), false);
        for (final BlockPos pos : blockPosList) {
            if (VoidESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK)) {
                continue;
            }
            if (this.isAnyBedrock(pos, Offsets.center)) {
                continue;
            }
            boolean aboveFree = false;
            if (!this.isAnyBedrock(pos, Offsets.above)) {
                aboveFree = true;
            }
            if (this.holeMode.getValue().equalsIgnoreCase("Above")) {
                if (!aboveFree) {
                    continue;
                }
                this.voidHoles.add((Object)pos);
            }
            else {
                boolean sidesFree = false;
                if (!this.isAnyBedrock(pos, Offsets.north)) {
                    sidesFree = true;
                }
                if (!this.isAnyBedrock(pos, Offsets.east)) {
                    sidesFree = true;
                }
                if (!this.isAnyBedrock(pos, Offsets.south)) {
                    sidesFree = true;
                }
                if (!this.isAnyBedrock(pos, Offsets.west)) {
                    sidesFree = true;
                }
                if (!this.holeMode.getValue().equalsIgnoreCase("Sides")) {
                    continue;
                }
                if (!aboveFree && !sidesFree) {
                    continue;
                }
                this.voidHoles.add((Object)pos);
            }
        }
    }
    
    private boolean isAnyBedrock(final BlockPos origin, final BlockPos[] offset) {
        for (final BlockPos pos : offset) {
            if (VoidESP.mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals(Blocks.BEDROCK)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (VoidESP.mc.player == null || this.voidHoles == null || this.voidHoles.isEmpty()) {
            return;
        }
        XuluTessellator.prepare(7);
        this.voidHoles.forEach(blockPos -> this.drawBlock(blockPos, this.red.getValue(), this.green.getValue(), this.blue.getValue()));
        XuluTessellator.release();
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.alpha.getValue());
        int mask = 0;
        if (this.renderMode.getValue().equalsIgnoreCase("Block")) {
            mask = 63;
        }
        if (this.renderMode.getValue().equalsIgnoreCase("Down")) {
            mask = 1;
        }
        XuluTessellator.drawBox(blockPos, color.getRGB(), mask);
    }
    
    @Override
    public String getHudInfo() {
        return this.holeMode.getValue();
    }
    
    private enum RenderMode
    {
        DOWN, 
        BLOCK;
    }
    
    private enum HoleMode
    {
        SIDES, 
        ABOVE;
    }
    
    private static class Offsets
    {
        static final BlockPos[] center;
        static final BlockPos[] above;
        static final BlockPos[] aboveStep1;
        static final BlockPos[] aboveStep2;
        static final BlockPos[] north;
        static final BlockPos[] east;
        static final BlockPos[] south;
        static final BlockPos[] west;
        
        static {
            center = new BlockPos[] { new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
            above = new BlockPos[] { new BlockPos(0, 3, 0), new BlockPos(0, 4, 0) };
            aboveStep1 = new BlockPos[] { new BlockPos(0, 3, 0) };
            aboveStep2 = new BlockPos[] { new BlockPos(0, 4, 0) };
            north = new BlockPos[] { new BlockPos(0, 1, -1), new BlockPos(0, 2, -1) };
            east = new BlockPos[] { new BlockPos(1, 1, 0), new BlockPos(1, 2, 0) };
            south = new BlockPos[] { new BlockPos(0, 1, 1), new BlockPos(0, 2, 1) };
            west = new BlockPos[] { new BlockPos(-1, 1, 0), new BlockPos(-1, 2, 0) };
        }
    }
}
