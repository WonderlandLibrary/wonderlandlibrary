// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.MathUtil;
import java.awt.Color;
import com.elementars.eclient.util.RainbowUtils;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import com.elementars.eclient.util.BlockInteractionHelper;
import com.elementars.eclient.module.combat.AutoCrystal;
import net.minecraft.client.renderer.culling.Frustum;
import com.elementars.eclient.util.EnumUtil;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import com.elementars.eclient.util.Pair;
import java.util.concurrent.ConcurrentHashMap;
import dev.xulu.settings.Value;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.module.Module;

public class HoleESP extends Module
{
    private final BlockPos[] surroundOffset;
    private final Value<Boolean> frustum;
    private final Value<Boolean> hideOwn;
    private final Value<Boolean> offset;
    private final Value<Boolean> future;
    private final Value<Float> renderDistance;
    private final Value<Boolean> max;
    private final Value<Integer> maxHoles;
    private final Value<String> holeMode;
    private final Value<String> renderMode;
    private final Value<String> drawMode;
    private final Value<Float> cuboid;
    private final Value<Boolean> rainbow;
    private final Value<Integer> obiRed;
    private final Value<Integer> obiGreen;
    private final Value<Integer> obiBlue;
    private final Value<Integer> brockRed;
    private final Value<Integer> brockGreen;
    private final Value<Integer> brockBlue;
    private final Value<Integer> alpha;
    private final Value<Integer> alpha2;
    private ConcurrentHashMap<BlockPos, Pair<Boolean, Boolean>> safeHoles;
    int holes;
    ICamera camera;
    
    public HoleESP() {
        super("HoleESP", "Highlights holes for pvp", 0, Category.RENDER, true);
        this.frustum = this.register(new Value<Boolean>("Frustum", this, true));
        this.hideOwn = this.register(new Value<Boolean>("HideOwn", this, false));
        this.offset = this.register(new Value<Boolean>("Offset Lower", this, false));
        this.future = this.register(new Value<Boolean>("Future Mode", this, false));
        this.renderDistance = this.register(new Value<Float>("RenderDistance", this, 8.0f, 1.0f, 32.0f));
        this.max = this.register(new Value<Boolean>("Maximum Holes", this, false));
        this.maxHoles = this.register(new Value<Integer>("Maximum Num", this, 8, 1, 100));
        this.holeMode = this.register(new Value<String>("Hole Mode", this, "Both", new String[] { "Bedrock", "Obsidian", "Both" }));
        this.renderMode = this.register(new Value<String>("RenderMode", this, "Solid", new ArrayList<String>(Arrays.asList("Solid", "Flat"))));
        this.drawMode = this.register(new Value<String>("DrawMode", this, "Solid", EnumUtil.enumConverter(Modes.class)));
        this.cuboid = this.register(new Value<Float>("Cuboid Height", this, 0.9f, 0.0f, 1.0f));
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.obiRed = this.register(new Value<Integer>("ObiRed", this, 104, 0, 255));
        this.obiGreen = this.register(new Value<Integer>("ObiGreen", this, 12, 0, 255));
        this.obiBlue = this.register(new Value<Integer>("ObiBlue", this, 35, 0, 255));
        this.brockRed = this.register(new Value<Integer>("BrockRed", this, 81, 0, 255));
        this.brockGreen = this.register(new Value<Integer>("BrockGreen", this, 12, 0, 255));
        this.brockBlue = this.register(new Value<Integer>("BrockBlue", this, 104, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 169, 0, 255));
        this.alpha2 = this.register(new Value<Integer>("Outline Alpha", this, 255, 0, 255));
        this.camera = (ICamera)new Frustum();
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
    
    @Override
    public void onUpdate() {
        final double d3 = HoleESP.mc.player.lastTickPosX + (HoleESP.mc.player.posX - HoleESP.mc.player.lastTickPosX) * HoleESP.mc.getRenderPartialTicks();
        final double d4 = HoleESP.mc.player.lastTickPosY + (HoleESP.mc.player.posY - HoleESP.mc.player.lastTickPosY) * HoleESP.mc.getRenderPartialTicks();
        final double d5 = HoleESP.mc.player.lastTickPosZ + (HoleESP.mc.player.posZ - HoleESP.mc.player.lastTickPosZ) * HoleESP.mc.getRenderPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap<BlockPos, Pair<Boolean, Boolean>>();
        }
        else {
            this.safeHoles.clear();
        }
        final int range = (int)Math.ceil(this.renderDistance.getValue());
        final List<BlockPos> blockPosList = BlockInteractionHelper.getSphere(AutoCrystal.getPlayerPos(), (float)range, range, false, true, 0);
        this.holes = 0;
        for (final BlockPos pos : blockPosList) {
            if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (this.hideOwn.getValue() && pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ))) {
                continue;
            }
            if (this.frustum.getValue() && !this.camera.isBoundingBoxInFrustum(HoleESP.mc.world.getBlockState(pos).getSelectedBoundingBox((World)HoleESP.mc.world, pos))) {
                continue;
            }
            boolean isSafe = true;
            boolean isBedrock = true;
            boolean hasBedrock = false;
            for (final BlockPos offset : this.surroundOffset) {
                final Block block = HoleESP.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (block != Blocks.BEDROCK) {
                    isBedrock = false;
                }
                if (block == Blocks.BEDROCK) {
                    hasBedrock = true;
                }
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    isSafe = false;
                    break;
                }
            }
            if (!isSafe) {
                continue;
            }
            if (isBedrock && this.holeMode.getValue().equalsIgnoreCase("Obsidian")) {
                continue;
            }
            if (!isBedrock && this.holeMode.getValue().equalsIgnoreCase("Bedrock")) {
                continue;
            }
            this.safeHoles.put(pos, new Pair<Boolean, Boolean>(isBedrock, hasBedrock));
            if (!this.max.getValue()) {
                continue;
            }
            ++this.holes;
            if (this.holes == this.maxHoles.getValue()) {
                return;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (HoleESP.mc.player == null || this.safeHoles == null) {
            return;
        }
        if (this.safeHoles.isEmpty()) {
            return;
        }
        if (this.drawMode.getValue().equalsIgnoreCase("Solid")) {
            XuluTessellator.prepare(7);
            this.safeHoles.forEach((blockPos, pair) -> {
                if (this.offset.getValue()) {
                    blockPos = blockPos.add(0, -1, 0);
                }
                if (pair.getKey()) {
                    this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                }
                else {
                    this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                }
                return;
            });
            XuluTessellator.release();
        }
        else if (this.drawMode.getValue().equalsIgnoreCase("Outline")) {
            this.safeHoles.forEach((blockPos, pair) -> {
                if (this.offset.getValue()) {
                    blockPos = blockPos.add(0, -1, 0);
                }
                if (this.renderMode.getValue().equalsIgnoreCase("Solid")) {
                    if (pair.getKey()) {
                        this.drawBlockO(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                    }
                    else {
                        this.drawBlockO(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                    }
                }
                else if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
                    if (pair.getKey()) {
                        this.drawBlockOF(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                    }
                    else {
                        this.drawBlockOF(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                    }
                }
            });
        }
        else if (this.drawMode.getValue().equalsIgnoreCase("Full")) {
            this.safeHoles.forEach((blockPos, pair) -> {
                if (this.offset.getValue()) {
                    blockPos = blockPos.add(0, -1, 0);
                }
                if (this.renderMode.getValue().equalsIgnoreCase("Solid")) {
                    if (pair.getKey()) {
                        XuluTessellator.prepare(7);
                        this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                        XuluTessellator.release();
                        this.drawBlockO(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                    }
                    else {
                        XuluTessellator.prepare(7);
                        this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                        XuluTessellator.release();
                        this.drawBlockO(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                    }
                }
                else if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
                    if (pair.getKey()) {
                        XuluTessellator.prepare(7);
                        this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                        XuluTessellator.release();
                        this.drawBlockOF(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                    }
                    else {
                        XuluTessellator.prepare(7);
                        this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                        XuluTessellator.release();
                        this.drawBlockOF(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                    }
                }
            });
        }
        else if (this.drawMode.getValue().equalsIgnoreCase("Cuboid")) {
            this.safeHoles.forEach((blockPos, pair) -> {
                if (this.offset.getValue()) {
                    blockPos = blockPos.add(0, -1, 0);
                }
                if (pair.getKey()) {
                    this.drawBlockCUB(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                }
                else {
                    this.drawBlockCUB(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                }
            });
        }
        else if (this.drawMode.getValue().equalsIgnoreCase("Indicator")) {
            this.safeHoles.forEach((blockPos, pair) -> {
                if (this.offset.getValue()) {
                    blockPos = blockPos.add(0, -1, 0);
                }
                if (pair.getKey()) {
                    this.drawBlockIndicator(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.brockRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.brockGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.brockBlue.getValue()), pair);
                }
                else {
                    this.drawBlockIndicator(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)this.obiRed.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)this.obiGreen.getValue()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)this.obiBlue.getValue()), pair);
                }
            });
        }
    }
    
    private boolean isIntermediate(final BlockPos pos) {
        boolean flag = false;
        boolean oflag = false;
        for (final BlockPos offset : this.surroundOffset) {
            final Block block = HoleESP.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
            if (block == Blocks.BEDROCK) {
                flag = true;
            }
            else if (block == Blocks.OBSIDIAN && block == Blocks.ENDER_CHEST && block == Blocks.ANVIL) {
                oflag = true;
            }
        }
        return flag && oflag;
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b, final Pair<Boolean, Boolean> pair) {
        Color color = new Color(r, g, b, this.alpha.getValue());
        if (this.future.getValue() && !pair.getKey() && pair.getValue()) {
            color = new Color(255, 255, 0, this.alpha.getValue());
        }
        int mask = 1;
        if (this.renderMode.getValue().equalsIgnoreCase("Solid")) {
            mask = 63;
        }
        XuluTessellator.drawBox(blockPos, color.getRGB(), mask);
    }
    
    private void drawBlockO(final BlockPos blockPos, final int r, final int g, final int b, final Pair<Boolean, Boolean> pair) {
        final int red = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : r;
        final int green = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : g;
        final int blue = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 0 : b;
        final IBlockState iBlockState2 = HoleESP.mc.world.getBlockState(blockPos);
        final Vec3d interp2 = MathUtil.interpolateEntity((Entity)HoleESP.mc.player, HoleESP.mc.getRenderPartialTicks());
        XuluTessellator.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)HoleESP.mc.world, blockPos).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, red, green, blue, this.alpha2.getValue());
    }
    
    private void drawBlockCUB(final BlockPos blockPos, final int r, final int g, final int b, final Pair<Boolean, Boolean> pair) {
        final int red = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : r;
        final int green = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : g;
        final int blue = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 0 : b;
        final IBlockState iBlockState2 = HoleESP.mc.world.getBlockState(blockPos);
        final Vec3d interp2 = MathUtil.interpolateEntity((Entity)HoleESP.mc.player, HoleESP.mc.getRenderPartialTicks());
        AxisAlignedBB aabb = iBlockState2.getSelectedBoundingBox((World)HoleESP.mc.world, blockPos);
        aabb = aabb.setMaxY(aabb.maxY - 1.0f * this.cuboid.getValue()).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z);
        XuluTessellator.drawFullBox2(aabb, blockPos, 1.5f, new Color(red, green, blue, this.alpha.getValue()).getRGB(), this.alpha2.getValue());
    }
    
    private void drawBlockIndicator(final BlockPos blockPos, final int r, final int g, final int b, final Pair<Boolean, Boolean> pair) {
        final int red = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : r;
        final int green = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : g;
        final int blue = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 0 : b;
        final IBlockState iBlockState2 = HoleESP.mc.world.getBlockState(blockPos);
        final Vec3d interp2 = MathUtil.interpolateEntity((Entity)HoleESP.mc.player, HoleESP.mc.getRenderPartialTicks());
        AxisAlignedBB aabb = iBlockState2.getSelectedBoundingBox((World)HoleESP.mc.world, blockPos);
        aabb = aabb.setMaxY(aabb.maxY + ((HoleESP.mc.player.getDistanceSq(blockPos) < 10.0) ? 0 : 3)).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z);
        GlStateManager.enableCull();
        XuluTessellator.drawIndicator(aabb, new Color(red, green, blue, this.alpha.getValue()).getRGB(), 63);
    }
    
    private void drawBlockOCUB(final BlockPos blockPos, final int r, final int g, final int b, final Pair<Boolean, Boolean> pair) {
        final int red = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : r;
        final int green = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : g;
        final int blue = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 0 : b;
        final IBlockState iBlockState2 = HoleESP.mc.world.getBlockState(blockPos);
        final Vec3d interp2 = MathUtil.interpolateEntity((Entity)HoleESP.mc.player, HoleESP.mc.getRenderPartialTicks());
        AxisAlignedBB aabb = iBlockState2.getSelectedBoundingBox((World)HoleESP.mc.world, blockPos);
        aabb = aabb.setMaxY(aabb.maxY - 1.0f * this.cuboid.getValue()).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z);
        XuluTessellator.drawBoundingBox(aabb, 1.5f, red, green, blue, this.alpha2.getValue());
    }
    
    private void drawBlockOF(final BlockPos blockPos, final int r, final int g, final int b, final Pair<Boolean, Boolean> pair) {
        final int red = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : r;
        final int green = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 255 : g;
        final int blue = (this.future.getValue() && !pair.getKey() && pair.getValue()) ? 0 : b;
        final IBlockState iBlockState = HoleESP.mc.world.getBlockState(blockPos);
        final Vec3d interp = MathUtil.interpolateEntity((Entity)HoleESP.mc.player, HoleESP.mc.getRenderPartialTicks());
        XuluTessellator.drawBoundingBoxFace(iBlockState.getSelectedBoundingBox((World)HoleESP.mc.world, blockPos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), 1.5f, red, green, blue, this.alpha2.getValue());
    }
    
    @Override
    public String getHudInfo() {
        return this.renderMode.getValue() + ", " + this.drawMode.getValue();
    }
    
    public enum Modes
    {
        SOLID, 
        OUTLINE, 
        FULL, 
        CUBOID, 
        INDICATOR;
    }
}
